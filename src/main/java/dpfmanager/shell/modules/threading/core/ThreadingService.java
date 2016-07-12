package dpfmanager.shell.modules.threading.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.database.messages.CheckTaskMessage;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.messages.messages.CloseMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;
import dpfmanager.shell.modules.timer.messages.TimerMessage;
import dpfmanager.shell.modules.timer.tasks.JobsStatusTask;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_THREADING)
@Scope("singleton")
public class ThreadingService extends DpfService {

  /**
   * The main executor service
   */
  private DpfExecutor myExecutor;

  /**
   * The number of threads
   */
  private int cores;

  /**
   * The resource bundle
   */
  private ResourceBundle bundle;

  private Map<Long, FileCheck> checks;
  private Queue<FileCheck> pendingChecks;

  private boolean needReload;
  private int totalChecks;

  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
    checks = new HashMap<>();
    pendingChecks = new LinkedList<>();
    needReload = true;
    totalChecks = 0;
  }

  @PreDestroy
  public void finish() {
    // Finish executor
    myExecutor.shutdownNow();
  }

  @Override
  protected void handleContext(DpfContext context) {
    cores = Runtime.getRuntime().availableProcessors() - 1;
    if (cores < 1) {
      cores = 1;
    }

    // Check for the -t option for tests
    if (GuiWorkbench.getAppParams() != null) {
      for (String param : GuiWorkbench.getAppParams().getRaw()) {
        if (param.startsWith("-t") && param.length() == 3) {
          String number = param.substring(2);
          int threads = Integer.valueOf(number);
          if (threads < cores && threads > 1) {
            cores = threads;
          }
          break;
        }
      }
    }

    myExecutor = new DpfExecutor(cores);
    myExecutor.handleContext(context);
  }

  public void run(DpfRunnable runnable, Long uuid) {
    runnable.setContext(getContext());
    runnable.setUuid(uuid);
    myExecutor.myExecute(runnable);
  }

  public void processThreadMessage(ThreadsMessage tm) {
    if (tm.isPause() && tm.isRequest()) {
      myExecutor.pause(tm.getUuid());
    } else if (tm.isResume()) {
      context.send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.RESUME, tm.getUuid()));
      myExecutor.resume(tm.getUuid());
    } else if (tm.isCancel() && tm.isRequest()) {
      cancelRequest(tm.getUuid());
    } else if (tm.isCancel() && !tm.isRequest()) {
      cancelFinish(tm.getUuid());
    } else if (tm.isPause() && !tm.isRequest()) {
      pauseFinish(tm.getUuid());
    }
  }

  public void closeRequested() {
    context.send(BasicConfig.MODULE_MESSAGE, new CloseMessage(CloseMessage.Type.THREADING, !checks.isEmpty()));
  }

  private void cancelRequest(Long uuid) {
    // Check if is pending
    FileCheck pending = null;
    for (FileCheck check : pendingChecks) {
      if (uuid.equals(check.getUuid())) {
        pending = check;
        break;
      }
    }
    if (pending != null) {
      // Cancel pending
      pendingChecks.remove(pending);
      context.send(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Target.CANCEL, uuid));
    } else {
      // Cancel threads
      myExecutor.cancel(uuid);
    }
  }

  public void cancelFinish(Long uuid) {
    // Update db
    getContext().send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.CANCEL, uuid));

    if (checks.get(uuid) != null) {
      // Remove folder
      String internal = checks.get(uuid).getInternal();
      if (internal != null) {
        removeInternalFolder(internal);
      }

      // Remove from checks pool
      checks.remove(uuid);
    }
  }

  public void pauseFinish(Long uuid) {
    // Update db
    getContext().send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.PAUSE, uuid));
    // Refresh tasks
    context.send(BasicConfig.MODULE_TIMER, new TimerMessage(TimerMessage.Type.RUN, JobsStatusTask.class));
  }

  public void handleGlobalStatus(GlobalStatusMessage gm, boolean silence) {
    if (gm.isNew()) {
      // New file check
      Long uuid = gm.getUuid();
      FileCheck fc = new FileCheck(uuid);
      boolean pending = false;
      if (runningChecks() >= DpFManagerConstants.MAX_CHECKS) {
        // Add pending check
        fc.setInitialTask(gm.getRunnable());
        pendingChecks.add(fc);
        pending = true;
      } else {
        //Start now
        checks.put(uuid, fc);
        context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(uuid, gm.getRunnable()));
      }
      context.send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.NEW, uuid, gm.getInput(), pending));
    } else if (gm.isInit()) {
      // Init file check
      FileCheck fc = checks.get(gm.getUuid());
      fc.init(gm.getSize(), gm.getConfig(), gm.getInternal(), gm.getInput());
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("startingCheck").replace("%1", gm.getInput())));
      context.send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.INIT, fc.getUuid(), fc.getTotal(), fc.getInternal()));
    } else if (gm.isFinish()) {
      // Finish file check
      FileCheck fc = checks.get(gm.getUuid());
      removeZipFolder(fc.getInternal());
      removeDownloadFolder(fc.getInternal());
      moveServerFolder(fc.getUuid(), fc.getInternal());
      if (context.isGui()) {
        // Notify task manager
        needReload = true;
      } else if (!silence) {
        // No ui, show to user
        showToUser(fc.getInternal(), fc.getConfig().getOutput());
      }
      context.send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.FINISH, gm.getUuid()));
      checks.remove(gm.getUuid());
      totalChecks++;
      if (totalChecks >= 10) {
        System.gc();
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("runGC")));
        totalChecks = 0;
      }
      // Start pending checks
      startPendingChecks();
    } else if (context.isGui() && gm.isReload()) {
      // Ask for reload
      if (needReload) {
        needReload = false;
        context.send(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
      }
    }
  }

  public void finishIndividual(IndividualReport ir, Long uuid) {
    FileCheck fc = checks.get(uuid);
    if (fc != null) {
      if (ir != null) {
        // Individual report finished
        fc.addIndividual(ir);
      } else {
        // Individual with errors
        fc.addError();
      }
      // Check if all finished
      if (fc.allFinished()) {
        // Tell reports module
        context.send(BasicConfig.MODULE_REPORT, new GlobalReportMessage(uuid, fc.getIndividuals(), fc.getConfig()));
      }
      context.send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.UPDATE, uuid));
    }
  }

  private void startPendingChecks() {
    if (!pendingChecks.isEmpty()) {
      FileCheck fc = pendingChecks.poll();
      checks.put(fc.getUuid(), fc);
      context.send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.START, fc.getUuid()));
      context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(fc.getUuid(), fc.getInitialTask()));
    }
  }

  private int runningChecks() {
    return checks.size();
  }

  /**
   * Remove functions
   */
  public void removeZipFolder(String internal) {
    try {
      File zipFolder = new File(internal + "zip");
      if (zipFolder.exists() && zipFolder.isDirectory()) {
        FileUtils.deleteDirectory(zipFolder);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("excZip"), e));
    }
  }

  private void removeDownloadFolder(String internal) {
    try {
      File zipFolder = new File(internal + "download");
      if (zipFolder.exists() && zipFolder.isDirectory()) {
        FileUtils.deleteDirectory(zipFolder);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("excDownload"), e));
    }
  }

  private void removeInternalFolder(String internal) {
    try {
      File folder = new File(internal);
      if (folder.exists() && folder.isDirectory()) {
        FileUtils.deleteDirectory(folder);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("excInternal"), e));
    }
  }

  private void removeServerFolder(Long uuid) {
    try {
      File folder = new File(DPFManagerProperties.getServerDir() + "/" + uuid);
      if (folder.exists() && folder.isDirectory()) {
        FileUtils.deleteDirectory(folder);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("excServer"), e));
    }
  }

  private void moveServerFolder(Long uuid, String internal) {
    try {
      File dest = new File(internal + "input");
      File src = new File(DPFManagerProperties.getServerDir() + "/" + uuid);
      if (src.exists() && src.isDirectory()) {
        FileUtils.moveDirectory(src, dest);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in remove server folder", e));
    }
  }

  /**
   * Show report
   */
  private void showToUser(String internal, String output) {
    String name = "report.html";
    String htmlPath = internal + name;
    if (output != null) {
      htmlPath = output + "/" + name;
    }
    File htmlFile = new File(htmlPath);
    if (htmlFile.exists() && Desktop.isDesktopSupported()) {
      try {
        String fullHtmlPath = htmlFile.getAbsolutePath();
        fullHtmlPath = fullHtmlPath.replaceAll("\\\\", "/");
        Desktop.getDesktop().browse(new URI("file:///" + fullHtmlPath.replaceAll(" ", "%20")));
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("browserError"), e));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("deskServError")));
    }
  }

}

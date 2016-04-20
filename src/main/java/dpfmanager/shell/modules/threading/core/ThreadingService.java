package dpfmanager.shell.modules.threading.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.threading.messages.CheckTaskMessage;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;
import dpfmanager.shell.modules.threading.runnable.PhassesRunnable;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_THREADING)
@Scope("singleton")
public class ThreadingService extends DpfService {

  private Map<Long, FileCheck> checks;
  private ExecutorService executor;
  private boolean needReload;

  @PostConstruct
  public void init() {
    // No context yet
    checks = new HashMap<>();
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Set maximum threads to " + (Runtime.getRuntime().availableProcessors()-1)));
    executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-1);
    needReload = false;
  }

  @PreDestroy
  public void finish() {
    // Finish executor
    executor.shutdownNow();
  }

  @Override
  protected void handleContext(DpfContext context) {
  }

  public void run(DpfRunnable runnable) {
    runnable.setContext(getContext());
    executor.execute(runnable);
  }

  public void handleGlobalStatus(GlobalStatusMessage gm, boolean silence) {
    if (gm.isInit()) {
      // Init new file check
      FileCheck fc = new FileCheck(gm.getUuid(), gm.getSize(), gm.getConfig(), gm.getInternal(), gm.getInput());
      checks.put(gm.getUuid(), fc);
      context.sendGui(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Starting check: " + gm.getInput()));
      context.sendGui(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Type.INIT, fc));
    } else if (gm.isFinish()) {
      // Finisdh file check
      FileCheck fc = checks.get(gm.getUuid());
      removeZipFolder(fc.getInternal());
      if (context.isGui()) {
        // Notify task manager
        needReload = true;
        context.sendGui(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Type.UPDATE, fc));
      } else if (!silence) {
        // No ui, show to user
        showToUser(fc.getInternal(), fc.getConfig().getOutput());
      }
      checks.remove(gm.getUuid());
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
    if (ir != null) {
      // Individual report finished
      fc.addIndividual(ir);

      // Check if all finished
      if (fc.allFinished()) {
        // Tell reports module
        context.send(BasicConfig.MODULE_REPORT, new GlobalReportMessage(fc.getIndividuals(), fc.getConfig()));
      }
    } else {
      // Individual with errors
      fc.addError();
    }
    context.sendGui(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Type.UPDATE, fc));
  }

  public void removeZipFolder(String internal) {
    try {
      File zipFolder = new File(internal + "zip");
      if (zipFolder.exists() && zipFolder.isDirectory()) {
        FileUtils.deleteDirectory(zipFolder);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in remove zip", e));
    }
  }

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
        Desktop.getDesktop().browse(new URI(fullHtmlPath));
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Error opening the bowser with the global report.", e));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Desktop services not suported."));
    }
  }

  public void runPhasses(List<DpfRunnable> firsts, List<DpfRunnable> seconds) {
    PhassesRunnable pr = new PhassesRunnable(firsts, seconds);
    run(pr);
  }

}

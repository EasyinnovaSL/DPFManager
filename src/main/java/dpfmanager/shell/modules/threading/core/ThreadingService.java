package dpfmanager.shell.modules.threading.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
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

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_THREADING)
@Scope("singleton")
public class ThreadingService extends DpfService {

  private Map<Long, FileCheck> checks;

  @PostConstruct
  public void init() {
    // No context yet
    checks = new HashMap<>();
  }

  @Override
  protected void handleContext(DpfContext context) {
  }

  public void run(DpfRunnable runnable) {
    runnable.setContext(getContext());
    Thread thread = new Thread(runnable);
    thread.start();
  }

  public void handleGlobalStatus(GlobalStatusMessage gm) {
    if (gm.isInit()) {
      // Init new file check
      FileCheck fc = new FileCheck(gm.getUuid(), gm.getSize(), gm.getConfig(), gm.getInternal());
      checks.put(gm.getUuid(), fc);
    } else {
      // Finisdh file check
      String internal = checks.get(gm.getUuid()).getInternal();
      List<String> formats = checks.get(gm.getUuid()).getConfig().getFormats();
      String output = checks.get(gm.getUuid()).getConfig().getOutput();
      removeZipFolder(internal);
      checks.remove(gm.getUuid());
      if (context.isGui()){
        showGui(internal, formats);
      } else {
        showToUser(internal, output);
      }
    }
  }

  public void finishIndividual(IndividualReport ir) {
    // Individual report finished
    FileCheck fc = checks.get(ir.getUuid());
    fc.addIndividual(ir);

    // Check if all finished
    if (fc.allFinished()) {
      // Tell reports module
      context.send(BasicConfig.MODULE_REPORT, new GlobalReportMessage(fc.getIndividuals(), fc.getConfig()));
    }
  }

  public void removeZipFolder(String internal){
    try{
      File zipFolder = new File(internal+"zip");
      if (zipFolder.exists() && zipFolder.isDirectory()){
        FileUtils.deleteDirectory(zipFolder);
      }
    } catch (Exception e){
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in remove zip", e));
    }
  }

  private void showToUser(String internal, String output){
    String name = "report.html";
    String htmlPath = internal + name;
    if (output != null){
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

  private void showGui(String filefolder, List<String> formats) {
    String type = "";
    String path = "";
    if (formats.contains("HTML")) {
      type = "html";
      path = filefolder + "report.html";
    } else if (formats.contains("XML")) {
      type = "xml";
      path = filefolder + "summary.xml";
    } else if (formats.contains("JSON")) {
      type = "json";
      path = filefolder + "summary.json";
    } else if (formats.contains("PDF")) {
      type = "pdf";
      path = filefolder + "report.pdf";
    }

    // Show reports
    if (!type.isEmpty()) {
      ArrayMessage am = new ArrayMessage();
      am.add(GuiConfig.PERSPECTIVE_DESSIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
      am.add(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
      am.add(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
      am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
      am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
      context.sendGui(GuiConfig.PERSPECTIVE_DESSIGN, am);
    } else {
      // No format
      context.sendGui(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.WARNING, "No output format file was selected", formats.toString()));
      // Hide loading
      ArrayMessage am = new ArrayMessage();
      am.add(GuiConfig.PERSPECTIVE_DESSIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
      am.add(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
      context.sendGui(GuiConfig.PERSPECTIVE_DESSIGN, am);
    }
  }

  public void runPhasses(List<DpfRunnable> firsts, List<DpfRunnable> seconds) {
    PhassesRunnable pr = new PhassesRunnable(firsts, seconds);
    run(pr);
  }

}

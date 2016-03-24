package dpfmanager.shell.modules.conformancechecker;

import dpfmanager.conformancechecker.tiff.ConformanceChecker;
import dpfmanager.conformancechecker.tiff.ProcessInput;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerModel;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.report.messages.ReportMessage;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_CONFORMANCE,
    name = BasicConfig.MODULE_CONFORMANCE,
    active = true)
public class ConformanceCheckerModule extends DpfModule {

  @Resource
  protected Context context;

  private ConformanceCheckerModel model;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(ConformanceMessage.class)){
      String path = dpfMessage.getTypedMessage(ConformanceMessage.class).getPath();
      String input = dpfMessage.getTypedMessage(ConformanceMessage.class).getInput();
      if (!getModel().readConfig(path)) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "Error reading configuration file"));
      } else {
        // Everything OK!
        getContext().send(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.SHOW));
        startCheck(input);
      }

    }
  }

  private void startCheck(String filename){
    ArrayList<String> files = new ArrayList<>();

    // Create a background task, because otherwise the loading message is not shown
    Task<Integer> task = new Task<Integer>() {
      @Override
      protected Integer call() throws Exception {
        try {
          if (new File(filename).isDirectory()) {
            File[] listOfFiles = new File(filename).listFiles();
            for (int j = 0; j < listOfFiles.length; j++) {
              if (listOfFiles[j].isFile()) {
                files.add(listOfFiles[j].getPath());
              }
            }
          } else {
            for (String sfile : filename.split(";")) {
              files.add(sfile);
            }
          }

          ProcessInput pi = new ProcessInput();
          pi.setContext(context);
          ArrayList<String> formats = getModel().getConfig().getFormats();

          String filefolder = pi.ProcessFiles(files, getModel().getConfig(), true);
          if (pi.isOutOfmemory()) {
            getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
          }

          // When finish, show report
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
            am.add(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
            am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
            getContext().send(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, am);
          } else {
            // No format
            getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.WARNING, "No output format file was selected", formats.toString()));
          }

          // Hide loading
          getContext().send(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
        } catch (Exception ex) {
          getContext().send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception occured", ex));
        } catch (OutOfMemoryError er) {
          getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
        }
        return 0;
      }
    };

    //start the background task
    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  private File getFileByPath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      String configDir = DPFManagerProperties.getConfigDir();
      file = new File(configDir + "/" + path);
    }
    return file;
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    model = new ConformanceCheckerModel();
  }

  private ConformanceCheckerModel getModel(){
    return model;
  }

  @Override
  public Context getContext(){
    return context;
  }

}

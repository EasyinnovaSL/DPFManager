package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.shell.core.DPFManagerProperties;
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
import javafx.concurrent.Task;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_CONFORMANCE)
@Scope("singleton")
public class ConformanceCheckerService {

  private ConformanceCheckerModel model;
  private DpfContext context;

  @PostConstruct
  private void init(){
    model = new ConformanceCheckerModel();
  }

  public void setContext(DpfContext c){
    context = c;
  }

  public boolean readConfig(String path){
    return getModel().readConfig(path);
  }

  public void startCheck(String filename){
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
            context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
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
            context.sendGui(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, am);
          } else {
            // No format
            context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.WARNING, "No output format file was selected", formats.toString()));
          }

          // Hide loading
          context.sendGui(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
          context.sendGui(GuiConfig.PERSPECTIVE_DESSIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
        } catch (Exception ex) {
          context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception occured", ex));
        } catch (OutOfMemoryError er) {
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
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

  private ConformanceCheckerModel getModel(){
    return model;
  }

}

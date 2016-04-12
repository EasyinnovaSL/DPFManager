package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.report.messages.StatusMessage;
import javafx.concurrent.Task;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_CONFORMANCE)
@Scope("singleton")
public class ConformanceCheckerService extends DpfService {

  private ConformanceCheckerModel model;
  private List<String> tempFiles;

  private int recursive;
  private boolean silence;

  @PostConstruct
  private void init(){
    model = new ConformanceCheckerModel();
    tempFiles = new ArrayList<>();
    setDefaultParameters();
  }

  @Override
  protected void handleContext(DpfContext context){
  }

  public boolean readConfig(String path){
    return getModel().readConfig(path);
  }

  public void setConfig(Configuration config){
    getModel().setConfig(config);
  }

  private void setDefaultParameters(){
    recursive = 1;
    silence = false;
  }

  public void setParameters(Configuration config, Integer r, Boolean s){
    if (config != null) {
      getModel().setConfig(config);
    }
    if (r != null){
      recursive = r;
    }
    if (s != null){
      silence = s;
    }
  }

  public void startMultiCheck(List<String> files){
    for (String filename : files){
      startCheck(filename);
    }
  }

  public void startCheck(String filename){
    try {
      ArrayList<String> files = new ArrayList<>();
      if (new File(filename).isDirectory()) {
        // Process directory
        addDirectoryToFiles(files, new File(filename), recursive, 1);
      } else {
        // Process list of files
        for (String sfile : filename.split(";")) {
          files.add(sfile);
        }
      }

      // Init
      String internalReportFolder = ReportGenerator.createReportPath();
      context.send(BasicConfig.MODULE_REPORT, new StatusMessage(StatusMessage.Type.INIT, internalReportFolder));

      ProcessInput pi = new ProcessInput();
      pi.setContext(context);

      String filefolder = pi.ProcessFiles(files, getModel().getConfig(), internalReportFolder);
      if (pi.isOutOfmemory()) {
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
      }
      tempFiles = pi.getTempFiles();

      // When finish, show report
      context.send(BasicConfig.MODULE_REPORT, new StatusMessage(StatusMessage.Type.FINISH, getModel().getConfig().getFormats(), filefolder, silence));
    } catch (Exception ex) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception occured", ex));
    } catch (OutOfMemoryError er) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
    }
  }

  private void addDirectoryToFiles(ArrayList<String> files, File directory, int recursive, int currentlevel) {
    File[] listOfFiles = directory.listFiles();
    for (int j = 0; j < listOfFiles.length; j++) {
      if (listOfFiles[j].isFile()) {
        files.add(listOfFiles[j].getPath());
      } else if (listOfFiles[j].isDirectory() && currentlevel < recursive) {
        addDirectoryToFiles(files, listOfFiles[j], recursive, currentlevel + 1);
      }
    }
  }

  public void deleteTmpFiles(){
    for (String file : tempFiles){
      new File(file).delete();
    }
    tempFiles.clear();
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

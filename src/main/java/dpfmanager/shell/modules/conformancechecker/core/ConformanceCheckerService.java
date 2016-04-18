package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.runnable.ConformanceRunnable;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;

import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
  private void init() {
    model = new ConformanceCheckerModel();
    tempFiles = new ArrayList<>();
    setDefaultParameters();
  }

  @Override
  protected void handleContext(DpfContext context) {
  }

  public boolean readConfig(String path) {
    return getModel().readConfig(path);
  }

  public void setConfig(Configuration config) {
    getModel().setConfig(config);
  }

  private void setDefaultParameters() {
    recursive = 1;
    silence = false;
  }

  public void setParameters(Configuration config, Integer r) {
    if (config != null) {
      getModel().setConfig(config);
    }
    if (r != null) {
      recursive = r;
    }
  }

  public void startMultiCheck(List<String> files) {
    String finalFiles = "";
    for (String filename : files) {
      finalFiles += filename + ";";
    }
    finalFiles = finalFiles.substring(0, finalFiles.length() - 1);
    startCheck(finalFiles);
  }

  public void startCheck(String filename) {
    String internalReportFolder = ReportGenerator.createReportPath();

    try {
      ArrayList<String> files = new ArrayList<>();
      if (new File(filename).isDirectory()) {
        // Process directory
        addDirectoryToFiles(files, new File(filename), recursive, 1);
      } else {
        if (filename.toLowerCase().endsWith(".zip") || filename.toLowerCase().endsWith(".rar")) {
          // Process zip
          List<String> zipFiles = getFilesInZip(filename, internalReportFolder);
          files.addAll(zipFiles);
        } else {
          // Process list of files
          for (String sfile : filename.split(";")) {
            files.add(sfile);
          }
        }
      }

      // Init
      long uuid = System.currentTimeMillis();
      context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.INIT, uuid, files.size(), getModel().getConfig(), internalReportFolder));

      // Now process files
      ProcessFiles(files, getModel().getConfig(), internalReportFolder, uuid);
    } catch (Exception ex) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception occured", ex));
    } catch (OutOfMemoryError er) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
    }
  }

  /**
   * Process a list of files and generate individual ang global reports.
   *
   * @param files  the files
   * @param config the config
   * @return the path to the internal report folder
   */
  private String ProcessFiles(ArrayList<String> files, Configuration config, String internalReportFolder, long uuid) {
    // Process each input of the list
    int idReport = 1;
    for (final String filename : files) {
      ConformanceRunnable run = new ConformanceRunnable();
      run.setParameters(filename, idReport, internalReportFolder, config, uuid);
      context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(run));
      idReport++;
    }

    if (!new File(internalReportFolder).exists()) {
      internalReportFolder = null;
    }

    return internalReportFolder;
  }

  private List<String> getFilesInZip(String filename, String internal) {
    List<String> files = new ArrayList<>();
    try {
      // Create zip folder
      File zipFolder = new File(internal + "zip/");
      if (!zipFolder.exists()){
        zipFolder.mkdirs();
      }

      // Extract it
      ZipFile zipFile = new ZipFile(filename);
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        // Process each file contained in the compressed file
        ZipEntry entry = entries.nextElement();
        String name = entry.getName();
        File targetFile = new File(internal + "zip/" + name);
        if (name.endsWith("/")){
          // Directory
          targetFile.mkdirs();
        } else {
          // File
          OutputStream outStream = new FileOutputStream(targetFile);
          InputStream inStream = zipFile.getInputStream(entry);
          byte[] buffer = new byte[8 * 1024];
          int bytesRead;
          while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
          }
          outStream.close();
          inStream.close();
          files.add(targetFile.getAbsolutePath());
        }
      }
      zipFile.close();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception during unzip", e));
    }
    return files;
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

  public void deleteTmpFiles() {
    for (String file : tempFiles) {
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

  private ConformanceCheckerModel getModel() {
    return model;
  }

}

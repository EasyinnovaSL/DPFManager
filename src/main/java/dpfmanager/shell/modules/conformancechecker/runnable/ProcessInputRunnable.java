package dpfmanager.shell.modules.conformancechecker.runnable;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 04/05/2016.
 */
public class ProcessInputRunnable extends DpfRunnable {

  private DpfContext context;

  private String filename;
  private String inputStr;
  private String internalReportFolder;
  private int recursive;
  private Configuration config;
  private List<String> files;
  private int toWait;

  private List<DpfRunnable> runnables;

  public ProcessInputRunnable(String filename, String inputStr, String internalReportFolder, int recursive, Configuration config) {
    this.filename = filename;
    this.inputStr = inputStr;
    this.internalReportFolder = internalReportFolder;
    this.recursive = recursive;
    this.config = config;
    files = new ArrayList<>();
    runnables = new ArrayList<>();
    toWait = 0;
  }

  @Override
  public void runTask() {
    // Process input
    if (internalReportFolder == null) {
      internalReportFolder = ReportGenerator.createReportPath();
    }
    parseInput(filename);

    // Start waiting the zips and urls runnables
    System.out.println("Waiting for " + toWait + " get inputs.");
    context.send(BasicConfig.MODULE_CONFORMANCE, new ProcessInputMessage(ProcessInputMessage.Type.WAIT, getUuid(), files, config, internalReportFolder, inputStr, toWait));

    // Send runnables
    for (DpfRunnable run : runnables) {
      System.out.println("Send get input runnable");
      context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(getUuid(), run));
    }
  }

  private void tractZipFile(File zipFile) {
    // Send runnable to threading
    GetInputRunnable gir = new GetInputRunnable(zipFile, internalReportFolder, getUuid());
    runnables.add(gir);
    toWait++;
  }

  private void tractUrl(String url) {
    // Send runnable to threading
    GetInputRunnable gir = new GetInputRunnable(url, internalReportFolder, getUuid());
    runnables.add(gir);
    toWait++;
  }

  public void parseInput(String input) {
    int filetype = getType(input);
    switch (filetype) {
      case -1:
        // ERROR
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error in input path " + input));
        break;
      case 0:
        // Folder
        addDirectoryToFiles(new File(input), recursive, 1);
        break;
      case 1:
        // Zip
        tractZipFile(new File(input));
        break;
      case 2:
        // URL
        tractUrl(input);
        break;
      case 3:
        // List of files
        for (String sfile : filename.split(";")) {
          parseInput(sfile);
        }
        break;
      case 4:
        // Simple file
        files.add(input);
        break;
    }
  }

  private int getType(String filename) {
    if (isUrl(filename)) {
      // URL
      return 2;
    } else if (new File(filename).isDirectory()) {
      // Folder
      return 0;
    } else if (new File(filename).isFile()) {
      if (filename.toLowerCase().endsWith(".zip") || filename.toLowerCase().endsWith(".rar")) {
        // ZIP
        return 1;
      } else {
        // Simple file
        return 4;
      }
    } else if (filename.contains(";")) {
      // List of files
      return 3;
    }
    return -1;
  }

  private void addDirectoryToFiles(File directory, int recursive, int currentlevel) {
    File[] listOfFiles = directory.listFiles();
    for (int j = 0; j < listOfFiles.length; j++) {
      File current = listOfFiles[j];
      String name = current.getName();
      if (current.isFile()) {
        if (name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".rar")) {
          // Zip
          tractZipFile(current);
        } else {
          // File
          files.add(current.getPath());
        }
      } else if (current.isDirectory() && currentlevel < recursive) {
        addDirectoryToFiles(current, recursive, currentlevel + 1);
      }
    }
  }

  public String getInput() {
    return inputStr;
  }

  /**
   * Checks if the filename is an URL.
   *
   * @param filename the filename
   * @return true, if it is a url
   */
  private boolean isUrl(String filename) {
    boolean ok = true;
    try {
      new java.net.URL(filename);
    } catch (Exception ex) {
      ok = false;
    }
    return ok;
  }

  @Override
  public void handleContext(DpfContext context) {
    this.context = context;
  }
}

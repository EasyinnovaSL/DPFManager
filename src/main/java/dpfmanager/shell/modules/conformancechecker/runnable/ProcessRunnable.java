package dpfmanager.shell.modules.conformancechecker.runnable;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import org.apache.logging.log4j.Level;

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

/**
 * Created by Adrià Llorens on 04/05/2016.
 */
public class ProcessRunnable extends DpfRunnable {

  private DpfContext context;

  private String filename;
  private String inputStr;
  private String internalReportFolder;
  private int recursive;
  private Configuration config;

  public ProcessRunnable(String filename, String inputStr, String internalReportFolder, int recursive, Configuration config) {
    this.filename = filename;
    this.inputStr = inputStr;
    this.internalReportFolder = internalReportFolder;
    this.recursive = recursive;
    this.config = config;
  }

  @Override
  public void runTask() {
    startCheck(filename, inputStr, internalReportFolder);
  }

  public String getInput() {
    return inputStr;
  }

  public void startCheck(String filename, String inputStr, String internalReportFolder) {
    if (internalReportFolder == null) {
      internalReportFolder = ReportGenerator.createReportPath();
    }
    int filetype = getType(filename);
    ArrayList<String> files = new ArrayList<>();

    switch (filetype) {
      case -1:
        // ERROR
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error in input path " + filename));
        break;
      case 0:
        // Folder
        addDirectoryToFiles(files, new File(filename), recursive, 1);
        break;
      case 1:
        // Zip
        List<String> zipFiles = getFilesInZip(filename, internalReportFolder);
        files.addAll(zipFiles);
        break;
      case 2:
        // URL
        try {
          // Download the file and store it in a temporary file
          InputStream input = new java.net.URL(filename).openStream();
          String filename2 = createTempFile(internalReportFolder, new File(filename).getName(), input);
          files.add(filename2);
          startCheck(filename2, inputStr, internalReportFolder);
          return;
        } catch (Exception ex) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Error in URL " + filename));
        }
        break;
      case 3:
        // List of files
        for (String sfile : filename.split(";")) {
          files.add(sfile);
        }
        break;
      case 4:
        // Simple file
        files.add(filename);
        break;
    }

    try {
      // Init
      context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.INIT, getUuid(), files.size(), config, internalReportFolder, inputStr));

      // Now process files
      ProcessFiles(files, config, internalReportFolder, getUuid());
    } catch (OutOfMemoryError er) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
    }
  }

  private int getType(String filename) {
    if (filename.startsWith("http")) {
      // URL
      return 2;
    } else if (new File(filename).isDirectory()) {
      // Folder
      return 0;
    } else if (new File(filename).isFile()) {
      if (filename.toLowerCase().endsWith(".zip") || filename.toLowerCase().endsWith(".rar")) {
        // ZIP
        return 1;
      } else if (filename.contains(";")) {
        // List of files
        return 3;
      } else {
        // Simple file
        return 4;
      }
    }
    return -1;
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
      context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(uuid, run));
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
      if (!zipFolder.exists()) {
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
        if (name.endsWith("/")) {
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

  /**
   * Creates a temporary file to store the input stream.
   *
   * @param internal the folder to store the created temporary file
   * @param name     the name of the temporary file
   * @param stream   the input stream
   * @return the path to the created temporary file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private String createTempFile(String internal, String name, InputStream stream) throws IOException {
    // Create the path to the temporary folder
    String filename = internal + "download/" + name;
    File targetFile = new File(filename);
    if (!targetFile.exists()) {
      targetFile.getParentFile().mkdirs();
    }

    // Write the file
    OutputStream outStream = new FileOutputStream(targetFile);
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = stream.read(buffer)) != -1) {
      outStream.write(buffer, 0, bytesRead);
    }
    outStream.close();
    return filename;
  }

  @Override
  public void handleContext(DpfContext context) {
    this.context = context;
  }
}

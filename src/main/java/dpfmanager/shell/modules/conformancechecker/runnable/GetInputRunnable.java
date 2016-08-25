package dpfmanager.shell.modules.conformancechecker.runnable;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
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
 * Created by Adrià Llorens on 09/05/2016.
 */
public class GetInputRunnable extends DpfRunnable {

  private Long uuid;
  private List<String> files;
  private String internalReportFolder;

  private File fileZip;
  private String url;
  private boolean error;

  @Override
  public void handleContext(DpfContext context) {
    files = new ArrayList<>();
    error = false;
  }

  // Url
  public GetInputRunnable(String ur, String internal, Long u){
    uuid = u;
    internalReportFolder = internal;
    url = ur;
    fileZip = null;
  }

  // Zip file
  public GetInputRunnable(File file, String internal, Long u){
    uuid = u;
    internalReportFolder = internal;
    fileZip = file;
  }

  @Override
  public void runTask() {
    // Get the input
    if (fileZip != null){
      runZip();
    } else {
      runUrl();
    }

    // Notify
    context.send(BasicConfig.MODULE_CONFORMANCE, new ProcessInputMessage(ProcessInputMessage.Type.FILE, getUuid(), files));
  }

  /**
   * Unzip file
   */
  private void runZip(){
    try {
      // Create zip folder
      File zipFolder = new File(internalReportFolder + "zip/");
      if (!zipFolder.exists()) {
        zipFolder.mkdirs();
      }

      // Extract it
      ZipFile zipFile = new ZipFile(fileZip);
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        // Process each file contained in the compressed file
        ZipEntry entry = entries.nextElement();
        String name = entry.getName();
        File targetFile = new File(internalReportFolder + "zip/" + name);
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
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("unzipException"), e));
    }
  }

  /**
   * Download file
   */
  private void runUrl(){
    try {
      // Download the file and store it in a temporary file
      InputStream is = new java.net.URL(url).openStream();
      String filename2 = createTempFile(internalReportFolder, new File(url).getName(), is);
      if (filename2.toLowerCase().endsWith(".zip") || filename2.toLowerCase().endsWith(".rar")) {
        // Downloaded zip
        fileZip = new File(filename2);
        runZip();
      } else {
        // Downloaded file
        files.add(filename2);
      }
      return;
    } catch (Exception ex) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorUrl").replace("%1",url)));
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

}

package dpfmanager.shell.conformancechecker;

import dpfmanager.shell.interfaces.UserInterface;
import dpfmanager.shell.reporting.IndividualReport;
import dpfmanager.shell.reporting.ReportGenerator;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.reader.TiffReader;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by easy on 04/09/2015.
 */
public class ProcessInput {
  public boolean outOfmemory = false;
  private Scene scene;
  private int idReport;

  /**
   * Sets the scene.
   *
   * @param scene the scene
   */
  public void setScene(Scene scene) {
    this.scene = scene;
  }

  /**
   * Process files string.
   *
   * @param files   the files
   * @param config  the config
   * @param silence the silence
   * @return the string
   */
  public String ProcessFiles(ArrayList<String> files, Configuration config, boolean silence) {
    ArrayList<IndividualReport> individuals = new ArrayList<IndividualReport>();
    String internalReportFolder = ReportGenerator.createReportPath();
    int n=files.size();
    idReport=1;
    for (final String filename : files) {
      System.out.println("");
      System.out.println("Processing file " + filename);
      List<IndividualReport> indReports = processFile(filename, internalReportFolder, config);
      if (scene != null) {
        Platform.runLater(() -> ((Label) scene.lookup("#lblLoading")).setText("Processing..." + (files.indexOf(filename)+1) + "/" + n));
      }
      if (indReports.size() > 0) {
        individuals.addAll(indReports);
      }
      idReport++;
    }

    // Global report
    String summaryXml = null;
    try {
      summaryXml = ReportGenerator.makeSummaryReport(internalReportFolder, individuals, config, silence);
    } catch (OutOfMemoryError e) {
      System.err.println("Out of memory.");
      outOfmemory = true;
    }

    // Send report over FTP (only for alpha testing)
    try {
      if(UserInterface.getFeedback() && summaryXml != null) {
        sendFtpCamel(summaryXml);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return internalReportFolder;
  }

  private List<ConformanceChecker> getConformanceCheckers() {
    TiffConformanceChecker tiffcc = new TiffConformanceChecker();
    ArrayList<ConformanceChecker> l = new ArrayList<>();
    l.add(tiffcc);
    return l;
  }

  /**
   * Process a Tiff file.
   *
   * @param filename the filename
   * @param internalReportFolder the internal report folder
   * @return the list
   */
  private List<IndividualReport> processFile(String filename, String internalReportFolder, Configuration config) {
    List<IndividualReport> indReports = new ArrayList<IndividualReport>();
    IndividualReport ir = null;
    if (filename.toLowerCase().endsWith(".zip") || filename.toLowerCase().endsWith(".rar")) {
      // Zip File
      try {
        System.err.println(filename);
        ZipFile zipFile = new ZipFile(filename);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
          ZipEntry entry = entries.nextElement();
          for (ConformanceChecker cc : getConformanceCheckers()) {
            if (cc.acceptsFile(entry.getName())) {
              InputStream stream = zipFile.getInputStream(entry);
              String filename2 = createTempFile(internalReportFolder, entry.getName(), stream);
              ir = cc.processFile(filename2, entry.getName(), internalReportFolder, config, idReport);
              if (ir != null) {
                indReports.add(ir);
              } else{
                outOfmemory = true;
                break;
              }
              File file = new File(filename2);
              file.delete();
              break;
            }
          }
        }
        zipFile.close();
      } catch (Exception ex) {
        System.err.println("Error reading zip file [" + ex.toString() + "]");
      }
    } else if (isUrl(filename)) {
      // URL
      try {
        boolean found = false;
        for (ConformanceChecker cc : getConformanceCheckers()) {
          if (cc.acceptsFile(filename)) {
            InputStream input = new java.net.URL(filename).openStream();
            String filename2 = createTempFile(internalReportFolder, new File(filename).getName(), input);
            filename = java.net.URLDecoder.decode(filename, "UTF-8");
            ir = cc.processFile(filename2, filename, internalReportFolder, config, idReport);
            if (ir != null) {
              indReports.add(ir);
            } else{
              outOfmemory = true;
            }
            File file = new File(filename2);
            file.delete();
            found = true;
            break;
          }
        }
        if (!found) {
          System.err.println("The file in the URL " + filename + " is not a Tiff");
        }
      } catch (Exception ex) {
        System.out.println("Error in URL " + filename);
      }
    } else {
      boolean found = false;
      for (ConformanceChecker cc : getConformanceCheckers()) {
        if (cc.acceptsFile(filename)) {
          // File
          try {
            ir = cc.processFile(filename, filename, internalReportFolder, config, idReport);
            if (ir != null) {
              indReports.add(ir);
            } else{
              outOfmemory = true;
            }
          } catch (Exception ex) {
            System.err.println("Error in File " + filename);
          }
          found = true;
          break;
        }
      }
      if (!found) {
        // Anything else
        System.err.println("File " + filename + " is not a Tiff");
      }
    }
    return indReports;
  }

  /**
   * Creates the temp file.
   *
   * @param name the name
   * @param stream the stream
   * @return the string
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private String createTempFile(String folder, String name, InputStream stream) throws IOException {
    String filename2 = "x" + name;
    if (filename2.contains("/")) {
      filename2 = filename2.substring(filename2.lastIndexOf("/") + 1);
    }
    while (new File(filename2).isFile()) {
      filename2 = "x" + filename2;
    }
    filename2 = folder + "/" + filename2;
    File targetFile = new File(filename2);
    OutputStream outStream = new FileOutputStream(targetFile);
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = stream.read(buffer)) != -1) {
      outStream.write(buffer, 0, bytesRead);
    }
    outStream.close();
    return filename2;
  }

  /**
   * Checks if is url.
   *
   * @param filename the filename
   * @return true, if is url
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

  /**
   * Send ftp.
   *
   * @param summaryXml the summary xml
   * @throws NoSuchAlgorithmException the no such algorithm exception
   */
  private void sendFtpCamel(String summaryXml)
      throws NoSuchAlgorithmException {
    String ftp = "84.88.145.109";
    String user = "preformaapp";
    String password = "2.eX#lh>";

    CamelContext context = new DefaultCamelContext();
    try {
      context.addRoutes(new RouteBuilder() {
        public void configure() {
          from("direct:sendFtp").to("sftp://" + user + "@" + ftp + "/?password=" + password);
        }
      });
      ProducerTemplate template = context.createProducerTemplate();
      context.start();
      template.sendBody("direct:sendFtp", summaryXml);
      context.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

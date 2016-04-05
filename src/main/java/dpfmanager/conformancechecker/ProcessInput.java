package dpfmanager.conformancechecker;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.external.ExternalConformanceChecker;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.jacpfx.rcp.context.Context;

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
 * Created by Victor Muñoz on 04/09/2015.
 */
public class ProcessInput {
  private boolean outOfmemory = false;
  private int idReport;
  private Context context;

  /**
   * Sets the label.
   *
   * @param context the JacpFX Context
   */
  public void setContext(Context context) {
    this.context = context;
  }

  /**
   * An out of memory occurred.
   *
   * @return the boolean
   */
  public boolean isOutOfmemory() {
    return outOfmemory;
  }

  /**
   * Process a list of files and generate individual ang global reports.
   *
   * @param files   the files
   * @param config  the config
   * @param silence do not open the report at the end
   * @return the path to the internal report folder
   */
  public String ProcessFiles(ArrayList<String> files, Configuration config, boolean silence) {
    ArrayList<IndividualReport> individuals = new ArrayList<>();
    String internalReportFolder = ReportGenerator.createReportPath();
    int n=files.size();
    idReport=1;

    // Process each input of the list which can be a file, folder, zip or url
    for (final String filename : files) {
      System.out.println("");
      System.out.println("Processing file " + filename);

      // Process the input and get a list of individual reports
      List<IndividualReport> indReports = processFile(filename, internalReportFolder, config);
      individuals.addAll(indReports);

      if (context != null) {
        context.send(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.TEXT, "Processing..." + (files.indexOf(filename) + 1) + "/" + n));
      }
      idReport++;
    }

    // Generate global report
    String summaryXml = null;
    try {
      summaryXml = ReportGenerator.makeSummaryReport(internalReportFolder, individuals, config, silence);
    } catch (OutOfMemoryError e) {
      System.err.println("Out of memory.");
      outOfmemory = true;
    }

    // Send report over FTP
    try {
      if(DPFManagerProperties.getFeedback() && summaryXml != null) {
        sendFtpCamel(summaryXml);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (!new File(internalReportFolder).exists()) {
     internalReportFolder = null;
    }

    return internalReportFolder;
  }

  /**
   * Get the list of conformance checkers available to use.
   */
  private List<ConformanceChecker> getConformanceCheckers() {
    TiffConformanceChecker tiffcc = new TiffConformanceChecker();
    ArrayList<ConformanceChecker> l = new ArrayList<>();
    l.add(tiffcc);

    String path = "package/resources/plugins/video/MediaConch.exe";
    if (!new File(path).exists()) path = "plugins/video/MediaConch.exe";
    if (new File(path).exists()) {
      ArrayList<String> params = new ArrayList<>();
      params.add("-mc");
      params.add("-fx");
      ArrayList<String> standards = new ArrayList<>();
      standards.add("MOV");
      ArrayList<String> extensions = new ArrayList<>();
      extensions.add("MOV");
      ExternalConformanceChecker ext = new ExternalConformanceChecker(path, params, standards, extensions);
      l.add(ext);
    }

    path = "package/resources/plugins/pdf/verapdf.bat";
    if (!new File(path).exists()) path = "plugins/pdf/verapdf.bat";
    if (new File(path).exists()) {
      ArrayList<String> params = new ArrayList<>();
      params.add("--format");
      params.add("xml");
      ArrayList<String> standards = new ArrayList<>();
      standards.add("PDF");
      ArrayList<String> extensions = new ArrayList<>();
      extensions.add("PDF");
      ExternalConformanceChecker ext = new ExternalConformanceChecker(path, params, standards, extensions);
      l.add(ext);
    }

    return l;
  }

  /**
   * Get the appropiate conformance checker to run the given file.
   */
  private ConformanceChecker getConformanceCheckerForFile(String filename) {
    ConformanceChecker result = null;
    for (ConformanceChecker cc : getConformanceCheckers()) {
      if (cc.acceptsFile(filename)) {
        result = cc;
        break;
      }
    }
    if (result != null) {
      System.out.println("Conformance checker for file " + filename + ": " + result.toString());
    } else {
      System.out.println("Conformance checker for file " + filename + " not found");
    }
    return result;
  }

  /**
   * Process an input.
   *
   * @param filename the source path
   * @param internalReportFolder the internal report folder
   * @param config the report configuration
   * @return generated list of individual reports
   */
  private List<IndividualReport> processFile(String filename, String internalReportFolder, Configuration config) {
    List<IndividualReport> indReports = new ArrayList<>();
    IndividualReport ir;
    if (filename.toLowerCase().endsWith(".zip") || filename.toLowerCase().endsWith(".rar")) {
      // Compressed File
      try {
        System.out.println(filename);
        ZipFile zipFile = new ZipFile(filename);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
          // Process each file contained in the compressed file
          ZipEntry entry = entries.nextElement();
          ConformanceChecker cc = getConformanceCheckerForFile(entry.getName());
          if (cc != null) {
            InputStream stream = zipFile.getInputStream(entry);
            // Extracts the file and creates a temporary file to store it and process it
            String filename2 = createTempFile(internalReportFolder, entry.getName(), stream);
            ir = cc.processFile(filename2, entry.getName(), internalReportFolder, config, idReport);
            if (ir != null) {
              indReports.add(ir);
            } else{
              outOfmemory = true;
              break;
            }
            // Delete the temporary file
            File file = new File(filename2);
            file.delete();
          }
        }
        zipFile.close();
      } catch (Exception ex) {
        System.err.println("Error reading zip file [" + ex.toString() + "]");
      }
    } else if (isUrl(filename)) {
      // URL
      try {
        ConformanceChecker cc = getConformanceCheckerForFile(filename);
        if (cc != null) {
          // Download the file and store it in a temporary file
          InputStream input = new java.net.URL(filename).openStream();
          String filename2 = createTempFile(internalReportFolder, new File(filename).getName(), input);
          filename = java.net.URLDecoder.decode(filename, "UTF-8");
          ir = cc.processFile(filename2, filename, internalReportFolder, config, idReport);
          if (ir != null) {
            indReports.add(ir);
          } else{
            outOfmemory = true;
          }
          // Delete the temporary file
          File file = new File(filename2);
          file.delete();
        } else {
          System.err.println("The file in the URL " + filename + " is not an accepted format");
        }
      } catch (Exception ex) {
        System.out.println("Error in URL " + filename);
      }
    } else {
      // File
      ConformanceChecker cc = getConformanceCheckerForFile(filename);
      if (cc != null) {
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
      } else {
        System.err.println("File " + filename + " is not an accepted format");
      }
    }
    return indReports;
  }

  /**
   * Creates a temporary file to store the input stream.
   *
   * @param folder the folder to store the created temporary file
   * @param name the name of the temporary file
   * @param stream the input stream
   * @return the path to the created temporary file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private String createTempFile(String folder, String name, InputStream stream) throws IOException {
    // Create the path to the temporary file
    String filename2 = "x" + name;
    if (filename2.contains("/")) {
      filename2 = filename2.substring(filename2.lastIndexOf("/") + 1);
    }
    while (new File(filename2).isFile()) {
      filename2 = "x" + filename2;
    }
    filename2 = folder + "/" + filename2;

    // Write the file
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

  /**
   * Sends the report to the preforma FTP.
   *
   * @param summaryXml the summary xml
   * @throws NoSuchAlgorithmException An error occurred
   */
  private void sendFtpCamel(String summaryXml)
      throws NoSuchAlgorithmException {
    System.out.println("Sending feedback");
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
    System.out.println("Feedback sent");
  }
}

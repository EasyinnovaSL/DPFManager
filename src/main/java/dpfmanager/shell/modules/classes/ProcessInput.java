package dpfmanager.shell.modules.classes;

import dpfmanager.shell.modules.interfaces.UserInterface;
import dpfmanager.shell.modules.reporting.IndividualReport;
import dpfmanager.shell.modules.reporting.ReportGenerator;
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
  private ReportGenerator reportGenerator;
  /**
   * The Allowed extensions.
   */
  List<String> allowedExtensions;
  private boolean checkBL, checkEP, checkPC;
  private int checkIT;
  private Scene scene;
  private int idReport;

  /**
   * Instantiates a new Process input.
   *
   * @param allowedExtensions the allowed extensions
   * @param checkBL           the check bl
   * @param checkEP           the check ep
   * @param checkIT           the check it
   * @param checkPC           the check pc
   * @param scene           the JavaFX scene
   */
  public ProcessInput(List<String> allowedExtensions, boolean checkBL, boolean checkEP, int checkIT, boolean checkPC, Scene scene) {
    this.allowedExtensions = allowedExtensions;
    this.checkBL = checkBL;
    this.checkEP = checkEP;
    this.checkIT = checkIT;
    this.checkPC = checkPC;
    this.scene = scene;
  }

  /**
   * Instantiates a new Process input.
   *
   * @param allowedExtensions the allowed extensions
   */
  public ProcessInput(List<String> allowedExtensions) {
    this.allowedExtensions = allowedExtensions;
  }

  /**
   * Process files string.
   *
   * @param files        the files
   * @param config       the config
   * @param outputFolder the output folder
   * @return the string
   */
  public String ProcessFiles(ArrayList<String> files, Configuration config, String outputFolder) {
    checkBL = config.getIsos().contains("Baseline");
    checkEP = config.getIsos().contains("Tiff/EP");
    checkIT = -1;
    if (config.getIsos().contains("Tiff/IT")) checkIT = 0;
    if (config.getIsos().contains("Tiff/IT-1")) checkIT = 1;
    if (config.getIsos().contains("Tiff/IT-2")) checkIT = 2;
    checkPC = config.getRules().getRules().size() > 0;
    ArrayList<String> formats = config.getFormats();

    boolean silence = true;

    reportGenerator = new ReportGenerator();
    reportGenerator.setReportsFormats(formats.contains("XML"), formats.contains("JSON"), formats.contains("HTML"), formats.contains("PDF"));
    reportGenerator.setRules(config.getRules());
    reportGenerator.setFixes(config.getFixes());

    // Process files
    ArrayList<IndividualReport> individuals = new ArrayList<IndividualReport>();
    String internalReportFolder = ReportGenerator.createReportPath();
    int n=files.size();
    idReport=1;
    for (final String filename : files) {
      System.out.println("");
      System.out.println("Processing file " + filename);
      List<IndividualReport> indReports = processFile(filename, internalReportFolder, outputFolder);
      if (scene != null) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            ((Label) scene.lookup("#lblLoading")).setText("Processing..." + (files.indexOf(filename)+1) + "/" + n);
          }
        });
      }
      if (indReports.size() > 0) {
        individuals.addAll(indReports);
      }
      idReport++;
    }
    // Global report
    String summaryXml =
        reportGenerator.makeSummaryReport(internalReportFolder, individuals, outputFolder,
            silence);

    // Send report over FTP (only for alpha testing)
    try {
      if(UserInterface.getFeedback()) {
        sendFtpCamel(reportGenerator, summaryXml);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    String htmlFileStr = internalReportFolder + "report.html";
    return htmlFileStr;
  }

  /**
   * Process files string.
   *
   * @param files        the files
   * @param xml          the xml
   * @param json         the json
   * @param html         the html
   * @param pdf          the pdf
   * @param outputFolder the output folder
   * @param silence      the silence
   * @param rules        the rules
   * @param fixes        the fixes
   * @return the string
   */
  public String ProcessFiles(ArrayList<String> files, boolean xml, boolean json, boolean html, boolean pdf, String outputFolder, boolean silence, Rules rules, Fixes fixes) {
    reportGenerator = new ReportGenerator();
    reportGenerator.setReportsFormats(xml, json, html, pdf);
    reportGenerator.setRules(rules);
    reportGenerator.setFixes(fixes);

    // Process files
    ArrayList<IndividualReport> individuals = new ArrayList<IndividualReport>();
    String internalReportFolder = ReportGenerator.createReportPath();
    int n=files.size();
    idReport=1;
    for (final String filename : files) {
      System.out.println("");
      System.out.println("Processing file " + filename);
      if (scene != null) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            ((Label) scene.lookup("#lblLoading")).setText("Processing..." + (files.indexOf(filename)+1) + "/" + n);
          }
        });
      }
      List<IndividualReport> indReports = processFile(filename, internalReportFolder, outputFolder);
      if (indReports.size() > 0) {
        individuals.addAll(indReports);
      }
      idReport++;
    }
    // Global report
    String summaryXml =
        reportGenerator.makeSummaryReport(internalReportFolder, individuals, outputFolder,
            silence);

    // Send report over FTP (only for alpha testing)
    try {
      if(UserInterface.getFeedback()) {
        sendFtpCamel(reportGenerator, summaryXml);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (html) {
      return internalReportFolder + "report.html";
    } else if (xml) {
      return internalReportFolder + "summary.xml";
    } else if (json) {
      return internalReportFolder + "summary.json";
    } else {
      return internalReportFolder + "report.pdf";
    }
  }

  /**
   * Process a Tiff file.
   *
   * @param filename the filename
   * @param internalReportFolder the internal report folder
   * @return the list
   */
  private List<IndividualReport> processFile(String filename, String internalReportFolder, String outputFolder) {
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
          if (isTiff(entry.getName())) {
            InputStream stream = zipFile.getInputStream(entry);
            String filename2 = createTempFile(internalReportFolder, entry.getName(), stream);
            ir = processTiffFile(filename2, entry.getName(), internalReportFolder, outputFolder);
            if (ir != null) {
              indReports.add(ir);
            }
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
        if (isTiff(filename)) {
          InputStream input = new java.net.URL(filename).openStream();
          String filename2 = createTempFile(internalReportFolder, new File(filename).getName(), input);
          filename = java.net.URLDecoder.decode(filename, "UTF-8");
          ir = processTiffFile(filename2, filename, internalReportFolder, outputFolder);
          if (ir != null) {
            indReports.add(ir);
          }
          File file = new File(filename2);
          file.delete();
        } else {
          System.err.println("The file in the URL " + filename + " is not a Tiff");
        }
      } catch (Exception ex) {
        System.out.println("Error in URL " + filename);
      }
    } else if (isTiff(filename)) {
      // File
      try {
        ir = processTiffFile(filename, filename, internalReportFolder, outputFolder);
        if (ir != null) {
          indReports.add(ir);
        }
      } catch (Exception ex) {
        System.err.println("Error in File " + filename);
      }
    } else {
      // Anything else
      System.err.println("File " + filename + " is not a Tiff");
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
   * Process tiff file.
   *
   * @param pathToFile the path in local disk to the file
   * @param reportFilename the file name that will be displayed in the report
   * @param internalReportFolder the internal report folder
   * @param outputFolder the output report folder (optional)
   * @return the individual report
   * @throws ReadTagsIOException the read tags io exception
   * @throws ReadIccConfigIOException the read icc config io exception
   */
  private IndividualReport processTiffFile(String pathToFile, String reportFilename,
                                           String internalReportFolder, String outputFolder) throws ReadTagsIOException, ReadIccConfigIOException {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(pathToFile);
      switch (result) {
        case -1:
          System.out.println("File '" + pathToFile + "' does not exist");
          break;
        case -2:
          System.out.println("IO Exception in file '" + pathToFile + "'");
          break;
        case 0:
          TiffDocument to = tr.getModel();

          ValidationResult baselineVal = null;
          if (checkBL) baselineVal = tr.getBaselineValidation();
          ValidationResult epValidation = null;
          if (checkEP) epValidation = tr.getTiffEPValidation();
          ValidationResult itValidation = null;
          if (checkIT >= 0) itValidation = tr.getTiffITValidation(checkIT);

          String pathNorm = reportFilename.replaceAll("\\\\", "/");
          String name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
          IndividualReport ir = new IndividualReport(name, pathToFile, to, baselineVal, epValidation, itValidation);
          ir.checkBL = checkBL;
          ir.checkEP = checkEP;
          ir.checkIT = checkIT;
          ir.checkPC = checkPC;

          internalReport(ir, reportFilename, internalReportFolder, outputFolder);
          to=null;
          tr=null;
          System.gc();
          return ir;
        default:
          System.out.println("Unknown result (" + result + ") in file '" + pathToFile + "'");
          break;
      }
    } catch (ReadTagsIOException e) {
      System.err.println("Error loading Tiff library dependencies");
    } catch (ReadIccConfigIOException e) {
      System.err.println("Error loading Tiff library dependencies");
    }
    return null;
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
   * Checks if is tiff.
   *
   * @param filename the filename
   * @return true, if is tiff
   */
  private boolean isTiff(String filename) {
    boolean isTiff = false;
    for (String extension : allowedExtensions) {
      if (filename.toLowerCase().endsWith(extension.toLowerCase())) {
        isTiff = true;
      }
    }
    return isTiff;
  }

  /**
   * Report the results of the reading process to the console.
   *
   * @param ir the individual report
   * @param realFilename the real filename
   * @param folder the internal report folder
   */
  private void internalReport(IndividualReport ir, String realFilename,
                              String folder, String outputFolder) {
    String outputfile = ReportGenerator.getReportName(folder, realFilename, idReport);
    reportGenerator.generateIndividualReport(outputfile, ir, outputFolder);
    System.out.println("Internal report '" + outputfile + "' created");
  }

  /**
   * Send ftp.
   *
   * @param reportGenerator the report generator
   * @param summaryXml the summary xml
   * @throws NoSuchAlgorithmException the no such algorithm exception
   */
  private void sendFtpCamel(ReportGenerator reportGenerator, String summaryXml)
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

package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.external.ExternalConformanceChecker;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.logging.log4j.Level;

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
  private DpfContext context;
  private int idReport;
  private List<String> tempFiles;

  public ProcessInput(){
    tempFiles = new ArrayList<>();
  }

  /**
   * Sets the logger.
   *
   * @param context the Dpf context
   */
  public void setContext(DpfContext context) {
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

//  /**
//   * Process a list of files and generate individual ang global reports.
//   *
//   * @param files   the files
//   * @param config  the config
//   * @return the path to the internal report folder
//   */
//  public String ProcessFiles(ArrayList<String> files, Configuration config, String internalReportFolder ) {
//    ArrayList<IndividualReport> individuals = new ArrayList<>();
//    int n=files.size();
//
//    // Process each input of the list which can be a file, folder, zip or url
//    for (final String filename : files) {
//      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, ""));
//      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Processing file " + filename));
//      context.sendGui(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.TEXT, "Processing... " + (files.indexOf(filename) + 1) + "/" + n));
//
//      // Process the input and get a list of individual reports
//      List<IndividualReport> indReports = processFile(filename, internalReportFolder, config);
//      individuals.addAll(indReports);
//
//      // Create report
//      context.send(BasicConfig.MODULE_REPORT, new IndividualReportMessage(indReports, config));
//    }
//
//    // Generate global report
//    GlobalReport gr = new GlobalReport();
//    for (final IndividualReport individual : individuals) {
//      gr.addIndividual(individual);
//    }
//    gr.generate();
//
//    // Create global report
//    context.send(BasicConfig.MODULE_REPORT, new GlobalReportMessage(gr, config));
//
//    if (!new File(internalReportFolder).exists()) {
//     internalReportFolder = null;
//    }
//
//    return internalReportFolder;
//  }

  /**
   * Get the list of conformance checkers available to use.
   */
  private List<ConformanceChecker> getConformanceCheckers() {
    TiffConformanceChecker tiffcc = new TiffConformanceChecker();
    ArrayList<ConformanceChecker> l = new ArrayList<>();
    l.add(tiffcc);

    String path = "package/resources/plugins/video/MediaConch.exe";
    if (!new File(path).exists()) path = "plugins/video/MediaConch.exe";
    if (!new File(path).exists()) path = "../plugins/video/MediaConch.exe";
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
    if (!new File(path).exists()) path = "../plugins/pdf/verapdf.bat";
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
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Conformance checker for file " + filename + ": " + result.toString()));
    } else {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Conformance checker for file " + filename + " not found"));
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
  public IndividualReport processFile(String filename, String internalReportFolder, Configuration config, int idReport) {
    IndividualReport ir = null;
    if (isUrl(filename)) {
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
            outOfmemory = true;
          }
          // Delete the temporary file
          File file = new File(filename2);
          file.delete();
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "The file in the URL " + filename + " is not an accepted format"));
        }
      } catch (Exception ex) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Error in URL " + filename));
      }
    } else {
      // File
      ConformanceChecker cc = getConformanceCheckerForFile(filename);
      if (cc != null) {
        try {
          ir = cc.processFile(filename, filename, internalReportFolder, config, idReport);
          if (ir != null) {
            outOfmemory = true;
          }
        } catch (Exception ex) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error in File " + filename));
          ex.printStackTrace();
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "File " + filename + " is not an accepted format"));
      }
    }
    return ir;
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
    filename2 = folder + filename2;

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

  public List<String> getTempFiles() {
    List<String> aux = new ArrayList<>(tempFiles);
    tempFiles.clear();
    return aux;
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

}

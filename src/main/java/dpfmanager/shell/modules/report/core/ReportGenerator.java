/**
 * <h1>ReportGenerator.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Sola
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.report.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.reporting.HtmlReport;
import dpfmanager.conformancechecker.tiff.reporting.MetsReport;
import dpfmanager.conformancechecker.tiff.reporting.PdfReport;
import dpfmanager.conformancechecker.tiff.reporting.XmlReport;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.util.ReportHtml;
import dpfmanager.shell.modules.report.util.ReportJson;
import dpfmanager.shell.modules.report.util.ReportPDF;
import dpfmanager.shell.modules.report.util.ReportXml;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.logging.log4j.Level;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Class ReportGenerator.
 */
public class ReportGenerator {

  private DpfContext context;
  private final ReportXml reportXml;
  private final ReportJson reportJson;
  private final ReportPDF reportPdf;
  private final ReportHtml reportHtml;
  private final ResourceBundle bundle;

  public DpfContext getContext() {
    return context;
  }

  public void setContext(DpfContext context) {
    this.context = context;
    reportXml.setContext(context);
    reportJson.setContext(context);
    reportPdf.setContext(context);
    reportHtml.setContext(context);
  }

  public ReportGenerator() {
    reportXml = new ReportXml();
    reportJson = new ReportJson();
    reportPdf = new ReportPDF();
    reportHtml = new ReportHtml();
    bundle = DPFManagerProperties.getBundle();
  }

  /**
   * Create report path string.
   *
   * @return the string
   */
  public static String createReportPath() {
    return createReportPath(false);
  }

  public static String getLastReportPath() {
    return createReportPath(true);
  }

  /**
   * Creates the report path.
   *
   * @param subtract1 the subtract 1
   * @return the string
   */
  public static String createReportPath(boolean subtract1) {
    // reports folder
    String path = getReportsFolder();
    validateDirectory(path);

    // date folder
    path += "/" + FastDateFormat.getInstance("yyyyMMdd").format(new Date());
    if (subtract1 && !new File(path).exists()) {
      return path;
    }
    validateDirectory(path);

    // index folder
    int index = 1;
    File file = new File(path + "/" + index);
    while (file.exists()) {
      index++;
      file = new File(path + "/" + index);
    }
    if (subtract1) {
      index--;
      return path + "/" + index + "/";
    }
    path += "/" + index;
    validateDirectory(path);

    path += "/";
    return path;
  }

  /**
   * Gets reports folder.
   *
   * @return the reports folder
   */
  public static String getReportsFolder() {
    String path = DPFManagerProperties.getConfigDir() + "/reports";
    return path;
  }

  /**
   * Check if the path exists and if it's a directory, otherwise create the directory with this
   * name.
   *
   * @param path the path
   */
  private static void validateDirectory(String path) {
    File theDir = new File(path);
    if (theDir.exists() && !theDir.isDirectory()) {
      theDir.delete();
    }
    if (!theDir.exists()) {
      theDir.mkdir();
    }
  }

  /**
   * Gets the file type.
   *
   * @param path the path
   * @return the file type
   */
  static String getFileType(String path) {
    String fileType = null;
    fileType = path.substring(path.lastIndexOf('.') + 1).toUpperCase();
    return fileType;
  }

  /**
   * Gets the report name of a given tiff file.
   *
   * @param internalReportFolder the internal report folder
   * @param realFilename         the real file name
   * @param idReport             the report id
   * @return the report name
   */
  public static String getReportName(String internalReportFolder, String realFilename, int idReport) {
    String reportName = internalReportFolder + idReport + "-" + new File(realFilename).getName();
    File file = new File(reportName);
    int index = 0;
    while (file.exists()) {
      index++;
      String ext = getFileType(reportName);
      reportName =
        internalReportFolder + idReport + "-"
          + new File(realFilename.substring(0, realFilename.lastIndexOf(".")) + index + "." + ext)
          .getName();
      file = new File(reportName);
    }
    return reportName;
  }

  /**
   * Read the file of the path.
   *
   * @param pathStr the file path to read.
   * @return the content of the file in path
   */
  public String readFile(String pathStr) {
    String text = "";
    String name = pathStr.substring(pathStr.lastIndexOf("/") + 1, pathStr.length());
    Path path = Paths.get(pathStr);
    try {
      if (Files.exists(path)) {
        // Look in current dir
        BufferedReader br = new BufferedReader(new FileReader(pathStr));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
          sb.append(line);
          sb.append(System.lineSeparator());
          line = br.readLine();
        }
        text = sb.toString();
        br.close();
      } else {
        // Look in JAR
        CodeSource src = ReportGenerator.class.getProtectionDomain().getCodeSource();
        if (src != null) {
          URL jar = src.getLocation();
          ZipInputStream zip;

          zip = new ZipInputStream(jar.openStream());
          ZipEntry zipFile;
          boolean found = false;
          while ((zipFile = zip.getNextEntry()) != null && !found) {
            if (zipFile.getName().endsWith(name)) {
              BufferedReader br = new BufferedReader(new InputStreamReader(zip));
              String line = br.readLine();
              while (line != null) {
                text += line + System.lineSeparator();
                line = br.readLine();
              }
              found = true;
            }
            zip.closeEntry();
          }
        }
      }
    } catch (FileNotFoundException e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Template for html not found in dir."));
    } catch (IOException e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error reading " + pathStr));
    }

    return text;
  }

  /**
   * Write the string into the file.
   *
   * @param outputfile the outPutFile
   * @param body       the body
   */
  public void writeToFile(String outputfile, String body) {
    try {
      File output = new File(outputfile);
      output.getParentFile().mkdirs();
      Writer out = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(outputfile), "UTF-8"));
      out.write(body);
      out.close();
    } catch (IOException e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("IOException", e));
    }
  }

  /**
   * Delete the file or folder and it subs folders / files.
   *
   * @param file the file/folder
   */
  public static void deleteFileOrFolder(File file) {
    File[] files = file.listFiles();
    if (files != null) {
      for (File f : files) {
        if (f.isDirectory()) {
          deleteFileOrFolder(f);
        } else {
          f.delete();
        }
      }
    }
    file.delete();
  }

  /**
   * Copy a file or directory from source to target.
   *
   * @param sourceLocation the source path.
   * @param targetLocation the target path.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void copy(File sourceLocation, File targetLocation) throws IOException {
    if (sourceLocation.isDirectory()) {
      copyDirectory(sourceLocation, targetLocation);
    } else {
      copyFile(sourceLocation, targetLocation);
    }
  }

  /**
   * Copy a directory from source to target.
   *
   * @param source the source path.
   * @param target the target path.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void copyDirectory(File source, File target) throws IOException {
    if (!target.exists()) {
      boolean result = target.mkdir();
      if (!result) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Could not create the directory " + target.getPath()));
        return;
      }
    }
    for (String f : source.list()) {
      copy(new File(source, f), new File(target, f));
    }
  }

  /**
   * Read filefrom resources string.
   *
   * @param pathStr the path str
   * @return the string
   */
  public String readFilefromResources(String pathStr) {
    String text = "";
    Path path = Paths.get(pathStr);
    try {
      if (Files.exists(path)) {
        // Look in current dir
        BufferedReader br = new BufferedReader(new FileReader(pathStr));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
          sb.append(line);
          sb.append(System.lineSeparator());
          line = br.readLine();
        }
        text = sb.toString();
        br.close();
      } else {
        // Look in JAR
        Class cls = ReportGenerator.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(pathStr);
        if (in != null) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          StringBuilder out = new StringBuilder();
          String newLine = System.getProperty("line.separator");
          String line;
          while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
          }
          text = out.toString();
        }
      }
    } catch (FileNotFoundException e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Template for html not found in dir."));
    } catch (IOException e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error reading " + pathStr));
    }

    return text;
  }

  /**
   * Copy file.
   *
   * @param source the source
   * @param target the target
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static void copyFile(File source, File target) throws IOException {
    try (InputStream in = new FileInputStream(source);
         OutputStream out = new FileOutputStream(target)) {
      byte[] buf = new byte[1024];
      int length;
      while ((length = in.read(buf)) > 0) {
        out.write(buf, 0, length);
      }
    }
  }

  /**
   * Extracts a zip entry (file entry).
   *
   * @param zipIn    the zip input stream
   * @param filePath the file path
   * @throws IOException exception
   */
  private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[4096];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }

  /**
   * Copies the HTML folder to the reports folder.
   *
   * @param name the name
   */
  synchronized private void copyHtmlFolder(String name) {
    // Get the target folder
    File nameFile = new File(name);
    String absolutePath = nameFile.getAbsolutePath();
    String targetPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    File target = new File(targetPath + File.separator + "html");
    if (!target.exists()) {
      target.mkdirs();
    }

    // Already copied
    File testFile = new File(target.getPath() + "/js/bootstrap.min.js");
    if (testFile.exists()) {
      return;
    }

    // Copy the html folder to target
    String pathStr = "./src/main/resources/html";
    Path path = Paths.get(pathStr);
    if (Files.exists(path)) {
      // Look in current dir
      File folder = new File(pathStr);
      if (folder.exists() && folder.isDirectory()) {
        try {
          copyDirectory(folder, target);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      // Look in JAR
      CodeSource src = ReportGenerator.class.getProtectionDomain().getCodeSource();
      if (src != null) {
        try {
          List<String> arrayFiles = new ArrayList<>();
          List<File> arrayFolders = new ArrayList<>();

          //files in js folder
          arrayFiles.add("html/js/bootstrap.min.js");
          arrayFiles.add("html/js/jquery-1.9.1.min.js");
          arrayFiles.add("html/js/jquery.flot.pie.min.js");
          arrayFiles.add("html/js/jquery.flot.min.js");

          //files in img folder
//          arrayFiles.add("html/img/noise.jpg");
          arrayFiles.add("html/img/error.jpg");
          arrayFiles.add("html/img/not-found.jpg");
          arrayFiles.add("html/img/logo.png");
//          arrayFiles.add("html/img/logo - copia.png");
          arrayFiles.add("html/img/check_radio_sheet.png");

          //files in fonts folder
          arrayFiles.add("html/fonts/fontawesome-webfont.woff2");
          arrayFiles.add("html/fonts/fontawesome-webfont.woff");
          arrayFiles.add("html/fonts/fontawesome-webfont.ttf");
          arrayFiles.add("html/fonts/fontawesome-webfont.svg");
          arrayFiles.add("html/fonts/fontawesome-webfont.eot");
          arrayFiles.add("html/fonts/fontello.woff2");
          arrayFiles.add("html/fonts/fontello.woff");
          arrayFiles.add("html/fonts/fontello.ttf");
          arrayFiles.add("html/fonts/fontello.svg");
          arrayFiles.add("html/fonts/fontello.eot");
          arrayFiles.add("html/fonts/FontAwesome.otf");
          arrayFiles.add("html/fonts/Roboto-Bold.ttf");
          arrayFiles.add("html/fonts/Roboto-Regular.ttf");

          //files in css folder
          arrayFiles.add("html/css/font-awesome.css");
          arrayFiles.add("html/css/default.css");
          arrayFiles.add("html/css/bootstrap.css");
          arrayFiles.add("html/css/fontello.css");

          arrayFolders.add(new File(targetPath + File.separator + "html" + File.separator + "js" + File.separator));
          arrayFolders.add(new File(targetPath + File.separator + "html" + File.separator + "img" + File.separator));
          arrayFolders.add(new File(targetPath + File.separator + "html" + File.separator + "fonts" + File.separator));
          arrayFolders.add(new File(targetPath + File.separator + "html" + File.separator + "css" + File.separator));

          //if originals folders does not exist
          for (File item : arrayFolders) {
            if (!item.exists()) {
              item.mkdirs();
            }
          }

          //copy files
          Class cls = ReportGenerator.class;
          ClassLoader cLoader = cls.getClassLoader();
          for (String filePath : arrayFiles) {
            InputStream in = cLoader.getResourceAsStream(filePath);
            if (in == null) {
              //context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("IOException", "Cannot find file " + filePath, new Exception()));
            } else {
              int readBytes;
              byte[] buffer = new byte[4096];
              OutputStream resStreamOut = new FileOutputStream(targetPath + "/" + filePath);
              while ((readBytes = in.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
              }
            }
          }

        } catch (Exception e) {
          context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception", e));
        }
      }
    }
  }

  /**
   * Parse an individual report to XML format.
   *
   * @param filename the file name.
   * @param content  the individual report.
   */
  public void writeProcomputedIndividual(String filename, String content) {
    try {
      if (content == null) {
        return;
      }
      Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
      out.write(content);
      out.close();
      getContext().sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("individualReport").replace("%1", filename)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Make individual report.
   *
   * @param reportName the output file name
   * @param ir         the individual report
   */
  public void generateIndividualReport(String reportName, IndividualReport ir, Configuration config) throws OutOfMemoryError {
    ir.setReportPath(reportName);
    writeIndividualReport(ir, config.getFormats(), config, reportName, false);
    if (!ir.containsData()) {
      return;
    }

    // Fixes -> New report
    IndividualReport ir2 = ir.getCompareReport();
    if (ir2 != null) {
      writeIndividualReport(ir2, config.getFormats(), config, reportName + "_fixed", false);
    }
  }

  /**
   * Make individual report.
   *
   * @param reportName the output file name
   * @param ir         the individual report
   */
  public void transformIndividualReport(String reportName, String format, IndividualReport ir, Configuration config) throws OutOfMemoryError {
    List<String> formats = new ArrayList<>();
    formats.add(format.toUpperCase());
    writeIndividualReport(ir, formats, config, reportName, true);
    if (!ir.containsData()) {
      return;
    }

    // Fixes -> New report
    IndividualReport ir2 = ir.getCompareReport();
    if (ir2 != null) {
      writeIndividualReport(ir2, formats, config, reportName + "_fixed", true);
    }
  }

  private void writeIndividualReport(IndividualReport ir, List<String> formats, Configuration config, String reportName, boolean explicit) {
    if (ir.hasPrecomputedOutput()) {
      // External CC precomputed output
      String xmlFileStr = reportName + ".xml";
      writeProcomputedIndividual(xmlFileStr, ir.getConformanceCheckerReport());
    } else {
      // Generate report
      XmlReport xmlReport = new XmlReport();
      String xmlOutput = "";
      if (formats.contains("XML") && !explicit) {
        String xmlFileStr = reportName + ".xml";
        xmlOutput = xmlReport.parseIndividual(ir, config.getRules());
        writeProcomputedIndividual(xmlFileStr, xmlOutput);
        MetsReport metsReport = new MetsReport();
        String metsOutput = metsReport.parseIndividual(ir, config);
        if (metsOutput != null) {
          String metsFileStr = reportName + ".mets.xml";
          writeProcomputedIndividual(metsFileStr, metsOutput);
        }
      } else if (formats.contains("XML")) {
        String xmlFileStr = reportName + ".xml";
        xmlOutput = xmlReport.parseIndividual(ir, config.getRules());
        writeProcomputedIndividual(xmlFileStr, xmlOutput);
      }
      if (formats.contains("METS") && explicit) {
        String metsFileStr = reportName + ".mets.xml";
        MetsReport metsReport = new MetsReport();
        String metsOutput = metsReport.parseIndividual(ir, config);
        writeProcomputedIndividual(metsFileStr, metsOutput);
      }
      if (formats.contains("JSON")) {
        String jsonFileStr = reportName + ".json";
        if (xmlOutput.isEmpty()) {
          xmlOutput = xmlReport.parseIndividual(ir, config.getRules());
        }
        reportJson.xmlToJson(xmlOutput, jsonFileStr, this);
      }
      if (formats.contains("HTML")) {
        String htmlFileStr = reportName + ".html";
        copyHtmlFolder(htmlFileStr);
        HtmlReport htmlReport = new HtmlReport();
        String htmlOutput = htmlReport.parseIndividual(ir, ir.getReportId(), ir.getInternalReportFodler());
        String name = htmlFileStr.substring(htmlFileStr.lastIndexOf("/") + 1, htmlFileStr.length());
        String outputfile = htmlFileStr.replace(name, "html/" + name);
        File outputFile = new File(outputfile);
        outputFile.getParentFile().mkdirs();
        writeProcomputedIndividual(outputfile, htmlOutput);
      }
      if (formats.contains("PDF")) {
        try {
          String pdfFileStr = reportName + ".pdf";
          PdfReport pdfReport = new PdfReport();
          PDDocument pdfDocument = pdfReport.parseIndividual(ir, ir.getReportId(), ir.getInternalReportFodler());
          if (pdfDocument != null) {
            pdfDocument.save(pdfFileStr);
            pdfDocument.close();
          }
        } catch (Exception e) {
          context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in ReportPDF", e));
        }
      }
    }
  }

  public String transformGlobalReport(String internalReportFolder, String format, GlobalReport gr) {
    String xmlFileStr = internalReportFolder + "summary.xml";
    String xmlTempFileStr = internalReportFolder + "summary.temp.xml";
    String jsonFileStr = internalReportFolder + "summary.json";
    String htmlFileStr = internalReportFolder + "report.html";
    String pdfFileStr = internalReportFolder + "report.pdf";
    List<SmallIndividualReport> reports = new ArrayList<>();
    reports.addAll(gr.getIndividualReports());

    if (format.equals("xml")) {
      reportXml.parseGlobal(xmlFileStr, gr, reports);
      return xmlFileStr;
    }
    if (format.equals("json")) {
      File xmlFile = new File(xmlFileStr);
      File xmlTempFile = new File(xmlTempFileStr);
      if (xmlFile.exists()) {
        reportJson.xmlToJsonFile(xmlFileStr, jsonFileStr, this);
      } else {
        reportXml.parseGlobal(xmlTempFileStr, gr, reports);
        reportJson.xmlToJsonFile(xmlTempFileStr, jsonFileStr, this);
        xmlTempFile.delete();
      }
      return jsonFileStr;
    }
    if (format.equals("html")) {
      copyHtmlFolder(htmlFileStr);
      reportHtml.parseGlobal(htmlFileStr, gr, reports, this);
      return htmlFileStr;
    }
    if (format.equals("pdf")) {
      reportPdf.parseGlobal(pdfFileStr, gr, reports);
      return pdfFileStr;
    }

    return null;
  }

  /**
   * Make full report.
   *
   * @param internalReportFolder the internal report folder
   * @param gr                   the global report
   * @return the string
   */
  public String makeSummaryReport(String internalReportFolder,
                                  GlobalReport gr, Configuration config) {

    String xmlFileStr = internalReportFolder + "summary.xml";
    String jsonFileStr = internalReportFolder + "summary.json";
    String htmlFileStr = internalReportFolder + "report.html";
    String pdfFileStr = internalReportFolder + "report.pdf";

    SmallGlobalReport sgr = new SmallGlobalReport(gr, internalReportFolder + "/summary.ser");

    gr.write(internalReportFolder, "summary.ser");
    sgr.write(internalReportFolder, "summary.min.ser");
//    gr = (GlobalReport) GlobalReport.read(internalReportFolder + "/summary.ser");

    if (config.getFormats().contains("XML")) {
      reportXml.parseGlobal(xmlFileStr, gr, gr.getIndividualReports());
    }
    if (config.getFormats().contains("JSON")) {
      boolean toDelete = false;
      if (!new File(xmlFileStr).exists()) {
        reportXml.parseGlobal(xmlFileStr, gr, gr.getIndividualReports());
        toDelete = true;
      }
      reportJson.xmlToJsonFile(xmlFileStr, jsonFileStr, this);
      if (toDelete) {
        new File(xmlFileStr).delete();
      }
    }
    if (config.getFormats().contains("HTML")) {
      copyHtmlFolder(htmlFileStr);
      reportHtml.parseGlobal(htmlFileStr, gr, gr.getIndividualReports(), this);
    }
    if (config.getFormats().contains("PDF")) {
      reportPdf.parseGlobal(pdfFileStr, gr, gr.getIndividualReports());
    }

    String outputFolder = config.getOutput();
    if (outputFolder != null) {
      File htmlFile = new File(htmlFileStr);
      File outFolder = new File(outputFolder);
      String absolutePath = htmlFile.getAbsolutePath();
      String targetPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
      try {
        copy(new File(targetPath), outFolder);
      } catch (IOException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Cannot copy the report folder to the output path."));
      }
    }

    return xmlFileStr;
  }
}

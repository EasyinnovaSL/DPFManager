/**
 * <h1>ReportGenerator.java</h1>
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.reporting;

import dpfmanager.shell.modules.autofixes.autofix;
import dpfmanager.shell.modules.classes.Fix;
import dpfmanager.shell.modules.classes.Fixes;
import dpfmanager.shell.modules.classes.Rules;
import dpfmanager.shell.modules.interfaces.UserInterface;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.apache.commons.lang.time.FastDateFormat;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Class ReportGenerator.
 */
public class ReportGenerator {

  /** If generates the XML report. */
  private boolean xml;

  /** If generates the JSON report. */
  private boolean json;

  /** If generates the HTML report. */
  private boolean html;

  /** If generates the PDF report. */
  private boolean pdf = true;

  /** Policy checker rules. */
  private Rules rules;

  /** Policy checker fixes. */
  private Fixes fixes;

  /**
   * Create report path string.
   *
   * @return the string
   */
  public static String createReportPath() {
    return createReportPath(false);
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
    validateDirectory(path);

    // index folder
    int index = 1;
    File file = new File(path + "/" + index);
    while (file.exists()) {
      index++;
      file = new File(path + "/" + index);
    }
    if (subtract1) index--;
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
    String path = UserInterface.getConfigDir() + "/reports";
    return path;
  }

  /**
   * Check if the path exists and if it's a directory, otherwise create the directory with this name.
   * @param path the path
   */
  private static void validateDirectory(String path){
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
   * @return the report name
   */
  public static String getReportName(String internalReportFolder, String realFilename) {
    String reportName = internalReportFolder + new File(realFilename).getName();
    File file = new File(reportName);
    int index = 0;
    while (file.exists()) {
      index++;
      String ext = getFileType(reportName);
      reportName =
          internalReportFolder
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
  public static String readFile(String pathStr) {
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
      System.err.println("Template for html not found in dir.");
    } catch (IOException e) {
      System.err.println("Error reading " + pathStr);
    }

    return text;
  }

  /**
   * Write the string into the file.
   *
   * @param outputfile the outPutFile
   * @param body       the body
   */
  public static void writeToFile(String outputfile, String body) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile));
      writer.write(body);
      writer.close();
    } catch (IOException e) {
      System.out.println("IOException");
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
   * Set the output formats.
   *
   * @param xml  the XML boolean.
   * @param json the JSON boolean.
   * @param html the HTML boolean.
   * @param pdf  the PDF boolean.
   */
  public void setReportsFormats(boolean xml, boolean json, boolean html, boolean pdf) {
    this.xml = xml;
    this.json = json;
    this.html = html;
    this.pdf = pdf;
  }

  /**
   * Sets rules.
   *
   * @param rules the rules
   */
  public void setRules(Rules rules) {
    this.rules = rules;
  }

  /**
   * Sets fixes.
   *
   * @param fixes the fixes
   */
  public void setFixes(Fixes fixes) {
    this.fixes = fixes;
  }

  /**
   * Copy a file or directory from source to target.
   *
   * @param sourceLocation the source path.
   * @param targetLocation the target path.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static void copy(File sourceLocation, File targetLocation) throws IOException {
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
  private static void copyDirectory(File source, File target) throws IOException {
    if (!target.exists()) {
      target.mkdir();
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
  public static String readFilefromResources(String pathStr) {
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
      System.err.println("Template for html not found in dir.");
    } catch (IOException e) {
      System.err.println("Error reading " + pathStr);
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
   * @param zipIn the zip input stream
   * @param filePath the file path
   * @throws IOException exception
   */
  private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
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
  private void copyHtmlFolder(String name) {
    // Get the target folder
    File nameFile = new File(name);
    String absolutePath = nameFile.getAbsolutePath();
    String targetPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    File target = new File(targetPath + File.separator + "html");
    if (!target.exists()) {
      target.mkdir();

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
          URL jar = src.getLocation();
          ZipInputStream zip;
          String jarFolder;
          try {
            Class cls = this.getClass();
            ClassLoader cLoader = cls.getClassLoader();
            String [] arrayFiles = new String[15];
            File [] arrayFoulders = new File[4];
            //files in js folder
            arrayFiles[0] = "html/js/jquery-1.9.1.min.js";
            arrayFiles[1] = "html/js/jquery.flot.pie.min.js";
            arrayFiles[2] = "html/js/jquery.flot.min.js";

            //files in img folder
            arrayFiles[3] = "html/img/noise.jpg";
            arrayFiles[4] = "html/img/logo.png";
            arrayFiles[5] = "html/img/logo - copia.png";

            //files in fonts folder
            arrayFiles[6] = "html/fonts/fontawesome-webfont.woff2";
            arrayFiles[7] = "html/fonts/fontawesome-webfont.woff";
            arrayFiles[8] = "html/fonts/fontawesome-webfont.ttf";
            arrayFiles[9] = "html/fonts/fontawesome-webfont.svg";
            arrayFiles[10] = "html/fonts/fontawesome-webfont.eot";
            arrayFiles[11] = "html/fonts/FontAwesome.otf";

            //files in css folder
            arrayFiles[12] = "html/css/font-awesome.css";
            arrayFiles[13] = "html/css/default.css";
            arrayFiles[14] = "html/css/bootstrap.css";

            arrayFoulders[0] = new File(targetPath + File.separator + "html/js/");
            arrayFoulders[1] = new File(targetPath + File.separator + "html/img/");
            arrayFoulders[2]= new File(targetPath + File.separator + "html/fonts/");
            arrayFoulders[3] = new File(targetPath + File.separator + "html/css/");

            //if originals folders not exists
            for (File item : arrayFoulders) {
              if(!item.exists()) {
                item.mkdirs();
              }
            }

            //copy files
            for(String filePath : arrayFiles){
              InputStream in = cLoader.getResourceAsStream(filePath);
              int readBytes;
              byte[] buffer = new byte[4096];
              jarFolder = targetPath + File.separator;
              File prova = new File(jarFolder + filePath);
              if(!prova.exists()) {
                prova.createNewFile();
              }
              OutputStream resStreamOut = new FileOutputStream(prova, false);
              while ((readBytes = in.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
              }
            }

           /* zip = new ZipInputStream(jar.openStream());
            ZipEntry zipFile;
            while ((zipFile = zip.getNextEntry()) != null) {
              if (zipFile.getName().startsWith("html/")) {
                String filePath = targetPath + File.separator + zipFile.getName();
                if (!zipFile.isDirectory()) {
                  // if the entry is a file, extracts it
                  extractFile(zip, filePath);
                } else {
                  // if the entry is a directory, make the directory
                  File dir = new File(filePath);
                  dir.mkdir();
                }
              }
              zip.closeEntry();
            }*/
          } catch (IOException e) {
            System.err.println("Exception!");
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * Opens the default browser with the HTML file.
   *
   * @param htmlfile the file to show
   */
  private void showToUser(String htmlfile) {
    if (Desktop.isDesktopSupported()) {
      try {
        String fullHtmlPath = new File(htmlfile).getAbsolutePath();
        fullHtmlPath = fullHtmlPath.replaceAll("\\\\", "/");
        Desktop.getDesktop().browse(new URI(fullHtmlPath));
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error opening the bowser with the global report." + e.getMessage());
      }
    } else {
      System.out.println("Desktop services not suported.");
    }
  }

  /**
   * Make individual report.
   *
   * @param reportName the output file name
   * @param ir         the individual report
   */
  public void generateIndividualReport(String reportName, IndividualReport ir, String outputFolder) {
    String output;
    String xmlFileStr = reportName + ".xml";
    String jsonFileStr = reportName + ".json";
    String htmlFileStr = reportName + ".html";
    String pdfFileStr = reportName + ".pdf";
    int htmlMode = 0;
    if (fixes != null && fixes.getFixes().size() > 0) htmlMode = 1;
    output = ReportXml.parseIndividual(xmlFileStr, ir, rules);
    ValidationResult pcValidation1 = getPcValidation(output);
    ir.setPCErrors(pcValidation1.getErrors());
    ir.setPCWarnings(pcValidation1.getWarnings());
    ir.setPcValidation(pcValidation1);
    if (html) {
      copyHtmlFolder(htmlFileStr);
      ReportHtml.parseIndividual(htmlFileStr, ir, htmlMode);
    }
    if (json) {
      ReportJson.xmlToJson(output, jsonFileStr);
    }
    if (pdf) {
      ReportPDF.parseIndividual(pdfFileStr, ir);
    }
    if (!xml) {
      ReportGenerator.deleteFileOrFolder(new File(xmlFileStr));
    }

    // Fixes -> New report
    if (fixes != null && fixes.getFixes().size() > 0) {
      xmlFileStr = reportName + "_fixed" + ".xml";
      jsonFileStr = reportName + "_fixed" + ".json";
      htmlFileStr = reportName + "_fixed" + ".html";
      pdfFileStr = reportName + "_fixed" + ".pdf";

      TiffDocument td = ir.getTiffModel();
      String nameOriginalTif = ir.getFilePath();

      try {
        TiffInputStream ti = new TiffInputStream(new File(nameOriginalTif));
        TiffWriter tw = new TiffWriter(ti);
        tw.SetModel(td);
        int idx = 0;
        while (new File("out" + idx + ".tif").exists()) idx++;
        String nameFixedTif = "out" + idx + ".tif";
        tw.write(nameFixedTif);

        TiffReader tr = new TiffReader();
        tr.readFile(nameFixedTif);
        ir.setTiffModel(tr.getModel());
        new File(nameFixedTif).delete();

        for (Fix fix : fixes.getFixes()) {
          if (fix.getOperator() != null) {
            if (fix.getOperator().equals("Add Tag")) {
              td.addTag(fix.getTag(), fix.getValue());
            } else if (fix.getOperator().equals("Remove Tag")) {
              td.removeTag(fix.getTag());
            }
          } else {
            String className = fix.getTag();
            autofix autofix = (autofix)Class.forName("dpfmanager.shell.modules.autofixes." + className).newInstance();
            autofix.run(td);
          }
        }

        ti = new TiffInputStream(new File(nameOriginalTif));
        tw = new TiffWriter(ti);
        tw.SetModel(td);
        idx = 0;
        while (new File("out" + idx + ".tif").exists()) idx++;
        nameFixedTif = "out" + idx + ".tif";
        tw.write(nameFixedTif);

        tr = new TiffReader();
        tr.readFile(nameFixedTif);
        TiffDocument to = tr.getModel();

        ValidationResult baselineVal = null;
        if (ir.checkBL) baselineVal = tr.getBaselineValidation();
        ValidationResult epValidation = null;
        if (ir.checkEP) epValidation = tr.getTiffEPValidation();
        ValidationResult itValidation = null;
        if (ir.checkIT >= 0) itValidation = tr.getTiffITValidation(ir.checkIT);

        String pathNorm = nameFixedTif.replaceAll("\\\\", "/");
        String name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
        IndividualReport ir2 = new IndividualReport(name, nameFixedTif, to, baselineVal, epValidation, itValidation);
        output = ReportXml.parseIndividual(xmlFileStr, ir2, rules);
        ValidationResult pcValidation2 = getPcValidation(output);
        ir2.setPcValidation(pcValidation2);
        ir2.setCompareReport(ir);
        ir2.setFileName(ir.getFileName() + "_fixed");
        if (html) {
          ReportHtml.parseIndividual(htmlFileStr, ir2, 2);
        }
        if (json) {
          ReportJson.xmlToJson(output, jsonFileStr);
        }
        if (pdf) {
          ReportPDF.parseIndividual(pdfFileStr, ir2);
        }
        if (!xml) {
          ReportGenerator.deleteFileOrFolder(new File(xmlFileStr));
        }

        if (outputFolder == null) {
          new File(nameFixedTif).delete();
        } else {
          File dir = new File(outputFolder + "/fixed/");
          if (!dir.exists()) dir.mkdir();
          String pathFixed = outputFolder + "/fixed/" + new File(nameOriginalTif).getName();
          Files.move(Paths.get(nameFixedTif), Paths.get(pathFixed));
          System.out.println("Fixed file " + pathFixed + " created");
        }
      } catch (Exception ex) {
        System.out.println("Error creating report of fixed image");
      }
    }
  }

  /**
   * Gets pc validation.
   *
   * @param output the output
   * @return the pc validation
   */
  ValidationResult getPcValidation(String output) {
    ValidationResult valid = new ValidationResult();
    int index = output.indexOf("<svrl:failed-assert");
    while (index > -1) {
      String text = output.substring(output.indexOf("text>", index));
      text = text.substring(text.indexOf(">") + 1);
      text = text.substring(0, text.indexOf("</"));
      index = output.indexOf("<svrl:failed-assert", index + 1);
      valid.addErrorLoc(text, "Policy checker");
    }
    return valid;
  }

  /**
   * Make full report.
   *
   * @param internalReportFolder the internal report folder
   * @param individuals          the individual reports list
   * @param outputFolder         the output folder
   * @param silence              the silence
   * @return the string
   */
  public String makeSummaryReport(String internalReportFolder,
      ArrayList<IndividualReport> individuals, String outputFolder, boolean silence) {
    GlobalReport gr = new GlobalReport();
    for (final IndividualReport individual : individuals) {
      gr.addIndividual(individual);
    }
    gr.generate();

    String output = null;
    String xmlFileStr = internalReportFolder + "summary.xml";
    String jsonFileStr = internalReportFolder + "summary.json";
    String htmlFileStr = internalReportFolder + "report.html";
    String pdfFileStr = internalReportFolder + "report.pdf";
    output = ReportXml.parseGlobal(xmlFileStr, gr);
    if (html) {
      copyHtmlFolder(htmlFileStr);
      ReportHtml.parseGlobal(htmlFileStr, gr);
      if (!silence) {
        //showToUser(htmlFileStr);
      }
    }
    if (json) {
      ReportJson.xmlToJson(output, jsonFileStr);
    }
    if (pdf) {
      ReportPDF.parseGlobal(pdfFileStr, gr);
    }
    if (!xml) {
      ReportGenerator.deleteFileOrFolder(new File(xmlFileStr));
    }

    if (outputFolder != null) {
      File htmlFile = new File(htmlFileStr);
      File outFolder = new File(outputFolder);
      String absolutePath = htmlFile.getAbsolutePath();
      String targetPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
      try {
        copy(new File(targetPath), outFolder);
        if (!silence) {
          Desktop desktop = Desktop.getDesktop();
          desktop.open(outFolder);
        }
      } catch (IOException e) {
        System.out.println("Cannot copy the report folder to the output path.");
      }
    }

    return output;
  }
}

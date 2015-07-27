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

package dpfmanager;

import org.apache.commons.lang.time.FastDateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

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

  /**
   * Creates the report path.
   *
   * @return the string
   */
  public static String createReportPath() {
    // reports folder
    String path = "reports";
    File theDir = new File(path);
    if (!theDir.exists()) {
      theDir.mkdir();
    }
    // date folder
    path += "/" + FastDateFormat.getInstance("yyyyMMdd").format(new Date());
    theDir = new File(path);
    if (!theDir.exists()) {
      theDir.mkdir();
    }
    // index folder
    int index = 1;
    File file = new File(path + "/" + index);
    while (file.exists()) {
      index++;
      file = new File(path + "/" + index);
    }
    path += "/" + index;
    theDir = new File(path);
    if (!theDir.exists()) {
      theDir.mkdir();
    }
    path += "/";
    return path;
  }

  /**
   * Gets the file type.
   *
   * @param path the path
   * @return the file type
   */
  static String getFileType(String path) {
    String fileType = null;
    fileType = path.substring(path.indexOf('.', path.lastIndexOf('/')) + 1).toUpperCase();
    return fileType;
  }

  /**
   * Gets the report name of a given tiff file.
   *
   * @param internalReportFolder the internal report folder
   * @param realFilename the real file name
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
              + new File(realFilename.substring(0, realFilename.lastIndexOf(".")) + index + ext)
                  .getName();
      file = new File(reportName);
    }
    return reportName;
  }

  /**
   * Read the file of the path.
   *
   * @param path the path to read.
   * @return the content of the file in path
   */
  public static String readFile(String path) {
    BufferedReader br;
    String text = "";
    try {
      br = new BufferedReader(new FileReader(path));
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
      }
      text = sb.toString();
      br.close();
    } catch (FileNotFoundException e) {
      System.out.println("Template for html not found.");
    } catch (IOException e) {
      System.out.println("Error reading " + path);
    }
    return text;
  }

  /**
   * Write the string into the file.
   *
   * @param outputfile the outPutFile
   * @param body the body
   */
  public static void writeToFile(String outputfile, String body) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(outputfile, "UTF-8");
      writer.println(body);
    } catch (FileNotFoundException e) {
      System.out.println("File not found exception");
    } catch (UnsupportedEncodingException e) {
      System.out.println("UnsupportedEncodingException exception");
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  /**
   * Delete the file or folder and it subs folders / files.
   * 
   * @param file the file or folder to remove
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
   * Make full report.
   *
   * @param xml the XML boolean.
   * @param json the JSON boolean.
   * @param html the HTML boolean.
   */
  public void setReportsFormats(boolean xml, boolean json, boolean html) {
    this.xml = xml;
    this.json = json;
    this.html = html;
  }

  /**
   * Make individual report.
   *
   * @param reportName the output file name
   * @param ir the individual report
   */
  public void generateIndividualReport(String reportName, IndividualReport ir) {
    String output = null;
    String reportNameXml = reportName + ".xml";
    String reportNameJson = reportName + ".json";
    String reportNameHtml = reportName + ".html";
    if (xml) {
      output = ReportXml.parseIndividual(reportNameXml, ir);
    }
    if (html) {
      ReportHtml.parseIndividual(reportNameHtml, ir);
    }
    if (json && output == null) {
      output = ReportXml.parseIndividual(reportNameXml, ir);
      ReportJson.xmlToJson(output, reportNameJson);
      ReportGenerator.deleteFileOrFolder(new File(reportNameXml));
    } else if (json) {
      ReportJson.xmlToJson(output, reportNameJson);
    }
  }

  /**
   * Make full report.
   *
   * @param internalReportFolder the internal report folder
   * @param individuals the individual reports list
   */
  public void makeSummaryReport(String internalReportFolder, ArrayList<IndividualReport> individuals) {
    if (individuals.size() > 0) {
      GlobalReport gr = new GlobalReport();
      for (final IndividualReport individual : individuals) {
        gr.addIndividual(individual);
      }
      gr.generate();

      String output = null;
      String xmlfile = internalReportFolder + "summary.xml";
      String jsonFile = internalReportFolder + "summary.json";
      String htmlfile = internalReportFolder + "report.html";
      if (xml) {
        output = ReportXml.parseGlobal(xmlfile, gr);
      }
      if (html) {
        ReportHtml.parseGlobal(htmlfile, gr);
      }
      if (json && output == null) {
        output = ReportXml.parseGlobal(xmlfile, gr);
        ReportJson.xmlToJson(output, jsonFile);
        ReportGenerator.deleteFileOrFolder(new File(xmlfile));
      } else if (json) {
        ReportJson.xmlToJson(output, jsonFile);
      }
    }
  }
}

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
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at
 * <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
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
 *
 */

package dpfmanager;

import org.apache.commons.lang.time.FastDateFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * The Class ReportGenerator.
 */
public class ReportGenerator {

  /** The tiff model. */
  private TiffDocument tiffModel;

  /** The validation. */
  private ValidationResult validation;

  /**
   * Default constructor.
   *
   * @param tiffModel the tiff model
   * @param val the val
   */
  public ReportGenerator(TiffDocument tiffModel, ValidationResult val) {
    this.tiffModel = tiffModel;
    validation = val;
  }

  /**
   * Creates the report path.
   *
   * @return the string
   */
  public static String createReportPath() {
    String path = "reports";
    File theDir = new File(path);
    if (!theDir.exists()) {
      theDir.mkdir();
    }
    path += "/" + FastDateFormat.getInstance("yyyyMMdd").format(new Date());
    theDir = new File(path);
    if (!theDir.exists()) {
      theDir.mkdir();
    }
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
   * Creates the ifd node.
   *
   * @param doc the doc
   * @param ifd the ifd
   * @param index the index
   * @return the element
   */
  private Element createIfdNode(Document doc, IFD ifd, int index) {
    Element ifdNode = doc.createElement("ifdNode");
    Element el;

    // Number
    el = doc.createElement("number");
    el.setTextContent("" + index);
    ifdNode.appendChild(el);

    // Image
    el = doc.createElement("isimg");
    if (ifd.isImage()) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // Image
    el = doc.createElement("hasSubIfd");
    if (ifd.getsubIFD() != null) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    return ifdNode;
  }

  /**
   * Generate xml.
   *
   * @param xmlfile the xmlfile
   */
  public void generateXml(String xmlfile) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element report = doc.createElement("report");
      doc.appendChild(report);

      // Tiff structure
      Element tiffStructureElement = doc.createElement("tiff_structure");
      Element ifdTree = doc.createElement("ifdTree");
      IFD ifd = tiffModel.getFirstIFD();
      if (ifd != null) {
        int index = 1;
        Element ifdNode = createIfdNode(doc, ifd, index++);
        ifdTree.appendChild(ifdNode);
        while (ifd.hasNextIFD()) {
          ifd = ifd.getNextIFD();
          ifdNode = createIfdNode(doc, ifd, index++);
          ifdTree.appendChild(ifdNode);
        }
      }
      tiffStructureElement.appendChild(ifdTree);
      report.appendChild(tiffStructureElement);

      // implementation checker
      Element implementationCheckerElement = doc.createElement("implementation_checker");
      report.appendChild(implementationCheckerElement);
      Element results = doc.createElement("results");

      // errors
      List<ValidationEvent> errors = validation.getErrors();
      for (int i = 0; i < errors.size(); i++) {
        ValidationEvent value = errors.get(i);
        Element error = doc.createElement("result");

        // level
        Element level = doc.createElement("level");
        level.setTextContent("critical");
        error.appendChild(level);

        // msg
        Element msg = doc.createElement("msg");
        msg.setTextContent(value.getDescription());
        error.appendChild(msg);

        results.appendChild(error);
      }

      // warnings
      List<ValidationEvent> warnings = validation.getWarnings();
      for (int i = 0; i < warnings.size(); i++) {
        ValidationEvent value = warnings.get(i);
        Element warning = doc.createElement("result");

        // level
        Element level = doc.createElement("level");
        level.setTextContent("warning");
        warning.appendChild(level);

        // msg
        Element msg = doc.createElement("msg");
        msg.setTextContent(value.getDescription());
        warning.appendChild(msg);

        results.appendChild(warning);
      }
      implementationCheckerElement.appendChild(results);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(xmlfile));

      transformer.transform(source, result);
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
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
    fileType = path.substring(path.indexOf('.', path.lastIndexOf('/')) + 1).toUpperCase();
    return fileType;
  }

  /**
   * Gets the report name.
   *
   * @param internalReportFolder the internal report folder
   * @param tiffreader the tiffreader
   * @return the report name
   */
  public static String getReportName(String internalReportFolder, TiffReader tiffreader) {
    String reportName =
        internalReportFolder + new File(tiffreader.getFilename()).getName() + ".xml";
    File file = new File(reportName);
    int index = 0;
    while (file.exists()) {
      index++;
      String ext = getFileType(reportName);
      reportName =
          internalReportFolder
              + new File(tiffreader.getFilename().substring(0,
                  tiffreader.getFilename().lastIndexOf("."))
                  + index + ext).getName();
      file = new File(reportName);
    }
    return reportName;
  }

  /**
   * Make full report.
   *
   * @param internalReportFolder the internal report folder
   */
  public static void makeSummaryReport(String internalReportFolder) {
    String xmlfile = internalReportFolder + "summary.xml";
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element globalreport = doc.createElement("globalreport");
      doc.appendChild(globalreport);

      Element individualreports = doc.createElement("individualreports");
      globalreport.appendChild(individualreports);
      // Individual reports
      File dir = new File(internalReportFolder);
      File[] listOfFiles = dir.listFiles();
      int nreports = 0;
      for (int j = 0; j < listOfFiles.length; j++) {
        if (listOfFiles[j].isFile()) {
          File xmlFile = listOfFiles[j];
          DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = dbFactory.newDocumentBuilder();
          Document doc2 = builder.parse(xmlFile);
          Node node = doc2.getChildNodes().item(0);
          Node newNode = doc.importNode(node, true);
          individualreports.appendChild(newNode);
          nreports++;
        }
      }

      // Statistics
      Element stats = doc.createElement("stats");
      globalreport.appendChild(stats);
      Element el = doc.createElement("reportscount");
      el.setTextContent("" + nreports);
      stats.appendChild(el);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(xmlfile));

      transformer.transform(source, result);
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


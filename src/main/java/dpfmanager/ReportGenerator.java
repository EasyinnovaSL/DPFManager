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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;

import org.apache.commons.lang.time.FastDateFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.io.StringWriter;
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
   * @param tiffreader the tiffreader
   * @return the report name
   */
  public static String getReportName(String internalReportFolder, TiffReader tiffreader) {
    String reportName =
        internalReportFolder + new File(tiffreader.getFilename()).getName();
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
   * Make individual report.
   *
   * @param outputfile the output file name
   * @param ir the individual report
   */
  public static void generateIndividualReport(String reportName, IndividualReport ir) {
    String reportNameXml = reportName + ".xml";
    String reportNameHtml = reportName + ".html";
    ReportXml.parseIndividual(reportNameXml, ir);
    ReportHtml.parseIndividual(reportNameHtml, ir);
  }
  
  /**
   * Make full report.
   *
   * @param internalReportFolder the internal report folder
   * @param individuals the individual reports list
   */
  public static void makeSummaryReport(String internalReportFolder, 
      ArrayList<IndividualReport> individuals) {
    GlobalReport gr = new GlobalReport();
    for (final IndividualReport individual : individuals) {
      gr.addIndividual(individual);
    }
    gr.generate();
    
    String xmlfile = internalReportFolder + "summary.xml";
    String htmlfile = internalReportFolder + "summary.html";
    ReportXml.parseGlobal(xmlfile, gr);
    ReportHtml.parseGlobal(htmlfile, gr);
  }

  /**
   * Xml to json.
   *
   * @param xml the xml
   * @throws Exception the exception
   */
  private static void xmlToJson(String xml, String jsonFilename) throws Exception {
    CamelContext context = new DefaultCamelContext();
    XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
    xmlJsonFormat.setEncoding("UTF-8");
    context.addRoutes(
        new RouteBuilder() {
          public void configure() {
            from("direct:marshal").marshal(xmlJsonFormat).to("file:"+jsonFilename);
          }
        }
    );
    ProducerTemplate template = context.createProducerTemplate();
    context.start();
    template.sendBody("direct:marshal", xml);
    context.stop();
  }
}


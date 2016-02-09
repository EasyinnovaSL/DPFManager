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
 * @author Adrià Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.reporting;

import dpfmanager.shell.conformancechecker.PolicyChecker.Rules;
import dpfmanager.shell.conformancechecker.PolicyChecker.Schematron;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.types.IFD;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
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
 * The Class ReportXml.
 */
public class ReportXml extends ReportGeneric {
  /**
   * Creates the ifd node.
   *
   * @param doc the doc
   * @param ir the ir
   * @param ifd the ifd
   * @param index the index
   * @return the element
   */
  private static Element createIfdNode(Document doc, IndividualReport ir, IFD ifd, int index) {
    Element ifdNode = doc.createElement("ifdNode");
    Element el, elchild, elchild2;

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

    // Thumbnail or main
    String typ = "Main image";
    if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = "Thumbnail";
    el = doc.createElement("imagetype");
    el.setAttribute("check_ifd0", "typ");
    el.setTextContent(typ);
    ifdNode.appendChild(el);

    // Strips or Tiles
    el = doc.createElement("image_representation");
    if (ifd.hasStrips()) {
      el.setTextContent("strips[" + ifd.getImageStrips().getStrips().size() + "]");
    } else if (ifd.hasTiles()) {
      el.setTextContent("tiles[" + ifd.getImageTiles().getTiles().size() + "]");
    } else {
      el.setTextContent("none");
    }
    ifdNode.appendChild(el);

    // Photometric
    el = doc.createElement("photometric");
    TagValue tagv = ifd.getMetadata().get("PhotometricInterpretation");
    if (tagv != null) {
      el.setTextContent(tagv.getFirstNumericValue() + "");
    } else {
      el.setTextContent("null");
    }
    ifdNode.appendChild(el);

    // SubImage
    el = doc.createElement("hasSubIfd");
    if (ifd.getsubIFD() != null) {
      typ="Thumbnail";
      if (ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = "Main image";
      el.setTextContent(typ);
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // Exif
    el = doc.createElement("hasExif");
    if (ifd.containsTagId(34665)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // XMP
    el = doc.createElement("hasXMP");
    if (ifd.containsTagId(700)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // IPTC
    el = doc.createElement("hasIPTC");
    if (ifd.containsTagId(33723)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // Tags
    el = doc.createElement("tags");
    IfdTags ifdTags = ifd.getMetadata();
    for (TagValue t : ifdTags.getTags()) {
      elchild = doc.createElement("tag");

      elchild2 = doc.createElement("name");
      elchild2.setTextContent(t.getName());
      elchild.appendChild(elchild2);

      elchild2 = doc.createElement("id");
      elchild2.setTextContent(t.getId()+"");
      elchild.appendChild(elchild2);

      elchild2 = doc.createElement("value");
      if (t.getCardinality() == 1 || t.toString().length() < 100)
        elchild2.setTextContent(t.toString());
      else
        elchild2.setTextContent("Array[" + t.getCardinality() + "]");
      elchild.appendChild(elchild2);

      el.appendChild(elchild);
    }
    ifdNode.appendChild(el);

    return ifdNode;
  }

  /**
   * Adds the errors warnings.
   *
   * @param doc the doc
   * @param results the results
   * @param errors the errors
   * @param warnings the warnings
   */
  private static void addErrorsWarnings(Document doc, Element results,
      List<ValidationEvent> errors, List<ValidationEvent> warnings) {
    // errors
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
  }

  /**
   * Parse an individual report to XML format.
   *
   * @param doc the doc
   * @param ir the individual report.
   * @return the element
   */
  private static Element buildReportIndividual(Document doc, IndividualReport ir) {
    Element report = doc.createElement("report");

    // file info
    Element fileInfoStructure = doc.createElement("file_info");
    Element nameElement = doc.createElement("name");
    nameElement.setTextContent(ir.getFileName());
    Element pathElement = doc.createElement("fullpath");
    pathElement.setTextContent(ir.getFilePath());
    fileInfoStructure.appendChild(nameElement);
    fileInfoStructure.appendChild(pathElement);
    report.appendChild(fileInfoStructure);

    // tiff structure
    Element tiffStructureElement = doc.createElement("tiff_structure");
    Element ifdTree = doc.createElement("ifdTree");
    int index = 0;
    IFD ifd = ir.getTiffModel().getFirstIFD();
    while (ifd != null) {
      Element ifdNode = createIfdNode(doc, ir, ifd, index++);
      ifdTree.appendChild(ifdNode);
      ifd = ifd.getNextIFD();
    }

    tiffStructureElement.appendChild(ifdTree);
    report.appendChild(tiffStructureElement);

    // basic info
    Element infoElement = doc.createElement("ImageWidth");
    infoElement.setAttribute("ImageWidth", "" + ir.getWidth());
    infoElement.setTextContent(ir.getWidth());
    report.appendChild(infoElement);
    infoElement = doc.createElement("ImageHeight");
    infoElement.setAttribute("ImageHeight", "" + ir.getHeight());
    infoElement.setTextContent(ir.getHeight());
    report.appendChild(infoElement);
    infoElement = doc.createElement("bitspersample");
    infoElement.setTextContent(ir.getBitsPerSample());
    report.appendChild(infoElement);
    infoElement = doc.createElement("PixelDensity");
    infoElement.setTextContent(ir.getPixelsDensity());
    infoElement.setAttribute("PixelDensity", "" + (int)Double.parseDouble(ir.getPixelsDensity()));
    report.appendChild(infoElement);
    infoElement = doc.createElement("ByteOrder");
    infoElement.setTextContent(ir.getEndianess());
    infoElement.setAttribute("ByteOrder", ir.getEndianess());
    report.appendChild(infoElement);

    // tags
    for (ReportTag tag : getTags(ir)) {
      try {
        infoElement = doc.createElement(tag.tv.getName());
        infoElement.setTextContent(tag.tv.toString());
        infoElement.setAttribute(tag.tv.getName(), tag.tv.toString());
        infoElement.setAttribute("id", tag.tv.getId() + "");
        infoElement.setAttribute("type", tag.dif + "");
        report.appendChild(infoElement);
      } catch (Exception ex) {
        ex.toString();
      }
    }

    // implementation checker
    Element implementationCheckerElement = doc.createElement("implementation_checker");
    report.appendChild(implementationCheckerElement);

    // Baseline
    Element results = doc.createElement("results_baseline");
    List<ValidationEvent> errors = ir.getBaselineErrors();
    List<ValidationEvent> warnings = ir.getBaselineWarnings();
    if (errors != null && warnings != null) {
      addErrorsWarnings(doc, results, errors, warnings);
      implementationCheckerElement.appendChild(results);
    }

    // TiffEP
    results = doc.createElement("results_tiffep");
    errors = ir.getEPErrors();
    warnings = ir.getEPWarnings();
    if (errors != null && warnings != null) {
      addErrorsWarnings(doc, results, errors, warnings);
      implementationCheckerElement.appendChild(results);
    }

    // TiffIT
    results = doc.createElement("results_tiffit");
    errors = ir.getITErrors();
    warnings = ir.getITWarnings();
    if (errors != null && warnings != null) {
      addErrorsWarnings(doc, results, errors, warnings);
      implementationCheckerElement.appendChild(results);
    }

    // Total
    results = doc.createElement("results");
    List<ValidationEvent> errorsTotal = new ArrayList<ValidationEvent>();
    List<ValidationEvent> warningsTotal = new ArrayList<ValidationEvent>();
    if (ir.getBaselineErrors() != null) errorsTotal.addAll(ir.getBaselineErrors());
    if (ir.getEPErrors() != null) errorsTotal.addAll(ir.getEPErrors());
    if (ir.getITErrors() != null) errorsTotal.addAll(ir.getITErrors());
    if (ir.getBaselineWarnings() != null) warningsTotal.addAll(ir.getBaselineWarnings());
    if (ir.getEPWarnings() != null) warningsTotal.addAll(ir.getEPWarnings());
    if (ir.getITWarnings() != null) warningsTotal.addAll(ir.getITWarnings());
    addErrorsWarnings(doc, results, errorsTotal, warningsTotal);
    implementationCheckerElement.appendChild(results);

    return report;
  }

  /**
   * Parse an individual report to XML format.
   *
   * @param xmlfile the file name.
   * @param ir      the individual report.
   * @param rules   the policy checker.
   * @return the XML string generated.
   */
  public static String parseIndividual(String xmlfile, IndividualReport ir, Rules rules) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element report = ReportXml.buildReportIndividual(doc, ir);
      doc.appendChild(report);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);

      //StreamResult result = new StreamResult(URLEncoder.encode(xmlfile, "UTF-8"));
      StreamResult result = new StreamResult(new File(xmlfile));
      transformer.transform(source, result);
      result.getOutputStream().close();

      // To String
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      String output = writer.getBuffer().toString();

      // Schematron
      Schematron sch = new Schematron();
      try {
        //String resultsch = sch.testXML(output);
        String resultsch = sch.testXML(output, rules);
        String presch = output.substring(0, output.indexOf("</report>"));
        String postsch = output.substring(output.indexOf("</report>"));
        if (resultsch.indexOf("<svrl:schematron-output") > -1) {
          resultsch = resultsch.substring(resultsch.indexOf("<svrl:schematron-output"));
        }
        output = presch + resultsch + postsch;

        // Rewrite
        PrintWriter out = new PrintWriter(xmlfile);
        out.print(output);
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

      return output;

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    } catch (FileNotFoundException tfe) {
      tfe.printStackTrace();
    } catch (IOException tfe) {
      tfe.printStackTrace();
    }
    return "";
  }

  /**
   * Parse a global report to XML format.
   *
   * @param xmlfile the file name.
   * @param gr      the global report.
   * @return the XML string generated
   */
  public static String parseGlobal(String xmlfile, GlobalReport gr) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element globalreport = doc.createElement("globalreport");
      doc.appendChild(globalreport);

      Element individualreports = doc.createElement("individualreports");
      globalreport.appendChild(individualreports);

      // Individual reports
      for (IndividualReport ir : gr.getIndividualReports()) {
        individualreports.appendChild(ReportXml.buildReportIndividual(doc, ir));
      }

      // Statistics
      Element stats = doc.createElement("stats");
      globalreport.appendChild(stats);
      Element el = doc.createElement("reports_count");
      el.setTextContent("" + gr.getReportsCount());
      stats.appendChild(el);
      el = doc.createElement("valid_files");
      el.setTextContent("" + gr.getReportsOk());
      stats.appendChild(el);
      el = doc.createElement("invalid_files");
      el.setTextContent("" + gr.getReportsKo());
      stats.appendChild(el);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);

      File f = new File(xmlfile);
      StreamResult result = new StreamResult(f);
      transformer.transform(source, result);

      // To String
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      String output = writer.getBuffer().toString().replaceAll("\n|\r", "");

      return output;

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
}

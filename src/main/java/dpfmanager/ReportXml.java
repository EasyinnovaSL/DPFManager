package dpfmanager;

import com.easyinnova.tiff.model.ValidationEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
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
public class ReportXml {

  /**
   * Creates the ifd node.
   *
   * @param doc the doc
   * @param ifd the ifd
   * @param index the index
   * @return the element
   */
  private static Element createIfdNode(Document doc, IndividualReport ir, int index) {
    Element ifdNode = doc.createElement("ifdNode");
    Element el;

    // Number
    el = doc.createElement("number");
    el.setTextContent("" + (index+1));
    ifdNode.appendChild(el);

    // Image
    el = doc.createElement("isimg");
    if (ir.getIsimgAt(index)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // SubImage
    el = doc.createElement("hasSubIfd");
    if (ir.getHasSubIfdAt(index)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    return ifdNode;
  }
  
  /**
   * Parse an individual report to XML format.
   *
   * @param xmlfile the file name.
   * @param ir the individual report.
   */
  private static Element buildReportIndividual(Document doc, IndividualReport ir) {
    Element report = doc.createElement("new_report");

    // tiff structure
    Element tiffStructureElement = doc.createElement("tiff_structure");
    Element ifdTree = doc.createElement("ifdTree");
    for (int index = 0 ; index < ir.getIfdCount() ; index++) {
      Element ifdNode = createIfdNode(doc, ir, index);
      ifdTree.appendChild(ifdNode);
    }
    tiffStructureElement.appendChild(ifdTree);
    report.appendChild(tiffStructureElement);
    
    // basic info
    Element infoElement = doc.createElement("width");
    infoElement.setTextContent(ir.getWidth());
    report.appendChild(infoElement);
    infoElement = doc.createElement("height");
    infoElement.setTextContent(ir.getHeight());
    report.appendChild(infoElement);

    // implementation checker
    Element implementationCheckerElement = doc.createElement("implementation_checker");
    report.appendChild(implementationCheckerElement);

    // errors
    Element results = doc.createElement("results");
    List<ValidationEvent> errors = ir.getErrors();
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
    List<ValidationEvent> warnings = ir.getWarnings();
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
    
    return report;
  }
  
  /**
   * Parse an individual report to XML format.
   *
   * @param xmlfile the file name.
   * @param ir the individual report.
   */
  public static void parseIndividual(String xmlfile, IndividualReport ir) {
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
      StreamResult result = new StreamResult(new File(xmlfile));

      transformer.transform(source, result);
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
  }
  
  /**
   * Parse a global report to XML format.
   *
   * @param xmlfile the file name.
   * @param gr the global report.
   */
  public static void parseGlobal(String xmlfile, GlobalReport gr) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element globalreport = doc.createElement("globalreport_new");
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

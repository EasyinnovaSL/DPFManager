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

package dpfmanager.shell.modules.report.util;

import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGeneric;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;

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
   * Parse a global report to XML format.
   *
   * @param xmlfile the file name.
   * @param gr      the global report.
   * @return the XML string generated
   */
  public String parseGlobal(String xmlfile, GlobalReport gr) {
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
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document docreport = db.parse(new ByteArrayInputStream(ir.getConformanceCheckerReport().getBytes("UTF-8")));
        Node node = doc.importNode(docreport.getDocumentElement(), true);
        individualreports.appendChild(node);
      }

      // Statistics
      Element stats = doc.createElement("stats");
      globalreport.appendChild(stats);
      Element el = doc.createElement("reports_count");
      el.setTextContent("" + gr.getReportsCount());
      stats.appendChild(el);
      el = doc.createElement("valid_files");
      el.setTextContent("" + gr.getAllReportsOk());
      stats.appendChild(el);
      el = doc.createElement("invalid_files");
      el.setTextContent("" + gr.getAllReportsKo());
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

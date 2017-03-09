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
 * @author Adria Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.report.util;

import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGeneric;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

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
   */
  public void parseGlobal(String xmlfile, GlobalReport gr) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element globalreport = doc.createElement("globalreport");
      doc.appendChild(globalreport);

      Element individualreports = doc.createElement("individualreports");
      globalreport.appendChild(individualreports);

      // Individual reports
      for (SmallIndividualReport ir : gr.getIndividualReports()) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        try {
          File file = new File(ir.getReportPath() + ".xml");
          if (file.exists()) {
            //InputStream inputStream = new FileInputStream(file);
            //Reader reader = new InputStreamReader(inputStream, "UTF-8");
            //InputSource is = new InputSource(reader);
            //is.setEncoding("UTF-8");
            //Document docreport = db.parse(is);
            //Node node = doc.importNode(docreport.getDocumentElement(), true);
            Element el = doc.createElement("report");
            el.setTextContent(ir.getReportPath() + ".xml");
            individualreports.appendChild(el);
          }
        } catch (Exception ex) {
          ex.toString();
        }
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
      el = doc.createElement("warnings");
      el.setTextContent("" + gr.getAllReportsWarnings());
      stats.appendChild(el);
      Element duration = doc.createElement("duration");
      el = doc.createElement("hours");
      el.setTextContent("" + gr.getDurationHours());
      duration.appendChild(el);
      el = doc.createElement("minutes");
      el.setTextContent("" + gr.getDurationMinutes());
      duration.appendChild(el);
      el = doc.createElement("seconds");
      el.setTextContent("" + gr.getDurationSeconds());
      duration.appendChild(el);
      el = doc.createElement("milliseconds");
      el.setTextContent("" + gr.getDurationMillis());
      duration.appendChild(el);
      stats.appendChild(duration);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);

      File f = new File(xmlfile);
      StreamResult result = new StreamResult(f);
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

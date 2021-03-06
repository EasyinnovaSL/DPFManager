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

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.ReportGeneric;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
public class ReportXml extends ReportGeneric {
  /**
   * Parse a global report to XML format.
   *
   * @param xmlfile the file name.
   * @param gr      the global report.
   */
  public void parseGlobal(String xmlfile, GlobalReport gr, List<SmallIndividualReport> reports) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element globalreport = doc.createElement("globalreport");
      doc.appendChild(globalreport);

      Element individualreports = doc.createElement("individualreports");
      globalreport.appendChild(individualreports);

      // Individual reports
      for (SmallIndividualReport ir : reports) {
        Element report = doc.createElement("report");

        // Image name
        Element el = doc.createElement("name");
        el.setTextContent(ir.getFileName());
        report.appendChild(el);

        // ISOs
        Element isos = doc.createElement("ISOs");
        int totalWarnings = 0;
        for (String iso : gr.getCheckedIsos()) {
          if (gr.hasValidation(iso)) {
            Element isoEl = doc.createElement("iso");

            el = doc.createElement("name");
            el.setTextContent(iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso));
            isoEl.appendChild(el);

            el = doc.createElement("errors");
            el.setTextContent(ir.getNErrors(iso) + "");
            isoEl.appendChild(el);

            if (gr.hasModifiedIso(iso)){
              el = doc.createElement("errors_policy");
              el.setTextContent(ir.getNErrorsPolicy(iso) + "");
              isoEl.appendChild(el);
            }

            el = doc.createElement("warnings");
            el.setTextContent(ir.getNWarnings(iso) + "");
            isoEl.appendChild(el);

            if (gr.hasModifiedIso(iso)){
              el = doc.createElement("warnings_policy");
              el.setTextContent(ir.getNWarningsPolicy(iso) + "");
              isoEl.appendChild(el);
            }

            totalWarnings += gr.hasModifiedIso(iso) ? ir.getNWarningsPolicy(iso) : ir.getNWarnings(iso);

            isos.appendChild(isoEl);
          }
        }

        // Image result
        el = doc.createElement("result");
        if (ir.getPercent() == 100) {
          el.setTextContent("Passed");
        } else if (totalWarnings > 0){
          el.setTextContent("Passed with warnings");
        } else {
          el.setTextContent("Error");
        }
        report.appendChild(el);

        // Image score
        el = doc.createElement("score");
        el.setTextContent(ir.getPercent() + "%");
        report.appendChild(el);

        // ISOs list
        report.appendChild(isos);

        // Path
        el = doc.createElement("path");
        el.setTextContent(ir.getReportPath() + ".xml");
        report.appendChild(el);

        individualreports.appendChild(report);
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

/**
 * <h1>InteroperabilityManager.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.interoperability.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.context.DpfContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

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
 * Created by Adrià Llorens on 05/10/2016.
 */
public class InteroperabilityManager {

  private DpfContext context;
  private ResourceBundle bundle;
  private InteroperabilityValidator validator;

  public InteroperabilityManager(DpfContext context, ResourceBundle bundle, InteroperabilityValidator validator) {
    this.context = context;
    this.bundle = bundle;
    this.validator = validator;
  }

  /**
   * Saves the conformance checkers to file
   */
  public boolean writeChanges(List<ConformanceConfig> conformances) {
    try {
      // Sort
      conformances.sort(new Comparator<ConformanceConfig>() {
        @Override
        public int compare(ConformanceConfig o1, ConformanceConfig o2) {
          if (o1.isBuiltIn()){
            return -1;
          } else if (o2.isBuiltIn()){
            return 1;
          }
          return o1.getName().compareTo(o2.getName());
        }
      });

      String xmlFileOld = DPFManagerProperties.getConformancesConfig();
      String xmlFileNew = DPFManagerProperties.getConformancesConfig() + ".new";
      Document doc = getXML(conformances);

      // Write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      File newFile = new File(xmlFileNew);
      StreamResult result = new StreamResult(newFile);
      transformer.transform(new DOMSource(doc), result);

      // All OK
      File oldFile = new File(xmlFileOld);
      oldFile.delete();
      return newFile.renameTo(oldFile);
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public Document getXML(List<ConformanceConfig> conformances) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element listElement = doc.createElementNS("http://www.preforma-project/interoperability", "tns:ListOutput");
      listElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      listElement.setAttribute("xmlns:schemaLocation", "http://www.preforma-project/interoperability preformainteroperability.xsd");
      doc.appendChild(listElement);
      Element conformanceCheckers = doc.createElement("ConformanceCheckers");
      listElement.appendChild(conformanceCheckers);

      // Individual conformances
      for (ConformanceConfig conformance : conformances) {
        Document conformanceDoc = conformance.toXML();
        if (conformanceDoc != null) {
          Node node = doc.importNode(conformanceDoc.getDocumentElement(), true);
          conformanceCheckers.appendChild(node);
        }
      }

      return doc;
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getString(List<ConformanceConfig> conformances) {
    try {
      // Get XML Document
      Document doc = getXML(conformances);

      // Write the content into String
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      String output = writer.getBuffer().toString();

      return output;
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * Read the conformance checkers from configuration file
   */
  public List<ConformanceConfig> loadFromFile() {
    List<ConformanceConfig> conformances = new ArrayList<>();
    try {
      String path = DPFManagerProperties.getConformancesConfig();
      File fXmlFile = new File(path);
      if (fXmlFile.exists()) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        conformances = readConformanceCheckers(doc);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return conformances;
  }

  /**
   * Load the built in conformance checkers
   */
  public List<ConformanceConfig> loadFromBuiltIn() {
    List<ConformanceConfig> conformances = new ArrayList<>();
    try {
      String xml = DPFManagerProperties.getBuiltInDefinition();
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
      conformances = readConformanceCheckers(doc);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return conformances;
  }

  private List<ConformanceConfig> readConformanceCheckers(Document doc) {
    List<ConformanceConfig> conformances = new ArrayList<>();
    try {
      // Read the conformance checkers
      NodeList nList = doc.getDocumentElement().getElementsByTagName("conformanceChecker");
      for (int i = 0; i < nList.getLength(); i++) {
        Node conformanceNode = nList.item(i);
        ConformanceConfig conformance = new ConformanceConfig();
        conformance.fromXML(conformanceNode);
        if (!validator.validateAll(conformance)) {
          conformance.setEnabled(false);
        }
        conformances.add(conformance);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return conformances;
  }

}

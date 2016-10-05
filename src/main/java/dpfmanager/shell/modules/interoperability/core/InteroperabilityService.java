/**
 * <h1>DatabaseService.java</h1> <p> This program is free software: you can redistribute it and/or
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
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
 * Created by Adrià Llorens on 20/04/2016.
 */
@Service(BasicConfig.SERVICE_INTEROPERABILITY)
@Scope("singleton")
public class InteroperabilityService extends DpfService {

  /**
   * The conformance checkers list
   */
  private List<Conformance> conformances;

  private ResourceBundle bundle;

  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
    conformances = new ArrayList<>();
    loadFromFile();
    loadFromBuiltIn();
  }

  @Override
  protected void handleContext(DpfContext context) {
  }

  private Conformance getConformanceByName(String name) {
    for (Conformance conformance : conformances) {
      if (conformance.getName().equals(name)) {
        return conformance;
      }
    }
    return null;
  }

  /**
   * Main functions
   */

  public void add(String name, String path, String parameters, String configure, List<String> extensions) {
    File tmp = new File(path);
    if (validateUniqueName(name)) {
      if (tmp.exists() && tmp.isFile()) {
        if (validateParameters(parameters)) {
          Conformance conformance = new Conformance(name, path);
          conformance.setParameters(parameters);
          conformance.setConfiguration(configure);
          conformance.setExtensions(extensions);
          conformance.setEnabled(true);
          conformances.add(conformance);
          if (writeChanges()) {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceAdded").replace("%1", name).replace("%2", path)));
          } else {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
          }
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParamsError")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidPath").replace("%1", path)));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidName").replace("%1", name)));
    }
  }

  public void edit(String name, String path) {
    Conformance conformance = getConformanceByName(name);
    if (conformance != null) {
      File tmp = new File(path);
      if (tmp.exists() && tmp.isFile()) {
        conformance.setPath(path);
        if (writeChanges()) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceEdited").replace("%1", name)));
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidPath").replace("%1", path)));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  public void remove(String name) {
    Conformance conformance = getConformanceByName(name);
    if (conformance != null) {
      conformances.remove(conformance);
      if (writeChanges()) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceRemoved").replace("%1", name)));
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  public void list(String name) {
    Conformance conformance = getConformanceByName(name);
    if (conformance != null) {
      String xml = conformance.toString();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, xml));
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  public void listAll() {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, getString()));
  }

  public void setParameters(String name, String params) {
    Conformance conformance = getConformanceByName(name);
    if (conformance != null) {
      if (validateParameters(params)) {
        conformance.setParameters(params);
        if (writeChanges()) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParams").replace("%1", name)));
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParamsError")));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  public void setConfiguration(String name, String config) {
    Conformance conformance = getConformanceByName(name);
    if (conformance != null) {
      File tmp = new File(config);
      if (tmp.exists() && tmp.isFile()) {
        conformance.setConfiguration(config);
        if (writeChanges()) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceConfigure").replace("%1", name)));
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidConfig").replace("%1", config)));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  public void setExtensions(String name, List<String> extensions) {
    Conformance conformance = getConformanceByName(name);
    if (conformance != null) {
      conformance.setExtensions(extensions);
      if (writeChanges()) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceConfigure").replace("%1", name)));
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  public void setEnabled(String name, boolean enabled) {
    Conformance conformance = getConformanceByName(name);
    if (conformance != null) {
      if (validateEnable(conformance, enabled)) {
        conformance.setEnabled(enabled);
        if (writeChanges()) {
          if (enabled) {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceEnabled").replace("%1", name)));
          } else {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceDisabled").replace("%1", name)));
          }
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceCannotEnable")));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  /**
   * Validacions
   */
  private boolean validateParameters(String params) {
    return params.contains("%config%") && params.contains("%input%");
  }

  private boolean validateEnable(Conformance conformance, boolean enabled) {
    if ((enabled && !conformance.getConfiguration().isEmpty() && !conformance.getParameters().isEmpty()) || !enabled) {
      return true;
    }
    return false;
  }

  private boolean validateUniqueName(String name) {
    return getConformanceByName(name) == null;
  }

  /**
   * Saves the conformance checkers to file
   */
  private boolean writeChanges() {
    try {
      String xmlFileOld = DPFManagerProperties.getConformancesConfig();
      String xmlFileNew = DPFManagerProperties.getConformancesConfig() + ".new";
      Document doc = getXML();

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

  private Document getXML() {
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
      for (Conformance conformance : conformances) {
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

  private String getString() {
    try {
      // Get XML Document
      Document doc = getXML();

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
  private void loadFromFile() {
    try {
      String path = DPFManagerProperties.getConformancesConfig();
      File fXmlFile = new File(path);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      conformances.addAll(readConformanceCheckers(doc));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Load the built in conformance checkers
   */
  private void loadFromBuiltIn() {
    try {
      String xml = IOUtils.toString(getClass().getResourceAsStream("/internalCheckers/BuiltIn.xml"));
      boolean needWrite = false;
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
      List<Conformance> builtInCC =  readConformanceCheckers(doc);
      for (Conformance conformance : builtInCC){
        if (getConformanceByName(conformance.getName()) == null){
          conformances.add(conformance);
          needWrite = true;
        }
      }

      // Write if needed
      if (needWrite){
        writeChanges();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private List<Conformance> readConformanceCheckers(Document doc) {
    List<Conformance> conformances = new ArrayList<>();
    try {
      // Read the conformance checkers
      NodeList nList = doc.getDocumentElement().getElementsByTagName("conformanceChecker");
      for (int i = 0; i < nList.getLength(); i++) {
        Node conformanceNode = nList.item(i);
        Conformance conformance = new Conformance();
        conformance.fromXML(conformanceNode);
        conformances.add(conformance);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return conformances;
  }

  @PreDestroy
  public void finish() {

  }

}

/**
 * <h1>ConformanceConfig.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.interoperability.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
 * Created by Adria Llorens on 04/10/2016.
 */
public class ConformanceConfig {

  private String uuid;
  private String name;
  private String path;
  private String parameters;
  private String configuration;
  private boolean enabled;
  private List<String> extensions;

  public ConformanceConfig() {
    extensions = new ArrayList<>();
    uuid = "";
  }

  public ConformanceConfig(ConformanceConfig copy) {
    uuid = copy.uuid;
    copy(copy);
  }

  public ConformanceConfig(String n, String p) {
    Long lUuid = System.currentTimeMillis();
    uuid = lUuid.toString();
    name = n;
    path = p;
    extensions = new ArrayList<>();
  }

  public void copy(ConformanceConfig copy){
    name = copy.getName();
    path = copy.getPath();
    parameters = copy.getParameters();
    configuration = copy.getConfiguration();
    enabled = copy.isEnabled();
    extensions = copy.getExtensions();
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getParameters() {
    return parameters;
  }

  public List<String> getParametersList() {
    List<String> params = new ArrayList<>();
    for (String arg : parameters.split(" ")) {
      if (!arg.isEmpty()) {
        params.add(arg);
      }
    }
    return params;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public List<String> getExtensions() {
    return extensions;
  }

  public void setExtensions(List<String> extensions) {
    this.extensions = extensions;
  }

  public void addExtension(String ext) {
    extensions.add(ext);
  }

  public boolean isBuiltIn() {
    return path.equals("built-in");
  }

  public String getPrettyExtensions() {
    String result = "";
    for (String ext : extensions) {
      result += ext + ", ";
    }
    if (!result.isEmpty()) {
      result = result.substring(0, result.length() - 2);
    }
    return result;
  }

  /**
   * Parse the configuration into XML
   */
  public Document toXML() {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element conformanceChecker = doc.createElement("conformanceChecker");
      doc.appendChild(conformanceChecker);

      // Uuid
      Element elUuid = doc.createElement("uuid");
      elUuid.setTextContent(uuid);
      conformanceChecker.appendChild(elUuid);

      // Name
      Element elName = doc.createElement("name");
      elName.setTextContent(name);
      conformanceChecker.appendChild(elName);

      // Location
      Element elLocation = doc.createElement("location");
      elLocation.setTextContent(path);
      conformanceChecker.appendChild(elLocation);

      // Parameters
      Element elParameters = doc.createElement("parameters");
      elParameters.setTextContent(parameters);
      conformanceChecker.appendChild(elParameters);

      // State
      Element elState = doc.createElement("state");
      String state = "disabled";
      if (enabled) {
        state = "enabled";
      }
      elState.setTextContent(state);
      conformanceChecker.appendChild(elState);

      // Configuration
      Element elConfiguration = doc.createElement("defaultConfiguration");
      elConfiguration.setTextContent(configuration);
      conformanceChecker.appendChild(elConfiguration);

      // Extensions
      Element elExtensions = doc.createElement("extensions");
      for (String ext : extensions) {
        Element elExt = doc.createElement("extension");
        elExt.setTextContent(ext);
        elExtensions.appendChild(elExt);
      }
      conformanceChecker.appendChild(elExtensions);

      return doc;
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse the configuration into XML
   */
  @Override
  public String toString() {
    try {
      // Get XML Document
      Document doc = getXMLFullDocument();
      if (doc != null) {
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
      }
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";

  }

  private Document getXMLFullDocument() {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElementNS("http://www.preforma-project/interoperability", "tns:ConformanceCheckerInfo");
      rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      rootElement.setAttribute("xmlns:schemaLocation", "http://www.preforma-project/interoperability preformainteroperability.xsd");
      doc.appendChild(rootElement);

      Document conformanceDoc = toXML();
      if (conformanceDoc != null) {
        Node node = doc.importNode(conformanceDoc.getDocumentElement(), true);
        rootElement.appendChild(node);
      }

      return doc;
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse the configuration from XML
   */
  public void fromXML(Node conformanceNode) {
    if (conformanceNode.getNodeType() == Node.ELEMENT_NODE) {
      NodeList attribs = conformanceNode.getChildNodes();
      for (int j = 0; j < attribs.getLength(); j++) {
        Node attribNode = attribs.item(j);
        if (attribNode.getNodeType() == Node.ELEMENT_NODE) {
          Element attribElem = (Element) attribNode;
          switch (attribElem.getTagName()) {
            case "uuid":
              setUuid(attribElem.getTextContent());
              break;
            case "name":
              setName(attribElem.getTextContent());
              break;
            case "location":
              setPath(attribElem.getTextContent());
              break;
            case "state":
              String state = attribElem.getTextContent();
              boolean bstate = false;
              if (state.equals("enabled")) {
                bstate = true;
              }
              setEnabled(bstate);
              break;
            case "defaultConfiguration":
              setConfiguration(attribElem.getTextContent());
              break;
            case "parameters":
              setParameters(attribElem.getTextContent());
              break;
            case "extensions":
              NodeList extensions = attribElem.getChildNodes();
              for (int x = 0; x < extensions.getLength(); x++) {
                Node extNode = extensions.item(x);
                if (extNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element extElem = (Element) extNode;
                  addExtension(extElem.getTextContent());
                }
              }
              break;
          }
        }
      }
    }
  }

}

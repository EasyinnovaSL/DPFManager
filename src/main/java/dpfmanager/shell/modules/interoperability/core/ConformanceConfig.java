package dpfmanager.shell.modules.interoperability.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.modules.report.core.IndividualReport;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
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
 * Created by Adrià Llorens on 04/10/2016.
 */
public class ConformanceConfig {

  private String name;
  private String path;
  private String parameters;
  private String configuration;
  private boolean enabled;
  private List<String> extensions;

  public ConformanceConfig(){
    extensions = new ArrayList<>();
  }

  public ConformanceConfig(String name, String path){
    this.name = name;
    this.path = path;
    extensions = new ArrayList<>();
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
    for (String arg : parameters.split(" ")){
      if (!arg.isEmpty()){
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

  public void addExtension(String ext){
    extensions.add(ext);
  }

  public boolean isBuiltIn(){
    return path.equals("built-in");
  }

  /**
   * Parse the configuration into XML
   */
  public Document toXML(){
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element conformanceChecker = doc.createElement("conformanceChecker");
      doc.appendChild(conformanceChecker);

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
      for (String ext : extensions){
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
  public String toString(){
    try{
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

  private Document getXMLFullDocument(){
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElementNS("http://www.preforma-project/interoperability","tns:ConformanceCheckerInfo");
      rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      rootElement.setAttribute("xmlns:schemaLocation", "http://www.preforma-project/interoperability preformainteroperability.xsd");
      doc.appendChild(rootElement);

      Document conformanceDoc = toXML();
      if (conformanceDoc != null){
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
  public void fromXML(Node conformanceNode){
    if (conformanceNode.getNodeType() == Node.ELEMENT_NODE) {
      NodeList attribs = conformanceNode.getChildNodes();
      for (int j = 0; j < attribs.getLength(); j++) {
        Node attribNode = attribs.item(j);
        if (attribNode.getNodeType() == Node.ELEMENT_NODE) {
          Element attribElem = (Element) attribNode;
          switch (attribElem.getTagName()) {
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

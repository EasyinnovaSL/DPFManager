/**
 * <h1>Configuration.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @author Víctor Muñoz Sola
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.configuration;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.policy_checker.model.Rule;
import com.easyinnova.policy_checker.model.Rules;

import dpfmanager.conformancechecker.tiff.metadata_fixer.Fix;
import dpfmanager.conformancechecker.tiff.metadata_fixer.Fixes;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by easy on 06/10/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "isos",
    "modifiedIsos",
    "rules",
    "formats",
    "fixes",
    "output",
    "description",
    "version",
    "bundle"
})
@XmlRootElement(name = "configuration")
public class Configuration {
  private ArrayList<String> isos;
  private Map<String, ArrayList<String>> modifiedIsos;
  private Rules rules;
  private ArrayList<String> formats;
  private Fixes fixes;
  private String output = null;
  private String description = null;
  private int version;

  @XmlTransient
  private boolean quick;

  private ResourceBundle bundle;

  /**
   * Instantiates a new Configuration.
   */
  public Configuration() {
    isos = new ArrayList<>();
    rules = new Rules();
    formats = new ArrayList<>();
    fixes = new Fixes();
    version = 0;
    bundle = DPFManagerProperties.getBundle();
    modifiedIsos = new HashMap<>();
  }

  /**
   * Instantiates a new Configuration with params
   */
  public Configuration(Rules rules, Fixes fixes, ArrayList<String> formats) {
    isos = new ArrayList<>();
    this.rules = rules;
    this.formats = formats;
    this.fixes = fixes;
    modifiedIsos = new HashMap<>();
  }

  /**
   * Set the default values for a new configuration
   */
  public void initDefault() {
    addISO(ImplementationCheckerLoader.getDefaultIso());
    addFormat("HTML");
  }

  /**
   * Gets isos.
   *
   * @return the isos
   */
  public ArrayList<String> getIsos() {
    return isos;
  }

  public void addISO(String iso) {
    if (!isos.contains(iso)) {
      isos.add(iso);
    }
  }

  public void addFormat(String format) {
    if (!formats.contains(format)) {
      formats.add(format);
    }
  }

  /**
   * Gets formats.
   *
   * @return the formats
   */
  public ArrayList<String> getFormats() {
    return formats;
  }

  /**
   * Gets fixes.
   *
   * @return the fixes
   */
  public Fixes getFixes() {
    return fixes;
  }

  /**
   * Gets rules.
   *
   * @return the rules
   */
  public Rules getRules() {
    return rules;
  }

  /**
   * Sets output.
   *
   * @param path the path
   */
  public void setOutput(String path) {
    output = path;
  }

  /**
   * Sets description.
   *
   * @param desc the description
   */
  public void setDescription(String desc) {
    description = desc;
  }

  /**
   * Gets output. Null means default.
   *
   * @return the output
   */
  public String getOutput() {
    return output;
  }

  private void deleteNonCheckedModifiedIsos(){
    Set<String> keys = modifiedIsos.keySet();
    for (String key : keys){
      if (!isos.contains(key)){
        modifiedIsos.remove(key);
      }
    }
  }

  /**
   * Save file.
   *
   * @param filename the filename
   * @throws Exception the exception
   */
  public void SaveFile(String filename) throws Exception {
    deleteNonCheckedModifiedIsos();

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    Document doc = docBuilder.newDocument();

    Element configurationE = doc.createElement("configuration");
    // XML Version
    Element versionE = doc.createElement("version");
    versionE.setTextContent(String.valueOf(DpFManagerConstants.CONFIGURATION_VERSION));
    configurationE.appendChild(versionE);
    // ISOs
    Element isosE = doc.createElement("isos");
    for (String iso : isos) {
      Element elem = doc.createElement("iso");
      elem.setTextContent(iso);
      isosE.appendChild(elem);
    }
    configurationE.appendChild(isosE);
    // Formats
    Element formatsE = doc.createElement("formats");
    for (String format : formats) {
      Element elem = doc.createElement("format");
      elem.setTextContent(format);
      formatsE.appendChild(elem);
    }
    configurationE.appendChild(formatsE);
    // Modified Isos
    Element modifiedE = doc.createElement("modified-isos");
    for (String key : modifiedIsos.keySet()) {
      List<String> deletedIds = modifiedIsos.get(key);
      if (deletedIds.size() > 0) {
        Element modificationE = doc.createElement("modification");
        Element isoE = doc.createElement("iso");
        isoE.setTextContent(key);
        Element rulesE = doc.createElement("rules");
        for (String id : deletedIds) {
          Element idE = doc.createElement("rule-id");
          idE.setTextContent(id);
          rulesE.appendChild(idE);
        }
        modificationE.appendChild(isoE);
        modificationE.appendChild(rulesE);
        modifiedE.appendChild(modificationE);
      }
    }
    configurationE.appendChild(modifiedE);
    // Rules
    Element rulesE = doc.createElement("rules");
    if (rules != null) {
      for (Rule rule : rules.getRules()) {
        Element ruleE = doc.createElement("rule");
        if (rule.getTag() != null) {
          Element elem = doc.createElement("tag");
          elem.setTextContent(rule.getTag());
          ruleE.appendChild(elem);
        }
        if (rule.getOperator() != null) {
          Element elem = doc.createElement("operator");
          elem.setTextContent(rule.getOperator());
          ruleE.appendChild(elem);
        }
        if (rule.getValue() != null) {
          Element elem = doc.createElement("value");
          elem.setTextContent(rule.getValue());
          ruleE.appendChild(elem);
        }
        if (rule.getWarning()) {
          Element elem = doc.createElement("warning");
          elem.setTextContent(String.valueOf(rule.getWarning()));
          ruleE.appendChild(elem);
        }
        rulesE.appendChild(ruleE);
      }
    }
    configurationE.appendChild(rulesE);
    Element fixesE = doc.createElement("fixes");
    // Fixes
    if (fixes != null) {
      for (Fix fix : fixes.getFixes()) {
        Element fixE = doc.createElement("fix");
        if (fix.getTag() != null) {
          Element elem = doc.createElement("tag");
          elem.setTextContent(fix.getTag());
          fixE.appendChild(elem);
        }
        if (fix.getOperator() != null) {
          Element elem = doc.createElement("operator");
          elem.setTextContent(fix.getOperator());
          fixE.appendChild(elem);
        }
        if (fix.getValue() != null) {
          Element elem = doc.createElement("value");
          elem.setTextContent(fix.getValue());
          fixE.appendChild(elem);
        }
        fixesE.appendChild(fixE);
      }
    }
    configurationE.appendChild(fixesE);
    // Output
    if (output != null) {
      Element elem = doc.createElement("output");
      elem.setTextContent(output);
      configurationE.appendChild(elem);
    }
    // Description
    if (description != null) {
      Element elem = doc.createElement("description");
      elem.setTextContent(description);
      configurationE.appendChild(elem);
    }
    // Finish
    doc.appendChild(configurationE);

    // write the content into xml file
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    DOMSource source = new DOMSource(doc);

    File f = new File(filename);
    StreamResult result = new StreamResult(f);
    transformer.transform(source, result);
  }

  public StreamResult configurationToDocument() {
    try {
      String filename = "configurationToString.xml";
      SaveFile(filename);
      File fXmlFile = new File(filename);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);

      fXmlFile.delete();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  private InputStream getInputStream(String filename) throws Exception {
    InputStream sc = null;
    if (Files.exists(Paths.get(filename))) {
      // Look in local dir
      sc = new FileInputStream(filename);
    } else {
      // Look in JAR
      CodeSource src = Configuration.class.getProtectionDomain().getCodeSource();
      if (src != null) {
        URL jar = src.getLocation();
        ZipInputStream zip = new ZipInputStream(jar.openStream());
        ZipEntry zipFile;
        while ((zipFile = zip.getNextEntry()) != null) {
          String name = zipFile.getName();
          if (name.contains(filename)) {
            try {
              sc = zip;
            } catch (Exception ex) {
              throw new Exception("");
            }
          }
        }
      } else {
        throw new Exception("");
      }
    }
    return sc;
  }

  /**
   * Read file new format (xml). From input stream
   *
   * @param is the input stream
   * @throws Exception the exception
   */
  public void ReadFileNew(InputStream is) throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(is);
    ReadFileNew(doc);
  }

  /**
   * Read file new format (xml). From path
   *
   * @param filename the file path
   * @throws Exception the exception
   */
  public void ReadFileNew(String filename) throws Exception {
    File fXmlFile = new File(filename);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(fXmlFile);
    ReadFileNew(doc);
  }

  /**
   * Read file new format (xml). From XML Document object
   *
   * @param doc the xml document object
   * @throws Exception the exception
   */
  public void ReadFileNew(Document doc) throws Exception {
    // Read xml version, output and description
    NodeList nList = doc.getDocumentElement().getChildNodes();
    for (int i = 0; i < nList.getLength(); i++) {
      Node node = nList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element elem = (Element) node;
        switch (elem.getTagName()) {
          case "version":
            version = Integer.parseInt(elem.getTextContent());
            break;
          case "output":
            output = elem.getTextContent();
            break;
          case "description":
            description = elem.getTextContent();
            break;
        }
      }
    }

    // Read ISOs
    NodeList tmpList = doc.getElementsByTagName("isos");
    if (tmpList.getLength() > 0){
      Node isosNode = tmpList.item(0);
      NodeList isoList = isosNode.getChildNodes();
      for (int i = 0; i < isoList.getLength(); i++) {
        Node node = isoList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element elem = (Element) node;
          String iso = elem.getTextContent();
          if (version == 1) {
            // Old iso format
            if (parseOldToNewIso(iso) != null) {
              isos.add(parseOldToNewIso(iso));
            }
          } else if (version >= 2) {
            // New iso format
            isos.add(elem.getTextContent());
          }
        }
      }
    }


    // Read formats
    NodeList formatsList = doc.getElementsByTagName("format");
    for (int i = 0; i < formatsList.getLength(); i++) {
      Node node = formatsList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element elem = (Element) node;
        formats.add(elem.getTextContent());
      }
    }

    // Read modified isos
    if (version >= 3) {
      NodeList modificationList = doc.getElementsByTagName("modification");
      for (int i = 0; i < modificationList.getLength(); i++) {
        Node node = modificationList.item(i);
        NodeList childs = node.getChildNodes();
        String iso = null;
        ArrayList<String> idsList = new ArrayList<>();
        for (int j = 0; j < childs.getLength(); j++) {
          Node child = childs.item(j);
          if (child.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) child;
            if (elem.getTagName().equals("iso")) {
              iso = elem.getTextContent();
            } else if (elem.getTagName().equals("rules")) {
              NodeList grandsons = child.getChildNodes();
              for (int x = 0; x < grandsons.getLength(); x++) {
                Node grandson = grandsons.item(x);
                if (grandson.getNodeType() == Node.ELEMENT_NODE) {
                  Element elem2 = (Element) grandson;
                  if (elem2.getTagName().equals("rule-id")) {
                    idsList.add(elem2.getTextContent());
                  }
                }
              }
            }
          }
        }
        if (iso != null) {
          modifiedIsos.put(iso, idsList);
        }
      }
    }

    // Read rules
    NodeList rulesList = doc.getElementsByTagName("rule");
    for (int i = 0; i < rulesList.getLength(); i++) {
      Node node = rulesList.item(i);
      String tag = null, operator = null, value = null;
      boolean warning = false;
      NodeList childs = node.getChildNodes();
      for (int j = 0; j < childs.getLength(); j++) {
        Node child = childs.item(j);
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          Element elem = (Element) child;
          switch (elem.getTagName()) {
            case "tag":
              tag = elem.getTextContent();
              break;
            case "operator":
              operator = elem.getTextContent();
              break;
            case "value":
              value = elem.getTextContent();
              break;
            case "warning":
              warning = elem.getTextContent().equals("true");
              break;
          }
        }
      }
      rules.addRule(tag, operator, value, warning);
    }

    // Read fixes
    NodeList fixesList = doc.getElementsByTagName("fix");
    for (int i = 0; i < fixesList.getLength(); i++) {
      Node node = fixesList.item(i);
      String tag = null, operator = null, value = null;
      NodeList childs = node.getChildNodes();
      for (int j = 0; j < childs.getLength(); j++) {
        Node child = childs.item(j);
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          Element elem = (Element) child;
          switch (elem.getTagName()) {
            case "tag":
              tag = elem.getTextContent();
              break;
            case "operator":
              operator = elem.getTextContent();
              break;
            case "value":
              value = elem.getTextContent();
              break;
          }
        }
      }
      fixes.addFix(tag, operator, value);
    }

    // Read output
    output = null;
    NodeList outputList = doc.getElementsByTagName("output");
    if (outputList.getLength() > 0) {
      Node node = outputList.item(0);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element elem = (Element) node;
        output = elem.getTextContent();
      }
    }
  }

  private String parseOldToNewIso(String old) {
    if (old.equals("Baseline")) return "TIFF_Baseline_Core_6_0";
    if (old.equals("Tiff/EP")) return "TIFF_EP";
    if (old.equals("Tiff/IT")) return "TiffITProfileChecker";
    if (old.equals("Tiff/IT-1")) return "TiffITP1ProfileChecker";
    if (old.equals("Tiff/IT-2")) return "TiffITP2ProfileChecker";
    return null;
  }

  /**
   * Read file.
   *
   * @param filename the filename
   * @throws Exception the exception
   */
  public void ReadFile(String filename) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream(filename)));
    try {
      String line = br.readLine();
      if (line != null && line.startsWith("<?xml")) {
        // New format
        br.close();
        ReadFileNew(filename);
      } else {
        // Old format
        output = null;
        while (line != null) {
          if (line.contains("\t")) {
            String field1 = line.substring(0, line.indexOf("\t"));
            String field2 = line.substring(line.indexOf("\t") + 1);
            switch (field1) {
              case "ISO":
                isos.add(parseOldToNewIso(field2));
                break;
              case "FORMAT":
                formats.add(field2);
                break;
              case "FIX":
                fixes.addFixFromTxt(field2);
                break;
              case "RULE":
                rules.addRuleFromTxt(field2);
                break;
              case "OUTPUT":
                output = field2;
                break;
            }
          }
          line = br.readLine();
        }
      }
    } finally {
      br.close();
    }
  }

  /**
   * Gets txt rules.
   *
   * @return the txt rules
   */
  public String getTxtRules() {
    String txt = "";
    for (int i = 0; i < rules.getRules().size(); i++) {
      Rule rule = rules.getRules().get(i);
      String val = "";
      if (rule.getValue() != null) val = rule.getValue();
      txt += (rule.getTag() + " " + rule.getOperator() + " " + val).trim();
      if (i + 1 < rules.getRules().size()) txt += ", ";
    }
    return txt;
  }

  /**
   * Gets txt fixes.
   *
   * @return the txt fixes
   */
  public String getTxtFixes() {
    String txt = "";
    for (int i = 0; i < fixes.getFixes().size(); i++) {
      Fix fix = fixes.getFixes().get(i);
      String val = "";
      String op = fix.getOperator();
      if (op == null) {
        op = "";
      } else {
        op = bundle.getString(fix.getOperator());
      }
      if (fix.getValue() != null && !fix.getValue().isEmpty()) val = "'" + fix.getValue() + "'";
      txt += (op + " " + fix.getTag() + " " + val).trim();
      if (i + 1 < fixes.getFixes().size()) txt += ", ";
    }
    return txt;
  }

  /**
   * Gets txt formats.
   *
   * @return the txt formats
   */
  public String getTxtFormats() {
    String txt = "";
    for (int i = 0; i < formats.size(); i++) {
      txt += formats.get(i);
      if (i + 1 < formats.size()) txt += ", ";
    }
    return txt;
  }

  /**
   * Gets txt isos.
   *
   * @return the txt isos
   */
  public String getTxtIsos() {
    String txt = "";
    for (int i = 0; i < isos.size(); i++) {
      txt += isos.get(i);
      if (i + 1 < isos.size()) txt += ", ";
    }
    return txt;
  }

  public String getDescription() {
    return description;
  }

  public void addModifiedIso(String isoId, ArrayList<String> deleted) {
    modifiedIsos.put(isoId, deleted);
  }

  public void removeModifiedIso(String isoId) {
    modifiedIsos.remove(isoId);
  }

  public boolean hasModifiedIsos(String isoId) {
    if (modifiedIsos.containsKey(isoId)) {
      return modifiedIsos.get(isoId).size() > 0;
    }
    return false;
  }

  public List<String> getModifiedIso(String isoId) {
    if (hasModifiedIsos(isoId)) {
      return modifiedIsos.get(isoId);
    }
    return new ArrayList<>();
  }

  public Map<String, ArrayList<String>> getModifiedIsos() {
    return modifiedIsos;
  }

  public void setFixes(Fixes fixes) {
    this.fixes = fixes;
  }

  public void setRules(Rules rules) {
    this.rules = rules;
  }

  public void setModifiedIsos(Map<String, ArrayList<String>> modifiedIsos) {
    this.modifiedIsos = modifiedIsos;
  }

  public void setIsos(ArrayList<String> isos) {
    this.isos = isos;
  }

  public void setFormats(ArrayList<String> formats) {
    this.formats = formats;
  }

  public boolean hasRules(){
    return rules != null && rules.getRules() != null && rules.getRules().size() > 0;
  }

  public boolean isQuick() {
    return quick;
  }

  public void setQuick(boolean quick) {
    this.quick = quick;
  }
}

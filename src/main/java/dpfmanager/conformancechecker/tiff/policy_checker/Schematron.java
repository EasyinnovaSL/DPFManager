/**
 * <h1>Schematron.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.policy_checker;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.AssertType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RulesType;
import dpfmanager.shell.core.DPFManagerProperties;

import com.easyinnova.tiff.model.TiffDocument;

import net.sf.saxon.TransformerFactoryImpl;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.schematron.constant.Constants;
import org.apache.camel.component.schematron.processor.ClassPathURIResolver;
import org.apache.camel.component.schematron.processor.SchematronProcessor;
import org.apache.camel.component.schematron.processor.SchematronProcessorFactory;
import org.apache.camel.component.schematron.processor.TemplatesFactory;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by easy on 21/09/2015.
 */
public class Schematron {
  /**
   * The Context.
   */
  CamelContext context;
  /**
   * The Template.
   */
  ProducerTemplate template;

  /**
   * Instantiates a new Schematron.
   */
  public Schematron()
  {
    context = new DefaultCamelContext();
    template = context.createProducerTemplate();
  }

  /**
   * Convert string.
   *
   * @param txt the txt
   * @return the string
   */
  String convert(String txt) {
    String converted = txt;
    converted = converted.replace("<","#PP#");
    converted = converted.replace(">","#GG#");
    return converted;
  }

  public Validator testXMLnoSchematron(TiffDocument tiffModel, Rules rules) throws Exception {
    String xmlTiffModel = TiffConformanceChecker.getValidationXmlString(tiffModel);

    ImplementationCheckerObjectType rulesObj = new ImplementationCheckerObjectType();
    RulesType rulesType = new RulesType();
    int index = 1;
    for (Rule rule : rules.getRules()) {
      RuleType ruleObj = new RuleType();
      if (rule.getWarning()) ruleObj.setLevel("warning");
      else ruleObj.setLevel("error");
      ruleObj.setId("pol-" + index++);
      ruleObj.setContext("ifd[class=image]");
      AssertType assertObj = new AssertType();
      String operator = rule.getOperator();
      boolean stringValue = false;
      if (operator.equals("=")) operator = "==";
      if (rule.getValue().contains(",")) stringValue = true;
      String sTest = "";
      ArrayList<String> values = new ArrayList<>();
      for (String value : rule.getValue().split(";")) {
        String value2 = value;
        String tag = rule.getTag();
        if (tag.equals("Compression")) value2 = TiffConformanceChecker.compressionCode(value) + "";
        if (tag.equals("Photometric")) {
          value2 = TiffConformanceChecker.photometricCode(value) + "";
          if (value2.equals("1")) values.add("0");
        }
        if (tag.equals("Planar")) {
          value2 = TiffConformanceChecker.planarCode(value) + "";
        }
        values.add(value2);
      }
      for (String value : values) {
        String tag = rule.getTag();
        if (tag.equals("Photometric")) tag = "PhotometricInterpretation";
        if (tag.equals("Planar")) tag = "PlanarConfiguration";
        if (sTest.length() > 0) sTest += " || ";
        sTest += "{tags.tag[name=" + tag + "] " + operator + " ";
        if (stringValue) sTest += "'";
        sTest += value;
        if (stringValue) sTest += "'";
        sTest += "}";
      }
      assertObj.setTest(sTest);
      if (rule.getWarning()) assertObj.setValue("Invalid value of tag " + rule.getTag());
      else assertObj.setValue("Warning on value of tag " + rule.getTag());
      ruleObj.setAssert(assertObj);
      rulesType.getRule().add(ruleObj);
    }
    rulesObj.getRules().add(rulesType);

    TiffValidationObject model = Validator.createValidationObjectFromXml(xmlTiffModel);

    Validator validation = new Validator();
    validation.validate(model, rulesObj, false);

    return validation;
  }

  /**
   * Test xml string.
   *
   * @param xmlFile the xml file
   * @param rules   the rules
   * @return the string
   * @throws Exception the exception
   */
  public String testXML(String xmlFile, Rules rules) throws Exception {
    InputStream sc = getInputStream("sch/rules.sch");
    if (sc != null) {
      Document doc = readXml(sc);

      // Reset
      NodeList nodes = doc.getElementsByTagName("rule");
      while (nodes.getLength() > 0) {
        Element el = (Element) nodes.item(0);
        el.getParentNode().removeChild(el);
      }

      // Insert rules
      Element pattern = (Element) doc.getElementsByTagName("pattern").item(0);
      if (rules != null) {
        for (Rule r : rules.getRules()) {
          Element newrule = doc.createElementNS(pattern.getNamespaceURI(), "rule");
          newrule.setAttribute("context", r.getTag());

          Element assertion;
          if (r.getWarning())
            assertion = doc.createElementNS(newrule.getNamespaceURI(), "report");
          else
            assertion = doc.createElementNS(newrule.getNamespaceURI(), "assert");
          String sval = r.getValue();
          if (r.getType().equals("string")) sval = "'" + sval + "'";
          String stest = "";
          if (sval.contains(";")) {
            for (String spart : sval.substring(1, sval.length() - 1).split(";")) {
              if (stest.length() > 0) stest += " or ";
              stest += "@" + r.getTag() + " " + convert(r.getOperator()) + " '" + spart + "'";
            }
          } else {
            stest = "@" + r.getTag() + " " + convert(r.getOperator()) + " " + sval;
          }
          assertion.setAttribute("test", stest);
          assertion.setTextContent("Invalid " + r.getTag() + ": #PP2#value-of select=\"@" + r.getTag() + "\"/#GG#");

          newrule.appendChild(assertion);

          pattern.appendChild(newrule);
        }
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      DOMSource source = new DOMSource(doc);

      StringWriter writer = new StringWriter();
      transformer.transform(source, new StreamResult(writer));
      String s = writer.toString();
      s = s.replace("#PP#", "&lt;").replace("#GG#", ">").replace("#PP2#", "<");

      String report = testXML(xmlFile, s);
      return report;
    } else {
      return "";
    }
  }

  /**
   * Read xml document.
   *
   * @param is the is
   * @return the document
   * @throws SAXException                 the sax exception
   * @throws IOException                  the io exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static Document readXml(InputStream is) throws SAXException, IOException,
      ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    dbf.setValidating(false);
    dbf.setIgnoringComments(false);
    dbf.setIgnoringElementContentWhitespace(true);
    dbf.setNamespaceAware(true);

    DocumentBuilder db = null;
    db = dbf.newDocumentBuilder();
    db.setEntityResolver(new NullResolver());

    return db.parse(is);
  }

  private InputStream getInputStream(String schema) throws Exception {
    InputStream sc = null;
    if (Files.exists(Paths.get(schema))) {
      // Look in local dir
      //System.out.println("Schematron rules found in local dir");
      sc = new FileInputStream(schema);
    } else {
      Class cls = Schematron.class;
      ClassLoader cLoader = cls.getClassLoader();
      sc = cLoader.getResourceAsStream(schema);
    }
    return sc;
  }

  /**
   * Test xml string.
   *
   * @param xmlFileOriginal the xml file
   * @param content  the content
   * @return the string
   * @throws Exception the exception
   */
  public String testXML(String xmlFileOriginal, String content) throws Exception {
    String result;
    try {
      String xmlFile = xmlFileOriginal;
      if (!xmlFileOriginal.contains("<?xml version"))
        xmlFile = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + xmlFile;
      InputStream sc = new StringInputStream(content);
      TransformerFactory factory = new TransformerFactoryImpl();
      factory.setURIResolver(new ClassPathURIResolver(Constants.SCHEMATRON_TEMPLATES_ROOT_DIR));
      Templates rules = TemplatesFactory.newInstance().newTemplates(sc);
      SchematronProcessor proc = SchematronProcessorFactory.newScehamtronEngine(rules);
      result = proc.validate(xmlFile);
      result = result.substring(0, result.indexOf("<!--")) + result.substring(result.indexOf("-->")+3);
    } catch (Exception ex) {
      ex.printStackTrace();
      result = "";
    }
    return result;
  }
}

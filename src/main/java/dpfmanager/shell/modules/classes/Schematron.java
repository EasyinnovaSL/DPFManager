package dpfmanager.shell.modules.classes;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

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
public class Schematron extends CamelTestSupport {
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
   * Run schematron string.
   *
   * @param xmlFilename the xml filename
   * @param folder      the folder
   * @return the string
   */
  public String RunSchematron(String xmlFilename, String folder) {
    try {
      context.addRoutes(new RouteBuilder() {
        public void configure() {
          from("direct:xml").to("schematron://sch/rules.sch").to("mock:result");
          //.to("file:result.xml");
        }
      });
      context.start();
      template.sendBody("direct:xml", xmlFilename);
      context.stop();

      MockEndpoint mock = getMockEndpoint("mock:result");
      String result = mock.getExchanges().get(0).getIn().getHeader(Constants.VALIDATION_REPORT, String.class);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Test xml string.
   *
   * @param xmlFile the xml file
   * @return the string
   * @throws Exception the exception
   */
  public String testXML(String xmlFile) throws Exception {
    return testXML(xmlFile, "sch/rules.sch");
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

  /**
   * Test xml string.
   *
   * @param xmlFile the xml file
   * @param rules   the rules
   * @return the string
   * @throws Exception the exception
   */
  public String testXML(String xmlFile, Rules rules) throws Exception {
    if (rules == null) {
      return testXML(xmlFile, "sch/rules.sch");
    } else {
      InputStream sc = getInputStream("sch/rules.sch");
      if (sc != null) {
        Document doc = readXml(sc);

        NodeList nodes = doc.getElementsByTagName("rule");
        while (nodes.getLength() > 0) {
          Element el = (Element) nodes.item(0);
          el.getParentNode().removeChild(el);
        }

        Element pattern = (Element) doc.getElementsByTagName("pattern").item(0);
        for (Rule r : rules.getRules()) {
          Element newrule = doc.createElementNS(pattern.getNamespaceURI(), "rule");
          newrule.setAttribute("context", r.getTag());

          Element assertion = doc.createElementNS(newrule.getNamespaceURI(), "assert");
          String sval = r.getValue();
          if (r.getType().equals("string")) sval = "'" + sval  + "'";
          assertion.setAttribute("test", "@" + r.getTag() + " " + convert(r.getOperator()) + " " + sval);
          assertion.setTextContent("Invalid " + r.getTag() + ": #PP2#value-of select=\"@" + r.getTag() + "\"/#GG#");

          newrule.appendChild(assertion);

          pattern.appendChild(newrule);
        }

        String tempname = System.getProperty("user.home") + "/DPF Manager/rules2.sch";
        int idx = 3;
        while (new File(tempname).exists()) tempname = System.getProperty("user.home") + "/DPF Manager/rules" + idx++ + ".sch";

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);

        File file = new File(tempname);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        byte[] encoded = Files.readAllBytes(Paths.get(tempname));
        String s = new String(encoded, Charset.defaultCharset());
        s = s.replace("#PP#", "&lt;").replace("#GG#", ">").replace("#PP2#", "<");
        PrintWriter out = new PrintWriter(tempname);
        out.print(s);
        out.close();

        String report = testXML(xmlFile, tempname);
        file.delete();
        return report;
      } else {
        return "";
      }
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
      Class cls = this.getClass();
      ClassLoader cLoader = cls.getClassLoader();
      sc = cLoader.getResourceAsStream(schema);
    }
    return sc;

    /*InputStream sc = null;
    if (Files.exists(Paths.get(schema))) {
      // Look in local dir
      System.out.println("Schematron rules found in local dir");
      sc = new FileInputStream(schema);
    } else {
      // Look in JAR
      CodeSource src = Schematron.class.getProtectionDomain().getCodeSource();
      if (src != null) {
        URL jar = src.getLocation();
        System.out.println("Reading JAR " + jar.toString());
        ZipInputStream zip = new ZipInputStream(jar.openStream());
        ZipEntry zipFile;
        while ((zipFile = zip.getNextEntry()) != null) {
          String name = zipFile.getName();
          if (name.equals(schema.substring(schema.indexOf("/")))) {
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
    return sc;*/
  }

  /**
   * Test xml string.
   *
   * @param xmlFile the xml file
   * @param schema  the schema
   * @return the string
   * @throws Exception the exception
   */
  public String testXML(String xmlFile, String schema) throws Exception {
    String result;
    try {
      InputStream sc = getInputStream(schema);
      String payload = xmlFile;
      TransformerFactory factory = new TransformerFactoryImpl();
      factory.setURIResolver(new ClassPathURIResolver(Constants.SCHEMATRON_TEMPLATES_ROOT_DIR));
      Templates rules = TemplatesFactory.newInstance().newTemplates(sc);
      SchematronProcessor proc = SchematronProcessorFactory.newScehamtronEngine(rules);
      result = proc.validate(payload);
      result = result.substring(0, result.indexOf("<!--")) + result.substring(result.indexOf("-->")+3);
    } catch (Exception ex) {
      result = "";
    }
    return result;
  }
}

package dpfmanager.shell.modules;

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

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;

/**
 * Created by easy on 21/09/2015.
 */
public class Schematron extends CamelTestSupport {
  CamelContext context;
  ProducerTemplate template;

  public Schematron()
  {
    context = new DefaultCamelContext();
    template = context.createProducerTemplate();
  }

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

  public String testXML(String xmlFile) throws Exception {
    return testXML(xmlFile, "sch/rules.sch");
  }

  public String testXML(String xmlFile, String schema) throws Exception {
    String result;
    try {
      InputStream sc = null;
      if (Files.exists(Paths.get(schema))) {
        // Look in local dir
        sc = new FileInputStream(schema);
      } else {
        // Look in JAR
        CodeSource src = Schematron.class.getProtectionDomain().getCodeSource();
        if (src != null) {
          URL jar = src.getLocation();
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
      String payload = xmlFile;
      TransformerFactory factory = new TransformerFactoryImpl();
      factory.setURIResolver(new ClassPathURIResolver(Constants.SCHEMATRON_TEMPLATES_ROOT_DIR));
      Templates rules = TemplatesFactory.newInstance().newTemplates(sc);
      SchematronProcessor proc = SchematronProcessorFactory.newScehamtronEngine(rules);
      result = proc.validate(payload);
    } catch (Exception ex) {
      result = "";
    }
    return result;
  }
}

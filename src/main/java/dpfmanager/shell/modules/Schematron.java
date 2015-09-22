package dpfmanager.shell.modules;

import dpfmanager.ReportGenerator;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;

import java.io.File;

/**
 * Created by easy on 21/09/2015.
 */
public class Schematron {
  public static void RunSchematron(String xmlFilename) {
    CamelContext context = new DefaultCamelContext();

    try {
      context.addRoutes(new RouteBuilder() {
        public void configure() {
          from("file:" + xmlFilename).to("schematron://sch/schematron.sch").to("mock:result");
        }
      });
      ProducerTemplate template = context.createProducerTemplate();
      context.start();
      template.sendBody("file:" + xmlFilename, xmlFilename);
      context.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}

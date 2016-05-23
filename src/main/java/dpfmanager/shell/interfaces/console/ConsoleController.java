package dpfmanager.shell.interfaces.console;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.application.launcher.noui.ApplicationParameters;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrià Llorens on 11/04/2016.
 */
public class ConsoleController {

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  // Config related params
  private String outputFolder;
  private ArrayList<String> files;
  private boolean xml;
  private boolean json;
  private boolean html;
  private boolean pdf;
  private Configuration config;
  private boolean explicitFormats;
  private boolean explicitOutput;

  // Global params
  private ApplicationParameters parameters;

  public ConsoleController(ConsoleContext c) {
    setDefault();
    context = c;
  }

  private void setDefault() {
    // Output formats
    xml = true;
    json = false;
    html = true;
    pdf = false;
    explicitFormats = false;
    // Output folder
    outputFolder = null;
    explicitOutput = false;
    // Configuration
    config = null;
  }

  /**
   * Main run function
   */
  public void run() {
    context.sendAllControllers(parameters);
    readConformanceChecker();
    if (config != null) {
      if (explicitFormats) {
        config.getFormats().clear();
        if (xml) config.getFormats().add("XML");
        if (pdf) config.getFormats().add("PDF");
        if (html) config.getFormats().add("HTML");
        if (json) config.getFormats().add("JSON");
      }
      if (explicitOutput) {
        config.setOutput(outputFolder);
      }
    } else { //Create it
      config = new Configuration(null, null, new ArrayList<>());
      if (xml) config.getFormats().add("XML");
      if (pdf) config.getFormats().add("PDF");
      if (html) config.getFormats().add("HTML");
      if (json) config.getFormats().add("JSON");
      config.getIsos().add("Baseline");
      config.getIsos().add("Tiff/EP");
      config.getIsos().add("Tiff/IT");
      config.setOutput(outputFolder);
    }

    context.send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(files, config, parameters.getRecursive()));
  }

  /**
   * Read conformance checker.
   */
  private void readConformanceChecker() {
    String xml = TiffConformanceChecker.getConformanceCheckerOptions();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setNamespaceAware(true);
    try {
      Document doc = loadXmlFromString(xml);

      NodeList name = doc.getElementsByTagName("name");
      if (name != null && name.getLength() > 0) {
        NodeList subList = name.item(0).getChildNodes();
        if (subList != null && subList.getLength() > 0) {
          printOut("Conformance checker: " + subList.item(0).getNodeValue());
        }
      }

      printOut("Extensions: ");
      NodeList extensions = doc.getElementsByTagName("extension");
      String extensionsStr = "";
      if (extensions != null && extensions.getLength() > 0) {
        for (int i = 0; i < extensions.getLength(); i++) {
          NodeList subList = extensions.item(i).getChildNodes();
          if (subList != null && subList.getLength() > 0) {
            if (i > 0) {
              extensionsStr += ", ";
            }
            extensionsStr += subList.item(0).getNodeValue();
          }
        }
      }
      printOut(extensionsStr);

      NodeList standards = doc.getElementsByTagName("standard");
      if (standards != null && standards.getLength() > 0) {
        for (int i = 0; i < standards.getLength(); i++) {
          NodeList nodes = standards.item(i).getChildNodes();
          String stdName = "";
          String desc = "";
          for (int j = 0; j < nodes.getLength(); j++) {
            if (nodes.item(j).getNodeName().equals("name")) {
              stdName = nodes.item(j).getTextContent();
            } else if (nodes.item(j).getNodeName().equals("description")) {
              desc = nodes.item(j).getTextContent();
            }
          }
          printOut("Standard: " + stdName + " (" + desc + ")");
        }
      }
      printOut("");
    } catch (Exception e) {
      printOut("Failed communication with conformance checker: " + e.getMessage());
    }
  }

  /**
   * Load XML from string.
   *
   * @param xml the XML
   * @return the document
   * @throws Exception the exception
   */
  private Document loadXmlFromString(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    return builder.parse(is);
  }

  /**
   * Shows program usage.
   */
  public void displayHelp() {
    printOut("");
    printOut("Usage: dpfmanager [options] source1 [source2 ... sourceN]");
    printOut("(the sources can be single TIF files, directories, zip files or URLs)");
    printOut("Options:");
    printOut("    -help: Displays this help message");
    printOut("    -v: Shows application version number");
    printOut("    -gui: Launches graphical user interface");
    printOut("    -configuration <filename>: Selects a configuration file");
    printOut("    -o <path>: Specifies the output folder (overriding the one specified in the configuration file, if selected).");
    printOut("    -r[deepness]: Check directories recursively, with the specified depth. Default is '-r1'");
    printOut("    -s: Silent execution (do not open the report at the end)");
    printOut("    -reportformat '[xml, json, pdf, html]': Specifies the report format. Default is 'xml,html'.");
  }

  /**
   * Shows program version.
   */
  public void displayVersion() {
    String sversion = DPFManagerProperties.getVersion();
    printOut("DPF Manager version " + sversion);
  }

  /**
   * Setters
   */
  public void setParameters(ApplicationParameters p) {
    parameters = p;
  }

  public void setOutputFolder(String outputFolder) {
    this.outputFolder = outputFolder;
  }

  public void setXml(boolean xml) {
    this.xml = xml;
  }

  public void setJson(boolean json) {
    this.json = json;
  }

  public void setHtml(boolean html) {
    this.html = html;
  }

  public void setPdf(boolean pdf) {
    this.pdf = pdf;
  }

  public void setConfig(Configuration config) {
    this.config = config;
  }

  public void setFiles(ArrayList<String> files) {
    this.files = files;
  }

  public void setExplicitFormats(boolean explicitFormats) {
    this.explicitFormats = explicitFormats;
  }

  public void setExplicitOutput(boolean explicitOutput) {
    this.explicitOutput = explicitOutput;
  }

  /**
   * Custom print lines
   */
  private void printOut(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private void printErr(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  private void printException(Exception ex) {
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception has occurred!", ex));
  }
}

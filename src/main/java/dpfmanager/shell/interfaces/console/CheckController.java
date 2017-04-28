/**
 * <h1>CheckController.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @since 13/10/2016
 */

package dpfmanager.shell.interfaces.console;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
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
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adria Llorens on 11/04/2016.
 */
public class CheckController {

  /**
   * Parameters contants
   */
  public static String threads = "threads";
  public static String silence = "silence";
  public static String showReport = "showReport";
  public static String overwrite = "overwrite";
  public static String recursive = "recursive";

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The Dpf resourceBundle
   */
  private ResourceBundle bundle;

  /**
   * Common controller
   */
  private CommonController common;

  /**
   * The errors flag
   */
  private boolean argsError;

  public CheckController(ConsoleContext c, ResourceBundle r) {
    context = c;
    bundle = r;
    argsError = false;
    common = new CommonController(context, bundle);
  }

  /**
   * Main parse parameters function
   */
  public void parse(List<String> params) {
    int idx = 0;
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      // -o --output
      if (arg.equals("-o") || arg.equals("--output")) {
        if (idx + 1 < params.size()) {
          String outputFolder = params.get(++idx);
          argsError = !common.parseOutput(outputFolder);
          if (!argsError) {
            common.putParameter(CommonController.output, outputFolder);
          }
        } else {
          printOutErr(bundle.getString("outputSpecify"));
        }
      }
      // -w --overwrite
      else if (arg.equals("-w") || arg.equals("--overwrite")) {
        common.putParameter(CheckController.overwrite, "true");
      }
      // -t --threads
      else if (arg.equals("-t") || arg.equals("--threads")) {
        if (idx + 1 < params.size()) {
          common.putParameter(CheckController.threads, params.get(++idx));
        } else {
          printOutErr(bundle.getString("threadsSpecify"));
        }
      }
      // -c --configuration
      else if (arg.equals("-c") || arg.equals("--configuration")) {
        if (idx + 1 < params.size()) {
          String xmlConfig = params.get(++idx);
          argsError = !common.parseConfiguration(xmlConfig);
          if (!argsError) {
            common.putParameter(CommonController.configuration, xmlConfig);
          }
        } else {
          printOutErr(bundle.getString("specifyConfig"));
        }
      }
      // -f --format
      else if (arg.equals("-f") || arg.equals("--format")) {
        if (idx + 1 < params.size()) {
          argsError = !common.parseFormats(params.get(++idx));
        } else {
          printOutErr(bundle.getString("specifyFormat"));
        }
      }
      // -r --recursive
      else if (arg.equals("-r") || arg.equals("--recursive")) {
        if (idx + 1 < params.size()) {
          Integer max = Integer.MAX_VALUE;
          String recursive = params.get(idx + 1);
          if (isNumeric(recursive)) {
            max = Integer.parseInt(recursive);
            idx++;
          }
          common.putParameter(CheckController.recursive, max.toString());
        } else {
          printOutErr(bundle.getString("specifyRecursive"));
        }
      }
      // -n --no-open
      else if (arg.equals("--show-report")) {
        common.putParameter(CheckController.showReport, "true");
      }
      // -s --silence
      else if (arg.equals("-s") || arg.equals("--silence")) {
        common.putParameter(CheckController.silence, "true");
      }
      // -h --help
      else if (arg.equals("-h") || arg.equals("--help")) {
        displayHelp();
      }
      // Unrecognised option
      else if (arg.startsWith("-")) {
        printOutErr(bundle.getString("unknownOption").replace("%1", arg));
      }
      // File or directory to process
      else {
        common.parseFiles(arg);
      }
      idx++;
    }

    // No params
    if (params.size() == 0) {
      argsError = true;
    }

    // No files
    if (common.getFiles().size() == 0) {
      printOutErr(bundle.getString("noFilesSpecified"));
    }
  }

  private static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * Main run function
   */
  public void run() {
    if (argsError) {
      displayHelp();
      return;
    }

    readConformanceChecker();
    common.parseConfig();
    context.send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(common.getFiles(), common.getConfig()));
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
          printOut(bundle.getString("confCheck").replace("%1", subList.item(0).getNodeValue()));
        }
      }

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
      printOut(bundle.getString("extensions") + " " + extensionsStr);

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
          printOut(bundle.getString("standard").replace("%1", stdName).replace("%2", desc));
        }
      }
      printOut("");
    } catch (Exception e) {
      printOut(bundle.getString("failedCC").replace("%1", e.getMessage()));
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
   * Displays help
   */
  public void displayHelp() {
    printOut("");
    printOut(bundle.getString("helpC0"));
    printOut(bundle.getString("helpSources"));
    printOut("");
    printOut(bundle.getString("helpOptions"));
    printOptions("helpC", 9);
    exit();
  }

  public void printOptions(String prefix, int max) {
    for (int i = 1; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.indexOf(":") + 1);
      String post = msg.substring(msg.indexOf(":") + 1);
      String line = String.format("%-38s%s", pre, post);
      printOut("    " + line);
    }
  }

  /**
   * Custom print lines
   */
  private void printOut(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private void printOutErr(String message) {
    argsError = true;
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private void printErr(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  private void printException(Exception ex) {
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("exception"), ex));
  }

  /**
   * Exit application
   */
  public void exit() {
    AppContext.close();
    System.exit(0);
  }
}

/**
 * <h1>ConsoleController.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.interfaces.console;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.client.messages.RequestMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 11/04/2016.
 */
public class RemoteController {

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The Dpf resourceBundle
   */
  private ResourceBundle bundle;

  /**
   * Config related params
   */
  private CommonController common;

  /**
   * The errors flag
   */
  private boolean argsError;

  public RemoteController(ConsoleContext c, ResourceBundle r) {
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
      // -u --url
      if (arg.equals("-u") || arg.equals("--url")) {
        if (idx + 1 < params.size()) {
          String url = params.get(++idx);
          if (!url.startsWith("http://")) {
            url = "http://" + url;
          }
          common.putParameter("-url", url);
        } else {
          printOutErr(bundle.getString("specifyUrl"));
        }
      }
      // -j --job
      else if (arg.equals("-j") || arg.equals("--job")) {
        if (idx + 1 < params.size()) {
          String jobId = params.get(++idx);
          common.putParameter("-job", jobId);
        } else {
          printOutErr(bundle.getString("specifyJob"));
        }
      }
      // -o --output
      else if (arg.equals("-o") || arg.equals("--output")) {
        if (idx + 1 < params.size()) {
          String outputFolder = params.get(++idx);
          argsError = !common.parseOutput(outputFolder);
          if (!argsError) {
            common.putParameter("-o", outputFolder);
          }
        } else {
          printOutErr(bundle.getString("outputSpecify"));
        }
      }
      // -c --configuration
      else if (arg.equals("-c") || arg.equals("--configuration")) {
        if (idx + 1 < params.size()) {
          String xmlConfig = params.get(++idx);
          argsError = !common.parseConfiguration(xmlConfig);
          if (!argsError) {
            common.putParameter("-configuration", xmlConfig);
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
      // -w --wait
      else if (arg.equals("-w") || arg.equals("--wait")) {
        common.putParameter("-w", "true");
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

    // No files
    if (common.getFiles().size() == 0 && !common.containsParameter("-job") ) {
      printOutErr(bundle.getString("noFilesSpecified"));
    }

    // No params
    if (params.size() == 0) {
      argsError = true;
    }

    // Job needs url
    if (common.containsParameter("-job") && !common.containsParameter("-url")) {
      printOutErr(bundle.getString("jobNeedUrl"));
    }
  }

  /**
   * Main run function
   */
  public void run() {
    if (argsError) {
      displayHelp();
    }

    if (common.containsParameter("-job")) {
      // Ask for job
      context.send(BasicConfig.MODULE_CLIENT, new RequestMessage(RequestMessage.Type.ASK, common.getParameter("-job")));
    } else {
      // Remote check
      common.parseConfig();
      context.send(BasicConfig.MODULE_CLIENT, new RequestMessage(RequestMessage.Type.CHECK, common.getFiles(), common.zipFolders(), common.getConfig()));
    }
  }

  /**
   * Displays help
   */
  private void displayHelp() {
    printOut("");
    printOut(bundle.getString("helpR0"));
    printOut(bundle.getString("helpSources"));
    printOut("");
    printOut(bundle.getString("helpOptions"));
    printOptions("helpR", 7);
    exit();
  }

  public void printOptions(String prefix, int max) {
    for (int i = 1; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.lastIndexOf(":") + 1);
      String post = msg.substring(msg.lastIndexOf(":") + 1);
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

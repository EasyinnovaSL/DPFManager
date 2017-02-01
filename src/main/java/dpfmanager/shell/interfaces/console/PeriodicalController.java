/**
 * <h1>InteroperabilityController.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adria Llorens
 * @version 1.0
 * @since 13/10/2016
 */

package dpfmanager.shell.interfaces.console;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicCheck;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;

import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 11/04/2016.
 */
public class PeriodicalController {

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

  public PeriodicalController(ConsoleContext c, ResourceBundle r) {
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
      // -a --add
      if (arg.equals("-a") || arg.equals("--add")) {
        List<String> coreModules =
            Arrays.asList("-listperiodic", "-editperiodic", "-removeperiodic");
        if (common.containsParameters(Arrays.asList("-listperiodic", "-editperiodic", "-removeperiodic"))) {
          printOutErr(bundle.getString("onlyOnePeriodicAction"));
        } else {
          common.putParameter("-addperiodic", "");
        }
      }
      // -e --edit
      else if (arg.equals("-e") || arg.equals("--edit")) {
        if (common.containsParameters(Arrays.asList("-listperiodic", "-addperiodic", "-removeperiodic"))) {
          printOutErr(bundle.getString("onlyOnePeriodicAction"));
        } else {
          if (idx + 1 < params.size()) {
            String uuid = params.get(++idx);
            common.putParameter("-editperiodic", uuid);
          } else {
            printOutErr(bundle.getString("specifyPeriodicId"));
          }
        }
      }
      // -r --remove
      else if (arg.equals("-r") || arg.equals("--remove")) {
        if (common.containsParameters(Arrays.asList("-listperiodic", "-editperiodic", "-addperiodic"))) {
          printOutErr(bundle.getString("onlyOnePeriodicAction"));
        } else {
          if (idx + 1 < params.size()) {
            String uuid = params.get(++idx);
            common.putParameter("-removeperiodic", uuid);
          } else {
            printOutErr(bundle.getString("specifyPeriodicId"));
          }
        }
      }
      // -l --list
      else if (arg.equals("-l") || arg.equals("--list")) {
        if (common.containsParameters(Arrays.asList("-addperiodic", "-editperiodic", "-removeperiodic"))) {
          printOutErr(bundle.getString("onlyOnePeriodicAction"));
        } else {
          common.putParameter("-listperiodic", "");
        }
      }
      // --time
      else if (arg.equals("--time")) {
        if (idx + 1 < params.size()) {
          String time = params.get(++idx);
          common.putParameter("-time", time);
        } else {
          printOutErr(bundle.getString("specifyTime"));
        }
      }
      // --configure
      else if (arg.equals("--configure")) {
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
      // --periodicity
      else if (arg.equals("--periodicity")) {
        String mode = "";
        if (idx + 1 < params.size()) {
          mode = params.get(++idx);
          common.putParameter("-periodicity", mode);
        } else {
          printOutErr(bundle.getString("specifyPeriodicityMode"));
        }
        // extra info (weekly & monthly)
        if (mode.equals("weekly") || mode.equals("monthly")) {
          if (idx + 1 < params.size()) {
            String extra = params.get(++idx);
            common.putParameter("-extra", extra);
          } else {
            printOutErr(bundle.getString("specifyPeriodicityExtra"));
          }
        }
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
    if (common.getFiles().size() == 0 && !common.containsParameter("-listperiodic") && !common.containsParameter("-removeperiodic")) {
      printOutErr(bundle.getString("noFilesSpecified"));
    }

  }

  /**
   * Main run function
   */
  public void run() {
    if (argsError) {
      displayHelp();
      return;
    }

    if (common.containsParameter("-listperiodic")) {
      // List periodicals checks
      context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.READ));
    } else if (common.containsParameter("-addperiodic")) {
      // Add periodical check
      PeriodicCheck info = new PeriodicCheck();
      if (info.parse(common.getParameters(), common.getFiles(), context, bundle)) {
        context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.SAVE, info));
      } else {
        printOut(bundle.getString("noSuchInfo"));
        displayHelp();
      }
    } else if (common.containsParameter("-editperiodic")) {
      // Edit periodical check
      String uuid = common.getParameter("-editperiodic");
      PeriodicCheck info = new PeriodicCheck();
      info.setUuid(uuid);
      if (info.parse(common.getParameters(), common.getFiles(), context, bundle)) {
        context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.EDIT, info));
      } else {
        printOut(bundle.getString("noSuchInfo"));
        displayHelp();
      }
    } else if (common.containsParameter("-removeperiodic")) {
      // Remove periodical check
      context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, common.getParameter("-removeperiodic")));
    }
  }

  /**
   * Displays help
   */
  public void displayHelp() {
    printOut("");
    printOut(bundle.getString("helpP0"));
    printOut("");
    printOut(bundle.getString("helpOptions"));
    printOptions("helpP", 6);
    printOptions("helpP6", 1, 2, 40);
    printOptions("helpP", 7, 8, 37);
    exit();
  }

  public void printOptions(String prefix, int max) {
    printOptions(prefix, 1, max, 37);
  }

  public void printOptions(String prefix, int min, int max, int spaces) {
    for (int i = min; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.lastIndexOf(":") + 1);
      String post = msg.substring(msg.lastIndexOf(":") + 1);
      String line = String.format("%-"+spaces+"s%s", pre, post);
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

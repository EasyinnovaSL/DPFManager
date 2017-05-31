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
import dpfmanager.shell.modules.statistics.messages.StatisticsMessage;

import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adria Llorens on 11/04/2016.
 */
public class StatisticsController {

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The Dpf resourceBundle
   */
  private ResourceBundle bundle;

  /**
   * The errors flag
   */
  private boolean argsError;

  private String path;
  private LocalDate from;
  private LocalDate to;

  public StatisticsController(ConsoleContext c, ResourceBundle r) {
    context = c;
    bundle = r;
    argsError = false;
    path = null;
    from = null;
    to = null;
  }

  /**
   * Main parse parameters function
   */
  public void parse(List<String> params) {
    int idx = 0;
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      // -p --path-filter
      if (arg.equals("-p") || arg.equals("--path-filter")) {
        if (idx + 1 < params.size()) {
          path = params.get(++idx);
        } else {
          printOutErr(bundle.getString("specifyPath"));
        }
      }
      // -f --from-date
      else if (arg.equals("-f") || arg.equals("--from-date")) {
        if (idx + 1 < params.size()) {
          from = parseDate(params.get(++idx));
          if (from == null){
            printOutErr(bundle.getString("dateMalformed"));
          }
        } else {
          printOutErr(bundle.getString("specifyDate"));
        }
      }
      // -t --to-date
      else if (arg.equals("-t") || arg.equals("--to-date")) {
        if (idx + 1 < params.size()) {
          to = parseDate(params.get(++idx));
          if (to == null){
            printOutErr(bundle.getString("dateMalformed"));
          }
        } else {
          printOutErr(bundle.getString("specifyDate"));
        }
      }
      // -h --help
      else if (arg.equals("-h") || arg.equals("--help")) {
        displayHelp();
      }
      // Unrecognised option
      else {
        printOutErr(bundle.getString("unknownOption").replace("%1", arg));
      }
      idx++;
    }
  }

  private LocalDate parseDate(String str) {
    LocalDate date = null;
    try {
      date = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } catch (Exception nfe) {
    }
    return date;
  }

  /**
   * Main run function
   */
  public void run() {
    if (argsError) {
      displayHelp();
      return;
    }

    context.send(BasicConfig.MODULE_STATISTICS, new StatisticsMessage(StatisticsMessage.Type.GENERATE, from, to, path));
  }

  /**
   * Displays help
   */
  public void displayHelp() {
    printOut("");
    printOut(bundle.getString("helpT0"));
    printOut("");
    printOut(bundle.getString("helpOptions"));
    printOptions("helpT", 4);
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

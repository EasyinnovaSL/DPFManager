/**
 * <h1>ServerController.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 13/10/2016
 */

package dpfmanager.shell.interfaces.console;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.client.messages.RequestMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.server.messages.ServerMessage;

import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 11/04/2016.
 */
public class ServerController {

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The Dpf resourceBundle
   */
  private ResourceBundle bundle;

  /**
   * The parsed args
   */
  private Map<String, String> parameters;

  /**
   * The errors flag
   */
  private boolean argsError;

  public ServerController(ConsoleContext c, ResourceBundle b) {
    context = c;
    bundle = b;
    parameters = (Map<String, String>) AppContext.getApplicationContext().getBean("parameters");
    parameters.put("mode", "SERVER");
  }

  /**
   * Main parse parameters function
   */
  public void parse(List<String> params) {
    int idx = 0;
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      // -p --port
      if (arg.equals("-p") || arg.equals("--port")) {
        idx++;
        if (idx < params.size()) {
          String port = params.get(idx);
          if (isNumeric(port)){
            parameters.put("-p",port);
          } else {
            argsError = true;
          }
        } else {
          argsError = true;
        }
      }
      // -h --help
      else if (arg.equals("-h") || arg.equals("--help")) {
        displayHelp();
      }
      idx++;
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
    // Start the server
    if (!argsError) {
      context.send(BasicConfig.MODULE_SERVER, new ServerMessage(ServerMessage.Type.START));
    } else {
      printOut(bundle.getString("paramError"));
      displayHelp();
    }
  }

  /**
   * Displays help
   */
  private void displayHelp() {
    printOut("");
    printOut(bundle.getString("helpS0"));
    printOut("");
    printOut(bundle.getString("helpOptions"));
    printOptions("helpS", 2);
    exit();
  }

  public void printOptions(String prefix, int max) {
    for (int i = 1; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.lastIndexOf(":") + 1);
      String post = msg.substring(msg.lastIndexOf(":") + 1);
      String line = String.format("%-27" +
          "s%s", pre, post);
      printOut("    " + line);
    }
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

  /**
   * Exit application
   */
  public void exit() {
    AppContext.close();
    System.exit(0);
  }

}

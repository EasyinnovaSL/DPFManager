/**
 * <h1>ConsoleLauncher.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.application.launcher.noui;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.interfaces.console.CheckController;
import dpfmanager.shell.interfaces.console.PeriodicalController;
import dpfmanager.shell.interfaces.console.RemoteController;
import dpfmanager.shell.interfaces.console.ServerController;
import dpfmanager.shell.interfaces.console.ModulesController;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class ConsoleLauncher {

  /**
   * Static param for tests wait for finish
   */
  private static boolean finished;

  /**
   * The args.
   */
  private List<String> params;

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The resource bundle
   */
  private ResourceBundle bundle;

  /**
   * The parsed args
   */
  private Map<String, String> parameters;

  public ConsoleLauncher(String[] args) {
    DPFManagerProperties.setFinished(false);
    // Program input
    params = new ArrayList(Arrays.asList(args));
    // Load bundle
    Locale.setDefault(new Locale(DPFManagerProperties.getLanguage()));
    bundle = DPFManagerProperties.getBundle();
  }

  /**
   * Initialize all services
   */
  private void initServices() {
    // Load spring context
    AppContext.loadContext("DpfSpringConsole.xml");
    parameters = (Map<String, String>) AppContext.getApplicationContext().getBean("parameters");
    parameters.put("mode", "CMD");
    //Load DpfContext
    context = new ConsoleContext(AppContext.getApplicationContext());
  }

  /**
   * Update the app language.
   */
  private void updateLanguage(List<String> params) {
    // First parameter must be language
    if (params.size() > 0) {
      String language = params.get(0);
      Locale newLocale = new Locale(language);
      if (newLocale != null) {
        DPFManagerProperties.setLanguage(language);
      }
    }
    Locale.setDefault(new Locale(DPFManagerProperties.getLanguage()));
    bundle = DPFManagerProperties.getBundle();
  }

  /**
   * Launch.
   */
  public void launch() {
    // Read the first parameter
    if (params.size() > 0) {
      String first = params.get(0);
      if (first.equals("periodic")) {
        params.remove(0);
        initServices();
        PeriodicalController controller = new PeriodicalController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("modules")){
        params.remove(0);
        initServices();
        ModulesController controller = new ModulesController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("remote")) {
        params.remove(0);
        initServices();
        RemoteController controller = new RemoteController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("check")) {
        params.remove(0);
        initServices();
        CheckController controller = new CheckController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("server")) {
        params.remove(0);
        initServices();
        ServerController controller = new ServerController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("-v") || first.equals("--version")) {
        params.remove(0);
        displayVersion();
        return;
      } else if (first.equals("-h") || first.equals("--help")) {
        params.remove(0);
        displayHelp();
        return;
      } else if (first.equals("-l") || first.equals("--language")) {
        params.remove(0);
        updateLanguage(params);
        return;
      } else {
        printOut(bundle.getString("unknownCommand").replace("%1", first));
        displayHelp();
      }
    }
  }

  /**
   * Shows program usage.
   */
  public void displayHelp() {
    printOut("");
    printOut(bundle.getString("help0"));
    printOut(bundle.getString("helpSources"));
    printOut("");
    printOut(bundle.getString("help1"));
    printOptions("help1", 6);
    printOut("");
    printOut(bundle.getString("help3"));
    printOptions("help3", 3);
    printOut("");
    printOut(bundle.getString("help2"));
    exit();
  }

  public void printOptions(String prefix, int max) {
    for (int i = 1; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.indexOf(":") + 1);
      String post = msg.substring(msg.indexOf(":") + 1);
      String line = String.format("%-27s%s", pre, post);
      printOut("    " + line);
    }
  }

  /**
   * Shows program version.
   */
  public void displayVersion() {
    printOut(bundle.getString("dpfVersion").replace("%1", DPFManagerProperties.getVersion()));
  }

  /**
   * Custom print lines
   */
  private void printOut(String message) {
    if (context != null){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
    } else {
      System.out.println(message);
    }
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
    if (AppContext.getApplicationContext() != null) {
      AppContext.close();
    }
    System.exit(0);
  }

}

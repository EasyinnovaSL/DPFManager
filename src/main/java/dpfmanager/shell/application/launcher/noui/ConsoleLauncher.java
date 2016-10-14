/**
 * <h1>ConsoleLauncher.java</h1> <p> This program is free software: you can redistribute it
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
 * @since 23/7/2015
 */

package dpfmanager.shell.application.launcher.noui;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.interfaces.console.CheckController;
import dpfmanager.shell.interfaces.console.ConsoleController;
import dpfmanager.shell.interfaces.console.PeriodicalController;
import dpfmanager.shell.interfaces.console.RemoteController;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
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
   * The main controller.
   */
  private ConsoleController controller;

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
  private void updateLanguage(List<String> params){
    // First parameter must be language
    if (params.size() > 0){
      String language = params.get(0);
      Locale newLocale = new Locale(language);
      if (newLocale != null){
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
    if (params.size() > 0){
      String first = params.get(0);
      if (first.equals("periodic")){
        params.remove(0);
        PeriodicalController controller = new PeriodicalController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("remote")){
        params.remove(0);
        RemoteController controller = new RemoteController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("check")){
        params.remove(0);
        CheckController controller = new CheckController(context, bundle);
        controller.parse(params);
        controller.run();
        return;
      } else if (first.equals("-v") || first.equals("--version")){
        params.remove(0);
        displayVersion();
        return;
      } else if (first.equals("-h") || first.equals("--help")){
        params.remove(0);
        displayHelp();
        return;
      } else if (first.equals("-l") || first.equals("--language")){
        params.remove(0);
        updateLanguage(params);
        return;
      }
    }
  }

  /**
   * Shows program usage.
   */
  public void displayHelp() {
    printOut("");
    printOut(bundle.getString("help1"));
    printOut(bundle.getString("help2"));
    printOut("");
    printOptions("helpO", 6);
    printOut("");
    printOptions("helpC", 7);
    printOut("");
    printOptions("helpR", 4);
    printOut("");
    printOptions("helpP", 6);
    printOut("        " + bundle.getString("helpP61"));
    printOut("        " + bundle.getString("helpP62"));
    printOut("    "+bundle.getString("helpP7"));
  }

  public void printOptions(String prefix, int max) {
    printOut(bundle.getString(prefix+"1"));
    for (int i = 2; i<=max; i++){
      printOut("    "+bundle.getString(prefix+i));
    }
  }

  /**
   * Shows program version.
   */
  public void displayVersion() {
    printOut(bundle.getString("dpfVersion").replace("%1",DPFManagerProperties.getVersion()));
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

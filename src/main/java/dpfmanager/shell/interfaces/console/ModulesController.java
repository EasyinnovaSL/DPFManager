/**
 * <h1>ConsoleController.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.console;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.client.messages.RequestMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicCheck;
import dpfmanager.shell.modules.periodic.core.Periodicity;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.tools.zip.ZipEntry;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrià Llorens on 11/04/2016.
 */
public class ModulesController {

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The Dpf resourceBundle
   */
  private ResourceBundle bundle;

  /**
   * The accepted options
   */
  private List<String> acceptedOptions = Arrays.asList("-a", "--add", "-e", "--edit", "-r", "--remove", "-l", "--list", "-i" , "--info", "-h", "--help", "--enable", "--disable", "--configure", "--parameters", "--extensions");

  /**
   * The actions list
   */
  private List<InteroperabilityMessage> actions;

  /**
   * The errors flag
   */
  private boolean argsError;

  public ModulesController(ConsoleContext c, ResourceBundle r) {
    context = c;
    bundle = r;
    argsError = false;
    actions = new ArrayList<>();
  }

  /**
   * Main parse parameters function
   */
  public void parse(List<String> params) {
    int idx = 0;
    InteroperabilityMessage lastAdd = null;
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      // -a --add
      if (arg.equals("-a") || arg.equals("--add")){
        if (idx + 2 < params.size()) {
          String name = params.get(++idx);
          String path = params.get(++idx);
          if (lastAdd != null && !isFinished(lastAdd)) {
            printOut(bundle.getString("lastAddIncompleted"));
            argsError = true;
          } else {
            lastAdd = new InteroperabilityMessage(InteroperabilityMessage.Type.ADD, name, path);
          }
        } else {
          argsError = true;
        }
      }
      // -e --edit
      else if (arg.equals("-e") || arg.equals("--edit")){
        if (idx + 2 < params.size()) {
          String name = params.get(++idx);
          String path = params.get(++idx);
          actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.EDIT, name, path));
        } else {
          argsError = true;
        }
      }
      // -r --remove
      else if (arg.equals("-r") || arg.equals("--remove")){
        if (idx + 1 < params.size()) {
          String name = params.get(++idx);
          actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.REMOVE, name));
        } else {
          argsError = true;
        }
      }
      // -l --list
      else if (arg.equals("-l") || arg.equals("--list")){
        actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.LIST));
      }
      // -i --info
      else if (arg.equals("-i") || arg.equals("--info")){
        if (idx + 1 < params.size()) {
          String name = params.get(++idx);
          actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.INFO, name));
        } else {
          argsError = true;
        }
      }
      // -h --help
      else if (arg.equals("-h") || arg.equals("--help")){
        argsError = true;
      }
      // --enable
      else if (arg.equals("--enable")){
        if (idx + 1 < params.size()) {
          String name = params.get(++idx);
          actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.ENABLE, name));
        } else {
          argsError = true;
        }
      }
      // --disable
      else if (arg.equals("--disable")){
        if (idx + 1 < params.size()) {
          String name = params.get(++idx);
          actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.DISABLE, name));
        } else {
          argsError = true;
        }
      }
      // --configure
      else if (arg.equals("--configure")){
        if (idx + 1 < params.size()) {
          String conf1 = params.get(++idx);
          if (idx + 1 < params.size() && !acceptedOptions.contains(params.get(idx+1))) {
            // Name + conf
            String name = conf1;
            String config = params.get(++idx);
            actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.CONFIGURE, name, config));
          } else {
            // Only configuration file
            if (lastAdd != null){
              lastAdd.setConfigure(conf1);
              if (isFinished(lastAdd)) {
                actions.add(lastAdd);
                lastAdd = null;
              }
            } else {
              printOut(bundle.getString("needPreviousAddParameters"));
              argsError = true;
            }
          }
        } else {
          argsError = true;
        }
      }
      // --parameters
      else if (arg.equals("--parameters")){
        if (idx + 1 < params.size()) {
          String conf1 = params.get(++idx);
          if (idx + 1 < params.size() && !acceptedOptions.contains(params.get(idx+1))) {
            // Name + parameters
            String name = conf1;
            String paramsAux = params.get(++idx);
            actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.CONFIGURE, name, paramsAux));
          } else {
            // Only parameters
            if (lastAdd != null){
              lastAdd.setParameters(conf1);
              if (isFinished(lastAdd)) {
                actions.add(lastAdd);
                lastAdd = null;
              }
            } else {
              printOut(bundle.getString("needPreviousAddConfigure"));
              argsError = true;
            }
          }
        } else {
          argsError = true;
        }
      }
      // --extensions
      else if (arg.equals("--extensions")){
        if (idx + 1 < params.size()) {
          String conf1 = params.get(++idx);
          if (idx + 1 < params.size() && !acceptedOptions.contains(params.get(idx+1))) {
            // Name + parameters
            String name = conf1;
            String extensions = params.get(++idx);
            actions.add(new InteroperabilityMessage(InteroperabilityMessage.Type.EXTENSIONS, name, extensions));
          } else {
            // Only parameters
            if (lastAdd != null){
              lastAdd.setExtensions(Arrays.asList(conf1.split(",")));
              if (isFinished(lastAdd)) {
                actions.add(lastAdd);
                lastAdd = null;
              }
            } else {
              printOut(bundle.getString("needPreviousAddExtensions"));
              argsError = true;
            }
          }
        } else {
          argsError = true;
        }
      }
      // Unrecognised option
      else {
        printOut(bundle.getString("unrecognizedOption").replace("%1", arg));
        argsError = true;
      }
      idx++;
    }
    if (lastAdd != null){
      printOut(bundle.getString("addMissingParams"));
      argsError = true;
    }
  }

  /**
   * Main run function
   */
  public void run() {
    if (argsError){
      printOut(bundle.getString("modulesParametersError"));
      displayHelp();
    } else {
      for (InteroperabilityMessage message : actions) {
        context.send(BasicConfig.MODULE_INTEROPERABILITY, message);
        printOut("");
      }
    }
  }

  /**
   * Displays help
   */
  private void displayHelp(){
    printOut("HELP!"); // TODO
  }

  private boolean isFinished(InteroperabilityMessage lastAdd){
    return (!lastAdd.getConfigure().isEmpty() && !lastAdd.getParameters().isEmpty() && !lastAdd.getExtensions().isEmpty());
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
}

/**
 * <h1>ModulesController.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @since 13/10/2016
 */

package dpfmanager.shell.interfaces.console;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.metadata_fixer.Fixes;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 11/04/2016.
 */
public class ConfigurationController {

  public enum Action{
    ADD,
    EDIT,
    REMOVE,
    INFO,
    LIST
  }

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
   * The accepted options
   */
  private List<String> acceptedOptions = Arrays.asList("-a", "--add", "-e", "--edit", "-r", "--remove", "-l", "--list", "-i", "--info", "-h", "--help", "--enable", "--disable", "--configure", "--parameters", "--extensions");

  /**
   * The errors flag
   */
  private boolean argsError;

  /**
   * Custom extra params
   */
  private Action action;
  private File configFile;
  private String path = "";
  private ArrayList<String> isos;
  private Fixes fixes;
  private Rules rules;
  private String description;
  private Map<String, List<String>> modifiedIsos;
  private TiffConformanceChecker conformance;

  public ConfigurationController(ConsoleContext c, ResourceBundle r) {
    context = c;
    bundle = r;
    argsError = false;
    action = null;
    isos = new ArrayList<>();
    modifiedIsos = new HashMap<>();
    fixes = new Fixes();
    rules = new Rules();
    common = new CommonController(context, bundle);
    conformance = new TiffConformanceChecker();
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
        setAction(Action.ADD);
      }
      // -e --edit
      else if (arg.equals("-e") || arg.equals("--edit")) {
        setAction(Action.EDIT);
      }
      // -r --remove
      else if (arg.equals("-r") || arg.equals("--remove")) {
        setAction(Action.REMOVE);
      }
      // -l --list
      else if (arg.equals("-l") || arg.equals("--list")) {
        setAction(Action.LIST);
        if (idx + 1 < params.size()) {
          String type = params.get(++idx);
          if (!type.equals("tag") && !type.equals("iso")){
            type = "config";
          }
          common.putParameter("-l", type);
        } else {
          printOutErr(bundle.getString("isoSpecify"));
        }
      }
      // -i --info
      else if (arg.equals("-i") || arg.equals("--info")) {
        setAction(Action.INFO);
      }
      // -h --help
      else if (arg.equals("-h") || arg.equals("--help")) {
        displayHelp();
      }
      // -f --format
      else if (arg.equals("-f") || arg.equals("--format")) {
        if (idx + 1 < params.size()) {
          argsError = !common.parseFormats(params.get(++idx));
        } else {
          printOutErr(bundle.getString("specifyFormat"));
        }
      }
      // -o --output
      if (arg.equals("-o") || arg.equals("--output")) {
        if (idx + 1 < params.size()) {
          String outputFolder = params.get(++idx);
          argsError = !common.parseOutput(outputFolder);
          if (!argsError){
            common.putParameter("-o", outputFolder);
          }
        } else {
          printOutErr(bundle.getString("outputSpecify"));
        }
      }
      // -d --description
      if (arg.equals("-d") || arg.equals("--description")) {
        if (idx + 1 < params.size()) {
          String desc = params.get(++idx);
          description = desc;
        } else {
          printOutErr(bundle.getString("descriptionSpecify"));
        }
      }
      // --iso
      if (arg.equals("--iso")) {
        if (idx + 1 < params.size()) {
          String iso = params.get(++idx);
          if (validateIso(iso)){
            isos.add(iso);
          } else {
            printOutErr(bundle.getString("badIsoName"));
          }
        } else {
          printOutErr(bundle.getString("isoSpecify"));
        }
      }
      // --no-iso-rule
      if (arg.equals("--no-iso-rule")) {
        if (idx + 2 < params.size()) {
          String iso = params.get(++idx);
          String ruleId = params.get(++idx);
          if (validateIso(iso)){
            List<String> list = new ArrayList<>();
            if (modifiedIsos.containsKey(iso)){
              list = modifiedIsos.get(iso);
            }
            list.add(ruleId);
            modifiedIsos.put(iso, list);
          } else {
            printOutErr(bundle.getString("badIsoName"));
          }
        } else {
          printOutErr(bundle.getString("isoSpecify"));
        }
      }
      // --fix
      if (arg.equals("--fix")) {
        if (idx + 2 < params.size()) {
          String operator = params.get(++idx);
          String tag = params.get(++idx);
          String value = getFixOperatorInt(operator) == 1 ? (idx + 1 < params.size() ? params.get(++idx) : null) : null;
          if (validateFix(tag, operator, value)){
            fixes.addFix(tag, parseFixOperator(operator), value);
          } else {
            printOutErr(bundle.getString("fixMalFormed"));
          }
        } else {
          printOutErr(bundle.getString("fixSpecify"));
        }
      }
      // --rule
      if (arg.equals("--rule")) {
        if (idx + 4 < params.size()) {
          String type = params.get(++idx);
          String tag = params.get(++idx);
          String operator = params.get(++idx);
          String value = params.get(++idx);
          if (validateRule(tag, operator, value, type)){
            rules.addRule(tag, operator, value, type.equals("warning"));
          } else {
            printOutErr(bundle.getString("ruleMalFormed"));
          }
        } else {
          printOutErr(bundle.getString("ruleSpecify"));
        }
      }
      // Unrecognised option
      else if (arg.startsWith("-")) {
        printOut(bundle.getString("unrecognizedOption").replace("%1", arg));
        argsError = true;
      }
      // File or directory to process
      else {
        if (path.isEmpty()) {
          path = arg;
        } else {
          printOutErr(bundle.getString("onlyOnePath"));
        }
      }
      idx++;
    }
  }

  private void setAction(Action act){
    if (action == null) {
      action = act;
    } else {
      printOut(bundle.getString("onlyOneConfigurationAction"));
      displayHelp();
    }
  }

  private Integer getFixOperatorInt(String operator){
    if (operator.equals("add")){
      return 1;
    } else if (operator.equals("remove")) {
      return 2;
    }
    return 0;
  }

  private String parseFixOperator(String operator){
    if (operator.equals("add")){
      operator = "addTag";
    } else if (operator.equals("remove")) {
      operator = "removeTag";
    } else {
      operator = null;
    }
    return operator;
  }

  /**
   * Main run function
   */
  public void run() {
    if (argsError) {
      displayHelp();
    } else {
      validatePath();
      switch (action){
        case ADD:
          addOrEditConfiguration();
          break;
        case EDIT:
          addOrEditConfiguration();
          if (configFile.delete()){

          }
          break;
        case REMOVE:
          if (configFile.delete()){
            printOut(bundle.getString("deletedConfig"));
          }
          break;
        case INFO:
          displayConfiguration(configFile, false);
          break;
        case LIST:
          if (common.getParameter("-l").equals("config")) {
            displayConfigurations();
          } else {
            // Show tags or iso
            // TODO
          }
          break;
      }
    }
  }

  private void addOrEditConfiguration(){
    Configuration config = common.getConfig();
    config.setIsos(isos);
    config.setFixes(fixes);
    config.setRules(rules);
    config.setModifiedIsos(modifiedIsos);
    config.setDescription(description);
  }

  private void displayConfigurations(){
    Collection<File> files = FileUtils.listFiles(new File(DPFManagerProperties.getConfigDir()), TrueFileFilter.INSTANCE, FalseFileFilter.INSTANCE);
    for (File file : files) {
      if (file.getName().endsWith(".dpf")) {
        displayConfiguration(file, true);
        printOut("");
      }
    }
  }

  private void displayConfiguration(File file, boolean onlyName){
    try {
      if (onlyName){
        printOut(file.getName().replace(".dpf",""));
      } else {
        printOut(file.getAbsolutePath());
      }
      printOut(FileUtils.readFileToString(file));
    } catch (IOException e) {
      printOut(bundle.getString("errorReadingConfig"));
    }
  }

  /**
   * Validations
   */
  private void validatePath(){
    if (action != Action.LIST){
      if (!path.isEmpty()){
        File file = new File(path);
        File fileInDir = new File(DPFManagerProperties.getConfigDir() + "/" + (path.endsWith(".dpf") ? path : path + ".dpf"));
        if (file.exists()){
          configFile = file;
          common.parseConfiguration(file.getAbsolutePath());
          common.parseConfig();
        } else if (fileInDir.exists()) {
          configFile = file;
          common.parseConfiguration(fileInDir.getAbsolutePath());
          common.parseConfig();
        } else {
          printOut(bundle.getString("errorConfigPath"));
          displayHelp();
        }
      } else {
        printOut(bundle.getString("errorConfigPath"));
        displayHelp();
      }
    }
  }

  private boolean validateRule(String tag, String operator, String value, String type){
    // Tag
    // TODO
    // Operator
    // TODO
    // Value
    // TODO
    // Type
    if (type.equals("error") || type.equals("warning")) {
      return false;
    }
    return true;
  }

  private boolean validateFix(String tag, String operator, String value){
    List<Field> validFixes = conformance.getConformanceCheckerFields();
    // Tag
    // TODO
    // Operator
    String parsedOP = parseFixOperator(operator);
    if (parsedOP == null){
      return false;
    }
    // Value
    if (parsedOP.equals("addTag") && value == null){
      return false;
    }
    return true;
  }

  private boolean validateIso(String iso){
    List<String> validIsos = ImplementationCheckerLoader.getPathsList();
    if (!validIsos.contains(iso) || isos.contains(iso)){
      return false;
    }
    return true;
  }

  /**
   * Displays help
   */
  private void displayHelp() {
//    printOut("");
//    printOut(bundle.getString("helpCO0"));
//    printOut("");
//    printOut(bundle.getString("helpOptions"));
//    printOptions("helpCO", 11);
    printOut("HELP!!");
    exit();
  }

  public void printOptions(String prefix, int max) {
    for (int i = 1; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.indexOf(":") + 1);
      String post = msg.substring(msg.indexOf(":") + 1);
      String line = String.format("%-40s%s", pre, post);
      printOut("    " + line);
    }
  }

  /**
   * Custom print lines
   */
  private void printOutErr(String message) {
    argsError = true;
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

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

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
 * @author Adria Llorens
 * @version 1.0
 * @since 13/10/2016
 */

package dpfmanager.shell.interfaces.console;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.metadata_fixer.Fixes;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.policy_checker.PolicyChecker;
import com.easyinnova.policy_checker.model.Field;
import com.easyinnova.policy_checker.model.Rules;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 11/04/2016.
 */
public class ConfigurationController {

  public enum Action {
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
   * The errors flag
   */
  private boolean argsError;

  /**
   * Custom extra params
   */
  private Action action;
  private File configFile;
  private String path = "";
  private boolean inDefaultDir;
  private ArrayList<String> isos;
  private ArrayList<String> formats;
  private ArrayList<String> autofixes;
  private Fixes fixes;
  private Rules rules;
  private String description;
  private Map<String, ArrayList<String>> modifiedIsos;
  private TiffConformanceChecker conformance;
  private String toShow;
  private String output;

  /**
   * Validation objects
   */
  private Map<String, Field> validRules;
  private List<String> validIsos;
  private List<String> validAutofixes;

  public ConfigurationController(ConsoleContext c, ResourceBundle r) {
    context = c;
    bundle = r;
    argsError = false;
    action = null;
    isos = new ArrayList<>();
    autofixes = new ArrayList<>();
    formats = new ArrayList<>();
    modifiedIsos = new HashMap<>();
    fixes = new Fixes();
    rules = new Rules();
    common = new CommonController(context, bundle);
    conformance = new TiffConformanceChecker();
    initValidationObjects();
  }

  private void initValidationObjects() {
    validRules = new HashMap<>();
    List<Field> fields = PolicyChecker.getPolicyCheckerFields();
    for (Field field : fields) {
      validRules.put(field.getName(), field);
    }
    validIsos = new ArrayList<>();
    for (String isoFile : ImplementationCheckerLoader.getPathsList()) {
      validIsos.add(ImplementationCheckerLoader.getFileName(isoFile));
    }
    validAutofixes = TiffConformanceChecker.getAutofixes(true);
  }

  private void preLoadConfiguration(Configuration config) {
    isos = config.getIsos();
    modifiedIsos = config.getModifiedIsos();
    rules = config.getRules();
    fixes = config.getFixes();
    description = config.getDescription();
    autofixes = config.getFixes().getAutofixes();
    output = config.getOutput();
    formats = config.getFormats();
  }

  /**
   * Main parse parameters function
   */
  public void parse(List<String> params) {
    if (params.size() == 0){
      displayHelp();
    }

    int idx = 0;
    /**
     * Actions
     */
    String arg = params.get(idx);
    // -a --add
    if (arg.equals("-a") || arg.equals("--add")) {
      if (idx + 1 < params.size()) {
        String name = params.get(++idx);
        if (path.isEmpty()) {
          path = name;
          setAction(Action.ADD);
        } else {
          printOutErr(bundle.getString("onlyOnePath"));
        }
      } else {
        printOutErr(bundle.getString("pathSpecify").replace("%1", "--add"));
      }
    }
    // -e --edit
    else if (arg.equals("-e") || arg.equals("--edit")) {
      if (idx + 1 < params.size()) {
        String name = params.get(++idx);
        if (path.isEmpty()) {
          path = name;
          setAction(Action.EDIT);
        } else {
          printOutErr(bundle.getString("onlyOnePath"));
        }
      } else {
        printOutErr(bundle.getString("pathSpecify").replace("%1", "--edit"));
      }
    }
    // -r --remove
    else if (arg.equals("-r") || arg.equals("--remove")) {
      if (idx + 1 < params.size()) {
        String name = params.get(++idx);
        if (path.isEmpty()) {
          path = name;
          setAction(Action.REMOVE);
        } else {
          printOutErr(bundle.getString("onlyOnePath"));
        }
      } else {
        printOutErr(bundle.getString("pathSpecify").replace("%1", "--remove"));
      }
    }
    // -l --list
    else if (arg.equals("-l") || arg.equals("--list")) {
      setAction(Action.LIST);
      if (idx + 1 < params.size()) {
        String type = params.get(++idx);
        if (!type.equals("rule") && !type.equals("fix") && !type.equals("iso") && !type.equals("autofix")) {
          type = "config";
        }
        toShow = type;
      } else {
        printOutErr(bundle.getString("isoSpecify"));
      }
    }
    // -i --info
    else if (arg.equals("-i") || arg.equals("--info")) {
      if (idx + 1 < params.size()) {
        String name = params.get(++idx);
        if (path.isEmpty()) {
          path = name;
          setAction(Action.INFO);
        } else {
          printOutErr(bundle.getString("onlyOnePath"));
        }
      } else {
        printOutErr(bundle.getString("pathSpecify").replace("%1", "--info"));
      }
    }
    // -h --help
    else if (arg.equals("-h") || arg.equals("--help")) {
      displayHelp();
    }
    // Unrecognised action
    else {
      printOut(bundle.getString("unrecognizedAction").replace("%1", arg));
      displayHelp();
    }
    idx++;
    validatePath();
    if (action.equals(Action.EDIT)) {
      validateConfigExists(true);
      common.parseConfiguration(configFile.getAbsolutePath());
      preLoadConfiguration(common.getConfig());
    } else if (action.equals(Action.ADD)) {
      common.parseConfig();
    }
    /**
     * Options
     */
    while (idx < params.size() && !argsError) {
      arg = params.get(idx);
      // -f --format
      if (arg.equals("-f") || arg.equals("--format")) {
        if (idx + 1 < params.size()) {
          validateFormats(params.get(++idx));
        } else {
          printOutErr(bundle.getString("specifyFormat"));
        }
      }
      // -o --output
      else if (arg.equals("-o") || arg.equals("--output")) {
        if (idx + 1 < params.size()) {
          String outputFolder = params.get(++idx);
          if (outputFolder.equals("DEFAULT")) {
            output = null;
          } else {
            argsError = !common.parseOutput(outputFolder);
            common.setExplicitOutput(false);
            output = outputFolder;
          }
        } else {
          printOutErr(bundle.getString("outputSpecify"));
        }
      }
      // -d --description
      else if (arg.equals("-d") || arg.equals("--description")) {
        if (idx + 1 < params.size()) {
          String desc = params.get(++idx);
          description = desc.equals("EMPTY") ? "" : desc;
        } else {
          printOutErr(bundle.getString("descriptionSpecify"));
        }
      }
      // --iso
      else if (arg.equals("--iso")) {
        if (idx + 1 < params.size()) {
          String iso = params.get(++idx);
          if (validateIso(iso, true)) {
            isos.add(iso);
          } else {
            printOutErr(bundle.getString("badIsoName"));
          }
        } else {
          printOutErr(bundle.getString("isoSpecify"));
        }
      }
      // --remove-iso
      else if (arg.equals("--remove-iso")) {
        if (idx + 1 < params.size()) {
          String iso = params.get(++idx);
          if (validateIso(iso, false)) {
            if (isos.contains(iso)) {
              isos.remove(iso);
            } else {
              printOutErr(bundle.getString("isoNotFound"));
            }
          }
        } else {
          printOutErr(bundle.getString("isoSpecify"));
        }
      }
      // --disable-iso-rule
      else if (arg.equals("--disable-iso-rule")) {
        if (idx + 2 < params.size()) {
          String iso = params.get(++idx);
          String ruleId = params.get(++idx);
          if (validateIso(iso, false)) {
            ArrayList<String> list = new ArrayList<>();
            if (modifiedIsos.containsKey(iso)) {
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
      // --enable-iso-rule
      else if (arg.equals("--enable-iso-rule")) {
        if (idx + 2 < params.size()) {
          String iso = params.get(++idx);
          String ruleId = params.get(++idx);
          if (validateIso(iso, false)) {
            ArrayList<String> list = new ArrayList<>();
            if (modifiedIsos.containsKey(iso)) {
              list = modifiedIsos.get(iso);
            }
            list.remove(ruleId);
            modifiedIsos.put(iso, list);
          } else {
            printOutErr(bundle.getString("badIsoName"));
          }
        } else {
          printOutErr(bundle.getString("isoSpecify"));
        }
      }
      // --fix
      else if (arg.equals("--fix")) {
        if (idx + 2 < params.size()) {
          String operator = params.get(++idx);
          String tag = params.get(++idx);
          String value = getFixOperatorInt(operator) == 1 ? (idx + 1 < params.size() ? params.get(++idx) : null) : null;
          if (validateFix(tag, operator, value)) {
            fixes.addFix(tag, parseFixOperator(operator), value);
          } else {
            printOutErr(bundle.getString("fixMalFormed"));
          }
        } else {
          printOutErr(bundle.getString("fixSpecify"));
        }
      }
      // --remove-fix
      else if (arg.equals("--remove-fix")) {
        if (idx + 2 < params.size()) {
          String operator = params.get(++idx);
          String tag = params.get(++idx);
          String value = getFixOperatorInt(operator) == 1 ? (idx + 1 < params.size() ? params.get(++idx) : null) : null;
          if (validateFix(tag, operator, value)) {
            if (!fixes.removeFix(tag, parseFixOperator(operator), value)) {
              printOutErr(bundle.getString("fixNotFound"));
            }
          } else {
            printOutErr(bundle.getString("fixMalFormed"));
          }
        } else {
          printOutErr(bundle.getString("fixSpecify"));
        }
      }
      // --autofix
      else if (arg.equals("--autofix")) {
        if (idx + 1 < params.size()) {
          String className = params.get(++idx);
          if (validateAutoFix(className)) {
            autofixes.add(className);
            fixes.addAutofix(className);
          } else {
            printOutErr(bundle.getString("autoFixMalformed"));
          }
        } else {
          printOutErr(bundle.getString("autoFixSpecify"));
        }
      }
      // --remove-autofix
      else if (arg.equals("--remove-autofix")) {
        if (idx + 1 < params.size()) {
          String className = params.get(++idx);
          if (validAutofixes.contains(className)) {
            autofixes.remove(className);
            if (!fixes.removeAutofix(className)) {
              printOutErr(bundle.getString("autofixNotFound"));
            }
          } else {
            printOutErr(bundle.getString("autoFixMalformed"));
          }
        } else {
          printOutErr(bundle.getString("autoFixSpecify"));
        }
      }
      // --rule
      else if (arg.equals("--rule")) {
        if (idx + 4 < params.size()) {
          String type = params.get(++idx);
          String tag = params.get(++idx);
          String operator = params.get(++idx);
          String value = params.get(++idx);
          if (validateRule(tag, operator, value, type)) {
            rules.addRule(tag, parseRuleOperator(operator), value, type.equals("warning"));
          } else {
            printOutErr(bundle.getString("ruleMalFormed"));
          }
        } else {
          printOutErr(bundle.getString("ruleSpecify"));
        }
      }
      // --remove-rule
      else if (arg.equals("--remove-rule")) {
        if (idx + 4 < params.size()) {
          String type = params.get(++idx);
          String tag = params.get(++idx);
          String operator = params.get(++idx);
          String value = params.get(++idx);
          if (validateRule(tag, operator, value, type)) {
            if (!rules.removeRule(tag, parseRuleOperator(operator), value, type.equals("warning"))) {
              printOutErr(bundle.getString("ruleNotFound"));
            }
          } else {
            printOutErr(bundle.getString("ruleMalFormed"));
          }
        } else {
          printOutErr(bundle.getString("ruleSpecify"));
        }
      }
      // Unrecognised option
      else {
        printOut(bundle.getString("unrecognizedOption").replace("%1", arg));
        argsError = true;
      }
      idx++;
    }
  }

  private void setAction(Action act) {
    if (action == null) {
      action = act;
    } else {
      printOut(bundle.getString("onlyOneConfigurationAction"));
      displayHelp();
    }
  }

  private Integer getFixOperatorInt(String operator) {
    if (operator.equals("add")) {
      return 1;
    } else if (operator.equals("remove")) {
      return 2;
    }
    return 0;
  }

  private String parseFixOperator(String operator) {
    if (operator.equals("add")) {
      operator = "addTag";
    } else if (operator.equals("remove")) {
      operator = "removeTag";
    } else {
      operator = "";
    }
    return operator;
  }

  private String parseRuleOperator(String op) {
    if (op.equals("GT") || op.equals("gt")) {
      return ">";
    } else if (op.equals("LT") || op.equals("lt")) {
      return "<";
    } else if (op.equals("EQ") || op.equals("eq")) {
      return "=";
    }
    return "";
  }

  /**
   * Main run function
   */
  public void run() {
    if (argsError) {
      displayHelp();
    } else {
      Configuration config;
      switch (action) {
        case ADD:
          validateConfigExists(false);
          config = generateConfiguration();
          try {
            config.SaveFile(configFile.getAbsolutePath());
            printOut(bundle.getString("addedConfig"));
            printOut("");
            displayConfiguration(configFile);
          } catch (Exception e) {
            printOut(bundle.getString("addedConfigError").replace("%1", path));
          }
          break;
        case EDIT:
          validateConfigExists(true);
          config = generateConfiguration();
          if (configFile.delete()) {
            try {
              config.SaveFile(configFile.getAbsolutePath());
              printOut(bundle.getString("editedConfig"));
              printOut("");
              displayConfiguration(configFile);
            } catch (Exception e) {
              printOut(bundle.getString("editedConfigError").replace("%1", path));
            }
          } else {
            printOut(bundle.getString("editedConfigError").replace("%1", path));
          }
          break;
        case REMOVE:
          validateConfigExists(true);
          if (configFile.delete()) {
            printOut(bundle.getString("deletedConfig"));
          } else {
            printOut(bundle.getString("deletedConfigError").replace("%1", path));
          }
          break;
        case INFO:
          validateConfigExists(true);
          displayConfiguration(configFile);
          break;
        case LIST:
          if (toShow.equals("config")) {
            displayConfigurations();
          } else if (toShow.equals("iso")) {
            displayIsos();
          } else if (toShow.equals("rule")) {
            displayRules();
          } else if (toShow.equals("fix")) {
            displayFixes();
          } else if (toShow.equals("autofix")) {
            displayAutoFixes();
          }
          break;
      }
    }
  }

  private Configuration generateConfiguration() {
    Configuration config = common.getConfig();
    config.setFormats(formats);
    config.setIsos(isos);
    config.setFixes(fixes);
    config.setRules(rules);
    config.setModifiedIsos(modifiedIsos);
    config.setDescription(description);
    config.setOutput(output);
    return config;
  }

  /**
   * Validations
   */
  private void validatePath() {
    if (action != Action.LIST) {
      if (!path.isEmpty()) {
        File file = new File(path);
        File fileInDir = new File(DPFManagerProperties.getConfigDir() + "/" + (path.endsWith(".dpf") ? path : path + ".dpf"));
        if (file.exists()) {
          configFile = file;
          inDefaultDir = false;
        } else if (fileInDir.exists()) {
          configFile = fileInDir;
          inDefaultDir = true;
        } else {
          if (path.contains("/") || path.contains("\\")) {
            configFile = file;
          } else {
            configFile = fileInDir;
          }
        }
      } else {
        printOut(bundle.getString("errorConfigPath"));
        displayHelp();
      }
    }
  }

  private void validateConfigExists(boolean exists) {
    if (exists && !configFile.exists()) {
      printOut(bundle.getString("errorConfigFileNoExists"));
      displayHelp();
    } else if (!exists && configFile.exists()) {
      printOut(bundle.getString("errorConfigFileExists"));
      displayHelp();
    }
  }

  private boolean validateRule(String tag, String operator, String value, String type) {
    // Type
    if (!type.equals("error") && !type.equals("warning")) {
      return false;
    }
    // Tag Operator Value
    if (validRules.containsKey(tag)) {
      Field field = validRules.get(tag);
      String op = parseRuleOperator(operator);
      if (field.getOperators().contains(op)) {
        if (field.getValues() != null) {
          // Specific values
          return field.getValues().contains(value);
        } else {
          // Free values
          if (field.getType().equals("integer")) {
            return isNumeric(value);
          } else if (field.getType().equals("boolean")) {
            return isBoolean(value);
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean validateFix(String tag, String operator, String value) {
    // Tag
    if (!conformance.getFixFields().contains(tag)) {
      return false;
    }
    // Operator
    String parsedOP = parseFixOperator(operator);
    if (!conformance.getFixes().contains(parsedOP)) {
      return false;
    }
    // Value
    if (parsedOP.equals("addTag") && value == null) {
      return false;
    }
    return true;
  }

  private boolean validateAutoFix(String fix) {
    return validAutofixes.contains(fix) && !autofixes.contains(fix);
  }

  private boolean validateIso(String iso, boolean checkAlreadyExists) {
    return validIsos.contains(iso) && (!checkAlreadyExists || !isos.contains(iso));
  }

  private void validateFormats(String formatsStr) {
    List<String> acceptedFormats = new ArrayList<>();
    acceptedFormats.add("xml");
    acceptedFormats.add("json");
    acceptedFormats.add("html");
    acceptedFormats.add("pdf");
    for (String format : formatsStr.split(",")) {
      if (acceptedFormats.contains(format.toLowerCase())) {
        formats.add(format.toUpperCase());
      } else {
        printOutErr(bundle.getString("incorrectReportFormat"));
      }
    }
  }

  private boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private boolean isBoolean(String str) {
    try {
      Boolean.parseBoolean(str);
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  /**
   * Displays help
   */
  private void displayHelp() {
    printOut("");
    printOut(bundle.getString("helpCF0"));
    printOut("");
    // Actions
    printOut(bundle.getString("helpCF1"));
    printOptions("helpCF1", 5, "");
    printOptions("helpCF15", 5, "    ");
    printOptions("helpCF1", 6, 6);
    printOut("");
    // Options
    printOut(bundle.getString("helpOptions"));
    printOptions("helpCF2", 13, "");
    printOut("");
    // Rule specification
    printOut(bundle.getString("helpCF3"));
    printSpecifications("helpCF3", 3, "");
    printSpecifications("helpCF33", 2, "    ");
    printOut("");
    // Fix specification
    printOut(bundle.getString("helpCF4"));
    printSpecifications("helpCF4", 3, "");
    printSpecifications("helpCF43", 2, "    ");
    printOut("");
    exit();
  }

  public void printSpecifications(String prefix, int max, String prespace) {
    for (int i = 1; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      printOut("    " + prespace + msg);
    }
  }

  public void printOptions(String prefix, int max, int min) {
    for (int i = min; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.indexOf(":") + 1);
      String post = msg.substring(msg.indexOf(":") + 1);
      String line = String.format("%-40s%s", pre, post);
      printOut("    " + line);
    }
  }

  public void printOptions(String prefix, int max, String prespace) {
    for (int i = 1; i <= max; i++) {
      String msg = bundle.getString(prefix + i);
      String pre = msg.substring(0, msg.indexOf(":") + 1);
      String post = msg.substring(msg.indexOf(":") + 1);
      String line = String.format("%-40s%s", prespace + pre, post);
      printOut("    " + line);
    }
  }

  private void displayConfigurations() {
    Collection<File> files = FileUtils.listFiles(new File(DPFManagerProperties.getConfigDir()), TrueFileFilter.INSTANCE, FalseFileFilter.INSTANCE);
    for (File file : files) {
      if (file.getName().endsWith(".dpf")) {
        displayConfiguration(file);
        printOut("");
      }
    }
  }

  private void displayConfiguration(File file) {
    try {
      if (inDefaultDir) {
        printOut(file.getName().replace(".dpf", ""));
      } else {
        printOut(file.getAbsolutePath());
      }
      printOut(FileUtils.readFileToString(file));
    } catch (IOException e) {
      printOut(bundle.getString("errorReadingConfig"));
    }
  }

  private void displayIsos() {
    List<String> validIsos = ImplementationCheckerLoader.getPathsList();
    printOut(bundle.getString("displayIsoHeader"));
    for (String iso : validIsos) {
      printOut("  " + ImplementationCheckerLoader.getFileName(iso) + ": " + ImplementationCheckerLoader.getIsoName(iso));
    }
  }

  private void displayRules() {
    printOut(bundle.getString("displayRuleHeader"));
    for (String tag : validRules.keySet()) {
      Field field = validRules.get(tag);
      if (field.getValues() != null) {
        printOut("  " + field.getName() + " [" + String.join(", ", field.getValues()) + "]");
      } else {
        printOut("  " + field.getName() + " (" + field.getType() + ")");
      }
    }
  }

  private void displayFixes() {
    printOut(bundle.getString("displayFixHeader"));
    for (String field : conformance.getFixFields()) {
      printOut("  " + field);
    }
  }

  private void displayAutoFixes() {
    printOut(bundle.getString("displayAutoFixHeader"));
    for (String autofix : validAutofixes) {
      printOut("  " + autofix);
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

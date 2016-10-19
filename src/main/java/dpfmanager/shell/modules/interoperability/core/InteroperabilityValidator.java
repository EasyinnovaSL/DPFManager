/**
 * <h1>InteroperabilityValidator.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.modules.interoperability.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import edu.emory.mathcs.backport.java.util.Arrays;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 05/10/2016.
 */
public class InteroperabilityValidator {

  private DpfContext context;
  private ResourceBundle bundle;

  public InteroperabilityValidator(DpfContext context, ResourceBundle bundle) {
    this.context = context;
    this.bundle = bundle;
  }

  public boolean validateConformance(ConformanceConfig conformance, String name, boolean acceptedBuiltIn) {
    if (conformance == null) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
      return false;
    } else if (conformance.getPath().equals("built-in") && !acceptedBuiltIn) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("builtInNotAccepted")));
      return false;
    }
    return true;
  }

  public boolean validateAll(ConformanceConfig conformance) {
    if (conformance.isBuiltIn()){
      return validateConfiguration(conformance.isBuiltIn(), conformance.getConfiguration());
    } else {
      return validateParameters(conformance.getParameters())
          && validatePath(conformance.getPath())
          && validateConfiguration(conformance.isBuiltIn(), conformance.getConfiguration());
    }
  }

  public boolean validateParameters(String params) {
    if (params.contains("%config%") && params.contains("%input%")) {
      return true;
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParamsError")));
      return false;
    }
  }

  public boolean validatePath(String path) {
    File tmp = new File(path);
    if (tmp.exists() && tmp.isFile()) {
      return true;
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidPath").replace("%1", path)));
    }
    return false;
  }

  public boolean validateConfiguration(boolean isBuiltIn, String config) {
    if (isBuiltIn) {
      // Internal
      File inConfigs = new File(DPFManagerProperties.getConfigDir() + "/" + config + ".dpf");
      File absolute = new File(config);
      if (config.isEmpty()) {
        // Default
        return true;
      } else if ((inConfigs.exists() && inConfigs.isFile()) || (absolute.exists() && absolute.isFile())) {
        // In configs or absolute path
        return true;
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidConfig").replace("%1", config)));
      }
    } else {
      // External
      return true;
      // TODO obligatory config file
//      File tmp = new File(config);
//      if (tmp.exists() && tmp.isFile()) {
//        return true;
//      } else {
//        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidConfig").replace("%1", config)));
//      }
    }
    return false;
  }

  public List<String> parseExtensions(String str){
    List<String> extensions = new ArrayList<>();
    str = str.replaceAll(" ", "");
    if (str.matches("[a-zA-Z1-9,]+")){
      extensions.addAll(Arrays.asList(str.split(",")));
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorExtensions")));
    }
    return extensions;
  }

  /**
   * Validates a loaded built in conformance checker
   */
  public boolean validateInitBuilt(ConformanceConfig conformance) {
    String path = conformance.getConfiguration();
    if (!path.isEmpty() && !path.contains("/") && !path.contains("\\")) {
      path = DPFManagerProperties.getConfigDir() + "/" + path + ".dpf";
    }

    File config = new File(path);
    if (!conformance.getConfiguration().isEmpty() && (!config.exists() || !config.isFile())) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingBuiltCC")));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeConfigPath")));
      return false;
    }

    if (conformance.getExtensions().isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingBuiltCC")));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeExtensions")));
      return false;
    }
    return true;
  }

  /**
   * Validates a loaded external conformance checker
   */
  public boolean validateInitExternal(ConformanceConfig conformance) {
    File tmp = new File(conformance.getPath());
    if (!tmp.exists() || !tmp.isFile()) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingCC").replace("%1", conformance.getName())));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causePath")));
      return false;
    }

    File config = new File(conformance.getConfiguration());
    if (!conformance.getConfiguration().isEmpty() && (!config.exists() || !config.isFile())) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingCC").replace("%1", conformance.getName())));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeConfigPath")));
      return false;
    }

    if (!conformance.getParameters().contains("%input%") || !conformance.getParameters().contains("%config%")) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingCC").replace("%1", conformance.getName())));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeParameters")));
      return false;
    }

    if (conformance.getExtensions().isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingCC").replace("%1", conformance.getName())));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeExtensions")));
      return false;
    }
    return true;
  }

}

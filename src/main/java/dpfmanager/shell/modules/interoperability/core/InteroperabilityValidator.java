package dpfmanager.shell.modules.interoperability.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
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

package dpfmanager.shell.modules.interoperability.core;

import dpfmanager.conformancechecker.external.ExternalConformanceChecker;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
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

  public boolean validateParameters(String params) {
    return params.contains("%config%") && params.contains("%input%");
  }

  public boolean validateEnable(ConformanceConfig conformance, boolean enabled) {
    if ((enabled && !conformance.getConfiguration().isEmpty() && !conformance.getParameters().isEmpty()) || !enabled) {
      return true;
    }
    return false;
  }

  /**
   * Validates a loaded built in conformance checker
   */
  public boolean validateInitBuilt(ConformanceConfig conformance) {
    File config = new File(conformance.getConfiguration());
    if (!conformance.getConfiguration().isEmpty() && (!config.exists() || !config.isFile())){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingBuiltCC")));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeConfigPath")));
      return false;
    }

    if (conformance.getExtensions().isEmpty()){
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
    if (!conformance.getConfiguration().isEmpty() && (!config.exists() || !config.isFile())){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingCC").replace("%1", conformance.getName())));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeConfigPath")));
      return false;
    }

    if (!conformance.getParameters().contains("%input%") || !conformance.getParameters().contains("%config%")){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingCC").replace("%1", conformance.getName())));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeParameters")));
      return false;
    }

    if (conformance.getExtensions().isEmpty()){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingCC").replace("%1", conformance.getName())));
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeExtensions")));
      return false;
    }
    return true;
  }

}

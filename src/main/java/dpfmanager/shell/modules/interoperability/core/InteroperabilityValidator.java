package dpfmanager.shell.modules.interoperability.core;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

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

  public boolean validateConformance(Conformance conformance, String name, boolean acceptedBuiltIn) {
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

  public boolean validateEnable(Conformance conformance, boolean enabled) {
    if ((enabled && !conformance.getConfiguration().isEmpty() && !conformance.getParameters().isEmpty()) || !enabled) {
      return true;
    }
    return false;
  }

}

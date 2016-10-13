/**
 * <h1>DatabaseService.java</h1> <p> This program is free software: you can redistribute it and/or
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

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.external.ExternalConformanceChecker;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.DessignMessage;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityResponseMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Service(BasicConfig.SERVICE_INTEROPERABILITY)
@Scope("singleton")
public class InteroperabilityService extends DpfService {

  /**
   * The conformance checkers config list
   */
  private List<ConformanceConfig> conformances;

  /**
   * The available conformance checkers list
   */
  private List<ConformanceChecker> available;

  private InteroperabilityValidator validator;

  private InteroperabilityManager manager;

  private ResourceBundle bundle;

  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
    conformances = new ArrayList<>();
    available = new ArrayList<>();
  }

  @Override
  protected void handleContext(DpfContext context) {
    validator = new InteroperabilityValidator(context, bundle);
    manager = new InteroperabilityManager(context, bundle, validator);
    loadConformanceCheckers();
    filterAvailableConformances();
    context.sendGui(GuiConfig.PERSPECTIVE_DESSIGN + "." + GuiConfig.COMPONENT_DESIGN, new DessignMessage());
  }

  private ConformanceConfig getConformanceById(String id) {
    for (ConformanceConfig conformance : conformances) {
      if (conformance.getUuid().equals(id) || conformance.getName().equals(id)) {
        return conformance;
      }
    }
    return null;
  }

  /**
   * Main functions
   */

  public void add(String name, String path, String parameters, String configure, List<String> extensions, boolean enable) {
    if (getConformanceById(name) == null) {
      if (validator.validatePath(path) && validator.validateParameters(parameters) && validator.validateConfiguration(false, configure)) {
        ConformanceConfig conformance = new ConformanceConfig(name, path);
        conformance.setParameters(parameters);
        conformance.setConfiguration(configure);
        conformance.setExtensions(extensions);
        conformance.setEnabled(enable);
        conformances.add(conformance);
        if (manager.writeChanges(conformances)) {
          // OK
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceAdded").replace("%1", name).replace("%2", path)));
          return;
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidName").replace("%1", name)));
    }
  }

  public void edit(String name, String path) {
    ConformanceConfig conformance = getConformanceById(name);
    if (validator.validateConformance(conformance, name, false) && validator.validatePath(path)) {
      conformance.setPath(path);
      if (manager.writeChanges(conformances)) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceEdited").replace("%1", name)));
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
  }

  public void guiedit(ConformanceConfig newConfig) {
    boolean error = false;
    if (validator.validateAll(newConfig)) {
      ConformanceConfig original = getConformanceById(newConfig.getUuid());
      if (getConformanceById(newConfig.getUuid()) == null) {
        // Add
        conformances.add(newConfig);
      } else if (getConformanceById(newConfig.getName()) == getConformanceById(newConfig.getUuid())){
        // Edit
        original.copy(newConfig);
      } else {
        // Repeated name
        error = true;
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidName").replace("%1", newConfig.getName())));
      }
      if (!error && manager.writeChanges(conformances)) {
        context.sendGui(GuiConfig.PERSPECTIVE_INTEROPERABILITY + "." + GuiConfig.COMPONENT_INTEROPERABILITY, new InteroperabilityResponseMessage(InteroperabilityResponseMessage.Type.SAVE, true, newConfig.getUuid()));
        return;
      }
    }
    // Notify KO
    context.sendGui(GuiConfig.PERSPECTIVE_INTEROPERABILITY + "." + GuiConfig.COMPONENT_INTEROPERABILITY, new InteroperabilityResponseMessage(InteroperabilityResponseMessage.Type.SAVE, false, newConfig.getUuid()));
  }

  public void remove(String name) {
    ConformanceConfig conformance = getConformanceById(name);
    if (validator.validateConformance(conformance, name, false)) {
      conformances.remove(conformance);
      if (manager.writeChanges(conformances)) {
        // OK
        context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceRemoved").replace("%1", name)));
        context.sendGui(GuiConfig.PERSPECTIVE_INTEROPERABILITY + "." + GuiConfig.COMPONENT_INTEROPERABILITY, new InteroperabilityResponseMessage(InteroperabilityResponseMessage.Type.REMOVE, true, name));
        return;
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
    // Notify KO
    context.sendGui(GuiConfig.PERSPECTIVE_INTEROPERABILITY + "." + GuiConfig.COMPONENT_INTEROPERABILITY, new InteroperabilityResponseMessage(InteroperabilityResponseMessage.Type.REMOVE, false, name));
  }

  public void list(String name) {
    ConformanceConfig conformance = getConformanceById(name);
    if (validator.validateConformance(conformance, name, true)) {
      String xml = conformance.toString();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, xml));
    }
  }

  public void listAll() {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, manager.getString(conformances)));
  }

  public List<ConformanceConfig> listObjects() {
    return conformances;
  }

  public void setParameters(String name, String params) {
    ConformanceConfig conformance = getConformanceById(name);
    if (validator.validateConformance(conformance, name, false) && validator.validateParameters(params)) {
      conformance.setParameters(params);
      if (manager.writeChanges(conformances)) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParams").replace("%1", name)));
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
  }

  public void setConfiguration(String name, String config) {
    ConformanceConfig conformance = getConformanceById(name);
    if (validator.validateConformance(conformance, name, true) && validator.validateConfiguration(conformance.isBuiltIn(), config)) {
      conformance.setConfiguration(config);
      if (manager.writeChanges(conformances)) {
        // OK
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceConfigure").replace("%1", name)));
        return;
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
  }

  public void setExtensions(String name, List<String> extensions) {
    ConformanceConfig conformance = getConformanceById(name);
    if (validator.validateConformance(conformance, name, false)) {
      conformance.setExtensions(extensions);
      if (manager.writeChanges(conformances)) {
        // OK
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceConfigure").replace("%1", name)));
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
  }

  public void setEnabled(String name, boolean enabled) {
    ConformanceConfig conformance = getConformanceById(name);
    if (validator.validateConformance(conformance, name, true) && validator.validateAll(conformance)) {
      conformance.setEnabled(enabled);
      if (manager.writeChanges(conformances)) {
        if (enabled) {
          context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceEnabled").replace("%1", name)));
          return;
        } else {
          context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceDisabled").replace("%1", name)));
          return;
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
    // Notify KO
    if (conformance != null) {
      context.sendGui(GuiConfig.PERSPECTIVE_INTEROPERABILITY + "." + GuiConfig.COMPONENT_INTEROPERABILITY, new InteroperabilityResponseMessage(InteroperabilityResponseMessage.Type.ENABLE, false, conformance.getUuid()));
    }
  }

  private void filterAvailableConformances() {
    available.clear();
    for (ConformanceConfig conformance : conformances) {
      if (conformance.isEnabled()) {
        if (conformance.isBuiltIn() && validator.validateInitBuilt(conformance)) {
          Configuration checkConfig = readCheckConfig(conformance.getConfiguration());
          if (checkConfig != null) {
            available.add(new TiffConformanceChecker(conformance, checkConfig));
          } else {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("errorLoadingBuiltCC")));
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("causeReadingCheckConfig")));
          }
        } else if (!conformance.isBuiltIn() && validator.validateInitExternal(conformance)) {
          ExternalConformanceChecker ext = new ExternalConformanceChecker(conformance);
          available.add(ext);
        }
      }
    }
  }

  private Configuration readCheckConfig(String path) {
    try {
      Configuration configuration = new Configuration();
      if (!path.isEmpty()) {
        if (!path.contains("/") && !path.contains("\\")) {
          // Read from configurations folder
          String pathAux = DPFManagerProperties.getConfigDir() + "/" + path + ".dpf";
          configuration.ReadFileNew(pathAux);
        } else {
          // Read from specified file
          configuration.ReadFileNew(path);
        }
      } else {
        // Read from jar (default default configuration)
        configuration.ReadFileNew(DPFManagerProperties.getDefaultBuiltInConfig());
      }
      return configuration;
    } catch (Exception e) {
      return null;
    }
  }

  public List<ConformanceChecker> getConformanceCheckers() {
    filterAvailableConformances();
    return available;
  }

  public String getDescriptionFromDefault() {
    for (ConformanceChecker cc : available) {
      if (cc.getConfig().isBuiltIn()) {
        return cc.getDefaultConfiguration().getDescription();
      }
    }
    return null;
  }

  /**
   * Read the conformance checkers from configuration file
   */
  private void loadConformanceCheckers() {
    boolean needWrite = false;
    conformances.addAll(manager.loadFromFile());
    List<ConformanceConfig> builtInCC = manager.loadFromBuiltIn();
    for (ConformanceConfig conformance : builtInCC) {
      if (getConformanceById(conformance.getUuid()) == null) {
        conformances.add(conformance);
        needWrite = true;
      }
    }
    // Write if needed
    if (needWrite) {
      manager.writeChanges(conformances);
    }
  }

  @PreDestroy
  public void finish() {

  }

}

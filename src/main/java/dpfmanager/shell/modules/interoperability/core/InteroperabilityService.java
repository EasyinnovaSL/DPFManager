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
import dpfmanager.conformancechecker.external.ExternalConformanceChecker;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Service(BasicConfig.SERVICE_INTEROPERABILITY)
@Scope("singleton")
public class InteroperabilityService extends DpfService {

  /**
   * The conformance checkers list
   */
  private List<Conformance> conformances;

  private InteroperabilityValidator validator;

  private InteroperabilityManager manager;

  private ResourceBundle bundle;

  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
    conformances = new ArrayList<>();
  }

  @Override
  protected void handleContext(DpfContext context) {
    validator = new InteroperabilityValidator(context, bundle);
    manager = new InteroperabilityManager(context, bundle);
    loadConformanceCheckers();
  }

  private Conformance getConformanceByName(String name) {
    for (Conformance conformance : conformances) {
      if (conformance.getName().equals(name)) {
        return conformance;
      }
    }
    return null;
  }

  /**
   * Main functions
   */

  public void add(String name, String path, String parameters, String configure, List<String> extensions) {
    File tmp = new File(path);
    if (getConformanceByName(name) == null) {
      if (tmp.exists() && tmp.isFile()) {
        if (validator.validateParameters(parameters)) {
          Conformance conformance = new Conformance(name, path);
          conformance.setParameters(parameters);
          conformance.setConfiguration(configure);
          conformance.setExtensions(extensions);
          conformance.setEnabled(true);
          conformances.add(conformance);
          if (manager.writeChanges(conformances)) {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceAdded").replace("%1", name).replace("%2", path)));
          } else {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
          }
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParamsError")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidPath").replace("%1", path)));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidName").replace("%1", name)));
    }
  }

  public void edit(String name, String path) {
    Conformance conformance = getConformanceByName(name);
    if (validator.validateConformance(conformance, name, false)) {
      File tmp = new File(path);
      if (tmp.exists() && tmp.isFile()) {
        conformance.setPath(path);
        if (manager.writeChanges(conformances)) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceEdited").replace("%1", name)));
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidPath").replace("%1", path)));
      }
    }
  }

  public void remove(String name) {
    Conformance conformance = getConformanceByName(name);
    if (validator.validateConformance(conformance, name, false)) {
      conformances.remove(conformance);
      if (manager.writeChanges(conformances)) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceRemoved").replace("%1", name)));
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
  }

  public void list(String name) {
    Conformance conformance = getConformanceByName(name);
    if (validator.validateConformance(conformance, name, true)) {
      String xml = conformance.toString();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, xml));
    }
  }

  public void listAll() {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, manager.getString(conformances)));
  }

  public void setParameters(String name, String params) {
    Conformance conformance = getConformanceByName(name);
    if (validator.validateConformance(conformance, name, false)) {
      if (validator.validateParameters(params)) {
        conformance.setParameters(params);
        if (manager.writeChanges(conformances)) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParams").replace("%1", name)));
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceParamsError")));
      }
    }
  }

  public void setConfiguration(String name, String config) {
    Conformance conformance = getConformanceByName(name);
    if (validator.validateConformance(conformance, name, true)) {
      File tmp = new File(config);
      if (tmp.exists() && tmp.isFile()) {
        conformance.setConfiguration(config);
        if (manager.writeChanges(conformances)) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceConfigure").replace("%1", name)));
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceInvalidConfig").replace("%1", config)));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceNotFound").replace("%1", name)));
    }
  }

  public void setExtensions(String name, List<String> extensions) {
    Conformance conformance = getConformanceByName(name);
    if (validator.validateConformance(conformance, name, false)) {
      conformance.setExtensions(extensions);
      if (manager.writeChanges(conformances)) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceConfigure").replace("%1", name)));
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
      }
    }
  }

  public void setEnabled(String name, boolean enabled) {
    Conformance conformance = getConformanceByName(name);
    if (validator.validateConformance(conformance, name, true)) {
      if (validator.validateEnable(conformance, enabled)) {
        conformance.setEnabled(enabled);
        if (manager.writeChanges(conformances)) {
          if (enabled) {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceEnabled").replace("%1", name)));
          } else {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceDisabled").replace("%1", name)));
          }
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceErrorWriting")));
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("conformanceCannotEnable")));
      }
    }
  }

  public List<ConformanceChecker> getConformanceCheckers(){
    List<ConformanceChecker> ccList = new ArrayList<>();
    for (Conformance conformance : conformances){
      if (conformance.isEnabled()) {
        if (conformance.isBuiltIn()) {
          ccList.add(new TiffConformanceChecker());
        } else {
          File tmp = new File(conformance.getPath());
          if (tmp.exists()) {
            ExternalConformanceChecker ext = new ExternalConformanceChecker(conformance.getPath(), conformance.getParametersList(), new ArrayList<>(), conformance.getExtensions(), conformance.getConfiguration());
            ccList.add(ext);
          }
        }
      }
    }
    return ccList;

//    String path = "package/resources/plugins/video/MediaConch.exe";
//    if (!new File(path).exists()) path = "plugins/video/MediaConch.exe";
//    if (!new File(path).exists()) path = "../plugins/video/MediaConch.exe";
//    if (new File(path).exists()) {
//      ArrayList<String> params = new ArrayList<>();
//      params.add("-mc");
//      params.add("-fx");
//      ArrayList<String> standards = new ArrayList<>();
//      standards.add("MOV");
//      ArrayList<String> extensions = new ArrayList<>();
//      extensions.add("MOV");
//      ExternalConformanceChecker ext = new ExternalConformanceChecker(path, params, standards, extensions);
//      l.add(ext);
//    }
//
//    path = "package/resources/plugins/pdf/verapdf.bat";
//    if (!new File(path).exists()) path = "plugins/pdf/verapdf.bat";
//    if (!new File(path).exists()) path = "../plugins/pdf/verapdf.bat";
//    if (new File(path).exists()) {
//      ArrayList<String> params = new ArrayList<>();
//      params.add("--format");
//      params.add("xml");
//      ArrayList<String> standards = new ArrayList<>();
//      standards.add("PDF");
//      ArrayList<String> extensions = new ArrayList<>();
//      extensions.add("PDF");
//      ExternalConformanceChecker ext = new ExternalConformanceChecker(path, params, standards, extensions);
//      l.add(ext);
//    }
  }

  /**
   * Read the conformance checkers from configuration file
   */
  private void loadConformanceCheckers() {
    boolean needWrite = false;
    conformances.addAll(manager.loadFromFile());
    List<Conformance> builtInCC = manager.loadFromBuiltIn();
    for (Conformance conformance : builtInCC){
      if (getConformanceByName(conformance.getName()) == null){
        conformances.add(conformance);
        needWrite = true;
      }
    }
    // Write if needed
    if (needWrite){
      manager.writeChanges(conformances);
    }
  }

  @PreDestroy
  public void finish() {

  }

}

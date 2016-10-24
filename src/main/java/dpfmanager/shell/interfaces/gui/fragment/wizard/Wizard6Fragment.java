/**
 * <h1>Wizard5Fragment.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RulesType;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.component.config.ConfigController;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

import org.controlsfx.control.CheckTreeView;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_6,
    viewLocation = "/fxml/config/subconfig6.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class Wizard6Fragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private Label labelEditing;
  @FXML
  private TextArea textArea;
  @FXML
  private TextField nameField;
  @FXML
  private HBox treeViewHBox;

  private CheckTreeView<String> checkTreeView;

  private ConfigController controller;

  private ImplementationCheckerObjectType rules;

  private String filePath;

  public Wizard6Fragment() {
    filePath = null;
  }

  public void setController(ConfigController controller) {
    this.controller = controller;
  }

  public void edit(String iso, String path) {
    filePath = path;
    rules = ImplementationCheckerLoader.getRules(iso);
    labelEditing.setText(bundle.getString("w6Editing").replace("%1", rules.getTitle()));
    if (filePath == null) {
      nameField.clear();
    } else {
      nameField.setText(rules.getTitle());
    }
    addTreeView();
  }

  private void addTreeView() {
    // Root node (my computer)
    CheckBoxTreeItem<String> rootNode = new CheckBoxTreeItem<>("rootDummy");
    checkTreeView = new CheckTreeView<>(rootNode);
    checkTreeView.setShowRoot(false);
    rootNode.setExpanded(true);

    // Root items
    for (RulesType rule : rules.getRules()) {
      String name = rule.getId() + ": " + rule.getTitle();
      CheckBoxTreeItem treeNode = new CheckBoxTreeItem(name);
      for (RuleType childRule : rule.getRule()) {
        String childName = childRule.getId() + ": " + childRule.getTitle().getValue();
        CheckBoxTreeItem treeNodeChild = new CheckBoxTreeItem(childName);
        treeNodeChild.setSelected(true);
        treeNode.getChildren().add(treeNodeChild);
      }
      treeNode.setSelected(true);
      rootNode.getChildren().add(treeNode);
    }

    // Add data and add to gui
    treeViewHBox.getChildren().clear();
    treeViewHBox.getChildren().add(checkTreeView);
    HBox.setHgrow(checkTreeView, Priority.ALWAYS);
  }

  @FXML
  protected void saveIso() {
    String isoTitle = nameField.getText();
    if (isoTitle.isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("w6EnterFilename")));
      return;
    }
    File file = askForFile();
    if (file == null) {
      return;
    }

    // All ok
    rules.setTitle(isoTitle);
    modifyRules();
    if (saveToFile(file)) {
      controller.editIsoSuccess(file.getAbsolutePath(), filePath == null);
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("w6ErrorSaving")));
    }
  }

  private File askForFile() {
    if (filePath == null) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(bundle.getString("w6SelectFileg"));
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
      fileChooser.setInitialDirectory(new File(DPFManagerProperties.getIsosDir()));
      return fileChooser.showSaveDialog(GuiWorkbench.getMyStage());
    } else {
      return new File(filePath);
    }
  }

  private void modifyRules() {
    for (TreeItem<String> item : checkTreeView.getRoot().getChildren()) {
      // First Level
      CheckBoxTreeItem<String> checkItem = (CheckBoxTreeItem<String>) item;
      String id = item.getValue().substring(0, item.getValue().indexOf(":"));
      if (!checkItem.isSelected()) {
        rules.removeRule(id);
      } else {
        // Second level
        for (TreeItem<String> child : item.getChildren()) {
          CheckBoxTreeItem<String> checkChild = (CheckBoxTreeItem<String>) child;
          String idChild = checkChild.getValue().substring(0, checkChild.getValue().indexOf(":"));
          if (!checkChild.isSelected()) {
            RulesType rulesType = rules.getRulesById(id);
            if (rulesType != null) {
              rulesType.removeRule(idChild);
            }
          }
        }
      }
    }
  }

  private boolean saveToFile(File file) {
    try {
      if (file.exists()) {
        file.delete();
      }

      JAXBContext jaxbContext = JAXBContext.newInstance(ImplementationCheckerObjectType.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      jaxbMarshaller.marshal(rules, file);
      return true;
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return false;
  }

  @FXML
  protected void cancel() {
    controller.editIsoCancelled();
  }

}

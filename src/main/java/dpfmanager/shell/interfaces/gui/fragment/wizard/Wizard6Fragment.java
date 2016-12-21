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

import dpfmanager.conformancechecker.configuration.Configuration;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import org.controlsfx.control.CheckTreeView;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
  private HBox treeViewHBox;

  private CheckTreeView<RuleTreeItem> checkTreeView;

  private ConfigController controller;

  private ImplementationCheckerObjectType rules;

  private String isoId;

  public Wizard6Fragment() {
  }

  public void setController(ConfigController controller) {
    this.controller = controller;
  }

  public void edit(String iso, List<String> deleted) {
    rules = ImplementationCheckerLoader.getRules(iso);
    isoId = iso;
    labelEditing.setText(bundle.getString("w6Editing").replace("%1", rules.getTitle()));
    addTreeView(deleted);
  }

  private void addTreeView(List<String> deleted) {
    // Root node (my computer)
    CheckBoxTreeItem<RuleTreeItem> rootNode = new CheckBoxTreeItem<>(new RuleTreeItem());
    checkTreeView = new CheckTreeView<>(rootNode);
    checkTreeView.setShowRoot(false);
    rootNode.setExpanded(true);

    // Load rules
    for (RulesType rule : rules.getOwnRules()) {
      CheckBoxTreeItem<RuleTreeItem> treeNode = new CheckBoxTreeItem(new RuleTreeItem(rule.getId(), rule.getTitle(), rule.getDescription()));
      boolean parentSelected = !deleted.contains(rule.getId());
      if (parentSelected) {
        treeNode.setSelected(true);
      }
      for (RuleType childRule : rule.getRule()) {
        RuleTreeItem item = new RuleTreeItem(childRule.getId(), childRule.getTitle().getValue(), childRule.getDescription().getValue());
        item.setReference(childRule.getReferenceText());
        CheckBoxTreeItem<RuleTreeItem> treeNodeChild = new CheckBoxTreeItem(item);
        if (!deleted.contains(childRule.getId()) && parentSelected) {
          treeNodeChild.setSelected(true);
        } else if (parentSelected) {
          treeNode.setIndeterminate(true);
        }
        treeNode.getChildren().add(treeNodeChild);
      }
      rootNode.getChildren().add(treeNode);
    }

    // Initialize tooltip
    checkTreeView.setCellFactory(new Callback<TreeView<RuleTreeItem>, TreeCell<RuleTreeItem>>() {
      @Override
      public TreeCell<RuleTreeItem> call(TreeView<RuleTreeItem> param) {
        TreeCell<RuleTreeItem> cell = new CheckBoxTreeCell<RuleTreeItem>() {
          @Override
          public void updateItem(RuleTreeItem item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
              setText(item.getId() + ": " + item.getName());
              String description = item.getDescription();
              if (item.getReference() != null){
                description += "\n" + item.getReference();
              }
              if (!description.isEmpty()) {
                setTooltip(new Tooltip(description));
              }
            }
          }
        };
        return cell;
      }
    });

    // Add data and add to gui
    treeViewHBox.getChildren().clear();
    treeViewHBox.getChildren().add(checkTreeView);
    HBox.setHgrow(checkTreeView, Priority.ALWAYS);
  }

  private List<String> getUnselectedRules() {
    ImplementationCheckerObjectType newRules = new ImplementationCheckerObjectType();
    newRules.makeCopy(rules);
    List<String> unselected = new ArrayList<>();
    for (TreeItem<RuleTreeItem> item : checkTreeView.getRoot().getChildren()) {
      // First Level
      CheckBoxTreeItem<RuleTreeItem> checkItem = (CheckBoxTreeItem<RuleTreeItem>) item;
      String id = item.getValue().getId();
      if (!checkItem.isSelected() && !checkItem.isIndeterminate()) {
        unselected.add(id);
      }
      // Second level
      RulesType rulesType = newRules.getRulesById(id);
      if (rulesType != null) {
        for (TreeItem<RuleTreeItem> child : item.getChildren()) {
          CheckBoxTreeItem<RuleTreeItem> checkChild = (CheckBoxTreeItem<RuleTreeItem>) child;
          String idChild = checkChild.getValue().getId();
          if (!checkChild.isSelected()) {
            unselected.add(idChild);
          }
        }
      }
      if (rulesType.getRule().size() == 0){
        unselected.add(id);
      }
    }
    return unselected;
  }

  @FXML
  protected void saveIso() {
    // All ok
    List<String> rules = getUnselectedRules();
    controller.editIsoSuccess(isoId, rules);
  }

  @FXML
  protected void cancel() {
    controller.editIsoCancelled();
  }

}

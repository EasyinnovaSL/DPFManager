/**
 * <h1>PeriodicFragment.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.modules.interoperability.core.ConformanceConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_INTEROP,
    viewLocation = "/fxml/interop.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class InteropFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private GridPane gridView;
//  @FXML
//  private GridPane gridEdit;

  @FXML
  private Label viewName;
  @FXML
  private Label viewExtensions;
  @FXML
  private Label viewConfig;

  @FXML
  private Button deleteButtonInView;
  //  @FXML
//  private Button deleteButtonInEdit;
//  @FXML
//  private Button saveButton;
  @FXML
  private Button editButton;

  /* Status */
  private boolean saved;
  private boolean newCheck;

  /* The Conformance checker configuration */
  private ConformanceConfig config;

  private void initDefault() {

  }

  // NEW
  public void init() {
    initDefault();

    // Empty conformance checker
    saved = false;
    newCheck = true;
  }

  // LOAD
  public void init(ConformanceConfig cc) {
    initDefault();
    config = cc;

    // Load periodical check
    saved = true;
    newCheck = false;

    printViewMode();
  }

  @FXML
  protected void editClicked(ActionEvent event) throws Exception {
//    // Load configurations
//    loadConfigurations();
//    loadPeriodicity();
//
//    // Show
//    NodeUtil.hideNode(gridView);
//    NodeUtil.showNode(gridEdit);
//    saved = false;
  }

  @FXML
  protected void deleteClicked(ActionEvent event) throws Exception {
//    showLoadingDelete();
//    if (newCheck) {
//      // Only from GUI
//      context.send(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, getUuid(), true));
//    } else {
//      // Delete from OS tasks
//      context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, getUuid()));
//    }
  }

  private void printViewMode() {
    viewName.setText(config.getName());
    viewExtensions.setText(prettyPrintExtensions(config.getExtensions()));
    viewConfig.setText(config.getConfiguration());
  }

  private String prettyPrintExtensions(List<String> extensions){
    String result = "";
    for (String ext : extensions){
      result += ext + ", ";
    }
    if (!result.isEmpty()){
      result = result.substring(0, result.length()-2);
    }
    return result;
  }

  /**
   * Loadings
   */
//  private void showLoadingSave() {
//    NodeUtil.showNode(saveLoading);
//    hideButtons();
//  }
//
//  private void showLoadingDelete() {
//    if (gridView.isVisible()) {
//      NodeUtil.showNode(deleteLoadingInView);
//    } else {
//      NodeUtil.showNode(deleteLoadingInEdit);
//    }
//    hideButtons();
//  }
//
//  public void hideLoading() {
//    NodeUtil.hideNode(saveLoading);
//    NodeUtil.hideNode(deleteLoadingInEdit);
//    NodeUtil.hideNode(deleteLoadingInView);
//    showButtons();
//  }
//
//  private void showButtons() {
//    NodeUtil.showNode(saveButton);
//    NodeUtil.showNode(editButton);
//    NodeUtil.showNode(deleteButtonInEdit);
//    NodeUtil.showNode(deleteButtonInView);
//  }
//
//  private void hideButtons() {
//    NodeUtil.hideNode(saveButton);
//    NodeUtil.hideNode(editButton);
//    NodeUtil.hideNode(deleteButtonInEdit);
//    NodeUtil.hideNode(deleteButtonInView);
//  }

  /**
   * Getters
   */
  public boolean isSaved() {
    return saved;
  }

}
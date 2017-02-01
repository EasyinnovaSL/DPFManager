/**
 * <h1>InteropFragment.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.interoperability.core.ConformanceConfig;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityMessage;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityResponseMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_INTEROP,
    viewLocation = "/fxml/fragments/interop.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class InteropFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  // Grid panes
  @FXML
  private GridPane gridView;
  @FXML
  private GridPane gridEdit;
  @FXML
  private GridPane gridEditInternalLeft;
  @FXML
  private GridPane gridEditInternalRight;
  @FXML
  private GridPane gridEditExternalLeft;
  @FXML
  private GridPane gridEditExternalRight;

  // View elements
  @FXML
  private Label viewName;
  @FXML
  private Label viewExtensions;
  @FXML
  private Label viewType;
  @FXML
  private Label viewConfig;

  // Buttons
  @FXML
  private Button deleteButtonInView;
  @FXML
  private Button deleteButtonInEdit;
  @FXML
  private Button saveButton;
  @FXML
  private Button editButton;

  // Text fields
  @FXML
  private TextField nameField;
  @FXML
  private TextField pathField;
  @FXML
  private TextField paramsField;
  @FXML
  private TextField extField;
  @FXML
  private TextField configField;
  @FXML
  private Label labelName;

  @FXML
  private ComboBox configChoice;


  // Loadings
  @FXML
  private ProgressIndicator saveLoading;
  @FXML
  private ProgressIndicator deleteLoadingInEdit;
  @FXML
  private ProgressIndicator deleteLoadingInView;

  // Enable check box
  @FXML
  private CheckBox enableInView;
  @FXML
  private CheckBox enableInEdit;

  /* Status */
  private boolean saved;
  private boolean saving;
  private boolean isBuiltIn;

  /* The Conformance checker configuration */
  private ConformanceConfig config;
  private String uuid;

  /* Current configs */
  private List<String> currentConfigs;

  /* imported configs */
  private List<String> importedConfigs;

  private void initDefault() {
    currentConfigs = new ArrayList<>();
    importedConfigs = new ArrayList<>();
    saving = false;
  }

  // NEW
  public void init(String id) {
    initDefault();
    uuid = id;
    enableInEdit.setSelected(true);

    // Empty conformance checker
    saved = false;
    isBuiltIn = false;

    hideLoading();
    showExternalFields();
    NodeUtil.hideNode(gridView);
    NodeUtil.showNode(gridEdit);
  }

  // LOAD
  public void init(ConformanceConfig cc) {
    initDefault();
    config = cc;

    // Load conformance config
    saved = true;
    isBuiltIn = cc.isBuiltIn();

    hideLoading();
    NodeUtil.hideNode(gridEdit);
    NodeUtil.showNode(gridView);

    printViewMode();
  }

  @FXML
  protected void editClicked(ActionEvent event) throws Exception {
    // Load configuration
    printEditMode();
    if (isBuiltIn) {
      showBuildInFields();
    } else {
      showExternalFields();
    }

    // Show
    NodeUtil.hideNode(gridView);
    NodeUtil.showNode(gridEdit);
    saved = false;
  }

  @FXML
  protected void deleteClicked(ActionEvent event) throws Exception {
    showLoadingDelete();
    if (config == null) {
      // Only from GUI
      context.send(GuiConfig.PERSPECTIVE_INTEROPERABILITY + "." + GuiConfig.COMPONENT_INTEROPERABILITY, new InteroperabilityResponseMessage(InteroperabilityResponseMessage.Type.REMOVE, true, getUuid()));
    } else {
      // Delete from saved conformances
      context.send(BasicConfig.MODULE_INTEROPERABILITY, new InteroperabilityMessage(InteroperabilityMessage.Type.REMOVE, config.getUuid()));
    }
  }

  @FXML
  protected void saveClicked(ActionEvent event) throws Exception {
    showLoadingSave();
    ConformanceConfig newConfig;
    if (isBuiltIn){
      newConfig = saveInternalConfiguration();
    } else {
      newConfig = saveExternalConfiguration();
    }
    if (newConfig != null) {
      config = newConfig;
      context.send(BasicConfig.MODULE_INTEROPERABILITY, new InteroperabilityMessage(InteroperabilityMessage.Type.GUIEDIT, config));
    } else {
      hideLoading();
    }
  }

  public void savedSuccess() {
    saving = false;
    saved = true;
    printViewMode();
    NodeUtil.showNode(gridView);
    NodeUtil.hideNode(gridEdit);
    hideLoading();
  }

  public void savedFailed() {
    saving = false;
    hideLoading();
  }

  public void enableFailed(){
    enableInView.setSelected(false);
  }

  @FXML
  protected void configurationChanged(ActionEvent event) throws Exception {
    if (configChoice.getValue() != null && configChoice.getValue().equals(bundle.getString("selectFromDisk"))) {
      selectConfiguration();
    }
  }

  @FXML
  protected void selectConfigClicked(ActionEvent event) throws Exception {
    String txtFile = selectInputFile();
    if (txtFile != null) {
      configField.setText(txtFile);
    }
  }

  @FXML
  protected void selectPathClicked(ActionEvent event) throws Exception {
    String txtFile = selectInputFile();
    if (txtFile != null) {
      pathField.setText(txtFile);
    }
  }

  @FXML
  protected void onChangeState(ActionEvent event) throws Exception {
    CheckBox currentCheck = (CheckBox) event.getSource();
    if (currentCheck.isSelected()) {
      context.send(BasicConfig.MODULE_INTEROPERABILITY, new InteroperabilityMessage(InteroperabilityMessage.Type.ENABLE, config.getUuid()));
    } else {
      context.send(BasicConfig.MODULE_INTEROPERABILITY, new InteroperabilityMessage(InteroperabilityMessage.Type.DISABLE, config.getUuid()));
    }
    if (config != null){
      config.setEnabled(currentCheck.isSelected());
    }
  }

  private String selectInputFile() {
    String txtFile = null;
    String configDir = DPFManagerProperties.getDefaultDirFile();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(bundle.getString("openFile"));
    fileChooser.setInitialDirectory(new File(configDir));
    File file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
    if (file != null && file.exists()) {
      txtFile = file.getAbsolutePath();
      String path = file.getParent();
      DPFManagerProperties.setDefaultDirFile(path);
    }
    return txtFile;
  }

  private void selectConfiguration() {
    String txtFile = null;
    String configDir = DPFManagerProperties.getDefaultDirConfig();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(bundle.getString("openFile"));
    fileChooser.setInitialDirectory(new File(configDir));
    File file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
    if (file != null) {
      txtFile = file.getAbsolutePath();
      if (file.exists() && file.getParent() != null && file.getParentFile().exists() && file.getParentFile().isDirectory()) {
        String path = file.getParent();
        DPFManagerProperties.setDefaultDirConfig(path);
      }
    }

    // Reload configs
    if (txtFile != null) {
      config.setConfiguration(txtFile);
      importedConfigs.add(txtFile);
      configChoice.getItems().clear();
      configChoice.getItems().addAll(currentConfigs);
      configChoice.getItems().addAll(importedConfigs);
      configChoice.getItems().add(bundle.getString("selectFromDisk"));
      configChoice.setValue(txtFile);
    } else {
      Platform.runLater(() -> configChoice.getSelectionModel().clearSelection());
    }
  }

  private void showBuildInFields() {
    NodeUtil.hideNode(gridEditExternalLeft);
    NodeUtil.hideNode(gridEditExternalRight);
    NodeUtil.showNode(gridEditInternalLeft);
    NodeUtil.showNode(gridEditInternalRight);
    loadConfigurations();
  }

  private void showExternalFields() {
    NodeUtil.showNode(gridEditExternalLeft);
    NodeUtil.showNode(gridEditExternalRight);
    NodeUtil.hideNode(gridEditInternalLeft);
    NodeUtil.hideNode(gridEditInternalRight);
  }

  private void loadConfigurations() {
    // Clear configurations
    currentConfigs.clear();
    configChoice.getItems().clear();

    // Add default
    configChoice.getItems().add(bundle.getString("default"));

    // Read configurations
    File folder = new File(DPFManagerProperties.getConfigDir());
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isFile()) {
        if (fileEntry.getName().toLowerCase().endsWith(".dpf")) {
          String name = fileEntry.getName().substring(0, fileEntry.getName().length() - 4);
          currentConfigs.add(name);
        }
      }
    }

    // Add current configs
    configChoice.getItems().addAll(currentConfigs);

    // Selected one
    if (config != null && config.getConfiguration() != null && !config.getConfiguration().isEmpty()) {
      if (!currentConfigs.contains(config.getConfiguration())) {
        // Add config from disk
        importedConfigs.add(config.getConfiguration());
        configChoice.getItems().add(config.getConfiguration());
      }
      configChoice.setValue(config.getConfiguration());
    } else {
      // Select default
      configChoice.setValue(bundle.getString("default"));
    }

    // Add select from disk
    configChoice.getItems().add(bundle.getString("selectFromDisk"));
  }

  private void printViewMode() {
    viewName.setText(config.getName());
    viewExtensions.setText(config.getPrettyExtensions());
    viewConfig.setText(prettyPrintConfig(config.getConfiguration()));
    viewType.setText(prettyPrintType(config.getPath()));
    enableInView.setSelected(config.isEnabled());
  }

  private void printEditMode() {
    nameField.setText(config.getName());
    labelName.setText(config.getName());
    pathField.setText(config.getPath());
    paramsField.setText(config.getParameters());
    extField.setText(config.getPrettyExtensions());
    enableInEdit.setSelected(config.isEnabled());
    configField.setText(config.getConfiguration());
  }

  private String prettyPrintType(String path) {
    if (path.equals("built-in")) {
      return bundle.getString("internal");
    }
    return bundle.getString("external");
  }

  private String prettyPrintConfig(String config) {
    if (config.isEmpty()) {
      return bundle.getString("default");
    }
    return config;
  }

  private ConformanceConfig saveExternalConfiguration() {
    ConformanceConfig newConfig = new ConformanceConfig();

    if (uuid != null){
      newConfig.setUuid(uuid);
    } else {
      newConfig.setUuid(config.getUuid());
    }

    if (nameField.getText().isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertMissing")));
      return null;
    }
    newConfig.setName(nameField.getText());

    if (pathField.getText().isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertMissing")));
      return null;
    }
    newConfig.setPath(pathField.getText());


    String config = configField.getText();
    // TODO change future
//    if (config.isEmpty()){
//      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertMissing")));
//      return null;
//    }
    newConfig.setConfiguration(config);

    if (paramsField.getText().isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertMissing")));
      return null;
    }
    newConfig.setParameters(paramsField.getText());

    String strExtensions = extField.getText().replaceAll(" ", "");
    if (strExtensions.isEmpty() || !strExtensions.matches("[a-zA-Z1-9,]+")) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertExtensions")));
      return null;
    }
    newConfig.setExtensions(Arrays.asList(strExtensions.split(",")));
    newConfig.setEnabled(enableInEdit.isSelected());
    return newConfig;
  }

  private ConformanceConfig saveInternalConfiguration() {
    ConformanceConfig newConfig = new ConformanceConfig(config);

    newConfig.setUuid(config.getUuid());

    String config = (String) configChoice.getValue();
    if (config == null || config.isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertMissing")));
      return null;
    }
    if (config.equals(bundle.getString("default"))){
      config = "";
    }
    newConfig.setConfiguration(config);

    newConfig.setEnabled(enableInEdit.isSelected());

    return newConfig;
  }

  /**
   * Loadings
   */
  private void showLoadingSave() {
    NodeUtil.showNode(saveLoading);
    hideButtons();
  }

  private void showLoadingDelete() {
    if (gridView.isVisible()) {
      NodeUtil.showNode(deleteLoadingInView);
    } else {
      NodeUtil.showNode(deleteLoadingInEdit);
    }
    hideButtons();
  }

  public void hideLoading() {
    NodeUtil.hideNode(saveLoading);
    NodeUtil.hideNode(deleteLoadingInEdit);
    NodeUtil.hideNode(deleteLoadingInView);
    showButtons();
  }

  private void showButtons() {
    NodeUtil.showNode(saveButton);
    NodeUtil.showNode(editButton);
    NodeUtil.showNode(deleteButtonInEdit);
    NodeUtil.showNode(deleteButtonInView);
  }

  //
  private void hideButtons() {
    NodeUtil.hideNode(saveButton);
    NodeUtil.hideNode(editButton);
    NodeUtil.hideNode(deleteButtonInEdit);
    NodeUtil.hideNode(deleteButtonInView);
  }

  /**
   * Getters
   */
  public boolean isSaved() {
    return saved;
  }

  public boolean isSaving() {
    return saving;
  }

  public String getName() {
    if (config == null){
      return "";
    }
    return config.getName();
  }

  public String getUuid() {
    if (config == null){
      return uuid;
    }
    return config.getUuid();
  }


}
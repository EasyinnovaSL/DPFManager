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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.common.TimeSpinner;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicCheck;
import dpfmanager.shell.modules.periodic.core.Periodicity;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.controlsfx.control.CheckComboBox;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_PERIODIC,
    viewLocation = "/fxml/fragments/periodic.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class PeriodicFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private GridPane gridView;
  @FXML
  private GridPane gridEdit;

  @FXML
  private Label viewInput;
  @FXML
  private Label viewConfig;
  @FXML
  private Label viewPeriod;

  @FXML
  private TextField inputText;
  @FXML
  private ComboBox comboChoice;
  @FXML
  private ComboBox configChoice;
  @FXML
  private HBox timeHbox;
  private TimeSpinner spinner;
  @FXML
  private RadioButton radioDaily;
  @FXML
  private RadioButton radioWeekly;
  @FXML
  private RadioButton radioMonthly;
  @FXML
  private ComboBox monthDay;
  @FXML
  private HBox hboxWeekly;
  private CheckComboBox weekDay;

  @FXML
  private ProgressIndicator saveLoading;
  @FXML
  private ProgressIndicator deleteLoadingInEdit;
  @FXML
  private ProgressIndicator deleteLoadingInView;

  @FXML
  private Button deleteButtonInView;
  @FXML
  private Button deleteButtonInEdit;
  @FXML
  private Button saveButton;
  @FXML
  private Button editButton;


  /* Periodical Check info */
  private PeriodicCheck info;

  /* Current configs */
  private List<String> currentConfigs;

  /* imported configs */
  private List<String> importedConfigs;

  /* Status */
  private boolean saved;
  private boolean newCheck;

  private void initDefault() {
    currentConfigs = new ArrayList<>();
    importedConfigs = new ArrayList<>();
  }

  public void init() {
    initDefault();

    // Empty periodical check
    saved = false;
    newCheck = true;
    info = new PeriodicCheck();

    loadInputType();
    loadConfigurations();
    loadPeriodicity();
    hideLoading();
    NodeUtil.hideNode(gridView);
    NodeUtil.showNode(gridEdit);

    weekDay.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        weekDay.requestFocus();
      }
    });
    monthDay.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        monthDay.requestFocus();
      }
    });
    comboChoice.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        comboChoice.requestFocus();
      }
    });
    configChoice.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        configChoice.requestFocus();
      }
    });

  }

  public void init(PeriodicCheck check) {
    initDefault();

    // Load periodical check
    saved = true;
    newCheck = false;
    info = check;

    loadInputType();
    hideLoading();
    NodeUtil.showNode(gridView);
    NodeUtil.hideNode(gridEdit);

    printViewMode();
  }

  @FXML
  protected void editClicked(ActionEvent event) throws Exception {
    // Load configurations
    loadConfigurations();
    loadPeriodicity();

    // Show
    NodeUtil.hideNode(gridView);
    NodeUtil.showNode(gridEdit);
    saved = false;
  }

  @FXML
  protected void deleteClicked(ActionEvent event) throws Exception {
    showLoadingDelete();
    if (newCheck) {
      // Only from GUI
    context.send(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, getUuid(), true));
    } else {
      // Delete from OS tasks
      context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, getUuid()));
    }
  }

  @FXML
  protected void saveClicked(ActionEvent event) throws Exception {
    showLoadingSave();
    if (savePeriodical()) {
      if (newCheck) {
        // Save new
        context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.SAVE, info));
      } else {
        // Edit old
        context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.EDIT, info));
      }
    } else {
      hideLoading();
    }
  }

  public void savedSuccess() {
    saved = true;
    newCheck = false;
    printViewMode();
    NodeUtil.showNode(gridView);
    NodeUtil.hideNode(gridEdit);
    hideLoading();
  }

  @FXML
  protected void selectFileClicked(ActionEvent event) throws Exception {
    selectInputAction();
  }

  @FXML
  protected void onChangeInputType(ActionEvent event) throws Exception {
    if (comboChoice.getValue() == bundle.getString("comboFile")) {
      inputText.setText(bundle.getString("selectFile"));
      NodeUtil.showNode(inputText);
    } else if (comboChoice.getValue() == bundle.getString("comboFolder")) {
      inputText.setText(bundle.getString("selectFolder"));
      NodeUtil.showNode(inputText);
    }
    selectInputAction();
  }

  @FXML
  protected void radioClicked(ActionEvent event) throws Exception {
    RadioButton currentRadio = (RadioButton) event.getSource();
    radioDaily.setSelected(false);
    radioWeekly.setSelected(false);
    radioMonthly.setSelected(false);
    weekDay.setDisable(!currentRadio.getId().equals("radioWeekly"));
    monthDay.setDisable(!currentRadio.getId().equals("radioMonthly"));
    currentRadio.setSelected(true);
  }

  @FXML
  protected void configurationChanged(ActionEvent event) throws Exception {
    if (configChoice.getValue() != null && configChoice.getValue().equals(bundle.getString("selectFromDisk"))) {
      selectConfiguration();
    }
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
      info.setConfiguration(txtFile);
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

  private void selectInputAction() {
    String txtFile = null;
    String configDir = DPFManagerProperties.getDefaultDirFile();
    if (comboChoice.getValue().equals(bundle.getString("comboFile"))) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(bundle.getString("openFile"));
      fileChooser.setInitialDirectory(new File(configDir));
      List<File> files = fileChooser.showOpenMultipleDialog(GuiWorkbench.getMyStage());
      if (files != null) {
        String sfiles = "";
        File last = null;
        for (File file : files) {
          if (sfiles.length() > 0) sfiles += ";";
          sfiles += file.getPath();
          last = file;
        }
        txtFile = sfiles;
        if (last.exists() && last.getParent() != null && last.getParentFile().exists() && last.getParentFile().isDirectory()) {
          String path = last.getParent();
          DPFManagerProperties.setDefaultDirFile(path);
        }
      }
    } else if (comboChoice.getValue().equals(bundle.getString("comboFolder"))) {
      DirectoryChooser folderChooser = new DirectoryChooser();
      folderChooser.setTitle(bundle.getString("openFolder"));
      folderChooser.setInitialDirectory(new File(configDir));
      File directory = folderChooser.showDialog(GuiWorkbench.getMyStage());
      if (directory != null) {
        txtFile = directory.getPath();
        DPFManagerProperties.setDefaultDirFile(directory.getPath());
      }
    }
    if (txtFile != null) {
      inputText.setText(txtFile);
    }
  }

  private void loadInputType() {
    // Add input types
    if (comboChoice.getItems().size() < 2) {
      comboChoice.getItems().add(bundle.getString("comboFile"));
      comboChoice.getItems().add(bundle.getString("comboFolder"));
      comboChoice.setValue(bundle.getString("comboFile"));
    }
    if (info.getInput() != null) {
      inputText.setText(info.getInput());
    }
  }

  private void loadConfigurations() {
    // Read configurations
    currentConfigs.clear();
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
    configChoice.getItems().clear();
    configChoice.getItems().addAll(currentConfigs);

    // Selected one
    if (info.getConfiguration() != null) {
      if (!currentConfigs.contains(info.getConfiguration())) {
        // Add config from disk
        importedConfigs.add(info.getConfiguration());
        configChoice.getItems().add(info.getConfiguration());
      }
      configChoice.setValue(info.getConfiguration());
    }

    // Add select from disk
    configChoice.getItems().add(bundle.getString("selectFromDisk"));


  }

  private void loadPeriodicity() {
    if (spinner == null) {
      if (info.getPeriodicity() != null) {
        spinner = new TimeSpinner(info.getPeriodicity().getTime());
      } else {
        spinner = new TimeSpinner();
      }
      timeHbox.getChildren().add(spinner);
    }

    if (hboxWeekly.getChildren().size() == 2) {
      weekDay = new CheckComboBox();
      weekDay.getStyleClass().addAll("combo-box-white", "dpf-bar");
      weekDay.setDisable(true);
      weekDay.setMinWidth(150);
      weekDay.setPrefWidth(150);
      weekDay.setMaxWidth(150);
      HBox.setMargin(weekDay, new Insets(0, 0, 0, 5));
      weekDay.getItems().addAll(bundle.getString("monday"), bundle.getString("tuesday"), bundle.getString("wednesday"), bundle.getString("thursday"), bundle.getString("friday"), bundle.getString("saturday"), bundle.getString("sunday"));
      hboxWeekly.getChildren().add(weekDay);

    }

    if (monthDay.getItems().isEmpty()) {
      for (int i = 1; i < 29; i++) {
        monthDay.getItems().add(i);
      }
      monthDay.setValue(1);
    }

    if (info.getPeriodicity() != null) {
      Periodicity periodicity = info.getPeriodicity();
      if (!periodicity.getDaysOfWeek().isEmpty()) {
        radioWeekly.setSelected(true);
        weekDay.setDisable(false);
        for (Integer day : periodicity.getDaysOfWeek()) {
          weekDay.getCheckModel().check(day - 1);
        }
      } else if (periodicity.getDayOfMonth() != null && periodicity.getDayOfMonth() > 0) {
        radioMonthly.setSelected(true);
        monthDay.setValue(periodicity.getDayOfMonth());
        monthDay.setDisable(false);
      } else {
        radioDaily.setSelected(true);
      }
    }
  }

  private boolean savePeriodical() {
    // Check input
    String input = inputText.getText();
    for (String file : input.split(";")) {
      if (!new File(file).exists()) {
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertFile")));
        return false;
      }
    }
    info.setInput(input);

    // Check configuration
    String value = (String) configChoice.getValue();
    if (value == null || value.isEmpty()) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertConfigFile")));
      return false;
    } else {
      info.setConfiguration(value);
    }

    // Periodicity
    if (radioDaily.isSelected()) {
      info.setPeriodicity(new Periodicity(Periodicity.Mode.DAILY, spinner.getValue()));
    } else if (radioWeekly.isSelected()) {
      Periodicity periodicity = new Periodicity(Periodicity.Mode.WEEKLY, spinner.getValue());
      List<Integer> days = getDaysOfWeek();
      if (!days.isEmpty()) {
        periodicity.setDaysOfWeek(days);
        info.setPeriodicity(periodicity);
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertPeriodicityWeek")));
        return false;
      }
    } else if (radioMonthly.isSelected()) {
      Periodicity periodicity = new Periodicity(Periodicity.Mode.MONTHLY, spinner.getValue());
      periodicity.setDayOfMonth((Integer) monthDay.getValue());
      info.setPeriodicity(periodicity);
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertPeriodicity")));
      return false;
    }
    return true;
  }

  private List<Integer> getDaysOfWeek() {
    List<Integer> list = new ArrayList<>();
    for (Object obj : weekDay.getCheckModel().getCheckedIndices()) {
      Integer index = (Integer) obj;
      list.add(index + 1);
    }
    return list;
  }

  private void printViewMode() {
    viewInput.setText(info.getInput());
    viewConfig.setText(info.getConfiguration());
    if (info.getPeriodicity() != null) {
      viewPeriod.setText(info.getPeriodicity().toString(bundle));
    } else {
      viewPeriod.setText("");
    }
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

  private void hideButtons() {
    NodeUtil.hideNode(saveButton);
    NodeUtil.hideNode(editButton);
    NodeUtil.hideNode(deleteButtonInEdit);
    NodeUtil.hideNode(deleteButtonInView);
  }

  /**
   * Getters
   */

  public String getUuid() {
    return info.getUuid();
  }

  public boolean isSaved() {
    return saved;
  }

}
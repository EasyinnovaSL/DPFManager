package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.messages.PeriodicalMessage;
import dpfmanager.shell.interfaces.gui.component.periodical.PeriodicalController;
import dpfmanager.shell.interfaces.gui.component.periodical.Periodicity;
import dpfmanager.shell.interfaces.gui.component.periodical.TimeSpinner;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.database.messages.CronMessage;
import dpfmanager.shell.modules.database.tables.Crons;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_PERIODIC,
    viewLocation = "/fxml/periodic.fxml",
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
  private ComboBox weekDay;
  @FXML
  private ComboBox monthDay;

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
  private String uuid;
  private String input;
  private String configuration;
  private Periodicity periodicity;

  /* Status */
  private boolean saved;
  private boolean editing;
  private boolean newC;
  private boolean delete;

  /* Controller */
  private PeriodicalController controller;

  public void init(PeriodicalController cont) {
    // Empty periodical check
    controller = cont;
    saved = false;
    editing = false;
    newC = true;
    delete = false;
    loadInputType();
    loadConfigurations();
    loadPeriodicity();
    hideLoading();
    NodeUtil.hideNode(gridView);
    NodeUtil.showNode(gridEdit);
  }

  public void init(PeriodicalController cont, Crons cron) {
    // Load periodical check
    controller = cont;
    uuid = cron.getId();
    this.saved = true;
    this.newC = false;
    this.editing = false;
    this.delete = false;
    this.input = cron.getInput();
    this.configuration = cron.getConfiguration();
    this.periodicity = new Periodicity(cron);

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
    editing = true;
  }

  @FXML
  protected void deleteClicked(ActionEvent event) throws Exception {
    showLoadingDelete();
    delete = true;
    if (newC) {
      // Only from GUI
      context.send(GuiConfig.COMPONENT_PERIODICAL, new PeriodicalMessage(PeriodicalMessage.Type.DELETE));
    } else {
      // Delete from OS task and BD
      if (controller.deletePeriodicalCheck(uuid)) {
        ArrayMessage am = new ArrayMessage();
        am.add(BasicConfig.MODULE_DATABASE, new CronMessage(CronMessage.Type.DELETE, uuid));
        am.add(GuiConfig.COMPONENT_PERIODICAL, new PeriodicalMessage(PeriodicalMessage.Type.DELETE));
        context.send(BasicConfig.MODULE_DATABASE, am);
      }
    }
    hideLoading();
  }

  @FXML
  protected void saveClicked(ActionEvent event) throws Exception {
    showLoadingSave();
    if (savePeriodical()) {
      if (uuid == null) {
        uuid = "dpf-" + System.currentTimeMillis();
      }
      if (controller.savePeriodicalCheck(uuid, input, configuration, periodicity, editing)) {
        // Everything ok
        saved = true;
        newC = false;
        printViewMode();
        NodeUtil.showNode(gridView);
        NodeUtil.hideNode(gridEdit);
        context.send(BasicConfig.MODULE_DATABASE, new CronMessage(CronMessage.Type.SAVE, uuid, input, configuration, periodicity));
      } else {
        // Error saving periodical check
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorSavePeriodic")));
      }
    }
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
    if (input != null) {
      inputText.setText(input);
    }
  }

  private void loadConfigurations() {
    // Add configurations
    configChoice.getItems().clear();
    File folder = new File(DPFManagerProperties.getConfigDir());
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isFile()) {
        if (fileEntry.getName().toLowerCase().endsWith(".dpf")) {
          String name = fileEntry.getName().substring(0, fileEntry.getName().length() - 4);
          configChoice.getItems().add(name);
        }
      }
    }
    if (configuration != null) {
      configChoice.setValue(configuration);
    }
  }

  private void loadPeriodicity() {
    if (spinner == null) {
      if (periodicity != null) {
        spinner = new TimeSpinner(periodicity.getTime());
      } else {
        spinner = new TimeSpinner();
      }
      timeHbox.getChildren().add(spinner);
    }

    if (weekDay.getItems().isEmpty()) {
      weekDay.getItems().addAll(bundle.getString("monday"), bundle.getString("tuesday"), bundle.getString("wednesday"), bundle.getString("thursday"), bundle.getString("friday"), bundle.getString("saturday"), bundle.getString("sunday"));
      weekDay.setValue(bundle.getString("monday"));
    }

    if (monthDay.getItems().isEmpty()) {
      for (int i = 1; i < 32; i++) {
        monthDay.getItems().add(i);
      }
      monthDay.setValue(1);
    }

    if (periodicity != null) {
      if (periodicity.getDayOfWeek() != null && periodicity.getDayOfWeek() > 0) {
        radioWeekly.setSelected(true);
        weekDay.setValue(periodicity.getDayOfWeekString(bundle));
        weekDay.setDisable(false);
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
    if (inputText.getText().equals(bundle.getString("selectFile")) || inputText.getText().equals(bundle.getString("selectFolder"))) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertFile")));
      return false;
    } else {
      input = inputText.getText();
    }

    // Check configuration
    String value = (String) configChoice.getValue();
    if (value == null) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertConfigFile")));
      return false;
    } else {
      configuration = value;
    }

    // Periodicity
    if (radioDaily.isSelected()) {
      periodicity = new Periodicity(Periodicity.Mode.DAILY, spinner.getValue());
    } else if (radioWeekly.isSelected()) {
      periodicity = new Periodicity(Periodicity.Mode.WEEKLY, spinner.getValue());
      periodicity.setDayOfWeek(getDayOfWeek());
      periodicity.setDayOfWeekString((String) weekDay.getValue());
    } else if (radioMonthly.isSelected()) {
      periodicity = new Periodicity(Periodicity.Mode.MONTHLY, spinner.getValue());
      periodicity.setDayOfMonth((Integer) monthDay.getValue());
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertPeriodicity")));
      return false;
    }
    return true;
  }

  private Integer getDayOfWeek() {
    int index = 0;
    boolean found = false;
    while (!found && index < 7) {
      String day = (String) weekDay.getItems().get(index);
      if (day.equals(weekDay.getValue())) {
        found = true;
      }
      index++;
    }
    return index;
  }

  private void printViewMode() {
    viewInput.setText(input);
    viewConfig.setText(configuration);
    viewPeriod.setText(parsePeriodicityInfo());
  }

  private String parsePeriodicityInfo() {
    if (periodicity != null) {
      switch (periodicity.getMode()) {
        case DAILY:
          return bundle.getString("dailyInfo").replace("%1", periodicity.getTimeString());
        case WEEKLY:
          return bundle.getString("weeklyInfo").replace("%1", periodicity.getDayOfWeekString(bundle)).replace("%2", periodicity.getTimeString());
        case MONTHLY:
          return bundle.getString("monthlyInfo").replace("%1", periodicity.getDayOfMonth().toString()).replace("%2", periodicity.getTimeString());
      }
    }
    return "";
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

  private void hideLoading() {
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

  public boolean isSaved() {
    return saved;
  }

  public boolean isEditing() {
    return editing;
  }

  public boolean isDelete() {
    return delete;
  }
}
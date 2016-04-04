package dpfmanager.shell.interfaces.gui.component.dessign;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.errorDialog.DefaultErrorDialog;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.handler.DefaultErrorDialogHandler;

import java.io.File;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_DESIGN,
    name = GuiConfig.COMPONENT_DESIGN,
    viewLocation = "/fxml/dessign.fxml",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_DESIGN)
public class DessignView extends DpfView<DessignModel, DessignController> {

  @Resource
  private Context context;

  @FXML
  private ComboBox comboChoice;
  @FXML
  private VBox loadingVbox;
  @FXML
  private ScrollPane configScroll;
  @FXML
  private TextField inputText;
  @FXML
  private Label lblLoading;

  private VBox vBoxConfig;
  private ToggleGroup group;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(AlertMessage.class)){
      AlertMessage am = message.getTypedMessage(AlertMessage.class);
      RadioButton radio = getSelectedConfig();
      if (radio != null && am.hasResult() && am.getResult()){
        getController().performDeleteConfigAction(radio.getText());
      }
    }
    return null;
  }

  @Override
  public Context getContext(){
    return context;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Set model and controller
    setModel(new DessignModel());
    setController(new DessignController());

    // hide loading
    hideLoading();

    // Add input types
    if (comboChoice.getItems().size() < 2) {
      comboChoice.setCursor(Cursor.HAND);
      comboChoice.setPrefWidth(10.0);
      comboChoice.setMaxWidth(10.0);
      comboChoice.setMinWidth(10.0);
      comboChoice.getItems().add("File");
      comboChoice.getItems().add("Folder");
      comboChoice.setValue("File");
    }

    // Add Config files
    addConfigFiles();
  }

  private void addConfigFiles(){
    group = new ToggleGroup();
    vBoxConfig = new VBox();
    vBoxConfig.setId("vBoxConfig");
    vBoxConfig.setSpacing(3);
    vBoxConfig.setPadding(new Insets(5));
    File folder = new File(DPFManagerProperties.getConfigDir());
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isFile()) {
        if (fileEntry.getName().toLowerCase().endsWith(".dpf")) {
          addConfigFile(fileEntry.getName());
        }
      }
    }

    configScroll.setContent(vBoxConfig);
  }

  public void addConfigFile(String text){
    RadioButton radio = new RadioButton();
    radio.setId("radioConfig" + vBoxConfig.getChildren().size());
    radio.setText(text);
    radio.setToggleGroup(group);
    vBoxConfig.getChildren().add(radio);
  }

  public void deleteSelectedConfig(){
    Toggle tog = group.getSelectedToggle();
    RadioButton rad = (RadioButton) tog;
    group.getToggles().remove(tog);
    vBoxConfig.getChildren().remove(rad);
  }

  public void showLoading(){
    loadingVbox.setVisible(true);
    loadingVbox.setManaged(true);
  }

  public void hideLoading(){
    loadingVbox.setVisible(false);
    loadingVbox.setManaged(false);
  }

  /** FXML Events Handlers */

  @FXML
  protected void selectFileClicked(ActionEvent event) throws Exception {
    getController().selectInputAction();
  }

  @FXML
  protected void checkFilesClicked(ActionEvent event) throws Exception {
    getController().mainCheckFiles();
  }

  @FXML
  protected void onChangeInputType(ActionEvent event) throws Exception {
    if (comboChoice.getValue() == "File"){
      inputText.setText("Select a file");
    } else if (comboChoice.getValue() == "Folder") {
      inputText.setText("Select a folder");
    }
    getController().selectInputAction();
  }

  @FXML
  protected void showFileInfo(ActionEvent event) throws Exception {
    String header = "The path to the files to check";
    String content = "This can be either a single file or a folder. Only the files with a valid TIF file extension will be processed.";
    getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, header, content));
  }

  @FXML
  protected void showConfigInfo(ActionEvent event) throws Exception {
    String header = "Configuration files define the options to check the files (ISO, report format and policy rules)";
    String content = "You can either create a new configuration file, import a new one from disk, or edit/delete one from the list";
    getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, header, content));
  }

  @FXML
  protected void newButtonClicked(ActionEvent event) throws Exception {
    ArrayMessage am = new ArrayMessage();
    am.add(GuiConfig.PERSPECTIVE_CONFIG, new UiMessage());
    am.add(GuiConfig.PERSPECTIVE_CONFIG + "." + GuiConfig.COMPONENT_CONFIG, new ConfigMessage(ConfigMessage.Type.NEW));
    getContext().send(GuiConfig.PERSPECTIVE_CONFIG, am);
  }

  @FXML
  protected void importButtonClicked(ActionEvent event) throws Exception {
    getController().performImportConfigAction();
  }

  @FXML
  protected void editButtonClicked(ActionEvent event) throws Exception {
//    getController().performEditConfigAction();
    getController().testAction();
  }

  @FXML
  protected void deleteButtonClicked(ActionEvent event) throws Exception {
    RadioButton radio = getSelectedConfig();
    if (radio != null) {
      AlertMessage am = new AlertMessage(AlertMessage.Type.CONFIRMATION, "Are you sure to delete the configuration file '" + radio.getText() + "'?", "The physical file in disk will be also removed");
      am.setTitle("Delete configuration file");
      getContext().send(BasicConfig.MODULE_MESSAGE, am);
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, "Please select a configuration file"));
    }
  }


  public RadioButton getSelectedConfig(){
    return (RadioButton) group.getSelectedToggle();
  }

  public ComboBox getComboChoice() {
    return comboChoice;
  }

  public TextField getInputText() {
    return inputText;
  }

  public Label getLblLoading() {
    return lblLoading;
  }

}

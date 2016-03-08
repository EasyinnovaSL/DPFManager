package dpfmanager.shell.interfaces.gui.component.dessign;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.mvc.DpfView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_DESSIGN,
    name = GuiConfig.COMPONENT_DESSIGN,
    viewLocation = "/fxml-jr/dessign.fxml",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_DESSIGN)
public class DessignView extends DpfView<DessignModel, DessignController> implements FXComponent {

  @Resource
  private Context context;

  @FXML
  private ComboBox comboChoice;
  @FXML
  private VBox loadingVbox;
  @FXML
  private ScrollPane configScroll;
  @FXML
  private Button selectButton;
  @FXML
  private TextField inputText;
  @FXML
  private Label lblLoading;
  @FXML
  private Button checkFilesButton;

  @FXML
  private Button fileInfoBut;
  @FXML
  private Button configInfoBut;

  @FXML
  private Button newButton;
  @FXML
  private Button importButton;
  @FXML
  private Button editButton;
  @FXML
  private Button deleteButton;

  private VBox vBoxConfig;
  private ToggleGroup group;

  @Override
  public Node handle(final Message<Event, Object> message) {
    return null;
  }

  @Override
  public Node postHandle(Node node, Message<Event, Object> message) {
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

    // Init buttons actions
    getController().initEventHandlers();
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

  /** Getters */

  public Button getDeleteButton() {
    return deleteButton;
  }

  public Button getEditButton() {
    return editButton;
  }

  public Button getImportButton() {
    return importButton;
  }

  public Button getNewButton() {
    return newButton;
  }

  public RadioButton getSelectedConfig(){
    return (RadioButton) group.getSelectedToggle();
  }

  public Button getFileInfoBut(){
    return fileInfoBut;
  }

  public Button getConfigInfoBut(){
    return configInfoBut;
  }

  public Button getSelectButton() {
    return selectButton;
  }

  public ComboBox getComboChoice() {
    return comboChoice;
  }

  public TextField getInputText() {
    return inputText;
  }

  public Button getCheckFilesButton() {
    return checkFilesButton;
  }

  public Label getLblLoading() {
    return lblLoading;
  }

}

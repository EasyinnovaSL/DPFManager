package dpfmanager.shell.interfaces.Gui.ui.dessign;

import dpfmanager.shell.interfaces.Gui.ui.bottom.BottomModel;
import dpfmanager.shell.interfaces.Gui.ui.main.MainModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import org.jrebirth.af.api.resource.fxml.FXMLItem;
import org.jrebirth.af.core.resource.Resources;
import org.jrebirth.af.core.resource.fxml.FXML;
import org.jrebirth.af.core.ui.DefaultView;

import java.io.File;

/**
 * Created by Adrià Llorens on 05/02/2016.
 */
public class DessignView extends DefaultView<DessignModel, ScrollPane, DessignController> {

  private static FXMLItem ROOT_EMBEDDED_FXML = Resources.create(new FXML("fxml-jr", "dessign"));

  // View elements
  private GridPane grid;
  private ScrollPane scrollPane;
  private Node rootNode;
  private AnchorPane pane0;
  private VBox loadingVbox;
  private Label mainTitle;
  private ToggleGroup group;
  private ScrollPane configScroll;
  private ComboBox comboInput;
  private Button newButton;
  private Button importButton;
  private Button editButton;
  private Button deleteButton;
  private Button configInfoBut;
  private Button fileInfoBut;
  private Button checkFilesButton;
  private Button selectButton;
  private VBox vBoxConfig;
  private TextField txtBox1;
  private Label labelLoading;

  public DessignView(final DessignModel model) {
    super(model);
  }

  @Override
  protected void initView() {
    // init view elements
    rootNode = getFXMLNode();
    group = new ToggleGroup();
    mainTitle = (Label) rootNode.lookup(".mainTitle");
    pane0 = (AnchorPane) rootNode.lookup("#pane0");
    loadingVbox = (VBox) rootNode.lookup("#loadingVbox");
    configScroll = (ScrollPane) rootNode.lookup("#configScroll");
    comboInput = (ComboBox) rootNode.lookup("#comboChoice");
    newButton = (Button) rootNode.lookup("#newButton");
    importButton = (Button) rootNode.lookup("#importButton");
    editButton = (Button) rootNode.lookup("#editButton");
    deleteButton = (Button) rootNode.lookup("#deleteButton");
    configInfoBut = (Button) rootNode.lookup("#configInfoBut");
    fileInfoBut = (Button) rootNode.lookup("#fileInfoBut");
    checkFilesButton = (Button) rootNode.lookup("#checkFilesButton");
    txtBox1 = (TextField) rootNode.lookup("#txtBox1");
    selectButton = (Button) rootNode.lookup("#selectButton");
    labelLoading = (Label) rootNode.lookup("#lblLoading");
  }

  @Override
  public void start() {
    try {
      grid = new GridPane();
      grid.getStyleClass().add("background-main");
      grid.setAlignment(Pos.TOP_CENTER);
      grid.add(getFXMLNode(),0,0);

      scrollPane = getRootNode();
      scrollPane.setFitToHeight(true);
      scrollPane.setFitToWidth(true);
      scrollPane.setMaxWidth(970);
      scrollPane.getStyleClass().add("background-main");
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setContent(grid);

      // Read config files
      addConfigFiles();

      // Add input types
      if (comboInput.getItems().size() < 2) {
        comboInput.setCursor(Cursor.HAND);
        comboInput.setPrefWidth(10.0);
        comboInput.setMaxWidth(10.0);
        comboInput.setMinWidth(10.0);
        comboInput.getItems().add("File");
        comboInput.getItems().add("Folder");
        comboInput.setValue("File");

        // Now init combo box handler
        getController().initComboBox();
      }

      // hide loading
      hideLoading();
    } catch (Exception ex) {
      ex.printStackTrace();
      getModel().getModel(MainModel.class).getErrorCommand().setMessage(ex.toString());
      getModel().getModel(MainModel.class).getErrorCommand().run();
    }
  }

  private void addConfigFiles(){
    vBoxConfig = new VBox();
    vBoxConfig.setId("vBoxConfig");
    vBoxConfig.setSpacing(3);
    vBoxConfig.setPadding(new Insets(5));
    File folder = new File(getModel().getModel(MainModel.class).getConfigDir());
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

  public void showLoading(){
    loadingVbox.setVisible(true);
    loadingVbox.setManaged(true);
    getModel().getModel(MainModel.class).showLoading();
  }

  public void hideLoading(){
    loadingVbox.setVisible(false);
    loadingVbox.setManaged(false);
    getModel().getModel(MainModel.class).hideLoading();
  }

  private Node getFXMLNode() {
    Node rootNode = ROOT_EMBEDDED_FXML.get().getNode();
    return rootNode;
  }

  public RadioButton getSelectedConfig(){
    return (RadioButton) group.getSelectedToggle();
  }

  public void deleteSelectedConfig(){
    Toggle tog = group.getSelectedToggle();
    RadioButton rad = (RadioButton) tog;
    group.getToggles().remove(tog);
    vBoxConfig.getChildren().remove(rad);
  }

  public Button getCheckFilesButton(){
    return checkFilesButton;
  }

  public Button getNewButton(){
    return newButton;
  }

  public Button getEditButton(){
    return editButton;
  }

  public Button getImportButton(){
    return importButton;
  }

  public Button getDeleteButton(){
    return deleteButton;
  }

  public Button getFileInfoBut(){
    return fileInfoBut;
  }

  public Button getConfigInfoBut(){
    return configInfoBut;
  }

  public TextField getInputText(){
    return txtBox1;
  }

  public Button getSelectButton(){
    return selectButton;
  }

  public ComboBox getComboInput(){
    return comboInput;
  }

  public ComboBox getChoiceType(){
    return comboInput;
  }

  public Label getLabelLoading(){
    return labelLoading;
  }
}

package dpfmanager.jrebirth.ui.firsttime;

import dpfmanager.jrebirth.ui.main.MainModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.jrebirth.af.api.resource.fxml.FXMLItem;
import org.jrebirth.af.core.resource.Resources;
import org.jrebirth.af.core.resource.fxml.FXML;
import org.jrebirth.af.core.ui.DefaultView;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
public class FirstTimeView extends DefaultView<FirstTimeModel, ScrollPane, FirstTimeController> {

  private static FXMLItem ROOT_EMBEDDED_FXML = Resources.create(new FXML("fxml-jr", "first"));

  private Node rootNode;
  private ScrollPane scrollPane;
  private Button proceedButton;

  // Form elements
  private TextField txtEmail;
  private TextField txtName;
  private TextField txtSurname;
  private TextField txtJob;
  private TextField txtOrganization;
  private TextField txtCountry;
  private TextArea txtWhy;
  private CheckBox chkSubmit;
  private CheckBox chkFeedback;

  public FirstTimeView(final FirstTimeModel model) {
    super(model);
  }

  @Override
  protected void initView() {
    rootNode = getFXMLNode();
    proceedButton = (Button) rootNode.lookup("#proceeed");
    txtEmail = (TextField) rootNode.lookup("#txtEmail");
    txtName = (TextField) rootNode.lookup("#txtName");
    txtSurname = (TextField) rootNode.lookup("#txtSurname");
    txtJob = (TextField) rootNode.lookup("#txtJob");
    txtOrganization = (TextField) rootNode.lookup("#txtOrganization");
    txtCountry = (TextField) rootNode.lookup("#txtCountry");
    txtWhy = (TextArea) rootNode.lookup("#txtWhy");
    chkSubmit = (CheckBox) rootNode.lookup("#chkSubmit");
    chkFeedback = (CheckBox) rootNode.lookup("#chkFeedback");
  }

  @Override
  public void start() {
    try {
      GridPane grid = new GridPane();
      grid.getStyleClass().add("background-main");
      grid.setAlignment(Pos.TOP_CENTER);
      grid.getChildren().add(getFXMLNode());

      scrollPane = getRootNode();
      scrollPane.setFitToHeight(true);
      scrollPane.setFitToWidth(true);
      scrollPane.setMaxWidth(970);
      scrollPane.getStyleClass().add("background-main");
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setContent(grid);
    } catch (Exception ex) {
      ex.printStackTrace();
      getModel().getModel(MainModel.class).getErrorCommand().setMessage(ex.toString());
      getModel().getModel(MainModel.class).getErrorCommand().run();
    }
  }

  public Button getProceedButton(){
    return proceedButton;
  }

  public TextField getTxtEmail() {
    return txtEmail;
  }

  public TextField getTxtName() {
    return txtName;
  }

  public TextField getTxtSurname() {
    return txtSurname;
  }

  public TextField getTxtJob() {
    return txtJob;
  }

  public TextField getTxtOrganization() {
    return txtOrganization;
  }

  public TextField getTxtCountry() {
    return txtCountry;
  }

  public TextArea getTxtWhy() {
    return txtWhy;
  }

  public CheckBox getChkSubmit() {
    return chkSubmit;
  }

  public CheckBox getChkFeedback() {
    return chkFeedback;
  }

  private Node getFXMLNode() {
    return ROOT_EMBEDDED_FXML.get().getNode();
  }

}
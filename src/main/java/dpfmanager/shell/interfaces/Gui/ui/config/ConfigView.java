package dpfmanager.shell.interfaces.Gui.ui.config;

import dpfmanager.shell.interfaces.Gui.ui.main.MainModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import org.jrebirth.af.api.resource.fxml.FXMLItem;
import org.jrebirth.af.core.resource.Resources;
import org.jrebirth.af.core.resource.fxml.FXML;
import org.jrebirth.af.core.ui.DefaultView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 08/02/2016.
 */
public class ConfigView extends DefaultView<ConfigModel, ScrollPane, ConfigController> {

  private static FXMLItem ROOT_EMBEDDED_FXML = Resources.create(new FXML("fxml-jr", "config"));

  // View elements
  private ScrollPane scrollPane;
  private Node rootNode;
  private List<Node> includedNodes;
  private List<Button> stepsButtons;
  private Button continueButton;
  private ImageView configArrow;
  private ImageView stepImage;
  private Label stepTitle;

  public ConfigView(final ConfigModel model) {
    super(model);
  }

  @Override
  protected void initView() {
    // init view elements
    rootNode = getFXMLNode();
    continueButton = (Button) rootNode.lookup("#continue");
    configArrow = (ImageView) rootNode.lookup("#configArrow");
    stepImage = (ImageView) rootNode.lookup("#stepImage");
    stepTitle = (Label) rootNode.lookup("#stepTitle");

    includedNodes = new ArrayList<>();
    stepsButtons = new ArrayList<>();
    for (int i = 1; i < 7; i++) {
      stepsButtons.add((Button) rootNode.lookup("#step" + i));
      includedNodes.add(rootNode.lookup("#included" + i));
    }
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

      getController().gotoConfig(1);
    } catch (Exception ex) {
      ex.printStackTrace();
      getModel().getModel(MainModel.class).getErrorCommand().setMessage(ex.toString());
      getModel().getModel(MainModel.class).getErrorCommand().run();
    }
  }

  private Node getFXMLNode() {
    Node rootNode = ROOT_EMBEDDED_FXML.get().getNode();
    return rootNode;
  }

  public void showSubConfig(int x) {
    if (x < 1 || x > 6) {
      System.err.println("Requested show sub config out of bounds!");
      return;
    }

    for (Node node : includedNodes) {
      node.setVisible(false);
      node.setManaged(false);
    }

    Node current = includedNodes.get(x - 1);
    current.setVisible(true);
    current.setManaged(true);
  }

  public void setStepsBlue(int x) {
    if (x < 1 || x > 6) {
      System.err.println("Requested step button out of bounds!");
      return;
    }

    for (int i = 1; i < 7; i++) {
      stepsButtons.get(i - 1).getStyleClass().remove("blue-but");
      stepsButtons.get(i - 1).getStyleClass().remove("grey-but");
      if (i <= x) {
        stepsButtons.get(i - 1).getStyleClass().add("blue-but");
      } else {
        stepsButtons.get(i - 1).getStyleClass().add("grey-but");
      }
    }
  }

  public void setConfigArrowTranslate(int x) {
    if (x == 1) {
      configArrow.setTranslateX(-35);
    } else {
      configArrow.setTranslateX(20 + (x - 2) * 55);
    }
  }

  public void changeStepTitle(int x){
    switch (x){
      case 1:
        stepTitle.setText("IMPLEMENTATION CHECKER");
        break;
      case 2:
        stepTitle.setText("POLICY CHECKER");
        break;
      case 3:
        stepTitle.setText("REPORT");
        break;
      case 4:
        stepTitle.setText("METADATA FIXER");
        break;
      case 5:
        stepTitle.setText("PERIODICAL CHECKS");
        break;
      case 6:
        stepTitle.setText("CONFIGURATION SUMMARY");
        break;
    }

    Image img = new Image("file:src/main/resources/images/tab-buttons/tab-"+x+"-blue.png");
    stepImage.setImage(img);
  }

  public void setContinueButtonTest(int x){
    if (x == 6) {
      continueButton.setText("Save configuration");
    } else {
      continueButton.setText("Continue");
    }
  }

  /* Elements Getters */

  public Button getStepButton(int x) {
    if (x < 1 || x > 6) {
      System.err.println("Requested step button out of bounds!");
      return null;
    }
    return stepsButtons.get(x - 1);
  }

  public Button getContinueButton() {
    return continueButton;
  }

  public boolean currentStep(String id){
    return rootNode.lookup("#"+id) != null;
  }

}

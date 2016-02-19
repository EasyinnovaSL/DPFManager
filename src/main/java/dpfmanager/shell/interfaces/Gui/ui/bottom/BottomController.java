package dpfmanager.shell.interfaces.Gui.ui.bottom;

import dpfmanager.shell.interfaces.Gui.reimplemented.Console;
import dpfmanager.shell.interfaces.Gui.ui.main.MainModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.jrebirth.af.api.ui.View;
import org.jrebirth.af.core.ui.fxml.AbstractFXMLController;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 18/02/2016.
 */
public class BottomController extends AbstractFXMLController<BottomModel, View<BottomModel, ?, ?>> {

  @FXML
  private TextArea consoleArea;

  @FXML
  private SplitPane botSplit;

  @FXML
  private AnchorPane botAnchor;
  @FXML
  private AnchorPane consoleAnchor;
  @FXML
  private AnchorPane taskAnchor;
  @FXML
  private VBox vboxBottom;

  @FXML
  private StackPane consoleSeparator;
  @FXML
  private StackPane tasksSeparator;

  // Buttons
  @FXML
  private Button consoleButInConsole;
  @FXML
  private Button taskButInConsole;
  @FXML
  private Button consoleButInTask;
  @FXML
  private Button taskButInTask;

  @FXML
  private Button hideBottomTask;
  @FXML
  private Button hideBottomConsole;

  private Node divider;

  @Override
  public void initialize(final URL url, final ResourceBundle resource) {
    getModel().setController(this);

    // Connect default output to text area
    Console console = new Console(consoleArea);
    PrintStream ps = new PrintStream(console, true);
    System.setOut(ps);
    System.setErr(ps);

    setDefault();
  }

  private void setDefault() {
    hideNode(taskButInConsole);
    hideNode(consoleButInTask);
    hideNode(consoleSeparator);
    hideNode(tasksSeparator);
    hideNode(hideBottomConsole);
    showNode(hideBottomTask);
  }

  private void resetDefault() {
    setDefault();
    showDivider();
    showAnchor(taskAnchor);
    showAnchor(consoleAnchor);
    botSplit.setDividerPositions(0.7);
  }

  private void hideNode(Node node) {
    node.setVisible(false);
    node.setManaged(false);
  }

  private void showNode(Node node) {
    node.setVisible(true);
    node.setManaged(true);
  }

  private void hideAnchor(AnchorPane ap) {
    ap.setVisible(false);
    ap.setManaged(false);
    ap.setMinWidth(0.0);
    ap.setMaxWidth(0.0);
  }

  private void showAnchor(AnchorPane ap) {
    ap.setVisible(true);
    ap.setManaged(true);
    ap.setMinWidth(AnchorPane.USE_COMPUTED_SIZE);
    ap.setMaxWidth(AnchorPane.USE_COMPUTED_SIZE);
  }

  private void hideDivider(){
    if (!getDivider().getStyleClass().contains("hide-divider")){
      getDivider().getStyleClass().add("hide-divider");
    }
  }

  private void showDivider(){
    if (getDivider().getStyleClass().contains("hide-divider")){
      getDivider().getStyleClass().remove("hide-divider");
    }
  }

  // Console pane

  @FXML
  protected void closeConsolePane(ActionEvent event) throws Exception {
    saveDividerPos();
    hideAnchor(consoleAnchor);
    showNode(consoleButInTask);
    botSplit.setDividerPositions(0.0);
    hideDivider();
    if (!taskAnchor.isVisible()) {
      resetDefault();
      hideBottomPane();
    } else {
      showNode(hideBottomTask);
      showNode(tasksSeparator);
    }
  }

  @FXML
  protected void showTaskPane(ActionEvent event) throws Exception {
    showAnchor(taskAnchor);
    hideNode(taskButInConsole);
    hideNode(consoleSeparator);
    hideNode(tasksSeparator);
    botSplit.setDividerPositions(getModel().getDividerPos());
    showDivider();
    showNode(hideBottomTask);
    hideNode(hideBottomConsole);
  }

  // Tasks pane

  @FXML
  protected void showConsolePane(ActionEvent event) throws Exception {
    showAnchor(consoleAnchor);
    hideNode(consoleButInTask);
    hideNode(consoleSeparator);
    hideNode(tasksSeparator);
    botSplit.setDividerPositions(getModel().getDividerPos());
    showDivider();
  }

  @FXML
  protected void closeTaskPane(ActionEvent event) throws Exception {
    saveDividerPos();
    hideAnchor(taskAnchor);
    showNode(taskButInConsole);
    botSplit.setDividerPositions(1.0);
    hideDivider();
    if (!consoleAnchor.isVisible()) {
      resetDefault();
      hideBottomPane();
    } else {
      showNode(hideBottomConsole);
      showNode(consoleSeparator);
    }
  }

  private void saveDividerPos(){
    double pos = botSplit.getDividerPositions()[0];
    if (pos != 0.0 && pos != 1.0){
      getModel().setDividerPos(pos);
    }
  }

  private Node getDivider(){
    if (divider == null){
      divider = botSplit.lookup("#botSplit > .split-pane-divider");
    }
    return divider;
  }

  // All Pane

  @FXML
  protected void hidePane(ActionEvent event) throws Exception {
    hideBottomPane();
  }

  public void hideBottomPane() {
    getModel().getModel(MainModel.class).getView().hideBottomPane();
    botAnchor.setMinHeight(0);
    botAnchor.setMaxHeight(0);
    botAnchor.setVisible(false);
    botAnchor.setManaged(false);
  }

  public void showBottomPane() {
    getModel().getModel(MainModel.class).getView().showBottomPane();
    botAnchor.setMinHeight(AnchorPane.USE_COMPUTED_SIZE);
    botAnchor.setMaxHeight(AnchorPane.USE_COMPUTED_SIZE);
    botAnchor.setVisible(true);
    botAnchor.setManaged(true);
  }

}

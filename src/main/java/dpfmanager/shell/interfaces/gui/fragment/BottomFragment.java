package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.messages.ConsoleMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.core.config.GuiConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_BOTTOM,
    viewLocation = "/fxml/bottom.fxml",
    scope = Scope.SINGLETON)
public class BottomFragment {

  @Resource
  private Context context;

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

  private double pos;

  private double posV;

  private boolean firsttime = true;

  private boolean visible = false;

  private String currentPrespective;

  // Main functions

  public void init() {
    if (firsttime){
      setDefault();
      firsttime = false;
    }
  }

  public void setDefault() {
    pos = 0.7;
    NodeUtil.hideNode(taskButInConsole);
    NodeUtil.hideNode(consoleButInTask);
    NodeUtil.hideNode(consoleSeparator);
    NodeUtil.hideNode(tasksSeparator);
    NodeUtil.hideNode(hideBottomConsole);
    NodeUtil.showNode(hideBottomTask);
  }

  private void resetDefault() {
    setDefault();
    showDivider();
    NodeUtil.showAnchor(taskAnchor);
    NodeUtil.showAnchor(consoleAnchor);
    botSplit.setDividerPositions(pos);
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  // Console pane

  @FXML
  protected void closeConsolePane(ActionEvent event) throws Exception {
    saveDividerPos();
    NodeUtil.hideAnchor(consoleAnchor);
    NodeUtil.showNode(consoleButInTask);
    botSplit.setDividerPositions(0.0);
    hideDivider();
    if (!taskAnchor.isVisible()) {
      resetDefault();
      context.send(GuiConfig.COMPONENT_BOTTOM, "hide");
    } else {
      NodeUtil.showNode(hideBottomTask);
      NodeUtil.showNode(tasksSeparator);
    }
  }

  @FXML
  protected void showTaskPane(ActionEvent event) throws Exception {
    NodeUtil.showAnchor(taskAnchor);
    NodeUtil.hideNode(taskButInConsole);
    NodeUtil.hideNode(consoleSeparator);
    NodeUtil.hideNode(tasksSeparator);
    botSplit.setDividerPositions(pos);
    showDivider();
    NodeUtil.showNode(hideBottomTask);
    NodeUtil.hideNode(hideBottomConsole);
  }

  // Tasks pane

  @FXML
  protected void showConsolePane(ActionEvent event) throws Exception {
    NodeUtil.showAnchor(consoleAnchor);
    NodeUtil.hideNode(consoleButInTask);
    NodeUtil.hideNode(consoleSeparator);
    NodeUtil.hideNode(tasksSeparator);
    botSplit.setDividerPositions(pos);
    showDivider();
  }

  @FXML
  protected void closeTaskPane(ActionEvent event) throws Exception {
    saveDividerPos();
    NodeUtil.hideAnchor(taskAnchor);
    NodeUtil.showNode(taskButInConsole);
    botSplit.setDividerPositions(1.0);
    hideDivider();
    if (!consoleAnchor.isVisible()) {
      resetDefault();
      context.send(GuiConfig.COMPONENT_BOTTOM, "hide");
    } else {
      NodeUtil.showNode(hideBottomConsole);
      NodeUtil.showNode(consoleSeparator);
    }
  }

  private void saveDividerPos() {
    double aux = botSplit.getDividerPositions()[0];
    if (aux != 0.0 && aux != 1.0) {
      pos = aux;
    }
  }

  private Node getDivider() {
    if (divider == null) {
      divider = botSplit.lookup("#botSplit > .split-pane-divider");
    }
    return divider;
  }

  // All Pane

  @FXML
  protected void hidePane(ActionEvent event) throws Exception {
    context.send(currentPrespective, new ConsoleMessage(ConsoleMessage.Type.HIDE));
  }
  public void setCurrentPrespective(String id){
    currentPrespective = id;
  }

  private void hideDivider() {
    if (!getDivider().getStyleClass().contains("hide-divider")) {
      getDivider().getStyleClass().add("hide-divider");
    }
  }

  private void showDivider() {
    if (getDivider().getStyleClass().contains("hide-divider")) {
      getDivider().getStyleClass().remove("hide-divider");
    }
  }

  public double getPosV() {
    return posV;
  }

  public void setPosV(double posV) {
    this.posV = posV;
  }

}

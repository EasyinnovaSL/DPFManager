package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.core.util.NodeUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

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
  private AnchorPane botAnchor;
  @FXML
  private SplitPane botSplit;

  @FXML
  private AnchorPane consoleAnchor;
  @FXML
  private AnchorPane taskAnchor;

  @FXML
  private TextArea consoleArea;

  private Node divider;
  private double pos;
  private boolean firsttime = true;

  // Main functions

  public void init() {
    if (firsttime){
      setDefault();
      addDividerListener();
      sendConsoleHandler();
      firsttime = false;
    }
  }

  private void sendConsoleHandler(){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(consoleArea));
  }

  private void addDividerListener(){
//    for (Divider divider : botSplit.getDividers()){
//      divider.positionProperty().addListener(new ChangeListener<Number>() {
//        @Override
//        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//          botSplitBar.setDividerPositions((double) newValue);
//        }
//      });
//    }

//    for (Divider divider : botSplitBar.getDividers()){
//      divider.positionProperty().addListener(new ChangeListener<Number>() {
//        @Override
//        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//          botSplit.setDividerPositions((double) newValue);
//        }
//      });
//    }
  }

  public void setDefault() {
    pos = 0.7;
    NodeUtil.hideAnchor(consoleAnchor);
    NodeUtil.hideAnchor(taskAnchor);
  }

  public boolean isConsoleVisible(){
    return consoleAnchor.isVisible();
  }

  public boolean isTasksVisible(){
    return taskAnchor.isVisible();
  }

  /**
   * Main functions
   */
  public void showConsole(){
    NodeUtil.showAnchor(consoleAnchor);
    if (taskAnchor.isVisible()){
      showDivider();
    } else{
      hideDivider(1.0);
    }
  }

  public void hideConsole(){
    NodeUtil.hideAnchor(consoleAnchor);
    if (taskAnchor.isVisible()){
      hideDivider(0.0);
    }
  }

  public void showTasks(){
    NodeUtil.showAnchor(taskAnchor);
    if (consoleAnchor.isVisible()){
      showDivider();
    } else{
      hideDivider(0.0);
    }
  }

  public void hideTasks(){
    NodeUtil.hideAnchor(taskAnchor);
    if (consoleAnchor.isVisible()){
      hideDivider(1.0);
    }
  }

  /**
   * Divider functions
   */
  private void hideDivider(double pos) {
    saveDividerPos();
    if (getDivider().getStyleClass().contains("show-divider")) {
      getDivider().getStyleClass().remove("show-divider");
    }
    botSplit.setDividerPositions(pos);
  }

  private void showDivider() {
    if (!getDivider().getStyleClass().contains("show-divider")) {
      getDivider().getStyleClass().add("show-divider");
    }
    botSplit.setDividerPositions(pos);
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

}

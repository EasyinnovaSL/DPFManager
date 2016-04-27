package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.WidgetMessage;
import dpfmanager.shell.core.util.NodeUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_BAR,
    viewLocation = "/fxml/bottom-bar.fxml",
    scope = Scope.SINGLETON)
public class BarFragment {

  @Resource
  private Context context;

  @FXML
  private SplitPane botSplitBar;

  @FXML
  private HBox hboxConsole;
  @FXML
  private HBox hboxTask;

  // Buttons in Console
  @FXML
  private Button consoleButInConsole;
  @FXML
  private StackPane consoleSeparator;
  @FXML
  private Button taskButInConsole;

  // Buttons in Tasks
  @FXML
  private Button consoleButInTask;
  @FXML
  private StackPane tasksSeparator;
  @FXML
  private Button taskButInTask;

  private Node dividerBar;
  private double pos;
  private boolean firsttime = true;
  private boolean consoleVisible = false;
  private boolean tasksVisible = false;

  // Main functions

  public void init() {
    if (firsttime) {
      setDefault();
//      listenProperties();
      firsttime = false;
    }
  }

  public void setDefault() {
    NodeUtil.showNode(hboxConsole);
    NodeUtil.showNode(consoleButInConsole);
    NodeUtil.showNode(consoleSeparator);
    NodeUtil.showNode(taskButInConsole);
  }

  private void listenProperties(){
    botSplitBar.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        botSplitBar.setDividerPositions(1);
      }
    });
  }

  /**
   * Console pane
   */
  @FXML
  protected void showHideConsoleInConsole(ActionEvent event) throws Exception {
    if (consoleButInConsole.getStyleClass().contains("active")) {
      // Hide Console
      consoleVisible = false;
      consoleButInConsole.getStyleClass().remove("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.HIDE, WidgetMessage.Target.CONSOLE));
    } else {
      // Show console
      consoleVisible = true;
      consoleButInConsole.getStyleClass().add("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.SHOW, WidgetMessage.Target.CONSOLE));
    }
//    NodeUtil.hideNode(hboxConsole);
//    NodeUtil.showNode(consoleButInTask);
//    botSplitBar.setDividerPositions(0.0);
//    hideDivider();
  }

  public void showHideTasksInConsole() {
    try {
      showHideTasksInConsole(null);
    } catch (Exception e) {
    }
  }

  @FXML
  protected void showHideTasksInConsole(ActionEvent event) throws Exception {
    if (taskButInConsole.getStyleClass().contains("active")) {
      // Hide tasks
      tasksVisible = false;
      taskButInConsole.getStyleClass().remove("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.HIDE, WidgetMessage.Target.TASKS));
    } else {
      // Show tasks
      tasksVisible = true;
      taskButInConsole.getStyleClass().add("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.SHOW, WidgetMessage.Target.TASKS));
    }
//    NodeUtil.showNode(hboxTask);
//    NodeUtil.hideNode(taskButInConsole);
//    NodeUtil.hideNode(consoleSeparator);
//    NodeUtil.hideNode(tasksSeparator);
//    showDivider();
  }

  /**
   * Tasks pane
   */
  @FXML
  protected void showHideConsoleInTask(ActionEvent event) throws Exception {
    if (taskButInConsole.getStyleClass().contains("active")) {
      // Hide tasks
      taskButInConsole.getStyleClass().remove("active");
    } else {
      // Show tasks
      taskButInConsole.getStyleClass().add("active");
    }
//    NodeUtil.showNode(hboxConsole);
//    NodeUtil.hideNode(consoleButInTask);
//    NodeUtil.hideNode(consoleSeparator);
//    NodeUtil.hideNode(tasksSeparator);
//    showDivider();
  }

  @FXML
  protected void showHideTaskInTask(ActionEvent event) throws Exception {
//    NodeUtil.hideNode(hboxTask);
//    NodeUtil.showNode(taskButInConsole);
//    botSplitBar.setDividerPositions(1.0);
//    hideDivider();
  }

  /**
   * Divider
   */

  private Node getDividerBar() {
    if (dividerBar == null) {
      dividerBar = botSplitBar.lookup(".split-pane-divider");
    }
    return dividerBar;
  }

  private void saveDividerPos() {
    double aux = botSplitBar.getDividerPositions()[0];
    if (aux != 0.0 && aux != 1.0) {
      pos = aux;
    }
  }

  private void hideDivider() {
    saveDividerPos();
    if (getDividerBar().getStyleClass().contains("show-divider")) {
      getDividerBar().getStyleClass().remove("show-divider");
    }
  }

  private void showDivider() {
    if (!getDividerBar().getStyleClass().contains("show-divider")) {
      getDividerBar().getStyleClass().add("show-divider");
    }
    botSplitBar.setDividerPositions(pos);
  }

  /**
   * Extra functions
   */

  public boolean isVisible() {
    return consoleVisible || tasksVisible;
  }

  public boolean isTasksvisible() {
    return tasksVisible;
  }

}

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.modules.threading.core.FileCheck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private VBox taskVBox;

  @FXML
  private TextArea consoleArea;

  private Node divider;
  private double pos;
  private boolean firsttime = true;

  // The task fragments
  Map<Long, ManagedFragmentHandler<TaskFragment>> taskFragments;

  // Main functions

  public void init() {
    if (firsttime){
      setDefault();
      addDividerListener();
      sendConsoleHandler();
      taskFragments = new HashMap<>();
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
   * Tasks functions
   */
  public void addTask(ManagedFragmentHandler<TaskFragment> taskFragment, Jobs job){
    taskFragment.getController().init(job);
    taskFragments.put(job.getId(), taskFragment);
    taskVBox.getChildren().add(taskFragment.getFragmentNode());
  }

  public void updateTask(Jobs job){
    ManagedFragmentHandler<TaskFragment> taskFragment = taskFragments.get(job.getId());
    taskFragment.getController().updateJob(job);
  }

  public boolean containsJob(long uuid){
    return taskFragments.containsKey(uuid);
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

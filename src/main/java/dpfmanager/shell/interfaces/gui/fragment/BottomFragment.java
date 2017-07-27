/**
 * <h1>BottomFragment.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.modules.database.messages.CheckTaskMessage;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adria Llorens on 03/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_BOTTOM,
    viewLocation = "/fxml/fragments/bottom.fxml",
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
  public void addTask(ManagedFragmentHandler<TaskFragment> taskFragment, Jobs job, int pid){
    taskFragment.getController().init(job, pid);
    taskFragments.put(job.getId(), taskFragment);

    // Reverse order
    ObservableList<Node> children = FXCollections.observableArrayList(taskVBox.getChildren());
    Collections.reverse(children);
    children.add(taskFragment.getFragmentNode());
    Collections.reverse(children);
    taskVBox.getChildren().setAll(children);
  }

  public void updateTask(Jobs job){
    ManagedFragmentHandler<TaskFragment> taskFragment = taskFragments.get(job.getId());
    if (job.getState() == 3){
      removeTask(job.getId());
    } else {
      taskFragment.getController().updateJob(job);
    }
  }

  public void finishActions(CheckTaskMessage ctm){
    ManagedFragmentHandler<TaskFragment> taskFragment = taskFragments.get(ctm.getUuid());
    if (ctm.isPause()){
      taskFragment.getController().finishPause();
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.PAUSE, ctm.getUuid(), false, "default"));
    } else if (ctm.isCancel()){
      removeTask(ctm.getUuid());
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.CANCEL, ctm.getUuid(), false, "default"));
    }
  }

  private void removeTask(Long uuid){
    if (taskFragments.containsKey(uuid)) {
      taskVBox.getChildren().remove(taskFragments.get(uuid).getFragmentNode());
      taskFragments.remove(uuid);
    }
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

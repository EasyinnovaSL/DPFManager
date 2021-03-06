/**
 * <h1>TaskFragment.java</h1> <p> This program is free software: you can redistribute it
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
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.NavMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.logging.log4j.Level;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_TASK,
    viewLocation = "/fxml/fragments/task.fxml",
    scope = Scope.PROTOTYPE)
public class TaskFragment {

  @Resource
  private Context context;

  @FXML
  private AnchorPane task;
  @FXML
  private Label inputLabel;
  @FXML
  private Label timeLabel;
  @FXML
  private Label originLabel;
  @FXML
  private ImageView taskImage;
  @FXML
  private ImageView reportsImage;
  @FXML
  private ImageView resumePauseImage;
  @FXML
  private ImageView cancelImage;
  @FXML
  private ProgressBar progress;
  @FXML
  private HBox mainHbox;
  @FXML
  private VBox mainVbox;
  @FXML
  private ProgressIndicator loadingPause;
  @FXML
  private ProgressIndicator loadingCancel;

  private Jobs job;
  private Jobs lastJob;

  private int myPid;
  private String type;
  private String path;
  private boolean isPause;

  private final double opacity = 0.7;

  public void init(Jobs newJob, int pid) {
    job = newJob;
    myPid = pid;
    if (job.getState() == 2) {
      // Done job
      showDoneJob();
    } else if (job.getState() == 1) {
      // Running job
      showRunningJob();
    } else if (job.getState() == 0){
      // Pending job
      showPendingJob();
    } else if (job.getState() == 4){
      // Paused job
      showPausedJob();
    } else if (job.getState() == 5){
      // Empty Job
      showEmptyJob();
    }
    NodeUtil.hideNode(loadingCancel);
    NodeUtil.hideNode(loadingPause);

    // Hide actions if not my job
    if (job.getPid() != myPid){
      NodeUtil.hideNode(cancelImage);
      NodeUtil.hideNode(resumePauseImage);
    }

    // Common
    isPause = true;
    progress.setProgress(job.getProgress());
    inputLabel.setText(getReadableInput(job.getInput()));
    originLabel.setText("Task from " + getReadableOrigin(job.getOrigin()));
    cancelImage.setImage(new Image("images/cancel.png"));
    resumePauseImage.setImage(new Image("images/pause.png"));
    bindWidth();
  }

  public void updateJob(Jobs updatedJob) {
    lastJob = job;
    job = updatedJob;
    if (lastJob.getState() != 2 && job.getState() == 2) {
      // From other to done
      showDoneJob();
    } else if (lastJob.getState() != 5 && job.getState() == 5){
      // From other to empty
      showEmptyJob();
    } else if (lastJob.getState() == 0 && job.getState() != 0) {
      // From pending to other
      mainVbox.setOpacity(1.0);
      NodeUtil.showNode(resumePauseImage);
    }

    // Paused opacity
    if (lastJob.getState() != 4 && job.getState() == 4){
      // From other to paused
      timeLabel.setText("Paused");
      mainVbox.setOpacity(opacity);
      progress.setProgress(job.getProgress());
    } else if (lastJob.getState() == 4 && job.getState() != 4){
      // From paused to other
      mainVbox.setOpacity(1.0);
    }

    // Default running job
    if (job.getState() == 1) {
      timeLabel.setText(getReadableData(System.currentTimeMillis() - job.getInit()));
      progress.setProgress(job.getProgress());
    }

    // Hide actions if not my job
    if (job.getPid() != myPid){
      NodeUtil.hideNode(cancelImage);
      NodeUtil.hideNode(resumePauseImage);
    }
  }

  private void showDoneJob(){
    getReportsInfo();
    NodeUtil.hideNode(resumePauseImage);
    NodeUtil.hideNode(cancelImage);

    reportsImage.setImage(new Image("images/formats/report.png"));
    timeLabel.setText(getReadableData(job.getFinish() - job.getInit()));
    NodeUtil.showNode(reportsImage);
    NodeUtil.hideNode(taskImage);

    progress.setProgress(job.getProgress());
    progress.getStyleClass().remove("blue-bar");
    progress.getStyleClass().add("green-bar");
  }

  private void showEmptyJob(){
    getReportsInfo();
    NodeUtil.hideNode(resumePauseImage);
    NodeUtil.hideNode(cancelImage);
    NodeUtil.hideNode(taskImage);
    NodeUtil.hideNode(reportsImage);
    timeLabel.setText(getReadableData(0L));
    progress.setProgress(1.0);
    progress.getStyleClass().remove("blue-bar");
    progress.getStyleClass().add("green-bar");
  }

  private void showRunningJob(){
    NodeUtil.showNode(cancelImage);
    NodeUtil.showNode(resumePauseImage);
    NodeUtil.hideNode(taskImage);
    NodeUtil.hideNode(reportsImage);
    timeLabel.setText("0:00");
  }

  private void showPendingJob(){
    timeLabel.setText("Pending");
    mainVbox.setOpacity(opacity);
    NodeUtil.showNode(cancelImage);
    NodeUtil.hideNode(resumePauseImage);
    NodeUtil.hideNode(taskImage);
    NodeUtil.hideNode(reportsImage);
  }

  private void showPausedJob(){
    hideLoadingPause();
    hideLoadingCancel();
    NodeUtil.hideNode(taskImage);
    NodeUtil.hideNode(reportsImage);
    mainVbox.setOpacity(opacity);
    timeLabel.setText("Paused");
  }

  private String getReadableOrigin(String origin){
    if (origin.equals("CMD")){
      return "command line";
    } else if (origin.equals("GUI")){
      return "graphical interface";
    } else if (origin.equals("SERVER")){
      return "server";
    } else {
      return "unknown";
    }
  }

  private String getReadableData(Long millis) {
    return String.format("%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(millis),
        TimeUnit.MILLISECONDS.toMinutes(millis) -
            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
        TimeUnit.MILLISECONDS.toSeconds(millis) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
  }

  private String getReadableInput(String input){
    return input;
  }

  private void getReportsInfo() {
    type = "";
    path = job.getOutput();
  }

  @FXML
  private void showReport() {
    // Show check
    ArrayMessage am = new ArrayMessage();
    am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
    am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
    context.send(GuiConfig.PERSPECTIVE_SHOW, am);
  }

  @FXML
  private void showReportsTab() {
    // Show check
    ReportGui rg = new ReportGui(path);
    ArrayMessage am = new ArrayMessage();
    am.add(GuiConfig.PERSPECTIVE_GLOBAL, new UiMessage(UiMessage.Type.SHOW));
    am.add(GuiConfig.PERSPECTIVE_GLOBAL + "." + GuiConfig.COMPONENT_NAV, new NavMessage(NavMessage.Selected.RELOAD));
    am.add(GuiConfig.PERSPECTIVE_GLOBAL + "." + GuiConfig.COMPONENT_GLOBAL, new GuiGlobalMessage(GuiGlobalMessage.Type.INIT, rg));
    context.send(GuiConfig.PERSPECTIVE_GLOBAL, am);
  }

  @FXML
  private void resumePause() {
    if (isPause) {
      // Pause check
      showLoadingPause();
      mainVbox.setOpacity(opacity);
      resumePauseImage.setImage(new Image("images/resume.png"));
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.PAUSE, job.getId(), true, "default"));
    } else {
      // Resume check
      mainVbox.setOpacity(1.0);
      resumePauseImage.setImage(new Image("images/pause.png"));
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.RESUME, job.getId(), true,"default"));
    }
    isPause = !isPause;
  }

  @FXML
  private void cancel() {
    showLoadingCancel();
    if (!loadingPause.isVisible()) {
      NodeUtil.hideNode(resumePauseImage);
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Cancelled check: "+job.getInput()));
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.CANCEL, job.getId(), true, "default"));
    }
  }

  public void finishPause(){
    if (!loadingCancel.isVisible()) {
      hideLoadingPause();
    } else {
      NodeUtil.hideNode(loadingPause);
      cancel();
    }
  }

  /**
   * Show / Hide loadings
   */
  private void showLoadingPause() {
    NodeUtil.showNode(loadingPause);
    NodeUtil.hideNode(resumePauseImage);
  }

  private void hideLoadingPause() {
    NodeUtil.hideNode(loadingPause);
    NodeUtil.showNode(resumePauseImage);
  }

  private void showLoadingCancel() {
    NodeUtil.showNode(loadingCancel);
    NodeUtil.hideNode(cancelImage);
  }

  private void hideLoadingCancel(){
    NodeUtil.hideNode(loadingCancel);
    NodeUtil.showNode(cancelImage);
  }

  private boolean exists(String path) {
    return new File(path).exists();
  }

  private void bindWidth() {
    progress.prefWidthProperty().bind(mainHbox.widthProperty());
  }
}

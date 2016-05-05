package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import org.apache.logging.log4j.Level;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adrià Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_TASK,
    viewLocation = "/fxml/task.fxml",
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
  private ImageView resumePauseImage;
  @FXML
  private ImageView cancelImage;
  @FXML
  private ProgressBar progress;
  @FXML
  private HBox mainHbox;
  @FXML
  private ProgressIndicator loadingPause;
  @FXML
  private ProgressIndicator loadingCancel;

  private Jobs job;
  private int myPid;
  private String type;
  private String path;
  private boolean isPause;

  private boolean done;
  private boolean pending;

  public void init(Jobs newJob, int pid) {
    job = newJob;
    myPid = pid;
    done = false;
    if (job.getState() == 2) {
      // Done job
      showDoneJob();
    } else if (job.getState() == 1) {
      // Running job
      showRunningJob();
    } else {
      // Pending job
      showPendingJob();
    }
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
    job = updatedJob;
    if (job.getState() == 2) {
      // Done
      showDoneJob();
    } else if (job.getState() == 1) {
      // Running
      timeLabel.setText(getReadableData(System.currentTimeMillis() - job.getInit()));
      progress.setProgress(job.getProgress());
      if (pending){
        hideLoadingCancel();
        hideLoadingPause();
        pending = false;
      }
    }
  }

  private void showDoneJob(){
    if (!done) {
      getReportsInfo();
      NodeUtil.hideNode(resumePauseImage);
      NodeUtil.hideNode(cancelImage);
      if (!type.isEmpty()) {
        taskImage.setImage(new Image("images/format_" + type + ".png"));
        timeLabel.setText(getReadableData(job.getFinish() - job.getInit()));
        NodeUtil.showNode(taskImage);
      } else {
        NodeUtil.hideNode(taskImage);
      }
      progress.setProgress(job.getProgress());
      progress.getStyleClass().remove("blue-bar");
      progress.getStyleClass().add("green-bar");
      done = true;
    }
  }

  private void showRunningJob(){
    hideLoadingPause();
    hideLoadingCancel();
    NodeUtil.hideNode(taskImage);
    timeLabel.setText("0:00");
  }

  private void showPendingJob(){
    pending = true;
    timeLabel.setText("Pending");
    NodeUtil.hideNode(loadingCancel);
    NodeUtil.hideNode(loadingPause);
    NodeUtil.hideNode(cancelImage);
    NodeUtil.hideNode(resumePauseImage);
    NodeUtil.hideNode(taskImage);
  }

  private String getReadableOrigin(String origin){
    if (origin.equals("CMD")){
      return "command line";
    } else if (origin.equals("GUI")){
      return "graphical interface";
    } else if (origin.equals("SER")){
      return "server";
    } else {
      return "unknown";
    }
  }

  private String getReadableData(Long time) {
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss"); //MMM dd,yyyy HH:mm:ss
    return sdf.format(new Date(time));
  }

  private String getReadableInput(String input){
    return input;
  }

  private void getReportsInfo() {
    String filefolder = job.getOutput();

    String htmlPath = filefolder + "report.html";
    String xmlPath = filefolder + "summary.xml";
    String jsonPath = filefolder + "summary.json";
    String pdfPath = filefolder + "report.pdf";

    type = "";
    path = "";
    if (exists(htmlPath)) {
      type = "html";
      path = htmlPath;
    } else if (exists(xmlPath)) {
      type = "xml";
      path = xmlPath;
    } else if (exists(jsonPath)) {
      type = "json";
      path = jsonPath;
    } else if (exists(pdfPath)) {
      type = "pdf";
      path = pdfPath;
    }
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
  private void resumePause() {
    if (isPause) {
      // Pause check
      showLoadingPause();
      resumePauseImage.setImage(new Image("images/resume.png"));
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.PAUSE, job.getId(), true));
    } else {
      // Resume check
      resumePauseImage.setImage(new Image("images/pause.png"));
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.RESUME, job.getId(), true));
    }
    isPause = !isPause;
  }

  @FXML
  private void cancel() {
    showLoadingCancel();
    if (!loadingPause.isVisible()) {
      NodeUtil.hideNode(resumePauseImage);
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Cancelled check: "+job.getInput()));
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.CANCEL, job.getId(), true));
    }
  }

  public void finishPause(){
    if (!loadingCancel.isVisible()){
      hideLoadingPause();
    } else {
      NodeUtil.hideNode(loadingPause);
      cancel();
    }
  }

  /**
   * Show / Hide loadings
   */
  private void showLoadingPause(){
    NodeUtil.showNode(loadingPause);
    NodeUtil.hideNode(resumePauseImage);
  }

  private void hideLoadingPause(){
    NodeUtil.hideNode(loadingPause);
    NodeUtil.showNode(resumePauseImage);
  }

  private void showLoadingCancel(){
    NodeUtil.showNode(loadingCancel);
    NodeUtil.hideNode(cancelImage);
  }

  private void hideLoadingCancel(){
    NodeUtil.hideNode(loadingCancel);
    NodeUtil.showNode(cancelImage);
  }

  public boolean isFinished(){
    return job != null && job.getState() == 2;
  }

  private boolean exists(String path) {
    return new File(path).exists();
  }

  private void bindWidth() {
    progress.prefWidthProperty().bind(mainHbox.widthProperty());
  }
}

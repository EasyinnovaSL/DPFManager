package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.modules.database.tables.Jobs;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
  private Label inputLabel;
  @FXML
  private Label timeLabel;
  @FXML
  private Label originLabel;
  @FXML
  private ImageView taskImage;
  @FXML
  private ProgressBar progress;
  @FXML
  private HBox mainHbox;

  private Jobs job;
  private String type;
  private String path;

  public void init(Jobs newJob) {
    job = newJob;
    if (job.getState() == 2 && !progress.getStyleClass().contains("bar-done")) {
      // Done job
      showDoneJob();
    } else {
      // New job
      timeLabel.setText("0:00");
      NodeUtil.hideNode(taskImage);
    }
    // Common
    progress.setProgress(job.getProgress());
    inputLabel.setText(getReadableInput(job.getInput()));
    originLabel.setText("Task from " + getReasableOrigin(job.getOrigin()));
    bindWidth();
  }

  public void updateJob(Jobs updatedJob) {
    job = updatedJob;
    if (job.getState() == 2) {
      // Done
      showDoneJob();
    } else{
      // Update
      timeLabel.setText(getReadableData(System.currentTimeMillis() - job.getInit()));
    }
    // Common
    progress.setProgress(job.getProgress());
  }

  private void showDoneJob(){
    getReportsInfo();
    if (!type.isEmpty()) {
      taskImage.setImage(new Image("images/format_" + type + ".png"));
      taskImage.setCursor(Cursor.HAND);
      timeLabel.setText(getReadableData(job.getFinish() - job.getInit()));
      NodeUtil.showNode(taskImage);
    }
    progress.getStyleClass().remove("blue-bar");
    progress.getStyleClass().add("green-bar");
  }

  private String getReasableOrigin(String origin){
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
    ArrayMessage am = new ArrayMessage();
    am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
    am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
    context.send(GuiConfig.PERSPECTIVE_SHOW, am);
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

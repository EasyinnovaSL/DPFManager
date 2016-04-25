package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.threading.core.FileCheck;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.List;

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
  private Label input;
  @FXML
  private ProgressBar progress;
  @FXML
  private VBox vbox;

  private Jobs job;

  public void init(Jobs job) {
    if (job.getState() == 2 && !progress.getStyleClass().contains("bar-done")){
      progress.getStyleClass().add("bar-done");
    }
    progress.setProgress(job.getProgress());
    input.setText("Input: " + job.getInput());
    bindWidth();
  }

  public void updateJob(Jobs updatedJob) {
    job = updatedJob;
    if (job.getState() == 2 && !progress.getStyleClass().contains("bar-done")){
      progress.getStyleClass().add("bar-done");
    }
    progress.setProgress(job.getProgress());
  }

  @FXML
  private void showReport() {
    if (isFinished()) {
      String filefolder = job.getOutput();

      String htmlPath = filefolder + "report.html";
      String xmlPath = filefolder + "summary.xml";
      String jsonPath = filefolder + "summary.json";
      String pdfPath = filefolder + "report.pdf";

      String type = "";
      String path = "";
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

      ArrayMessage am = new ArrayMessage();
      am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
      am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
      context.send(GuiConfig.PERSPECTIVE_SHOW, am);
    }
  }

  private boolean exists(String path){
    return new File(path).exists();
  }

  private boolean isFinished() {
    return (job.getProcessedFiles() == job.getTotalFiles());
  }

  private void bindWidth() {
    progress.prefWidthProperty().bind(vbox.widthProperty());
  }
}

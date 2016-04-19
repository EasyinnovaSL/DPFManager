package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.threading.core.FileCheck;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

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

  private int current;
  private int total;
  private FileCheck fileCheck;

  public void init(FileCheck fc){
    progress.setProgress(0.0);
    input.setText("Input: " + fc.getInput());
    current = 0;
    total = fc.getTotal() + 1;
    fileCheck = fc;
    bindWidth();
  }

  public void updateProgressBar(){
    current++;
    progress.setProgress((current * 1.0) / (total * 1.0));
    if (current == total && !progress.getStyleClass().contains("bar-done")){
      progress.getStyleClass().add("bar-done");
    }
  }

  @FXML
  private void showReport(){
    if (isFinished()){
      String filefolder = fileCheck.getInternal();
      List<String> formats = fileCheck.getConfig().getFormats();

      String type = "";
      String path = "";
      if (formats.contains("HTML")) {
        type = "html";
        path = filefolder + "report.html";
      } else if (formats.contains("XML")) {
        type = "xml";
        path = filefolder + "summary.xml";
      } else if (formats.contains("JSON")) {
        type = "json";
        path = filefolder + "summary.json";
      } else if (formats.contains("PDF")) {
        type = "pdf";
        path = filefolder + "report.pdf";
      }

      ArrayMessage am = new ArrayMessage();
//      am.add(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
      am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
      am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
      context.send(GuiConfig.PERSPECTIVE_SHOW, am);
    }
  }

  private boolean isFinished(){
    return (current == total);
  }

  private void bindWidth(){
    progress.prefWidthProperty().bind(vbox.widthProperty());
  }
}

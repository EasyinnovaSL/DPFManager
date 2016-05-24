package dpfmanager.shell.modules.messages.core;

import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class AlertsManager {

  private static final String YES = "Yes";
  private static final String NO = "No";

  public static Alert createSimpleAlert(AlertMessage am) {
    Alert alert = new Alert(parseType(am.getType()));
    alert.setTitle(am.getTitle());
    alert.setHeaderText(am.getHeader());
    alert.setContentText(am.getContent());
    alert.initOwner(GuiWorkbench.getMyStage());
    return alert;
  }

  public static Alert createAskAlert() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Close requested");
    alert.setHeaderText("There are running file checks");
    alert.setContentText("Are you sure you want to close and cancel them?");
    ButtonType buttonNo = new ButtonType(NO, ButtonData.NO);
    ButtonType buttonYes = new ButtonType(YES, ButtonData.YES);
    alert.getButtonTypes().setAll(buttonNo, buttonYes);
    return alert;
  }

  public static Alert createConfirmationAlert(AlertMessage am) {
    Alert alert = new Alert(parseType(am.getType()));
    alert.setTitle(am.getTitle());
    alert.setHeaderText(am.getHeader());
    alert.setContentText(am.getContent());
    ButtonType buttonNo = new ButtonType(NO, ButtonData.NO);
    ButtonType buttonYes = new ButtonType(YES, ButtonData.YES);
    alert.getButtonTypes().setAll(buttonNo, buttonYes);
    return alert;
  }

  public static Alert createExceptionAlert(ExceptionMessage am) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(am.getTitle());
    alert.setHeaderText(am.getHeader());
    alert.setContentText(am.getContent());

    if (!am.isOutOfMemory()) {
      String exceptionText = getExceptionText(am.getException());
      Label label = new Label("The exception stacktrace was:");

      TextArea textArea = new TextArea(exceptionText);
      textArea.setEditable(false);
      textArea.setWrapText(true);
      textArea.setMaxWidth(Double.MAX_VALUE);
      textArea.setMaxHeight(Double.MAX_VALUE);
      GridPane.setVgrow(textArea, Priority.ALWAYS);
      GridPane.setHgrow(textArea, Priority.ALWAYS);

      GridPane expContent = new GridPane();
      expContent.setMaxWidth(Double.MAX_VALUE);
      expContent.add(label, 0, 0);
      expContent.add(textArea, 0, 1);

      // Set expandable Exception into the dialog pane.
      alert.getDialogPane().setExpandableContent(expContent);
    }

    alert.initOwner(GuiWorkbench.getMyStage());
    return alert;
  }

  public static String getExceptionText(Exception ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    return sw.toString();
  }

  public static String getThrowableText(Throwable th) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    th.printStackTrace(pw);
    return sw.toString();
  }

  private static Alert.AlertType parseType(AlertMessage.Type type) {
    if (type.equals(AlertMessage.Type.ERROR)) {
      return Alert.AlertType.ERROR;
    } else if (type.equals(AlertMessage.Type.WARNING) || type.equals(AlertMessage.Type.ALERT)) {
      return Alert.AlertType.WARNING;
    } else if (type.equals(AlertMessage.Type.INFO)) {
      return Alert.AlertType.INFORMATION;
    } else if (type.equals(AlertMessage.Type.CONFIRMATION)) {
      return Alert.AlertType.CONFIRMATION;
    }
    return null;
  }
}

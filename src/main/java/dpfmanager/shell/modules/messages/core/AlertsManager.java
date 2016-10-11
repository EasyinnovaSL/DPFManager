/**
 * <h1>AlertsManager.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.messages.core;

import dpfmanager.shell.core.DPFManagerProperties;
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
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class AlertsManager {

  public static Alert createSimpleAlert(AlertMessage am) {
    Alert alert = new Alert(parseType(am.getType()));
    alert.setTitle(am.getTitle());
    alert.setHeaderText(am.getHeader());
    alert.setContentText(am.getContent());
    alert.initOwner(GuiWorkbench.getMyStage());
    return alert;
  }

  public static Alert createAskAlert(String header, String content) {
    ResourceBundle bundle = DPFManagerProperties.getBundle();
    String YES = bundle.getString("yes");
    String NO = bundle.getString("no");
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(bundle.getString("askAlertClose"));
    alert.setHeaderText(header);
    alert.setContentText(content);
    ButtonType buttonNo = new ButtonType(NO, ButtonData.NO);
    ButtonType buttonYes = new ButtonType(YES, ButtonData.YES);
    alert.getButtonTypes().setAll(buttonNo, buttonYes);
    return alert;
  }

  public static Alert createConfirmationAlert(AlertMessage am) {
    ResourceBundle bundle = DPFManagerProperties.getBundle();
    String YES = bundle.getString("yes");
    String NO = bundle.getString("no");
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
      Label label = new Label(DPFManagerProperties.getBundle().getString("exAlertStacktrace"));

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

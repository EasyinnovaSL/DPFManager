package dpfmanager.shell.modules.messages;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.util.TextAreaAppender;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_MESSAGE,
    name = BasicConfig.MODULE_MESSAGE,
    active = true)
public class MessagesModule extends DpfModule {

  @Resource
  protected Context context;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(LogMessage.class)){
      tractLogMessage(dpfMessage.getTypedMessage(LogMessage.class));
    } else if (dpfMessage.isTypeOf(AlertMessage.class)){
      tractAlertMessage(dpfMessage.getTypedMessage(AlertMessage.class));
    }
  }

  private void tractLogMessage(LogMessage lm){
    if (lm.hasTextArea() && !TextAreaAppender.hasTextArea()){
      // Init text area handler
      TextAreaAppender.setTextArea(lm.getTextArea());
    }
    else {
      // Log message
      String clazz = lm.getMyClass().toString();
      clazz = clazz.substring(clazz.lastIndexOf(".") + 1, clazz.length());
      if (lm.getLevel().equals(Level.DEBUG)) {
        // use marker for custom pattern
        LogManager.getLogger(clazz).log(lm.getLevel(), MarkerManager.getMarker("PLAIN"), lm.getMessage());
      } else {
        // Default pattern
        LogManager.getLogger(clazz).log(lm.getLevel(), lm.getMessage());
      }
    }
  }

  private void tractAlertMessage(AlertMessage am){
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        Alert alert;
        if (am.getType().equals(AlertMessage.Type.EXCEPTION)){
          alert = createSimpleAlert(am);
        } else{
          alert = createExceptionAlert(am);
        }
        alert.show();
      }

      private Alert createSimpleAlert(AlertMessage am){
        Alert alert = new Alert(parseType(am.getType()));
        alert.setTitle(am.getTitle());
        alert.setHeaderText(am.getHeader());
        alert.setContentText(am.getContent());
        alert.initOwner(GuiWorkbench.getMyStage());
        return alert;
      }

      private Alert createExceptionAlert(AlertMessage am){
        Alert alert = new Alert(parseType(am.getType()));
        alert.setTitle(am.getTitle());
        alert.setHeaderText(am.getHeader());
        alert.setContentText(am.getContent());
        alert.initOwner(GuiWorkbench.getMyStage());
        return alert;
      }

    });
  }

  private Alert.AlertType parseType(AlertMessage.Type type){
    if (type.equals(AlertMessage.Type.ERROR) || type.equals(AlertMessage.Type.EXCEPTION)){
      return Alert.AlertType.ERROR;
    } else if (type.equals(AlertMessage.Type.WARNING) || type.equals(AlertMessage.Type.ALERT)){
      return Alert.AlertType.WARNING;
    } else if (type.equals(AlertMessage.Type.INFO)){
      return Alert.AlertType.INFORMATION;
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
  }

  @Override
  public Context getContext(){
    return context;
  }

}

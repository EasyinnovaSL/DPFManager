package dpfmanager.shell.modules.messages;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.CustomErrorHandler;
import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.util.TextAreaAppender;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.messages.core.AlertsManager;
import dpfmanager.shell.modules.messages.core.MessagesService;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.JacpExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
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

  @Autowired
  private MessagesService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(LogMessage.class)){
      tractLogMessage(dpfMessage.getTypedMessage(LogMessage.class));
    } else if (dpfMessage.isTypeOf(AlertMessage.class)){
      tractAlertMessage(dpfMessage.getTypedMessage(AlertMessage.class));
    } else if (dpfMessage.isTypeOf(ExceptionMessage.class)){
      tractExceptionMessage(dpfMessage.getTypedMessage(ExceptionMessage.class));
    } else if (dpfMessage.isTypeOf(JacpExceptionMessage.class)){
      tractGuiExceptionMessage(dpfMessage.getTypedMessage(JacpExceptionMessage.class));
    }
  }

  private void tractLogMessage(LogMessage lm){
    if (lm.hasTextArea() && !TextAreaAppender.hasTextArea()){
      // Init text area handler
      TextAreaAppender.setTextArea(lm.getTextArea());
      // Init JacpFX Error handler
      CustomErrorHandler.setContext(context);
    } else {
      service.logMessage(lm);
    }
  }

  private void tractExceptionMessage(ExceptionMessage em){
    // Show in console
    service.exceptionMessage(em);

    // Show alert
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        Alert alert = AlertsManager.createExceptionAlert(em);
        alert.show();
      }
    });
  }

  private void tractGuiExceptionMessage(JacpExceptionMessage jm){
    // Show in console
    service.systemOut(jm.getHeader());
    service.systemOut(jm.getThrowable().getMessage());
    service.systemOut(AlertsManager.getThrowableText(jm.getThrowable()));
  }

  private void tractAlertMessage(AlertMessage am){
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        Alert alert;
        // Create alert
        if (am.getType().equals(AlertMessage.Type.CONFIRMATION)) {
          alert = AlertsManager.createConfirmationAlert(am);
        } else {
          alert = AlertsManager.createSimpleAlert(am);
        }

        // Show alert
        if (!am.getType().equals(AlertMessage.Type.CONFIRMATION)) {
          alert.show();
        } else {
          Optional<ButtonType> result = alert.showAndWait();
          am.setResult(result.get().getButtonData().equals(ButtonBar.ButtonData.YES));
          context.send(am.getSourceId(), am);
        }
      }
    });
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
  }

  @Override
  public Context getContext(){
    return context;
  }

}

package dpfmanager.shell.modules.messages;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.messages.core.MessagesService;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_MESSAGE)
public class MessagesController extends DpfSpringController {

  @Autowired
  private MessagesService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(LogMessage.class)){
      service.logMessage(dpfMessage.getTypedMessage(LogMessage.class));
    } else if (dpfMessage.isTypeOf(ExceptionMessage.class)){
      service.exceptionMessage(dpfMessage.getTypedMessage(ExceptionMessage.class));
    } else if (dpfMessage.isTypeOf(AlertMessage.class)){
      service.alertMessage(dpfMessage.getTypedMessage(AlertMessage.class));
    }
  }

}

package dpfmanager.shell.modules.client;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.client.core.ClientService;
import dpfmanager.shell.modules.client.messages.RequestMessage;
import dpfmanager.shell.modules.client.messages.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_CLIENT)
public class ClientController extends DpfSpringController {

  @Autowired
  private ClientService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(RequestMessage.class)) {
      service.makeRequest(dpfMessage.getTypedMessage(RequestMessage.class));
    } else if (dpfMessage.isTypeOf(ResponseMessage.class)) {
      ResponseMessage rm = dpfMessage.getTypedMessage(ResponseMessage.class);
      service.parseResponse(rm);
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage dpfMessage) {
    return null;
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }

}

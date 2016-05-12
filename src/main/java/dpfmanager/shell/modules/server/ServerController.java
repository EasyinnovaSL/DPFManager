package dpfmanager.shell.modules.server;

import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.modules.server.core.ServerService;
import dpfmanager.shell.modules.server.messages.ServerMessage;
import dpfmanager.shell.modules.threading.core.ThreadingService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_SERVER)
public class ServerController extends DpfSpringController {

  @Autowired
  private ServerService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(ServerMessage.class)){
      service.startServer();
    }
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }

}

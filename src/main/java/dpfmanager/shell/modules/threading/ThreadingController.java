package dpfmanager.shell.modules.threading;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.threading.core.ThreadingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_THREADING)
public class ThreadingController extends DpfSpringController {

  @Autowired
  private ThreadingService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage){

  }

  @PostConstruct
  public void init( ) {
    service.setContext(new ConsoleContext(appContext));
  }

}

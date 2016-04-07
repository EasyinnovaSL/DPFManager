package dpfmanager.shell.modules.conformancechecker;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_CONFORMANCE)
public class ConformanceCheckerController extends DpfSpringController {

  @Autowired
  private ConformanceCheckerService service;

  @Override
  public void handleMessage(DpfMessage message) {

  }

  @PostConstruct
  public void init( ) {
    service.setContext(new ConsoleContext());
  }
}

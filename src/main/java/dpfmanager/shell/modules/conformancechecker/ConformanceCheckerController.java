package dpfmanager.shell.modules.conformancechecker;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_CONFORMANCE)
public class ConformanceCheckerController extends DpfSpringController {

  @Autowired
  private ConformanceCheckerService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage message) {
    if (message.isTypeOf(ConformanceMessage.class)){
      ConformanceMessage cm = message.getTypedMessage(ConformanceMessage.class);
      if (cm.isDelete()){
        service.deleteTmpFiles();
      } else if (!cm.isGui()) {
        service.setParameters(cm.getConfig(),cm.getRecursive(), cm.isSilence());
        service.startMultiCheck(cm.getFiles());
      }
    }
  }

  @PostConstruct
  public void init( ) {
    service.setContext(new ConsoleContext(appContext));
  }
}

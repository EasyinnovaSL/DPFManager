package dpfmanager.shell.modules.conformancechecker;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_CONFORMANCE)
public class ConformanceCheckerController extends DpfSpringController {

  @Autowired
  private ConformanceCheckerService service;

  @Autowired
  private ApplicationContext appContext;

  @Resource(name = "parameters")
  private Map<String, String> parameters;

  @Override
  public void handleMessage(DpfMessage message) {
    if (message.isTypeOf(ConformanceMessage.class)) {
      ConformanceMessage cm = message.getTypedMessage(ConformanceMessage.class);
      if (!cm.hasPaths()) {
        // From console
        Integer recursive = null;
        if (parameters.containsKey("-r")){
          recursive = Integer.parseInt(parameters.get("-r"));
        }
        service.setParameters(cm.getConfig(), recursive, null);
        service.initMultiProcessInputRun(cm.getFiles());
      } else {
        // From server
        String path = cm.getPath();
        String input = cm.getInput();
        if (!service.readConfig(path)) {
          // Error reading config file
        } else {
          service.setParameters(null, null, cm.getUuid());
          service.initProcessInputRun(input);
        }
      }
    } else if (message.isTypeOf(ProcessInputMessage.class)){
      service.tractProcessInputMessage(message.getTypedMessage(ProcessInputMessage.class));
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage message) {
    return null;
  }

  @PostConstruct
  public void init() {
    ConsoleContext context = new ConsoleContext(appContext);
    ConformanceChecker.setLogger(new DpfLogger(context, true));
    service.setContext(context);
  }
}

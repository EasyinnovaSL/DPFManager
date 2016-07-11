package dpfmanager.shell.modules.periodic;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicService;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;
import dpfmanager.shell.modules.timer.core.TimerService;
import dpfmanager.shell.modules.timer.messages.TimerMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Controller(BasicConfig.MODULE_PERIODICAL)
public class PeriodicController extends DpfSpringController {

  @Autowired
  private PeriodicService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(PeriodicMessage.class)) {
      PeriodicMessage pm = dpfMessage.getTypedMessage(PeriodicMessage.class);
      if (pm.isRead()){
        service.readPeriodicalChecksCmd();
      } else if (pm.isDelete()){
        service.deletePeriocicalCheck(pm.getUuid());
      } else if (pm.isSave()){
        service.savePeriocicalCheck(pm.getPeriodicCheck());
      } else if (pm.isEdit()){
        service.editPeriocicalCheck(pm.getPeriodicCheck());
      }
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage message) {
    return null;
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }
}

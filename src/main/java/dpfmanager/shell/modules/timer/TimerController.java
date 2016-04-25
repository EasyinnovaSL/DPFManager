package dpfmanager.shell.modules.timer;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.core.DatabaseService;
import dpfmanager.shell.modules.database.messages.DatabaseMessage;
import dpfmanager.shell.modules.timer.core.TimerService;
import dpfmanager.shell.modules.timer.messages.TimerMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Controller(BasicConfig.MODULE_TIMER)
public class TimerController extends DpfSpringController {

  @Autowired
  private TimerService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(TimerMessage.class)) {
      TimerMessage tm = dpfMessage.getTypedMessage(TimerMessage.class);
      if (tm.isPlay()){
        service.playTask(tm.getClazz());
      } else if (tm.isStop()){
        service.stopTask(tm.getClazz());
      } else if (tm.isFinish()){
        service.runTask(tm.getClazz());
      }
    }
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }
}

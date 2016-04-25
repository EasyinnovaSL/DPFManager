package dpfmanager.shell.modules.timer;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.core.DatabaseService;
import dpfmanager.shell.modules.database.messages.DatabaseMessage;
import dpfmanager.shell.modules.timer.core.TimerService;
import dpfmanager.shell.modules.timer.messages.TimerMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
@Component(id = BasicConfig.MODULE_TIMER,
    name = BasicConfig.MODULE_TIMER,
    active = true)
public class TimerModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private TimerService service;

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
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    service.setContext(new GuiContext(context));
  }

  @PreDestroy
  public void onPreDestroyComponent() {
  }

  @Override
  public Context getContext() {
    return context;
  }

}
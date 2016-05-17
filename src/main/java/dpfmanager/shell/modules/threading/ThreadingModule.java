package dpfmanager.shell.modules.threading;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.messages.messages.CloseMessage;
import dpfmanager.shell.modules.threading.core.ThreadingService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_THREADING,
    name = BasicConfig.MODULE_THREADING,
    active = true)
public class ThreadingModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private ThreadingService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(IndividualStatusMessage.class)) {
      IndividualStatusMessage sm = dpfMessage.getTypedMessage(IndividualStatusMessage.class);
      service.finishIndividual(sm.getIndividual(), sm.getUuid());
    } else if (dpfMessage.isTypeOf(GlobalStatusMessage.class)) {
      GlobalStatusMessage gm = dpfMessage.getTypedMessage(GlobalStatusMessage.class);
      service.handleGlobalStatus(gm, false);
    } else if (dpfMessage.isTypeOf(RunnableMessage.class)) {
      RunnableMessage rm = dpfMessage.getTypedMessage(RunnableMessage.class);
      service.run(rm.getRunnable(), rm.getUuid());
    } else if (dpfMessage.isTypeOf(ThreadsMessage.class)){
      ThreadsMessage tm = dpfMessage.getTypedMessage(ThreadsMessage.class);
      service.processThreadMessage(tm);
    } else if (dpfMessage.isTypeOf(CloseMessage.class)){
      service.closeRequested();
    }
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    service.setContext(new GuiContext(context));
  }

  @PreDestroy
  public void onPreDestroyComponent() {
    service.finish();
  }

  @Override
  public Context getContext() {
    return context;
  }

}

package dpfmanager.shell.modules.periodic;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicService;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;

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
@Component(id = BasicConfig.MODULE_PERIODICAL,
    name = BasicConfig.MODULE_PERIODICAL,
    active = true)
public class PeriodicModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private PeriodicService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(PeriodicMessage.class)) {
      PeriodicMessage pm = dpfMessage.getTypedMessage(PeriodicMessage.class);
      if (pm.isRead()){
        service.readPeriodicalChecksGui();
      } else if (pm.isDelete()){
        service.deletePeriocicalCheck(pm.getUuid());
      } else if (pm.isSave()){
        service.savePeriocicalCheck(pm.getPeriodicCheck());
      } else if (pm.isEdit()){
        service.editPeriocicalCheck(pm.getPeriodicCheck());
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
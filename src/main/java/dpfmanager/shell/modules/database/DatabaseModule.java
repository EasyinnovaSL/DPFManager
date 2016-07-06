package dpfmanager.shell.modules.database;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.core.DatabaseService;
import dpfmanager.shell.modules.database.messages.JobsMessage;

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
@Component(id = BasicConfig.MODULE_DATABASE,
    name = BasicConfig.MODULE_DATABASE,
    active = true)
public class DatabaseModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private DatabaseService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(JobsMessage.class)){
      service.handleJobsMessage(dpfMessage.getTypedMessage(JobsMessage.class));
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

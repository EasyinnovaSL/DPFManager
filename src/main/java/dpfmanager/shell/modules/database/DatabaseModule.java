package dpfmanager.shell.modules.database;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.core.DatabaseService;
import dpfmanager.shell.modules.database.messages.DatabaseMessage;
import dpfmanager.shell.modules.threading.core.ThreadingService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;

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
    if (dpfMessage.isTypeOf(DatabaseMessage.class)){
      DatabaseMessage dm = dpfMessage.getTypedMessage(DatabaseMessage.class);
      if (dm.isNew()){
        service.createJob(dm);
      } else if (dm.isUpdate()){
        service.updateJob(dm);
      } else if (dm.isFinish()){
        service.finishJob(dm);
      } else if (dm.isGet()){
        service.getJobs(dm);
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

package dpfmanager.shell.modules.timer.tasks;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.timer.core.DpfTask;

/**
 * Created by Adrià Llorens on 25/04/2016.
 */
public class JobsStatusTask extends DpfTask {

  @Override
  protected void handleContext(DpfContext context) {
    // Init
  }

  @Override
  public void perform() {
    context.send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.GET));
  }

}

package dpfmanager.shell.modules.threading.runnable;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public class TestRunnable extends DpfRunnable {

  private String message;

  public TestRunnable(String m){
    // No context yet
    message = m;
  }

  @Override
  public void handleContext(DpfContext context) {
  }

  @Override
  public void runTask() {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

}

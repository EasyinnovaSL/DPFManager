package dpfmanager.shell.modules.threading.runnable;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public class TestWaitRunnable extends DpfRunnable {

  private DpfContext context;

  public TestWaitRunnable(){
    // No context yet
  }

  @Override
  public void handleContext(DpfContext context) {
  }

  @Override
  public void runTask() {
    try {
      for (int i = 5; i > 0; i--) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Thread " + name + ": "+ i));
        Thread.sleep(1000);
      }
    } catch(Exception e){

    }
  }

}

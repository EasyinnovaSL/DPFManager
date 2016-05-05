package dpfmanager.shell.core.app;

import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

/**
 * Created by Adrià Llorens on 02/05/2016.
 */
public class TestRunnable extends DpfRunnable {
  @Override
  public void runTask() {
    int count = 0;
    while (true){
      System.out.println(count);
      count++;
//      Thread.sleep(500);
    }
  }

  @Override
  public void handleContext(DpfContext context) {

  }
}

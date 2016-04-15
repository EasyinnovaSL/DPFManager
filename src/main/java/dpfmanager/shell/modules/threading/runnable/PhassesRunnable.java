package dpfmanager.shell.modules.threading.runnable;

import dpfmanager.shell.core.context.DpfContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 14/04/2016.
 */
public class PhassesRunnable extends DpfRunnable {

  private List<DpfRunnable> firstList;
  private List<DpfRunnable> secondList;

  public PhassesRunnable(List<DpfRunnable> l1, List<DpfRunnable> l2){
    // No context yet
    firstList = l1;
    secondList = l2;
  }

  @Override
  public void handleContext(DpfContext c) {
  }

  @Override
  public void runTask() {
    // Launch all first runnables
    List<Thread> threads = new ArrayList<>();
    int i = 1;
    for (DpfRunnable runnable : firstList){
      runnable.setName("First "+i);
      threads.add(run(runnable));
      i++;
    }

    // Wait for al of them
    boolean error = false;
    for (Thread waiting : threads){
      try {
        waiting.join();
      } catch(Exception e){
        error = true;
        break;
      }
    }

    // Now run second list
    if (!error){
      int j = 1;
      for (DpfRunnable runnable : secondList){
        runnable.setName("Second "+j);
        run(runnable);
        j++;
      }
    }
  }

  public Thread run(DpfRunnable runnable){
    runnable.setContext(context);
    Thread thread = new Thread(runnable);
    thread.start();
    return thread;
  }
}

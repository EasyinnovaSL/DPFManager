package dpfmanager.shell.modules.timer.core;

import dpfmanager.shell.core.context.DpfContext;

/**
 * Created by Adrià Llorens on 25/04/2016.
 */
public abstract class DpfTask {

  protected DpfContext context;

  private int interval;
  private boolean stop = true;

  // Main run method
  public void run(){
    if (!stop) {
      perform();
      sleep(interval);
    }
  }

  // Task logic
  public abstract void perform();



  private boolean sleep(int milis) {
    try {
      Thread.sleep(milis);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Task methods
   */
  public void setInterval(int interval) {
    this.interval = interval;
  }

  public void play(){
    stop = false;
    run();
  }

  public void stop(){
    stop = true;
  }

  /**
   * Context methods
   */
  public DpfContext getContext(){
    return context;
  }

  public void setContext(DpfContext c){
    if (context == null) {
      context = c;
      handleContext(context);
    }
  }

  protected abstract void handleContext(DpfContext context);
}

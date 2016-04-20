package dpfmanager.shell.modules.threading.runnable;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public abstract class DpfRunnable implements Runnable {

  protected String name;
  protected DpfContext context;

  public DpfRunnable() {
    name = "";
  }

  public void setName(String n){
    name = n;
  }

  @Override
  public void run() {
    runTask();
  }

  public void setContext(DpfContext c){
    context = c;
    handleContext(context);
  }

  public abstract void runTask();

  public abstract void handleContext(DpfContext context);

  /**
   * Custom print lines
   */
  protected void printOut(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  protected void printErr(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  protected void printException(String header, Exception ex){
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(header, ex));
  }

}

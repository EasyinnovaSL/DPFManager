package dpfmanager.shell.core.context;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.messages.DpfMessage;

import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class GuiContext implements DpfContext {

  private Context context;

  public GuiContext(Context c){
    context = c;
  }

  @Override
  public void send(String target, Object message) {
    if (context != null){
      context.send(target, message);
    } else{
      System.out.println("ERROR!");
      System.out.println("Application context is null.");
    }
  }

  @Override
  public void sendAfter(String target, Object message, Integer seconds) {
    if (context != null){
      try {
        Thread.sleep(seconds * 1000);
      } catch (InterruptedException e){
      }
      context.send(target, message);
    } else{
      System.out.println("ERROR!");
      System.out.println("Application context is null.");
    }
  }

  @Override
  public void sendGui(String target, Object message){
    send(target, message);
  }

  @Override
  public void sendConsole(String target, Object message){
  }

  @Override
  public Object sendAndWaitResponse(String target, Object message) {
    return null;
  }

  @Override
  public boolean isConsole() {
    return false;
  }

  @Override
  public boolean isGui() {
    return true;
  }
}

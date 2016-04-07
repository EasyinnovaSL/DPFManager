package dpfmanager.shell.core.context;

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
  public void sendGui(String target, Object message){
    send(target, message);
  }

  @Override
  public void sendConsole(String target, Object message){
  }
}

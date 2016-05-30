package dpfmanager.shell.core.context;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.messages.DpfMessage;

import org.springframework.context.ApplicationContext;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class ConsoleContext implements DpfContext {

  private ApplicationContext context;

  public ConsoleContext(ApplicationContext c){
    context = c;
  }

  @Override
  public void send(String target, Object message) {
    if (context != null){
      DpfSpringController controller = (DpfSpringController) context.getBean(target);
      controller.handleDpfMessage((DpfMessage) message);
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
      DpfSpringController controller = (DpfSpringController) context.getBean(target);
      controller.handleDpfMessage((DpfMessage) message);
    } else{
      System.out.println("ERROR!");
      System.out.println("Application context is null.");
    }
  }

  @Override
  public void sendGui(String target, Object message){
  }

  @Override
  public void sendConsole(String target, Object message){
    send(target, message);
  }

  @Override
  public Object sendAndWaitResponse(String target, Object message) {
    if (context != null){
      DpfSpringController controller = (DpfSpringController) context.getBean(target);
      return controller.handleDpfMessageWithResponse((DpfMessage) message);
    } else{
      System.out.println("ERROR!");
      System.out.println("Application context is null.");
      return null;
    }
  }

  @Override
  public boolean isConsole() {
    return true;
  }

  @Override
  public boolean isGui() {
    return false;
  }
}

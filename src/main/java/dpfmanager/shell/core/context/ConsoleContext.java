package dpfmanager.shell.core.context;

import dpfmanager.shell.application.launcher.noui.ApplicationParameters;
import dpfmanager.shell.application.launcher.noui.ParametersMessage;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.messages.DpfMessage;

import org.springframework.context.ApplicationContext;

import java.util.List;

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
  public void sendGui(String target, Object message){
  }

  @Override
  public void sendConsole(String target, Object message){
    send(target, message);
  }

  public void sendAllControllers(ApplicationParameters params){
    for (String name : context.getBeanDefinitionNames()){
      if (name.startsWith("m0")) {
        send(name, new ParametersMessage(params));
      }
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

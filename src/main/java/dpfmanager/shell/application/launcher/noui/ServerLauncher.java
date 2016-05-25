package dpfmanager.shell.application.launcher.noui;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.interfaces.console.ConsoleController;
import dpfmanager.shell.interfaces.console.ServerController;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.server.messages.ServerMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class ServerLauncher {

  /**
   * The main controller.
   */
  private ServerController controller;

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The parsed args
   */
  private Map<String, String> parameters;

  /**
   * The args.
   */
  private List<String> params;

  public ServerLauncher(String[] args) {
    // Load spring context
    AppContext.loadContext("DpfSpringServer.xml");
    parameters = (Map<String, String>) AppContext.getApplicationContext().getBean("parameters");
    parameters.put("mode", "SERVER");
    //Load DpfContext
    context = new ConsoleContext(AppContext.getApplicationContext());
    // The main controller
    controller = new ServerController(context);
    // Parameters
    params = new ArrayList(Arrays.asList(args));
  }

  /**
   * Start.
   */
  public void start() {
    // Read the params
    int idx = 0;
    boolean argsError = false;
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      if (arg.equals("-p")) {
        idx++;
        if (idx < params.size()) {
          String port = params.get(idx);
          if (isNumeric(port)){
            parameters.put("-p",port);
          } else {
            argsError = true;
          }
        } else {
          argsError = true;
        }
      }
      idx++;
    }

    // Start the server
    if (!argsError) {
      context.send(BasicConfig.MODULE_SERVER, new ServerMessage(ServerMessage.Type.START));
    } else {
      printOut("Parameters error");
    }
  }

  /**
   * Read params functions
   */
  private boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * Custom print lines
   */
  private void printOut(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private void printErr(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  private void printException(Exception ex){
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception has occurred!", ex));
  }

  /**
   * Exit application
   */
  public void exit(){
    AppContext.close();
    System.exit(0);
  }

}

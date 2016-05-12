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

  public ServerLauncher(String[] args) {
    // Load spring context
    AppContext.loadContext("DpfSpringServer.xml");
    //Load DpfContext
    context = new ConsoleContext(AppContext.getApplicationContext());
    // The main controller
    controller = new ServerController(context);
  }

  /**
   * Start.
   */
  public void start() {
    // Start the server
    context.send(BasicConfig.MODULE_SERVER, new ServerMessage(ServerMessage.Type.START));
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

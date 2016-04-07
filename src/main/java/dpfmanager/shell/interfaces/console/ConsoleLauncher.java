package dpfmanager.shell.interfaces.console;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class ConsoleLauncher {

  public void run(String[] args) {
    ConsoleContext context = new ConsoleContext();
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Hello World!"));
  }

}

package dpfmanager.shell.modules.messages.core;

import dpfmanager.shell.core.modules.DpfModule;

import org.apache.log4j.Logger;

/**
 * Created by Adrià Llorens on 24/02/2016.
 */
public class MessageModule extends DpfModule {

  public enum LogType {
    INFO,
    DEBUG,
    WARNING,
    ERROR
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  public void logMessage(Class clas, LogType type, String message) {
    Logger LOGGER = Logger.getLogger(clas);
    switch (type) {
      case INFO:
        LOGGER.info(message);
        break;
      case DEBUG:
        LOGGER.debug(message);
        break;
      case WARNING:
        LOGGER.warn(message);
        break;
      case ERROR:
        LOGGER.error(message);
        break;
    }
  }

}

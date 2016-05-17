package dpfmanager.shell.modules.messages.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.interfaces.gui.workbench.DpfCloseEvent;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import javafx.stage.WindowEvent;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_MESSAGES)
@Scope("singleton")
public class MessagesService extends DpfService {

  @PostConstruct
  public void init() {
    if (System.getProperty("app.home") == null){
      System.setProperty("app.home", DPFManagerProperties.getConfigDir());
    }
  }

  @Override
  protected void handleContext(DpfContext context){
  }

  public void logMessage(LogMessage lm){
    String clazz = lm.getMyClass().toString();
    clazz = clazz.substring(clazz.lastIndexOf(".") + 1, clazz.length());
    if (lm.getLevel().equals(Level.DEBUG)) {
      // Log in console
      systemOut(lm.getMessage());
    } else {
      // Default pattern
      LogManager.getLogger(clazz).log(lm.getLevel(), lm.getMessage());
    }
  }

  public void exceptionMessage(ExceptionMessage em) {
    LogManager.getLogger("").log(Level.ERROR, em.getHeader());
    systemErr(em.getException().getMessage());
    systemErr(AlertsManager.getExceptionText(em.getException()));
  }

  public void alertMessage(AlertMessage am){
    Level level = Level.WARN;
    if (am.getType().equals(AlertMessage.Type.ERROR)){
      level = Level.ERROR;
    }
    LogManager.getLogger("").log(level, am.getHeader());
    systemOut(am.getContent());
  }

  public void systemOut(String message){
    LogManager.getLogger("").log(Level.DEBUG, MarkerManager.getMarker("PLAIN"), message);
  }

  public void systemErr(String message){
    LogManager.getLogger("").log(Level.ERROR, MarkerManager.getMarker("EXCEPTION"), message);
  }


}

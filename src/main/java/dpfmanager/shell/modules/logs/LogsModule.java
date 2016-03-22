package dpfmanager.shell.modules.logs;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.LogMessage;
import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.util.TextAreaAppender;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_LOGS,
    name = BasicConfig.MODULE_LOGS,
    active = true)
public class LogsModule extends DpfModule {

  @Resource
  protected Context context;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(LogMessage.class)){
      LogMessage lm = dpfMessage.getTypedMessage(LogMessage.class);
      if (!TextAreaAppender.hasTextArea() && lm.hasTextArea()){
        // Init text area handler
        TextAreaAppender.setTextArea(lm.getTextArea());
      }
      else {
        // Log message
        String clazz = lm.getMyClass().toString();
        clazz = clazz.substring(clazz.lastIndexOf(".")+1, clazz.length());
        if (lm.getLevel().equals(Level.DEBUG)){
          // use marker for custom pattern
          LogManager.getLogger(clazz).log(lm.getLevel(), MarkerManager.getMarker("PLAIN"), lm.getMessage());
        }
        else{
          // Default pattern
          LogManager.getLogger(clazz).log(lm.getLevel(), lm.getMessage());
        }

      }
    }
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
  }

  @Override
  public Context getContext(){
    return context;
  }

}

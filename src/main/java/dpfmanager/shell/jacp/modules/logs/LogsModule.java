package dpfmanager.shell.jacp.modules.logs;

import dpfmanager.shell.jacp.core.config.BasicConfig;
import dpfmanager.shell.jacp.core.modules.DpfModule;
import javafx.event.Event;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.COMPONENT_MESSAGES,
    name = BasicConfig.COMPONENT_MESSAGES,
    active = true)
public class LogsModule extends DpfModule {

  @Resource
  protected Context context;

  private String initial;

  @Override
  public Object handle(final Message<Event, Object> message) {
    if (message.messageBodyEquals("ping")) {
      return "pong";
    } else if (message.messageBodyEquals("initial")) {
      System.out.println(initial);
      return answer(context, message, initial);
    } else if (message.messageBodyEquals("pong")){
      return "ping";
    }
    return "No method";
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    initial = "OMG";
    System.out.println("Component initialized");
  }
}

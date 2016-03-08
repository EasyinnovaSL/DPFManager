package dpfmanager.shell.jacp.core.modules;

import dpfmanager.shell.jacp.core.config.BasicConfig;
import javafx.event.Event;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.CallbackComponent;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 02/03/2016.
 */
public abstract class DpfModule implements CallbackComponent {

  // Fix Jacp Bug
  protected Object answer(Context context, Message<Event, Object> message, Object returned) {
    String target = message.getSourceId();
    if (target.contains(BasicConfig.WORKBENCH)){
      target = target.substring(BasicConfig.WORKBENCH.length() +1);
    }
    context.send(target, returned);
    return null;
  }

}

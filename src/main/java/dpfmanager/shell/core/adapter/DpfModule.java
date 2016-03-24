package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import javafx.event.Event;

import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.CallbackComponent;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 02/03/2016.
 */
public abstract class DpfModule implements CallbackComponent {

  @Override
  public Object handle(final Message<Event, Object> message) {
    String sId = message.getSourceId();
    if (message.isMessageBodyTypeOf(ArrayMessage.class)) {
      // Array message
      ArrayMessage aMessage = message.getTypedMessageBody(ArrayMessage.class);
      tractMessage(aMessage.getFirstMessage(), sId);
      if (aMessage.hasNext()) {
        aMessage.removeFirst();
        getContext().send(aMessage.getFirstTarget(), aMessage);
      }
    } else if (message.isMessageBodyTypeOf(DpfMessage.class)) {
      // Single message
      DpfMessage dpfMessage = message.getTypedMessageBody(DpfMessage.class);
      tractMessage(dpfMessage, sId);
    }
    return null;
  }

  private void tractMessage(DpfMessage dpfMessage, String source) {
    dpfMessage.setSourceId(source);
    handleMessage(dpfMessage);
  }

  /**
   * Methods to override
   */
  public abstract void handleMessage(DpfMessage dpfMessage);

  public abstract Context getContext();

}

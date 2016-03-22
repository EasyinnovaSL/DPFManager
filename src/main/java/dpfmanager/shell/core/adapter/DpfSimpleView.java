package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import javafx.event.Event;
import javafx.scene.Node;

import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;

/**
 * Created by Adrià Llorens on 22/03/2016.
 */
public abstract class DpfSimpleView implements FXComponent {

  /**
   * FxComponent methods
   */

  @Override
  public Node handle(Message<Event, Object> message) {
    if (message.isMessageBodyTypeOf(ArrayMessage.class)) {
      // Array message
      ArrayMessage aMessage = message.getTypedMessageBody(ArrayMessage.class);
      tractMessageOnWorker(aMessage.getFirstMessage());
    } else if (message.isMessageBodyTypeOf(DpfMessage.class)) {
      // Single message
      DpfMessage dpfMessage = message.getTypedMessageBody(DpfMessage.class);
      tractMessageOnWorker(dpfMessage);
    }
    return null;
  }

  @Override
  public Node postHandle(Node node, Message<Event, Object> message) {
    Node returned = null;
    if (message.isMessageBodyTypeOf(ArrayMessage.class)) {
      // Array message
      ArrayMessage aMessage = message.getTypedMessageBody(ArrayMessage.class);
      returned = tractMessageOnFX(aMessage.getFirstMessage());
      if (aMessage.hasNext()) {
        aMessage.removeFirst();
        sendMessage(aMessage.getFirstTarget(), aMessage);
      }
    } else if (message.isMessageBodyTypeOf(DpfMessage.class)) {
      // Single message
      DpfMessage dpfMessage = message.getTypedMessageBody(DpfMessage.class);
      returned = tractMessageOnFX(dpfMessage);
    } else {
      returned = tractMessageOnFX(null);
    }
    return returned;
  }

  private void tractMessageOnWorker(DpfMessage dpfMessage) {
    handleMessageOnWorker(dpfMessage);
  }

  private Node tractMessageOnFX(DpfMessage dpfMessage) {
    return handleMessageOnFX(dpfMessage);
  }

  /**
   * Methods to override
   */
  public abstract void handleMessageOnWorker(DpfMessage dpfMessage);

  public abstract Node handleMessageOnFX(DpfMessage dpfMessage);

  public abstract void sendMessage(String target, Object message);

}

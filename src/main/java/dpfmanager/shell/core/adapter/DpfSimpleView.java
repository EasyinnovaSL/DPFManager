/**
 * <h1>DpfSimpleView.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;
import javafx.event.Event;
import javafx.scene.Node;

import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;

/**
 * Created by Adria Llorens on 22/03/2016.
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

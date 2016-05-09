package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 09/05/2016.
 */
public class CloseMessage extends DpfMessage {

  private boolean ask;

  public CloseMessage(boolean a){
    ask = a;
  }

  public boolean isAsk() {
    return ask;
  }
}

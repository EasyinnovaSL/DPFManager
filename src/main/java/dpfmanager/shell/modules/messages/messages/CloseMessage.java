package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 09/05/2016.
 */
public class CloseMessage extends DpfMessage {

  public enum Type {
    THREADING, PERIODICAL
  }

  private Type type;
  private boolean ask;

  public CloseMessage(Type t){
    type = t;
  }

  public CloseMessage(Type t, boolean a){
    // Threading && Periodical
    type = t;
    ask = a;
  }

  public boolean isThreading() {
    return type.equals(Type.THREADING);
  }

  public boolean isPeriodical() {
    return type.equals(Type.PERIODICAL);
  }

  public boolean isAsk() {
    return ask;
  }
}

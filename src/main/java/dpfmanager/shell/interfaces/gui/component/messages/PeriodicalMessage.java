package dpfmanager.shell.interfaces.gui.component.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 01/07/2016.
 */
public class PeriodicalMessage extends DpfMessage {

  public enum Type {
    DELETE
  }

  private Type type;

  public PeriodicalMessage(Type t) {
    type = t;
  }

  public boolean isDelete() {
    return type.equals(Type.DELETE);
  }

}

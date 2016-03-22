package dpfmanager.shell.core.messages;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class ReportsMessage extends DpfMessage {
  public enum Type {
    RELOAD,
    SHOW
  }

  private Type type;

  public ReportsMessage(Type t) {
    type = t;
  }

  public Type getType() {
    return type;
  }

  public boolean isReload() {
    return type.equals(Type.RELOAD);
  }

  public boolean isShow() {
    return type.equals(Type.SHOW);
  }

}
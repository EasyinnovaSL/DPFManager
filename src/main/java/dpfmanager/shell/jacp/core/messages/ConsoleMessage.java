package dpfmanager.shell.jacp.core.messages;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class ConsoleMessage extends DpfMessage {
  public enum Type {
    SHOW,
    HIDE
  }

  private Type type;

  public ConsoleMessage(Type t){
    type = t;
  }

  public Type getType(){
    return type;
  }

  public boolean isShow(){
    return type.equals(Type.SHOW);
  }

  public boolean isHide(){
    return type.equals(Type.HIDE);
  }

}

package dpfmanager.shell.core.messages;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class UiMessage extends DpfMessage {
  public enum Type {
    INIT,
    RELOAD,
    SHOW,
    HIDE
  }

  private Type type;

  private String next;

  private DpfMessage message;

  public UiMessage(Type t){
    type = t;
    next = null;
    message = null;
  }

  public UiMessage(Type t, String n, DpfMessage m){
    type = t;
    next = n;
    message = m;
  }

  public Type getType(){
    return type;
  }

  public DpfMessage getMessage() {
    return message;
  }

  public String getNext() {
    return next;
  }

  public boolean isInit(){
    return type.equals(Type.INIT);
  }

  public boolean isReload(){
    return type.equals(Type.RELOAD);
  }

  public boolean isShow(){
    return type.equals(Type.SHOW);
  }

  public boolean isHide(){
    return type.equals(Type.HIDE);
  }

  public boolean hasNext(){
    return (next != null && message != null);
  }
}

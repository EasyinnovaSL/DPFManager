package dpfmanager.shell.core.messages;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class UiMessage extends DpfMessage {

  public enum Type {
    SHOW,
    RELOAD
  }

  private Type type;

  public UiMessage(){
    type = Type.SHOW;
  }

  public UiMessage(Type t){
    type = t;
  }

  public boolean isShow(){
    return type.equals(Type.SHOW);
  }

  public boolean isReload(){
    return type.equals(Type.RELOAD);
  }


}

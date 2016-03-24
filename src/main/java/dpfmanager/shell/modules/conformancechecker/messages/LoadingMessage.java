package dpfmanager.shell.modules.conformancechecker.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class LoadingMessage extends DpfMessage {

  public enum Type {
    SHOW,
    HIDE,
    TEXT
  }

  private Type type;
  private String text;

  public LoadingMessage(Type t){
    type = t;
  }

  public LoadingMessage(Type t, String te){
    type = t;
    text = te;
  }

  public boolean isShow(){
    return type.equals(Type.SHOW);
  }

  public boolean isHide(){
    return type.equals(Type.HIDE);
  }

  public boolean isText(){
    return type.equals(Type.TEXT);
  }

  public String getText(){
    return text;
  }

}

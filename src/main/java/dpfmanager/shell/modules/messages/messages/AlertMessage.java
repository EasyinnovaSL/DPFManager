package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import javafx.scene.control.Alert;

/**
 * Created by Adrià Llorens on 23/03/2016.
 */
public class AlertMessage extends DpfMessage {

  public enum Type{
    ALERT,
    INFO,
    WARNING,
    ERROR,
    EXCEPTION
  }

  private Type type;
  private String title;
  private String header;
  private String content;
  private Exception exception;

  public AlertMessage(Type t, String h){
    type = t;
    title = getDefaultTitle(type);
    header = h;
    content = null;
  }

  public AlertMessage(Type t, String h, String c){
    type = t;
    title = getDefaultTitle(type);
    header = h;
    content = c;
  }

  public AlertMessage(Type t, String h, Exception e){
    type = t;
    title = getDefaultTitle(type);
    header = h;
    content = e.getMessage();
    exception = e;
  }

  private String getDefaultTitle(Type type){
    if (type.equals(Type.ERROR)){
      return "Error";
    } else if (type.equals(Type.WARNING)){
      return "Warning";
    } else if (type.equals(Type.INFO)){
      return "Help";
    } else if (type.equals(Type.ALERT)){
      return "Alert";
    } else if (type.equals(Type.EXCEPTION)){
      return "Exception";
    }
    return "";
  }

  public Type getType(){
    return type;
  }

  public String getTitle(){
    return title;
  }

  public String getHeader(){
    return header;
  }

  public String getContent(){
    return content;
  }
}

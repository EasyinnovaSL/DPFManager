package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 23/03/2016.
 */
public class AlertMessage extends DpfMessage {

  public enum Type{
    ALERT,
    INFO,
    WARNING,
    ERROR,
    CONFIRMATION
  }

  private Type type;
  private String title;
  private String header;
  private String content;
  private Boolean result = null;

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

  public void setTitle(String t){
    title = t;
  }

  public void setResult(boolean bol){
    result = bol;
  }

  public boolean getResult(){
    return result;
  }

  public boolean hasResult(){
    return result != null;
  }

  private String getDefaultTitle(Type type){
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.language");
    if (type.equals(Type.ERROR)){
      return bundle.getString("error");
    } else if (type.equals(Type.WARNING)){
      return bundle.getString("warning");
    } else if (type.equals(Type.INFO)){
      return bundle.getString("help");
    } else if (type.equals(Type.ALERT)){
      return bundle.getString("alert");
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

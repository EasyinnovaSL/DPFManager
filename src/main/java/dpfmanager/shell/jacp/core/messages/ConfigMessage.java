package dpfmanager.shell.jacp.core.messages;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ConfigMessage extends DpfMessage {

  public enum Type {
    NEW,
    EDIT
  }

  private Type type;
  private String path;

  public ConfigMessage(Type t){
    type = t;
  }

  public ConfigMessage(Type t, String p){
    type = t;
    path = p;
  }

  public String getPath(){
    return path;
  }

  public Type getType(){
    return type;
  }

  public boolean isNew(){
    return type.equals(Type.NEW);
  }

  public boolean isEdit(){
    return type.equals(Type.EDIT);
  }

}

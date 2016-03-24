package dpfmanager.shell.core.messages;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class DpfMessage {

  private String sourceId;

  public <T> boolean isTypeOf(Class<T> clazz){
    return clazz.isAssignableFrom(this.getClass());
  }

  public <T> T getTypedMessage(Class<T> clazz){
    return clazz.cast(this);
  }

  public void setSourceId(String s){
    sourceId = s;
  }

  public String getSourceId(){
    return sourceId;
  }

}

package dpfmanager.shell.core.messages;

/**
 * Created by Adrià Llorens on 22/03/2016.
 */
public class SwitchMessage extends DpfMessage {

  private String id;

  public SwitchMessage(String i){
    id = i;
  }

  public String getId(){
    return id;
  }

}

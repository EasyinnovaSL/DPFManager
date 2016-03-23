package dpfmanager.shell.core.messages;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class ShowMessage extends DpfMessage {

  private String type;
  private String path;


  public ShowMessage(String t, String p) {
    type = t;
    path = p;
  }

  public String getPath() {
    return path;
  }

  public String getType() {
    return type;
  }

}
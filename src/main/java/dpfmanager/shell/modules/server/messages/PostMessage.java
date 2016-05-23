package dpfmanager.shell.modules.server.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class PostMessage extends DpfMessage {

  public enum Type {
    POST, GET
  }

  private Type type;
  private Long uuid;
  private String json;
  private String filepath;
  private String configpath;

  public PostMessage(Type t, Long u, String j, String fp, String cp) {
    type = t;
    uuid = u;
    json = j;
    filepath = fp;
    configpath = cp;
  }

  public boolean isPost() {
    return type.equals(Type.POST);
  }

  public Long getUuid() {
    return uuid;
  }

  public String getJson() {
    return json;
  }

  public String getFilepath() {
    return filepath;
  }

  public String getConfigpath() {
    return configpath;
  }
}

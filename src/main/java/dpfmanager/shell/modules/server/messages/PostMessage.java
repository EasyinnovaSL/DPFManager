package dpfmanager.shell.modules.server.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class PostMessage extends DpfMessage {

  public enum Type {
    POST, ASK
  }

  private Type type;
  private Long uuid;
  private Long id;
  private String filepath;
  private String configpath;
  private String hash;

  public PostMessage(Type t, Long u, String fp, String cp) {
    // Post
    type = t;
    uuid = u;
    filepath = fp;
    configpath = cp;
    hash = null;
  }

  public PostMessage(Type t, Long i) {
    // Ask with id
    type = t;
    id = i;
    hash = null;
  }

  public PostMessage(Type t, String h) {
    // Ask with hash
    type = t;
    hash = h;
  }

  public boolean isPost() {
    return type.equals(Type.POST);
  }

  public boolean isAsk() {
    return type.equals(Type.ASK);
  }

  public Long getUuid() {
    return uuid;
  }

  public Long getId() {
    return id;
  }

  public String getFilepath() {
    return filepath;
  }

  public String getConfigpath() {
    return configpath;
  }

  public String getHash() {
    return hash;
  }
}

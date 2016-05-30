package dpfmanager.shell.modules.client.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;

import java.util.List;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class RequestMessage extends DpfMessage {

  public enum Type {
    ASK, CHECK
  }

  private Type type;
  private List<String> files;
  private List<String> tmpFiles;
  private Configuration config;
  private String str;

  public RequestMessage(Type t, String i) {
    // ASK
    type = t;
    str = i;
  }

  public RequestMessage(Type t, List<String> f, List<String> tmp, Configuration c) {
    // CHECK
    type = t;
    files = f;
    tmpFiles = tmp;
    config = c;
  }

  public boolean isAsk() {
    return type.equals(Type.ASK);
  }

  public boolean isCheck() {
    return type.equals(Type.CHECK);
  }

  public List<String> getFiles() {
    return files;
  }

  public List<String> getTmpFiles() {
    return tmpFiles;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getString() {
    return str;
  }
}

package dpfmanager.shell.modules.conformancechecker.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;

import java.util.List;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class ConformanceMessage extends DpfMessage {

  private Type type;
  private String input;
  private String path;
  private List<String> files;
  private Configuration config;
  private int recursive;

  public enum Type {
    GUI, CONSOLE, DELETE
  }

  public ConformanceMessage(Type t){
    type = t;
  }

  public ConformanceMessage(Type t, String i, String p, int r){
    type = t;
    input = i;
    path = p;
    recursive = r;
  }

  public ConformanceMessage(Type t, List<String> f, Configuration c, int r){
    type = t;
    files = f;
    config = c;
    recursive = r;
  }

  public boolean isDelete(){
    return type.equals(Type.DELETE);
  }

  public boolean isGui(){
    return type.equals(Type.GUI);
  }

  public String getPath(){
    return path;
  }

  public String getInput(){
    return input;
  }

  public List<String> getFiles() {
    return files;
  }

  public Configuration getConfig() {
    return config;
  }

  public int getRecursive() {
    return recursive;
  }
}

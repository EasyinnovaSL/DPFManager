package dpfmanager.shell.modules.conformancechecker.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;

import java.util.List;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class ConformanceMessage extends DpfMessage {

  private Long uuid;
  private String input;
  private String path;
  private List<String> files;
  private Configuration config;
  private int recursive;

  public ConformanceMessage(String i, String p, int r) {
    // With paths, Gui
    input = i;
    path = p;
    recursive = r;
  }

  public ConformanceMessage(Long u, String i, String p) {
    // With paths, Server
    input = i;
    path = p;
    uuid = u;
  }

  public ConformanceMessage(List<String> f, Configuration c, int r) {
    // Console
    files = f;
    config = c;
    recursive = r;
    input = null;
    path = null;
  }

  public boolean hasPaths() {
    return input != null && path != null;
  }

  public Long getUuid() {
    return uuid;
  }

  public String getPath() {
    return path;
  }

  public String getInput() {
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

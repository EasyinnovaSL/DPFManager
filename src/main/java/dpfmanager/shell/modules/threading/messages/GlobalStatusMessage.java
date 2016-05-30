package dpfmanager.shell.modules.threading.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

/**
 * Created by Adrià Llorens on 08/04/2016.
 */
public class GlobalStatusMessage extends DpfMessage {

  public enum Type {
    NEW,
    INIT,
    FINISH,
    RELOAD
  }

  private Type type;
  private long uuid;
  private int size;
  private Configuration config;
  private String internal;
  private String input;
  private DpfRunnable run;

  public GlobalStatusMessage(Type t, Long u, DpfRunnable r, String i) {
    // New
    type = t;
    uuid = u;
    run = r;
    input = i;
  }

  public GlobalStatusMessage(Type t, long u, int n, Configuration c, String i, String ri) {
    // Init
    type = t;
    uuid = u;
    size = n;
    config = c;
    internal = i;
    input = ri;
  }

  public GlobalStatusMessage(Type t, long u) {
    // Finish
    type = t;
    uuid = u;
  }

  public GlobalStatusMessage(Type t) {
    // Asking for reload
    type = t;
  }

  public boolean isNew() {
    return type.equals(Type.NEW);
  }

  public boolean isInit() {
    return type.equals(Type.INIT);
  }

  public boolean isFinish() {
    return type.equals(Type.FINISH);
  }

  public boolean isReload() {
    return type.equals(Type.RELOAD);
  }

  public long getUuid() {
    return uuid;
  }

  public int getSize() {
    return size;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getInternal() {
    return internal;
  }

  public String getInput() {
    return input;
  }

  public DpfRunnable getRunnable() {
    return run;
  }

}

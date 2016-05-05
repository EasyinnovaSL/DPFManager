package dpfmanager.shell.modules.threading.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 04/05/2016.
 */
public class ThreadsMessage extends DpfMessage {
  public enum Type {
    PAUSE, RESUME, CANCEL
  }

  private Type type;
  private Long uuid;
  private boolean request;

  public ThreadsMessage(Type t, Long u, boolean r) {
    type = t;
    uuid = u;
    request = r;
  }

  public boolean isPause() {
    return type.equals(Type.PAUSE);
  }

  public boolean isResume() {
    return type.equals(Type.RESUME);
  }

  public boolean isCancel() {
    return type.equals(Type.CANCEL);
  }

  public boolean isRequest() {
    return request;
  }

  public Long getUuid() {
    return uuid;
  }
}

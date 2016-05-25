package dpfmanager.shell.modules.server.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class StatusMessage extends DpfMessage {

  public enum Status {
    RUNNING, FINISHED
  }

  private Status status;
  private String folder;

  public StatusMessage(Status s, String f) {
    status = s;
    folder = f;
  }

  public boolean isRunning() {
    return status.equals(Status.RUNNING);
  }

  public boolean isFinished() {
    return status.equals(Status.FINISHED);
  }

  public String getFolder() {
    return folder;
  }
}

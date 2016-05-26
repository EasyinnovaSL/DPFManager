package dpfmanager.shell.modules.server.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class StatusMessage extends DpfMessage {

  public enum Status {
    RUNNING, FINISHED, NOTFOUND
  }

  private Status status;
  private String folder;
  private Integer processed;
  private Integer total;

  public StatusMessage(Status s, String f, Integer p, Integer t) {
    status = s;
    folder = f;
    processed = p;
    total = t;
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

  public Integer getProcessed() {
    return processed;
  }

  public Integer getTotal() {
    return total;
  }
}

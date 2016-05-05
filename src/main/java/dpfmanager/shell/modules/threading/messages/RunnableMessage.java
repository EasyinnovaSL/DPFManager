package dpfmanager.shell.modules.threading.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public class RunnableMessage extends DpfMessage {

  private Long uuid;
  private DpfRunnable runnable;

  public RunnableMessage(Long u, DpfRunnable r) {
    uuid = u;
    runnable = r;
  }

  public DpfRunnable getRunnable() {
    return runnable;
  }

  public Long getUuid() {
    return uuid;
  }
}

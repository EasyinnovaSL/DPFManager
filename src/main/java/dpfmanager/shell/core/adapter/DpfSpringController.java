package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public abstract class DpfSpringController {

  private boolean locked = false;

  public void handleDpfMessage(DpfMessage message) {
    handleMessage(message);
  }

  public Object handleDpfMessageWithResponse(DpfMessage message) {
    return handleMessageWithResponse(message);
  }

  public abstract void handleMessage(DpfMessage message);

  public abstract Object handleMessageWithResponse(DpfMessage message);

  public boolean lock() {
    if (locked) {
      return false;
    } else {
      locked = true;
      return true;
    }
  }

  public void unlock() {
    locked = false;
  }

}

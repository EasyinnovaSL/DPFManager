package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public abstract class DpfSpringController {
  public abstract void handleMessage(DpfMessage message);
}

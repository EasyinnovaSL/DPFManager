package dpfmanager.shell.modules.server.runnable;

import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

/**
 * Created by Adrià Llorens on 20/05/2016.
 */
public class RequestRunnable extends DpfRunnable {

  private DpfContext context;

  private String target;
  private DpfMessage message;

  public RequestRunnable(String t, DpfMessage m) {
    target = t;
    message = m;
  }

  @Override
  public void runTask() {
    context.send(target, message);
  }

  @Override
  public void handleContext(DpfContext c) {
    context = c;
  }
}

package dpfmanager.shell.core.context;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public interface DpfContext {
  void send(String target, Object message);
  void sendGui(String target, Object message);
  void sendConsole(String target, Object message);
}

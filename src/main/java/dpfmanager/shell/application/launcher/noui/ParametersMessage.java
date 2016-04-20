package dpfmanager.shell.application.launcher.noui;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 18/04/2016.
 */
public class ParametersMessage extends DpfMessage {

  private ApplicationParameters params;

  public ParametersMessage(ApplicationParameters ap) {
    params = ap;
  }

  public ApplicationParameters getParams() {
    return params;
  }
}

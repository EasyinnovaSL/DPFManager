package dpfmanager.shell.modules.client.messages;

import dpfmanager.shell.core.messages.DpfMessage;

import java.io.File;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class ResponseMessage extends DpfMessage {

  private String message;

  public ResponseMessage(String m) {
    // Json response
    message = m;
  }

  public String getMessage() {
    return message;
  }

}

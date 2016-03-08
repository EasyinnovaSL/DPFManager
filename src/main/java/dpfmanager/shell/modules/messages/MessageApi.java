package dpfmanager.shell.modules.messages;

import dpfmanager.shell.modules.messages.core.MessageModule;

/**
 * Created by Adrià Llorens on 24/02/2016.
 */
public class MessageApi {

  /**
   * The associated module
   */
  private MessageModule module;

  public MessageApi() {

  }

  public void setModule(MessageModule module) {
    this.module = module;
  }

}

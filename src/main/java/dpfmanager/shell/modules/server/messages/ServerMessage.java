package dpfmanager.shell.modules.server.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class ServerMessage extends DpfMessage {

  public enum Type {
    START, STOP
  }

  private Type type;

  public ServerMessage(Type t){
    type = t;
  }

  public boolean isStart(){
    return type.equals(Type.START);
  }

}

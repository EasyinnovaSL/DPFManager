package dpfmanager.shell.modules.threading.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.threading.core.FileCheck;

/**
 * Created by Adrià Llorens on 18/04/2016.
 */
public class CheckTaskMessage extends DpfMessage {

  private FileCheck fileCheck;
  private Type type;

  public enum Type{
    INIT, UPDATE, FINISH
  }

  public CheckTaskMessage(Type t, FileCheck fc){
    type = t;
    fileCheck = fc;
  }

  public boolean isInit() {
    return type.equals(Type.INIT);
  }

  public boolean isUpdate() {
    return type.equals(Type.UPDATE);
  }

  public FileCheck getFileCheck() {
    return fileCheck;
  }
}

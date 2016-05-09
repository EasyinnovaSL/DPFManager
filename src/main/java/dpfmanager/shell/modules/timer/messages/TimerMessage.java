package dpfmanager.shell.modules.timer.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class TimerMessage extends DpfMessage {

  public enum Type {
    PLAY, STOP, FINISH, RUN
  }

  private Type type;
  private Class clazz;

  public TimerMessage(Type type, Class clazz) {
    // Play & Stop & Finish & Run
    this.type = type;
    this.clazz = clazz;
  }

  public boolean isPlay(){
    return type.equals(Type.PLAY);
  }

  public boolean isStop(){
    return type.equals(Type.STOP);
  }

  public boolean isFinish(){
    return type.equals(Type.FINISH);
  }

  public boolean isRun(){
    return type.equals(Type.RUN);
  }

  public Class getClazz() {
    return clazz;
  }
}

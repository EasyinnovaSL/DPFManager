package dpfmanager.shell.core.messages;

/**
 * Created by Adrià Llorens on 21/03/2016.
 */
public class WidgetMessage extends DpfMessage {

  public enum Action {
    SHOW,
    HIDE
  }

  public enum Target {
    CONSOLE,
    TASKS
  }

  private Action action;
  private Target target;

  public WidgetMessage(Action a){
    action = a;
  }

  public WidgetMessage(Action a, Target t){
    action = a;
    target = t;
  }

  public boolean isShow(){
    return action.equals(Action.SHOW);
  }

  public boolean isHide(){
    return action.equals(Action.HIDE);
  }

  public boolean isConsole(){
    return target.equals(Target.CONSOLE);
  }

  public boolean isTasks(){
    return target.equals(Target.TASKS);
  }
}

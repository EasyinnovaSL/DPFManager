package dpfmanager.shell.modules.database.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.threading.core.FileCheck;

import java.util.List;

/**
 * Created by Adrià Llorens on 18/04/2016.
 */
public class CheckTaskMessage extends DpfMessage {

  private List<Jobs> jobs;
  private Type type;

  public enum Type{
    INIT, UPDATE, FINISH
  }

  public CheckTaskMessage(List<Jobs> lj){
    jobs = lj;
  }

  public boolean isInit() {
    return type.equals(Type.INIT);
  }

  public boolean isUpdate() {
    return type.equals(Type.UPDATE);
  }

  public List<Jobs> getJobs() {
    return jobs;
  }
}

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
  private int pid;

  private Target target;
  private Long uuid;

  public enum Target{
    PAUSE, CANCEL
  }

  public CheckTaskMessage(List<Jobs> lj, int p){
    // Jobs list
    jobs = lj;
    pid = p;
  }

  public CheckTaskMessage(Target tg, Long u){
    // Finish actions
    target = tg;
    pid = -1;
    uuid = u;
  }

  public boolean isFinishActions() {
    return pid == -1;
  }

  public boolean isCancel() {
    return target != null && target.equals(Target.CANCEL);
  }

  public boolean isPause() {
    return target != null && target.equals(Target.PAUSE);
  }

  public List<Jobs> getJobs() {
    return jobs;
  }

  public int getPid() {
    return pid;
  }

  public Long getUuid() {
    return uuid;
  }
}

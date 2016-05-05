package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.tables.Jobs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrià Llorens on 25/04/2016.
 */
public class DatabaseCache {

  private Map<Long, Jobs> jobs;
  private DpfContext context;

  public DatabaseCache(DpfContext c) {
    context = c;
    jobs = new HashMap<>();
  }

  public void insertNewJob(Long uuid, int state, int total, String input, String origin, int pid, String output ) {
    Jobs job = new Jobs();
    job.setId(uuid);
    job.setState(state);
    job.setTotalFiles(total);
    job.setProcessedFiles(0);
    if (state == 0){
      job.setInit(null);
    } else {
      job.setInit(System.currentTimeMillis());
    }
    job.setFinish(null);
    job.setInput(input);
    job.setOrigin(origin);
    job.setPid(pid);
    job.setOutput(output);
    jobs.put(uuid, job);
  }

  public void initJob(long uuid, int total, String output ) {
    Jobs job = jobs.get(uuid);
    job.setTotalFiles(total);
    job.setOutput(output);
  }

  public void updateJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    if (job != null) {
      job.setProcessedFiles(job.getProcessedFiles() + 1);
    }
  }

  public void resumeJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setInit(System.currentTimeMillis());
    job.setState(1);
  }

  public void cancelJob(Long uuid) {
    jobs.remove(uuid);
  }

  public void finishJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setState(2);
    job.setFinish(System.currentTimeMillis());
    job.setProcessedFiles(job.getTotalFiles());
  }

  public void clear(Long uuid) {
    jobs.remove(uuid);
  }

  public Jobs getJob(Long uuid) {
    return jobs.get(uuid);
  }

}

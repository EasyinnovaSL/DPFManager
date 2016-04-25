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

  public void insertNewJob(Long uuid, int total, String input, String origin, int pid, String output) {
    Jobs job = new Jobs();
    job.setId(uuid);
    job.setState(1);
    job.setTotalFiles(total);
    job.setProcessedFiles(0);
    job.setInit(System.currentTimeMillis());
    job.setFinish(null);
    job.setInput(input);
    job.setOrigin(origin);
    job.setPid(pid);
    job.setOutput(output);
    jobs.put(uuid, job);
  }

  public void updateJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setProcessedFiles(job.getProcessedFiles() + 1);
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

package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.interfaces.gui.component.periodical.Periodicity;
import dpfmanager.shell.modules.database.tables.Crons;
import dpfmanager.shell.modules.database.tables.Jobs;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrià Llorens on 25/04/2016.
 */
public class DatabaseCache {

  private Map<Long, Jobs> jobs;
  private Map<String, Crons> crons;
  private DpfContext context;

  public DatabaseCache(DpfContext c) {
    context = c;
    jobs = new HashMap<>();
    crons = new HashMap<>();
  }

  /**
   * Crons
   */
  public void insertNewCron(String uuid, String input, String configuration, Periodicity periodicity) {
    Crons cron = new Crons();
    cron.setId(uuid);
    cron.setInput(input);
    cron.setConfiguration(configuration);
    cron.setPeriodTime(periodicity.getTimeString());
    switch (periodicity.getMode()){
      case DAILY:
        cron.setPeriodMode(0);
        break;
      case WEEKLY:
        cron.setPeriodMode(1);
        cron.setSpecWeekly(periodicity.getDayOfWeek());
        break;
      case MONTHLY:
        cron.setPeriodMode(2);
        cron.setSpecMonthly(periodicity.getDayOfMonth());
        break;
    }
    crons.put(uuid, cron);
  }

  public void updateCron(String uuid, String input, String configuration, Periodicity periodicity) {
    Crons cron = crons.get(uuid);
    cron.setId(uuid);
    cron.setInput(input);
    cron.setConfiguration(configuration);
    cron.setPeriodTime(periodicity.getTimeString());
    switch (periodicity.getMode()){
      case DAILY:
        cron.setPeriodMode(0);
        break;
      case WEEKLY:
        cron.setPeriodMode(1);
        cron.setSpecWeekly(periodicity.getDayOfWeek());
        break;
      case MONTHLY:
        cron.setPeriodMode(2);
        cron.setSpecMonthly(periodicity.getDayOfMonth());
        break;
    }
  }

  public void setCrons(List<Crons> list) {
    for (Crons cron : list){
      crons.put(cron.getId(), cron);
    }
  }

  public boolean containsCron(String uuid) {
    return crons.containsKey(uuid);
  }

  public void deleteCron(String uuid) {
    crons.remove(uuid);
  }

  public Crons getCron(String uuid) {
    return crons.get(uuid);
  }

  public Collection<Crons> getCrons() {
    return crons.values();
  }

  /**
   * Jobs
   */
  public void insertNewJob(Long uuid, int state, int total, String input, String origin, int pid, String output) {
    Long current = System.currentTimeMillis();
    Jobs job = new Jobs();
    job.setId(uuid);
    job.setHash(DigestUtils.sha256Hex(uuid.toString()));
    job.setState(state);
    job.setTotalFiles(total);
    job.setProcessedFiles(0);
    if (state == 0) {
      job.setInit(null);
    } else {
      job.setInit(current);
    }
    job.setFinish(null);
    job.setInput(input);
    job.setOrigin(origin);
    job.setPid(pid);
    job.setOutput(output);
    job.setTime(null);
    job.setLastUpdate(current);
    jobs.put(uuid, job);
  }

  public void initJob(long uuid, int total, String output) {
    Jobs job = jobs.get(uuid);
    job.setTotalFiles(total);
    job.setOutput(output);
  }

  public void incressProcessed(Long uuid) {
    Jobs job = jobs.get(uuid);
    if (job != null) {
      job.setProcessedFiles(job.getProcessedFiles() + 1);
    }
  }

  public void startJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setInit(System.currentTimeMillis());
    job.setState(1);
  }

  public void resumeJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setState(1);
    job.setInit(System.currentTimeMillis() - job.getTime());
  }

  public void cancelJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setState(3);
  }

  public void pauseJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setState(4);
    job.setTime(System.currentTimeMillis() - job.getInit());
  }

  public void finishJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setState(2);
    job.setFinish(System.currentTimeMillis());
    job.setProcessedFiles(job.getTotalFiles());
  }

  public boolean containsJob(Long uuid) {
    return jobs.containsKey(uuid);
  }

  public void clearJob(Long uuid) {
    jobs.remove(uuid);
  }

  public Jobs getJob(Long uuid) {
    return jobs.get(uuid);
  }

  public Collection<Jobs> getJobs() {
    return jobs.values();
  }

}

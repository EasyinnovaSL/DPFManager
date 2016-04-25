package dpfmanager.shell.modules.timer.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.timer.tasks.JobsStatusTask;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_TIMER)
@Scope("singleton")
public class TimerService extends DpfService {

  private Map<Class, DpfTask> tasks;
  private boolean resend;

  @PostConstruct
  public void init() {
    // No context yet
    resend = false;
    tasks = new HashMap<>();
  }

  @Override
  protected void handleContext(DpfContext context) {
    addJobsStatusTask();
  }

  @PreDestroy
  public void finish() {
  }

  public void addJobsStatusTask() {
    DpfTask task = new JobsStatusTask();
    task.setContext(context);
    task.setInterval(1000);
    tasks.put(JobsStatusTask.class, task);
  }

  public void playTask(Class clazz) {
    tasks.get(clazz).play();
  }

  public void runTask(Class clazz) {
    tasks.get(clazz).run();
  }

  public void stopTask(Class clazz) {
    tasks.get(clazz).stop();
  }

}

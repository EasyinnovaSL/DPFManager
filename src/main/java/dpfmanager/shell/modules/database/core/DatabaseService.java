package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.messages.CheckTaskMessage;
import dpfmanager.shell.modules.database.messages.DatabaseMessage;
import dpfmanager.shell.modules.database.tables.Jobs;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Service(BasicConfig.SERVICE_DATABASE)
@Scope("singleton")
public class DatabaseService extends DpfService {

  private Integer pid;
  private DatabaseConnection connection;
  private DatabaseCache cache;

  public static final Integer UPDATE_INTERVAL = 1000; // Miliseconds

  @PostConstruct
  public void init() {
    // No context yet
  }

  @Override
  protected void handleContext(DpfContext context) {
    cache = new DatabaseCache(context);
    connection = new DatabaseConnection(context);
    connection.init();
    pid = connection.getProgramPid();
  }

  public void handleDatabaseMessage(DatabaseMessage dm){
    if (dm.isNew()){
      createJob(dm);
    } else if (dm.isInit()){
      initJob(dm);
    } else if (dm.isUpdate()){
      updateJob(dm);
    } else if (dm.isFinish()){
      finishJob(dm);
    } else if (dm.isGet()){
      getJobs(dm);
    } else if (dm.isResume()){
      resumeJob(dm);
    } else if (dm.isCancel()){
      cancelJob(dm);
    }
  }

  public void createJob(DatabaseMessage dm) {
    // Get origin
    String origin = "CMD";
    if (context.isGui()) {
      origin = "GUI";
    }
    int state = 1;
    if (dm.getPending()){
      state = 0;
    }
    cache.insertNewJob(dm.getUuid(), state, 0, dm.getInput(), origin, pid, "");
    connection.insertNewJob(cache.getJob(dm.getUuid()));
  }

  public void initJob(DatabaseMessage dm){
    cache.initJob(dm.getUuid(), dm.getTotal(), dm.getOutput());
    connection.updateJob(cache.getJob(dm.getUuid()));
  }

  public void updateJob(DatabaseMessage dm) {
    cache.updateJob(dm.getUuid());
    if (System.currentTimeMillis() - connection.getLastUpdate() > UPDATE_INTERVAL ){
      connection.updateJob(cache.getJob(dm.getUuid()));
    }
  }

  public void finishJob(DatabaseMessage dm) {
    cache.finishJob(dm.getUuid());
    connection.updateJob(cache.getJob(dm.getUuid()));
    cache.clear(dm.getUuid());
  }

  public void resumeJob(DatabaseMessage dm) {
    cache.resumeJob(dm.getUuid());
    connection.updateJob(cache.getJob(dm.getUuid()));
  }

  public void cancelJob(DatabaseMessage dm) {
    cache.cancelJob(dm.getUuid());
    connection.deleteJob(dm.getUuid());
  }

  public void getJobs(DatabaseMessage dm) {
    List<Jobs> jobs = connection.getJobs();
    context.sendGui(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(jobs,pid));
  }

  @PreDestroy
  public void finish() {
    connection.close();
  }

}

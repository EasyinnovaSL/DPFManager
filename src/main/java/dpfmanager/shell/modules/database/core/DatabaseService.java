/**
 * <h1>DatabaseService.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.messages.CheckTaskMessage;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.server.messages.StatusMessage;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Adria Llorens on 20/04/2016.
 */
@Service(BasicConfig.SERVICE_DATABASE)
@Scope("singleton")
public class DatabaseService extends DpfService {

  private Integer pid;
  private DatabaseConnection connection;
  private DatabaseCache cache;

  @Resource(name = "parameters")
  private Map<String, String> parameters;

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
    cleanDatabase();
  }

  private void cleanDatabase() {
    connection.cleanJobs();
  }

  /**
   * Jobs
   */

  public void handleJobsMessage(JobsMessage dm) {
    if (dm.isNew()) {
      createJob(dm);
    } else if (dm.isInit()) {
      initJob(dm);
    } else if (dm.isUpdate()) {
      updateJob(dm);
    } else if (dm.isFinish()) {
      finishJob(dm);
    } else if (dm.isGet()) {
      getJobs();
    } else if (dm.isResume()) {
      resumeJob(dm);
    } else if (dm.isCancel()) {
      cancelJob(dm);
    } else if (dm.isEmpty()) {
      emptyJob(dm);
    } else if (dm.isPause()) {
      pauseJob(dm);
    } else if (dm.isStart()) {
      startJob(dm);
    }
  }

  public void createJob(JobsMessage dm) {
    // Get origin
    String origin = "GUI";
    if (!context.isGui()) {
      origin = parameters.get("mode");
    }
    int state = 1;
    if (dm.getPending()) {
      state = 0;
    }
    cache.insertNewJob(dm.getUuid(), state, 0, dm.getInput(), origin, pid, "");
    connection.insertNewJob(cache.getJob(dm.getUuid()));

    // Now force refresh tasks
    getJobs();
  }

  public void initJob(JobsMessage dm) {
    cache.initJob(dm.getUuid(), dm.getTotal(), dm.getOutput());
    forceSyncDatabase();
  }

  public void startJob(JobsMessage dm) {
    cache.startJob(dm.getUuid());
    forceSyncDatabase();
  }

  public void updateJob(JobsMessage dm) {
    cache.incressProcessed(dm.getUuid());
    syncDatabase();
  }

  public void finishJob(JobsMessage dm) {
    cache.finishJob(dm.getUuid());
    forceSyncDatabase();
    cache.clearJob(dm.getUuid());
  }

  public void resumeJob(JobsMessage dm) {
    cache.resumeJob(dm.getUuid());
    forceSyncDatabase();
  }

  public void cancelJob(JobsMessage dm) {
    if (cache.containsJob(dm.getUuid())) {
      cache.cancelJob(dm.getUuid());
      forceSyncDatabase();
      cache.clearJob(dm.getUuid());
    }
  }

  public void emptyJob(JobsMessage dm) {
    if (cache.containsJob(dm.getUuid())) {
      cache.emptyJob(dm.getUuid());
      forceSyncDatabase();
    }
  }

  public void pauseJob(JobsMessage dm) {
    if (cache.containsJob(dm.getUuid())) {
      cache.pauseJob(dm.getUuid());
      forceSyncDatabase();
    }
  }

  public StatusMessage getJobStatus(Long id) {
    Jobs job = connection.getJob(id);
    StatusMessage.Status status = StatusMessage.Status.RUNNING;
    if (job.getState() == 2) {
      status = StatusMessage.Status.FINISHED;
    } else if (job.getState() == -1) {
      status = StatusMessage.Status.NOTFOUND;
    }
    return new StatusMessage(status, job.getOutput(), job.getProcessedFiles(), job.getTotalFiles());
  }

  public StatusMessage getJobStatusByHash(String hash) {
    Jobs job = connection.getJob(hash);
    StatusMessage.Status status = StatusMessage.Status.RUNNING;
    if (job.getState() == 2) {
      status = StatusMessage.Status.FINISHED;
    } else if (job.getState() == -1) {
      status = StatusMessage.Status.NOTFOUND;
    }
    return new StatusMessage(status, job.getOutput(), job.getProcessedFiles(), job.getTotalFiles());
  }

  public void getJobs() {
    List<Jobs> jobs = connection.getJobs();
    context.sendGui(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(jobs, pid));
  }

  /**
   * Sync functions
   */
  private void syncDatabase() {
    if (System.currentTimeMillis() - connection.getLastUpdate() > DpFManagerConstants.UPDATE_INTERVAL) {
      forceSyncDatabase();
    }
  }

  private void forceSyncDatabase() {
    connection.updateJobs(cache.getJobs());
  }

}

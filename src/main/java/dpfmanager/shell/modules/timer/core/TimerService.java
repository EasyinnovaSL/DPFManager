/**
 * <h1>TimerService.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.timer.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.timer.tasks.JobsStatusTask;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adria Llorens on 07/04/2016.
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

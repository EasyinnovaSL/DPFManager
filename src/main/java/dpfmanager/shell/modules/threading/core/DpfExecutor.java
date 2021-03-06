/**
 * <h1>DpfExecutor.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.threading.core;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.messages.CheckTaskMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adria Llorens on 04/05/2016.
 */
public class DpfExecutor extends ThreadPoolExecutor {

  /** Dpf Context */
  private DpfContext context;

  /** Pending checks */
  private Map<Long,List<Runnable>> pending;

  /** Cancelled checks */
  private List<Long> cancelled;

  /** Running threads of each check */
  private Map<Long,Integer> runningThreads;

  /** Semaphore */
  private Semaphore semaphore;

  public DpfExecutor(int corePoolSize) {
    super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    pending = new HashMap<>();
    runningThreads = new HashMap<>();
    cancelled = new ArrayList<>();
    semaphore = new Semaphore(1);
  }

  public void handleContext(DpfContext context) {
    this.context = context;
  }

  public void myExecute(DpfRunnable run){
    if (pending.containsKey(run.getUuid())){
      // Check is paused
      pending.get(run.getUuid()).add(run);
    } else if (!cancelled.contains(run.getUuid())){
      // If no cancelled, add to executor
      execute(run);
    }
  }

  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    // Block execution
    acquireSemaphore();

    // Increment threads count
    DpfRunnable run = (DpfRunnable) r;
    if (!runningThreads.containsKey(run.getUuid())){
      runningThreads.put(run.getUuid(), 1);
    } else {
      runningThreads.put(run.getUuid(), runningThreads.get(run.getUuid())+1);
    }

    // Unblock execution
    releaseSemaphore();
  }


  @Override
  protected void afterExecute(Runnable r, Throwable t) {
    // Block execution
    acquireSemaphore();

    DpfRunnable run = (DpfRunnable) r;
    Long uuid = run.getUuid();

    // Decrement threads count
    if (runningThreads.containsKey(uuid)){
      runningThreads.put(uuid, runningThreads.get(uuid) - 1);
    }

    // Check if the last waiting for pause
    if (pending.containsKey(uuid) && runningThreads.get(uuid) == 0){
      context.send(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Target.PAUSE, uuid));
    }

    // Check if the last waiting for cancel
    if (cancelled.contains(uuid) && runningThreads.get(uuid) == 0) {
      context.send(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Target.CANCEL, uuid));
    }

    // Unblock execution
    releaseSemaphore();
  }

  public void cancel(Long uuid) {
    // Block execution
    acquireSemaphore();
    cancelled.add(uuid);

    // Get tasks to remove
    List<Runnable> removes = new ArrayList<>();
    for (Runnable run : getQueue()){
      DpfRunnable dpfRun = (DpfRunnable) run;
      if (dpfRun.getUuid().equals(uuid)){
        removes.add(run);
      }
    }

    // Remove them from queue
    for (Runnable run : removes){
      getQueue().remove(run);
    }

    // Check if the tasks were paused or running
    if (pending.containsKey(uuid)){
      pending.remove(uuid);
      context.send(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Target.CANCEL, uuid));
    } else if (!runningThreads.containsKey(uuid) || runningThreads.get(uuid) == 0){
      context.send(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Target.CANCEL, uuid));
    }

    // Unblock execution
    releaseSemaphore();
  }

  public void pause(Long uuid) {
    // Block execution
    acquireSemaphore();

    // Get tasks to pause
    List<Runnable> pauses = new ArrayList<>();
    for (Runnable run : getQueue()) {
      DpfRunnable dpfRun = (DpfRunnable) run;
      if (dpfRun.getUuid().equals(uuid)) {
        pauses.add(run);
      }
    }

    // Remove them from queue
    for (Runnable run : pauses) {
      getQueue().remove(run);
    }

    // Save into pending tasks
    pending.put(uuid, pauses);

    // Check if no tasks were running
    if (!runningThreads.containsKey(uuid) || runningThreads.get(uuid) == 0){
      context.send(GuiConfig.COMPONENT_PANE, new CheckTaskMessage(CheckTaskMessage.Target.PAUSE, uuid));
    }

    // Unblock execution
    releaseSemaphore();
  }

  public void resume(Long uuid) {
    // Add to queue
    List<Runnable> resumes = pending.get(uuid);
    getQueue().addAll(resumes);
    pending.remove(uuid);
  }

  private void sleep(int milis){
    try {
      Thread.sleep(milis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Semaphore functions
   */
  private void acquireSemaphore(){
    semaphore.acquireUninterruptibly();
  }

  private void releaseSemaphore(){
    semaphore.release();
  }

}

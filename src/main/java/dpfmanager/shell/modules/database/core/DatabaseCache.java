/**
 * <h1>DatabaseCache.java</h1> <p> This program is free software: you can redistribute it
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

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.tables.Jobs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adria Llorens on 25/04/2016.
 */
public class DatabaseCache {

  private Map<Long, Jobs> jobs;
  private DpfContext context;

  public DatabaseCache(DpfContext c) {
    context = c;
    jobs = new HashMap<>();
  }

  /**
   * Jobs
   */
  public void insertNewJob(Long uuid, int state, int total, String input, String origin, int pid, String output) {
    Long current = System.currentTimeMillis();
    Jobs job = new Jobs();
    job.setId(uuid);
    String hash = Hashing.sha256().newHasher().putString(uuid.toString(), Charsets.UTF_8).hash().toString();
    job.setHash(hash);
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
      job.setLastUpdate(System.currentTimeMillis());
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

  public void emptyJob(Long uuid) {
    Jobs job = jobs.get(uuid);
    job.setState(5);
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

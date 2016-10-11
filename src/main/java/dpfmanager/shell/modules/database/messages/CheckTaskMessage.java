/**
 * <h1>CheckTaskMessage.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

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

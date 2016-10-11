/**
 * <h1>StatusMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.server.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class StatusMessage extends DpfMessage {

  public enum Status {
    RUNNING, FINISHED, NOTFOUND
  }

  private Status status;
  private String folder;
  private Integer processed;
  private Integer total;

  public StatusMessage(Status s, String f, Integer p, Integer t) {
    status = s;
    folder = f;
    processed = p;
    total = t;
  }

  public boolean isRunning() {
    return status.equals(Status.RUNNING);
  }

  public boolean isFinished() {
    return status.equals(Status.FINISHED);
  }

  public String getFolder() {
    return folder;
  }

  public Integer getProcessed() {
    return processed;
  }

  public Integer getTotal() {
    return total;
  }
}

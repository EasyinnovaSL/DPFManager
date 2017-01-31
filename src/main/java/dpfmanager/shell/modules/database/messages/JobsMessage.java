/**
 * <h1>JobsMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.database.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adria Llorens on 20/04/2016.
 */
public class JobsMessage extends DpfMessage {

  public enum Type {
    NEW, INIT, UPDATE, FINISH, GET, RESUME, CANCEL, PAUSE, START
  }

  private Type type;
  private Long uuid;
  private int total;
  private String input;
  private String output;
  private boolean pending;

  public JobsMessage(Type type, Long uuid, String input, boolean pending) {
    // New
    this.type = type;
    this.uuid = uuid;
    this.input = input;
    this.pending = pending;
  }

  public JobsMessage(Type type, Long uuid, int total, String output) {
    // Init
    this.type = type;
    this.uuid = uuid;
    this.total = total;
    this.output = output;
  }

  public JobsMessage(Type type, Long uuid) {
    // Update && Finish && Resume && Cancel && Pause && Start
    this.type = type;
    this.uuid = uuid;
  }

  public JobsMessage(Type type) {
    // Get
    this.type = type;
  }

  public boolean isInit(){
    return type.equals(Type.INIT);
  }

  public boolean isNew(){
    return type.equals(Type.NEW);
  }

  public boolean isUpdate(){
    return type.equals(Type.UPDATE);
  }

  public boolean isFinish(){
    return type.equals(Type.FINISH);
  }

  public boolean isGet(){
    return type.equals(Type.GET);
  }

  public boolean isResume(){
    return type.equals(Type.RESUME);
  }

  public boolean isCancel(){
    return type.equals(Type.CANCEL);
  }

  public boolean isPause(){
    return type.equals(Type.PAUSE);
  }

  public boolean isStart(){
    return type.equals(Type.START);
  }

  public Long getUuid() {
    return uuid;
  }

  public int getTotal() {
    return total;
  }

  public String getInput() {
    return input;
  }

  public String getOutput() {
    return output;
  }

  public boolean getPending() {
    return pending;
  }
}

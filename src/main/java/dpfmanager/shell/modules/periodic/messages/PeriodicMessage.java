/**
 * <h1>PeriodicMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.periodic.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicCheck;

import java.util.List;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class PeriodicMessage extends DpfMessage {

  public enum Type {
    SAVE, EDIT, DELETE, READ, LIST
  }

  private Type type;
  private String uuid;
  private boolean result;
  private PeriodicCheck check;
  private List<PeriodicCheck> periodicChecks;

  public PeriodicMessage(Type type) {
    // Read
    this.type = type;
  }

  public PeriodicMessage(Type type, PeriodicCheck check) {
    // Save & Edit
    this.type = type;
    this.check = check;
  }

  public PeriodicMessage(Type type, String uuid) {
    // Delete
    this.type = type;
    this.uuid = uuid;
  }

  public PeriodicMessage(Type type, String uuid, boolean result) {
    // Delete response
    this.type = type;
    this.uuid = uuid;
    this.result = result;
  }

  public PeriodicMessage(Type type, List<PeriodicCheck> periodicChecks) {
    // List
    this.type = type;
    this.periodicChecks = periodicChecks;
  }

  public boolean isSave(){
    return type.equals(Type.SAVE);
  }

  public boolean isEdit(){
    return type.equals(Type.EDIT);
  }

  public boolean isDelete(){
    return type.equals(Type.DELETE);
  }

  public boolean isRead(){
    return type.equals(Type.READ);
  }

  public boolean isList(){
    return type.equals(Type.LIST);
  }

  public String getUuid() {
    return uuid;
  }

  public boolean getResult() {
    return result;
  }

  public PeriodicCheck getPeriodicCheck() {
    return check;
  }

  public List<PeriodicCheck> getPeriodicChecks() {
    return periodicChecks;
  }
}

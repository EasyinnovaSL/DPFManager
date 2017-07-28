/**
 * <h1>GlobalReportMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.global.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.report.util.ReportIndividualGui;

/**
 * Created by Adria Llorens on 24/03/2016.
 */
public class GuiGlobalMessage extends DpfMessage {

  public enum Type {
    INIT, SORT, ADD_INDIVIDUAL, READ
  }

  private Type type;
  private ReportGui reportGui;
  private String vboxId;
  private ReportIndividualGui reportIndividualGui;

  // Init
  public GuiGlobalMessage(Type t, ReportGui rg) {
    type = t;
    reportGui = rg;
  }

  // Add individual
  public GuiGlobalMessage(Type t, String i, ReportIndividualGui rig) {
    type = t;
    vboxId = i;
    reportIndividualGui = rig;
  }

  // Sort
  public GuiGlobalMessage(Type t) {
    type = t;
  }

  // Read
  public void setType(Type type) {
    this.type = type;
  }

  public boolean isInit(){
    return type.equals(Type.INIT);
  }

  public boolean isRead(){
    return type.equals(Type.READ);
  }

  public boolean isSort(){
    return type.equals(Type.SORT);
  }

  public boolean isAddIndividual(){
    return type.equals(Type.ADD_INDIVIDUAL);
  }

  public ReportGui getReportGui() {
    return reportGui;
  }

  public ReportIndividualGui getReportIndividualGui() {
    return reportIndividualGui;
  }

  public String getVboxId() {
    return vboxId;
  }
}

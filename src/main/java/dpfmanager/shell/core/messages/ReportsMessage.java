/**
 * <h1>ReportsMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.core.messages;

import dpfmanager.shell.modules.report.util.ReportGui;

/**
 * Created by Adria Llorens on 04/03/2016.
 */
public class ReportsMessage extends DpfMessage {
  public enum Type {
    RELOAD,
    READ,
    DELETE,
    ADD
  }

  private Type type;
  private String uuid;
  private ReportGui report;

  // READ & RELOAD
  public ReportsMessage(Type t) {
    type = t;
  }

  // DELETE
  public ReportsMessage(Type t, String u) {
    type = t;
    uuid = u;
  }

  // ADD
  public ReportsMessage(Type t, ReportGui r) {
    type = t;
    report = r;
  }

  public Type getType() {
    return type;
  }

  public boolean isReload() {
    return type.equals(Type.RELOAD);
  }

  public boolean isRead() {
    return type.equals(Type.READ);
  }

  public boolean isDelete() {
    return type.equals(Type.DELETE);
  }

  public boolean isAdd() {
    return type.equals(Type.ADD);
  }

  public String getUuid() {
    return uuid;
  }

  public ReportGui getReportGui() {
    return report;
  }
}
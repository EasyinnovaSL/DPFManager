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

package dpfmanager.shell.modules.statistics.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.statistics.core.StatisticsObject;

import java.time.LocalDate;

/**
 * Created by Adria Llorens on 24/03/2016.
 */
public class StatisticsMessage extends DpfMessage {

  public enum Type {
    GENERATE, RENDER
  }

  private Type type;
  private StatisticsObject object;
  private LocalDate from;
  private LocalDate to;
  private String path;

  // Generate
  public StatisticsMessage(Type t, LocalDate f, LocalDate t2, String p) {
    type = t;
    from = f;
    to = t2;
    path = p;
  }

  // Render
  public StatisticsMessage(Type t, StatisticsObject o) {
    type = t;
    object = o;
  }

  public StatisticsObject getStatisticsObject() {
    return object;
  }

  public LocalDate getFrom() {
    return from;
  }

  public LocalDate getTo() {
    return to;
  }

  public String getPath() {
    return path;
  }

  public boolean isGenerate() {
    return type.equals(Type.GENERATE);
  }

  public boolean isRender() {
    return type.equals(Type.RENDER);
  }

}

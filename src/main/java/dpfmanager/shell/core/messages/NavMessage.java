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
public class NavMessage extends DpfMessage {

  public enum Selected {
    TABLE,
    REPORT,
    SPECIFIC,
    TEXT,
    PDF,
    RELOAD
  }

  private Selected selected;
  private String text;
  private Integer count;
  private Integer max;

  public NavMessage(Selected s) {
    selected = s;
  }

  public NavMessage(String s) {
    selected = Selected.TEXT;
    text = s;
  }

  public NavMessage(Integer c, Integer m) {
    selected = Selected.PDF;
    count = c;
    max = m;
  }

  public String getText() {
    return text;
  }

  public boolean isTable() {
    return selected.equals(Selected.TABLE);
  }

  public boolean isReport() {
    return selected.equals(Selected.REPORT);
  }

  public boolean isSpecific() {
    return selected.equals(Selected.SPECIFIC);
  }

  public boolean isText() {
    return selected.equals(Selected.TEXT);
  }

  public boolean isPdf() {
    return selected.equals(Selected.PDF);
  }

  public boolean isReload() {
    return selected.equals(Selected.RELOAD);
  }

  public Integer getCount() {
    return count;
  }

  public Integer getMax() {
    return max;
  }
}
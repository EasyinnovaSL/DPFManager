/**
 * <h1>ShowReport.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 */

package dpfmanager.shell.interfaces.gui.component.show;

import dpfmanager.shell.modules.report.runnable.MakeReportRunnable;

/**
 * Created by Adrià Llorens on 15/06/2017.
 */
public class ShowReport {

  public Long uuid;
  public Integer count = 0;
  public Integer max = 0;
  public Integer globalValue = 0;
  public MakeReportRunnable mrr = null;
  public boolean finished = false;
  public boolean onlyGlobal = false;

  public ShowReport(Long u){
    uuid = u;
  }

}

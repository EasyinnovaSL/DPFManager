/**
 * <h1>ReportsModel.java</h1> <p> This program is free software: you can redistribute it and/or
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
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.interfaces.gui.fragment.ReportFragment;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.util.ReportGui;

import org.apache.commons.io.FileUtils;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Adria Llorens on 04/03/2016.
 */
public class ReportsModel extends DpfModel<ReportsView, ReportsController> {

  private Context context;
  private ResourceBundle bundle;

  /**
   * Constructor
   */
  public ReportsModel(Context ctx) {
    context = ctx;
    sizeTime = 0L;
    bundle = DPFManagerProperties.getBundle();
  }

  private Long reportsSize;
  private Long sizeTime;

  public void readReportsSize() {
    Long currentTime = System.currentTimeMillis();
    if ((currentTime - sizeTime) > 60000L){
      reportsSize = FileUtils.sizeOfDirectory(new File(DPFManagerProperties.getReportsDir()));
      sizeTime = currentTime;
    }
  }

  public Long getReportsSize() {
    return reportsSize;
  }

}

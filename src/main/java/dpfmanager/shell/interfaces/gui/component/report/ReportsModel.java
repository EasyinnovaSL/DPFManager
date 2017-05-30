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
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Adria Llorens on 04/03/2016.
 */
public class ReportsModel extends DpfModel<ReportsView, ReportsController> {

  private Context context;
  private ResourceBundle bundle;

  private SortedSet<ReportGui> data;
  private List<ManagedFragmentHandler<ReportFragment>> reportsFragments;

  private int reports_loaded = 0;
  public static int reports_to_load = 25;

  /**
   * Constructor
   */
  public ReportsModel(Context ctx) {
    context = ctx;
    bundle = DPFManagerProperties.getBundle();
    data = new TreeSet<>();
    reportsFragments = new ArrayList<>();
    loadReportsFromDir();
  }

  public void addReportFragment(ManagedFragmentHandler<ReportFragment> handler) {
    reportsFragments.add(handler);
  }

  public void removeReportFragment(ManagedFragmentHandler<ReportFragment> handler) {
    reportsFragments.remove(handler);
  }

  public ManagedFragmentHandler<ReportFragment> getReportGuiByUuid(String uuid) {
    for (ManagedFragmentHandler<ReportFragment> handler : reportsFragments) {
      if (handler.getController().getUuid().equals(uuid)) {
        return handler;
      }
    }
    return null;
  }

  public void loadReportsFromDir() {
    String baseDir = ReportGenerator.getReportsFolder();
    File reportsDir = new File(baseDir);
    if (reportsDir.exists()) {
      String[] directories = reportsDir.list((current, name) -> new File(current, name).isDirectory());
      for (int i = 0; i < directories.length; i++) {
        String reportDay = directories[i];
        File reportDayFile = new File(baseDir + "/" + reportDay);
        String[] directoriesDay = reportDayFile.list((current, name) -> new File(current, name).isDirectory());
        for (int j = 0; j < directoriesDay.length; j++) {
          String reportDir = directoriesDay[j];
          ReportGui rg = new ReportGui(baseDir, reportDay, reportDir);
          if (rg.exists() && !data.contains(rg)) {
            data.add(rg);
          }
        }
      }
    }
  }

  public void printReports() {
    printReports(0, reports_to_load);
  }

  public void printMoreReports() {
    printReports(reports_loaded, reports_loaded + reports_to_load);
  }

  public void printReports(int from, int to) {
    int index = 0;
    int loaded = 0;
    Iterator<ReportGui> it = data.iterator();
    while (it.hasNext()) {
      ReportGui rg = it.next();
      if (index >= from && index < to) {
        rg.load();
        rg.setLast(!it.hasNext() || index == to - 1);
        getView().getContext().send(new ReportsMessage(ReportsMessage.Type.ADD, rg));
        reports_loaded++;
        loaded++;
      }
      index++;
    }
    if (loaded == 0) {
      getView().hideLoading();
      getView().calculateMinHeight();
    }
  }

  public boolean isAllReportsLoaded() {
    return reports_loaded == data.size();
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public void clearData() {
    data.clear();
  }

  public void clearReportsLoaded() {
    reports_loaded = 0;
  }

  private Long reportsSize;

  public void readReportsSize() {
    reportsSize = FileUtils.sizeOfDirectory(new File(DPFManagerProperties.getReportsDir()));
  }

  public Long getReportsSize() {
    return reportsSize;
  }

}

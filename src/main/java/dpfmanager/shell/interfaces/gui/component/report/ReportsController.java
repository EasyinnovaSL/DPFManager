/**
 * <h1>ReportsController.java</h1> <p> This program is free software: you can redistribute it and/or
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
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.interfaces.gui.component.report.comparators.ReportsComparator;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.util.ReportGui;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adria Llorens on 07/03/2016.
 */
public class ReportsController extends DpfController<ReportsModel, ReportsView> {

  public static Integer itemsPerPage = 15;

  private Map<String, ReportGui> dataCache;
  private List<ReportGui> data;

  public ReportsController() {
    data = new ArrayList<>();
    dataCache = new HashMap<>();
  }

  public boolean clearReports(LocalDate date) {
    try {
      File reports = new File(DPFManagerProperties.getReportsDir());
      for (File folder : reports.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
        LocalDate folderDate = parseFolderName(folder.getName());
        if (folderDate != null && folderDate.isBefore(date)) {
          FileUtils.deleteDirectory(folder);
        }
      }
      return true;
    } catch (Exception e){
      return false;
    }
  }

  private LocalDate parseFolderName(String folder) {
    try {
      return LocalDate.parse(folder, DateTimeFormatter.ofPattern("yyyyMMdd"));
    } catch (Exception e) {
      return null;
    }
  }

  public void readReports(){
    data.clear();
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
          if (dataCache.containsKey(rg.getUuid())) {
            data.add(dataCache.get(rg.getUuid()));
          } else if (rg.exists()) {
            dataCache.put(rg.getUuid(), rg);
            data.add(rg);
          }
        }
      }
    }
    sortReports();
  }

  public void removeReport(String uuid){
    ReportGui rg = dataCache.get(uuid);
    if (rg != null){
      data.remove(rg);
      dataCache.remove(uuid);
    }
  }

  public void sortReports(){
    data.sort(new ReportsComparator(getView().getCurrentMode(), getView().getCurrentOrder()));
  }

  public void loadAndPrintReports(String vboxId, int page){
    int init = page * itemsPerPage;
    int end = init + itemsPerPage;
    int i = init;
    while (i < data.size() && i < end) {
      ReportGui rg = data.get(i);
      rg.setLast(i == data.size() - 1 || i == end - 1);
      getContext().send(new ReportsMessage(ReportsMessage.Type.ADD, vboxId, rg));
      i++;
    }
  }

  public Integer getPagesCount(){
    int size = data.size();
    int pages = size / itemsPerPage;
    if (size % itemsPerPage > 0) pages++;
    return pages;
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public void clearData() {
    data.clear();
  }
}

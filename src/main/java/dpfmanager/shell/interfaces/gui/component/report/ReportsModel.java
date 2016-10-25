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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.util.ReportRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class ReportsModel extends DpfModel<ReportsView, ReportsController> {

  private boolean all_reports_loaded = false;
  public static int reports_loaded = 25;
  private ObservableList<ReportRow> data;
  private boolean reload;

  public ReportsModel() {
    reload = true;
    data = FXCollections.observableArrayList(new ArrayList<>());
  }

  public void readIfNeed() {
    if (reload) {
      clearData();
      readReports();
      reload = false;
    }
  }

  public void readReports() {
    int start = getData().size() - 1;
    if (start < 0) {
      start = 0;
    }
    int count = reports_loaded;

    ObservableList<ReportRow> rows = FXCollections.observableArrayList(new ArrayList<>());
    String baseDir = ReportGenerator.getReportsFolder();
    File reportsDir = new File(baseDir);
    if (reportsDir.exists()) {
      String[] directories = reportsDir.list((current, name) -> new File(current, name).isDirectory());
      Arrays.sort(directories, Collections.reverseOrder());
      int index = 0;
      for (int i = 0; i < directories.length; i++) {
        String reportDay = directories[i];
        File reportsDay = new File(baseDir + "/" + reportDay);
        String[] directories2 = reportsDay.list((current, name) -> new File(current, name).isDirectory());

        // Convert to ints for ordering
        Integer[] int_directories = new Integer[directories2.length];
        for (int j = 0; j < directories2.length; j++) {
          int_directories[j] = Integer.parseInt(directories2[j]);
        }
        Arrays.sort(int_directories, Collections.reverseOrder());

        if (index + directories2.length >= start) {
          String[] available_formats = {"html", "xml", "json", "pdf"};
          for (int j = 0; j < int_directories.length; j++) {
            String reportDir = String.valueOf(int_directories[j]);
            if (index >= start && index < start + count) {
              ReportRow rr = null;
              File reportXml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.xml");
              File reportJson = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.json");
              File reportHtml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.html");
              File reportPdf = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.pdf");

              if (reportXml.exists() && reportXml.length() > 0) {
                rr = ReportRow.createRowFromXml(reportDay, reportXml, getBundle());
              }
              if (rr == null && reportJson.exists() && reportJson.length() > 0) {
                rr = ReportRow.createRowFromJson(reportDay, reportJson, getBundle());
              }
              if (rr == null && reportHtml.exists() && reportHtml.length() > 0) {
                rr = ReportRow.createRowFromHtml(reportDay, reportHtml, getBundle());
              }
              if (rr == null && reportPdf.exists() && reportPdf.length() > 0) {
                rr = ReportRow.createRowFromPdf(reportDay, reportPdf, getBundle());
              }

              if (rr != null) {
                // Add formats
                for (String format : available_formats) {
                  File report;
                  if (format == "json" || format == "xml") {
                    report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary." + format);
                  } else {
                    report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report." + format);
                  }
                  if (report.exists() && report.length() > 0) {
                    rr.addFormat(format, report.getPath());
                  }
                }
                // Add mets
                File folder = new File(baseDir + "/" + reportDay + "/" + reportDir + "/");
                if (folder.exists() && folder.isDirectory()) {
                  String[] filter = {"mets.xml"};
                  Collection<File> childs = FileUtils.listFiles(folder, filter, false);
                  if (childs.size() > 0) {
                    rr.addFormat("mets", folder.getPath());
                  }
                }
                rows.add(rr);
                index++;
              }

              // Check if all done
              if (i == directories.length - 1 && j == directories2.length - 1) {
                all_reports_loaded = true;
              }
            } else {
              index++;
            }
          }

        } else {
          index += directories2.length;
        }
      }
    }
    data.addAll(rows);
  }

  public String getReportsSize() {
    Long bytes = FileUtils.sizeOfDirectory(new File(DPFManagerProperties.getReportsDir()));
    return readableFileSize(bytes);
  }

  private String readableFileSize(long size) {
    if (size <= 0) return "0";
    final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }

  public void setReload(boolean r) {
    reload = r;
    all_reports_loaded = false;
  }

  public void clearData() {
    data.clear();
  }

  public ObservableList<ReportRow> getData() {
    return data;
  }

  public boolean isAllReportsLoaded() {
    return all_reports_loaded;
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  public void removeItem(String search) {
    data.remove(getItemById(search));
  }

  public ReportRow getItemById(String search) {
    for (ReportRow rr : data) {
      if (rr.getUuid().equals(search)) {
        return rr;
      }
    }
    return null;
  }

}

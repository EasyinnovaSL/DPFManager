package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.util.ReportRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class ReportsModel extends DpfModel<ReportsView, ReportsController>{

  private boolean empty = true;
  private boolean all_reports_loaded = false;
  public static int reports_loaded = 25;
  private ObservableList<ReportRow> data;
  private boolean reload;

  public ReportsModel(){
    reload = true;
    data = FXCollections.observableArrayList(new ArrayList<>());
  }

  public void readIfNeed(){
    if (reload){
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
                rows.add(rr);
                index++;
              }

              // Check if all done
              if (i == directories.length - 1 && j == directories2.length - 1) {
                all_reports_loaded = true;
              }
              empty = false;
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

  public void setReload(boolean r){
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

  public boolean isEmpty(){
    return empty;
  }

}

/**
 * <h1>ReportService.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.modules.statistics.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;
import dpfmanager.shell.modules.statistics.model.HistogramTag;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;
import dpfmanager.shell.modules.statistics.model.StatisticsRule;
import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_STATISTICS)
@Scope("singleton")
public class StatisticsService extends DpfService {

  private List<String> repeatedList;

  private ResourceBundle bundle;
  
  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
  }

  @Override
  protected void handleContext(DpfContext context) {
  }

  public StatisticsObject generateStatistics(LocalDate from, LocalDate to, String path) {
    repeatedList = new ArrayList<>();
    StatisticsObject so = new StatisticsObject();
    so.setReportsCount(countGlobalReportsFromDir(from, to, path));
    for (IndividualReport ir : loadIndividualReportsFromDir(from, to, path)) {
      so.parseIndividualReport(ir);
    }
    return so;
  }

  private Integer countGlobalReportsFromDir(LocalDate from, LocalDate to, String search) {
    int count = 0;
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
          File serializedGlobalFile = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.ser");
          if (serializedGlobalFile.exists() && complainsDate(serializedGlobalFile, from, to)){
            GlobalReport gr = (GlobalReport) GlobalReport.read(serializedGlobalFile.getAbsolutePath());
            if (gr != null && complainsPath(gr, search)) {
              count++;
            }
          }
        }
      }
    }
    return count;
  }

  private List<IndividualReport> loadIndividualReportsFromDir(LocalDate from, LocalDate to, String search) {
    List<IndividualReport> irList = new ArrayList<>();
    String baseDir = ReportGenerator.getReportsFolder();
    File reportsDir = new File(baseDir);
    if (reportsDir.exists()) {
      String[] directories = reportsDir.list((current, name) -> new File(current, name).isDirectory());
      for (int i = directories.length-1; i >= 0; i--) {
        String reportDay = directories[i];
        File reportDayFile = new File(baseDir + "/" + reportDay);
        String[] directoriesDay = reportDayFile.list((current, name) -> new File(current, name).isDirectory());
        for (int j =  directoriesDay.length-1; j >= 0; j--) {
          String reportDir = directoriesDay[j];
          File reportDirFile = new File(baseDir + "/" + reportDay + "/" + reportDir + "/serialized");
          String[] serializedReports = reportDirFile.list((current, name) -> new File(current, name).isFile());
          if (serializedReports == null) continue;
          for (int k = 0; k < serializedReports.length; k++) {
            String reportSer = serializedReports[k];
            File serializedIndividualFile = new File(baseDir + "/" + reportDay + "/" + reportDir + "/serialized/" + reportSer);
            if (serializedIndividualFile.exists() && complainsDate(serializedIndividualFile, from, to)){
              IndividualReport ir = (IndividualReport) IndividualReport.read(baseDir + "/" + reportDay + "/" + reportDir + "/serialized/" + reportSer);
              if (ir != null && complainsPath(ir, search) && complainsRepeated(ir)) {
                irList.add(ir);
              }
            }
          }
        }
      }
    }
    return irList;
  }

  private Long getTimestamp(File file) {
    try {
      BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class);
      return attr.creationTime().toMillis();
    } catch (Exception e) {
      return 0L;
    }
  }

  /**
   * Filters
   */
  private boolean complainsDate(File file, LocalDate from, LocalDate to){
    Long timestamp = getTimestamp(file);
    LocalDate date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    return (to == null || date.isBefore(to) || date.isEqual(to))
        && (from == null || date.isAfter(from) || date.isEqual(from));
  }

  private boolean complainsPath(GlobalReport gr, String search){
    for(SmallIndividualReport ir : gr.getIndividualReports()){
      String path = ir.getFilePath();
      if (complainsPath(path, search)) return true;
    }
    return false;
  }

  private boolean complainsPath(IndividualReport ir, String search){
    String path = ir.getFilePath();
    return complainsPath(path, search);
  }

  private boolean complainsPath(String path, String search){
    if (search == null) return true;
    String normalizedPath = path.replaceAll("\\\\","/").toUpperCase();
    String normalizedSearch = search.replaceAll("\\\\", "/").toUpperCase();
    return normalizedPath.contains(normalizedSearch);
  }

  private boolean complainsRepeated(IndividualReport ir){
    if (repeatedList.contains(ir.getFilePath())){
      return false;
    }
    repeatedList.add(ir.getFilePath());
    return true;
  }

  /**
   * Print Statistics in console
   */
  public void printResults(StatisticsObject so){
    printOut("");

    // EMPTY
    if (so.getReportsCount() == 0){
      printOut(bundle.getString("emptyStatistics"));
      return;
    }

    // SUMMARY
    printOut(bundle.getString("summaryTitle").toUpperCase());
    int max0 = getMaxLength(bundle.getString("sumRow11"), bundle.getString("sumRow21"), bundle.getString("sumRow31"), bundle.getString("sumRow41"),
        bundle.getString("sumRow12"), bundle.getString("sumRow22"), bundle.getString("sumRow32"));
    printOut("  " + String.format("%-" + String.valueOf(max0) + "s%s", bundle.getString("sumRow11"), "  " + so.getReportsCount()));
    printOut("  " + String.format("%-" + String.valueOf(max0) + "s%s", bundle.getString("sumRow21"), "  " + so.getTiffsCount()));
    Double tiffsPerReport = (so.getReportsCount() == 0) ? 0 : (so.getTiffsCount() * 1.0) / (so.getReportsCount() * 1.0);
    printOut("  " + String.format("%-" + String.valueOf(max0) + "s%s", bundle.getString("sumRow31"), "  " + parseDouble(tiffsPerReport, 1)));
    Long averageSize = (so.getTiffsCount() == 0) ? 0 : so.getTotalSize() / so.getTiffsCount();
    printOut("  " + String.format("%-" + String.valueOf(max0) + "s%s", bundle.getString("sumRow41"), "  " + readableFileSize(averageSize)));
    printOut("  " + String.format("%-" + String.valueOf(max0) + "s%s", bundle.getString("sumRow12"), "  " + so.getMainImagesCount()));
    Double mainIfdPerTiff = (so.getTiffsCount() == 0) ? 0 : (so.getMainImagesCount() * 1.0) / (so.getTiffsCount() * 1.0);
    printOut("  " + String.format("%-" + String.valueOf(max0) + "s%s", bundle.getString("sumRow22"), "  " + parseDouble(mainIfdPerTiff, 1)));
    printOut("  " + String.format("%-" + String.valueOf(max0) + "s%s", bundle.getString("sumRow32"), "  " + so.getThumbnailsCount()));
    printOut("");


    // TAGS
    if (so.getTags().size() == 0){
      printOut(bundle.getString("tagsTitle").toUpperCase());
      printOut("  " + bundle.getString("emptyTags"));
    } else {
      printOut(bundle.getString("tagsTitle").toUpperCase() + " " + bundle.getString("tagsInMain") );
      for(HistogramTag hTag : so.getTags().values()){
        if (hTag.main > 0) printOut("  " + hTag.tv.getName() + ":  " + hTag.main);
      }
      printOut("");
      printOut(bundle.getString("tagsTitle").toUpperCase() + " " + bundle.getString("tagsInThumb") );
      for(HistogramTag hTag : so.getTags().values()){
        if (hTag.thumb > 0) printOut("  " + hTag.tv.getName() + ":  " + hTag.thumb);
      }
      printOut("");
    }

    // ISOS
    int max = getMaxLength(bundle.getString("colErrors"), bundle.getString("colWarnings"), bundle.getString("colPassed")) + 2;
    printOut(bundle.getString("isosTitle").toUpperCase());
    for(String iso : so.getIsos().keySet()){
      StatisticsIso sIso = so.getIsos().get(iso);
      printOut("  " + sIso.iso + ":");
      printOut("    " + String.format("%-" + String.valueOf(max) + "s%s", bundle.getString("colErrors") + ": ", sIso.errors));
      printOut("    " + String.format("%-" + String.valueOf(max) + "s%s", bundle.getString("colWarnings") + ": ", sIso.warnings));
      printOut("    " + String.format("%-" + String.valueOf(max) + "s%s", bundle.getString("colPassed") + ": ", sIso.passed));
    }
    if (so.getIsos().size() == 0){
      printOut("  " + bundle.getString("emptyIsos"));
    }
    printOut("");

    // Policy checkers
    int max2 = getMaxLength(bundle.getString("colRType"), bundle.getString("colRTotal"), bundle.getString("colRError")) + 2;
    printOut(bundle.getString("policyTitle").toUpperCase());
    for(String key : so.getPolicys().keySet()){
      StatisticsRule sRule = so.getPolicys().get(key);
      printOut("  " + sRule.name + ":");
      printOut("    " + String.format("%-" + String.valueOf(max2) + "s%s", bundle.getString("colRType") + ": ", sRule.type));
      printOut("    " + String.format("%-" + String.valueOf(max2) + "s%s", bundle.getString("colRTotal") + ": ", sRule.total));
      printOut("    " + String.format("%-" + String.valueOf(max2) + "s%s", bundle.getString("colRError") + ": ", sRule.count));
    }
    if (so.getPolicys().size() == 0){
      printOut("  " + bundle.getString("emptyPolicy"));
    }
    printOut("");
  }

  private void printOut(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private String parseDouble(Double val, int max){
    NumberFormat nf = DecimalFormat.getInstance();
    nf.setMaximumFractionDigits(max);
    return nf.format(val);
  }

  private String readableFileSize(long size) {
    if (size <= 0) return "0";
    final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }

  private int getMaxLength(String... strs){
    int max = 0;
    for (String str : strs){
      int current = str.length();
      if (current > max) max = current;
    }
    return max;
  }
}

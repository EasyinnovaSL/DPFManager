/**
 * <h1>ReportRow.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
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

package dpfmanager.shell.modules.report.util;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by easy on 17/09/2015.
 */
public class ReportIndividualGui {

  private String[] available_formats = {"html", "xml", "json", "pdf"};

  private Integer id;
  private String path;
  private String name;
  private boolean loaded;
  private boolean error;

  private String filename;
  private Integer selectedIsos;
  private Integer errors;
  private Integer warnings;
  private Integer passed;
  private Integer score;
  private Integer reportId;
  private Map<String, String> formats;

  private Configuration config;
  private Integer reportVersion = 0;

  public ReportIndividualGui(String path, Configuration config, Integer id) {
    this.path = path;
    this.name = parseFileName(path);
    this.config = config;
    this.id = id;
    this.loaded = false;
    this.error = false;
  }

  private String parseFileName(String path){
    String serName = new File(path).getName();
    return serName.substring(serName.indexOf("-") + 1, serName.lastIndexOf("."));
  }

  public void load(){
    if (loaded) return;
    resetValues();
    createRowFromSer();
    readFormats();
  }

  private void resetValues(){
    errors = 0;
    passed = 0;
    warnings = 0;
    score = 0;
    filename = "";
  }

  private void createRowFromSer(){
    try {
      IndividualReport ir = (IndividualReport) IndividualReport.read(path);
      if (ir == null) {
        error = true;
        return;
      }
      if (ir.getVersion() != null) {
        reportVersion = ir.getVersion();
      }

      for (String iso : ir.getSelectedIsos()){
        if (ir.getNErrors(iso) > 0) errors++;
        else if (ir.getNWarnings(iso) > 0) warnings++;
      }
      Integer n = ir.getSelectedIsos().size();
      selectedIsos = ir.getSelectedIsos().size();
      passed = n - errors - warnings;
      filename = ir.getFileName();
      score = (n > 0) ? (passed + warnings) * 100 / n : 0;
      reportId = ir.getIdReport();
    } catch (Exception e) {
      error = true;
    }
  }

  public void readFormats() {
    formats = new HashMap<>();
    File baseFile = new File(path).getParentFile().getParentFile();
    if (!error && baseFile.exists() && baseFile.isDirectory()) {

      // Add formats
      for (String format : available_formats) {
        File report;
        if (format.equals("json") || format.equals("xml") || format.equals("pdf")) {
          report = new File(baseFile.getPath() + "/" + reportId + "-" + filename + "." + format);
        } else {
          report = new File(baseFile.getPath() + "/html/" + reportId + "-" + filename + "." + format);
        }
        if (report.exists() && report.length() > 0) {
          formats.put(format, report.getPath());
        }
      }

      // Add mets
      String[] filter = {"mets.xml"};
      Collection<File> childs = FileUtils.listFiles(baseFile, filter, false);
      for (File child : childs){
        if (child.getName().contains(reportId + "-" + filename + ".mets.xml")) {
          formats.put("mets", child.getPath());
        }
      }

      // All OK
      loaded = true;
    }
  }

  /**
   * Getters
   */

  public String getName() {
    return name;
  }
  public String getPath() {
    return path;
  }
  public Integer getErrors() {
    return errors;
  }
  public Integer getWarnings() {
    return warnings;
  }
  public Integer getPassed() {
    return passed;
  }
  public Integer getScore() {
    return score;
  }
  public Map<String, String> getFormats() {
    return formats;
  }
  public String getFilename() {
    return filename;
  }
  public Integer getReportVersion() {
    return reportVersion;
  }
  public Integer getSelectedIsos() {
    return selectedIsos;
  }
  public Configuration getConfig() {
    return config;
  }
  public Integer getId() {
    return id;
  }
}



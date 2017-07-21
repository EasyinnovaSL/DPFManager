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
import dpfmanager.shell.modules.report.core.SmallIndividualReport;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by easy on 17/09/2015.
 */
public class ReportIndividualGui {

  private String[] available_formats = {"html", "xml", "json", "pdf", "mets.xml"};

  private Integer id;
  private String path;
  private String name;
  private String filePath;
  private boolean loaded;
  private boolean loadedFormats;
  private boolean error;
  private boolean last;
  private boolean quick;
  private boolean old;

  private String filename;
  private Integer errors;
  private Integer warnings;
  private Integer reportId;
  private Map<String, String> formats;

  private Configuration config;
  private Integer reportVersion = 0;
  private SmallIndividualReport individual;

  public ReportIndividualGui(String path, Integer id) {
    this.id = id;
    this.path = path;
    this.loaded = false;
    this.error = false;
    this.last = false;
    this.old = true;
  }

  public ReportIndividualGui(SmallIndividualReport sir, Configuration config, Integer id) {
    this.individual = sir;
    this.path = sir.getSerPath();
    this.name = parseFileName(path);
    this.config = config;
    this.id = id;
    this.loaded = false;
    this.error = false;
    this.last = false;
    this.old = false;
  }

  public ReportIndividualGui(String path, Configuration config, Integer id) {
    this.individual = null;
    this.path = path;
    this.name = parseFileName(path);
    this.config = config;
    this.id = id;
    this.loaded = false;
    this.error = false;
    this.last = false;
    this.loadedFormats = false;
    this.old = false;
  }

  public void setLast(boolean last) {
    this.last = last;
  }

  private String parseFileName(String path){
    String serName = new File(path).getName();
    return serName.substring(serName.indexOf("-") + 1, serName.lastIndexOf("."));
  }

  public void load(){
    if (loaded) return;
    resetValues();
    createRow();
  }

  public void loadFormats(){
    if (!loaded) return;
    if (loadedFormats) return;
    readFormats();
  }

  private void resetValues(){
    errors = 0;
    warnings = 0;
    filename = "";
  }

  private void createRow(){
    if (old) {
      createRowFromOld();
    } else if (individual != null) {
      createRowFromSir();
    } else {
      createRowFromSer();
    }
  }

  private void createRowFromOld(){
    reportVersion = 0;
    filePath = path;
    name = new File(path).getName();
    reportId = Integer.parseInt(name.substring(0, name.indexOf("-")));
    filename = name.substring(name.indexOf("-") + 1, name.lastIndexOf("."));
    if (filename.endsWith(".mets")) {
      filename = filename.substring(0, filename.lastIndexOf("."));
    }
    quick = false;
    loaded = true;
  }

  private void createRowFromSir(){
    reportVersion = individual.getReportVersion();
    for (String iso : individual.getSelectedIsos()){
      errors += individual.getNErrors(iso);
      warnings += individual.getNWarnings(iso);
    }
    filename = individual.getFileName();
    reportId = individual.getIdReport();
    filePath = individual.getFilePath();
    quick = individual.isQuick();
    loaded = true;
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
        errors += ir.getNErrors(iso);
        warnings += ir.getNWarnings(iso);
      }
      filename = ir.getFileName();
      reportId = ir.getIdReport();
      filePath = ir.getFilePath();
      quick = ir.isQuick();
      loaded = true;
    } catch (Exception e) {
      error = true;
    }
  }

  public void readFormats() {
    formats = new HashMap<>();
    File pathFile = new File(path);
    File baseFile = pathFile.getParentFile();
    if (baseFile.getName().endsWith("serialized")) {
      baseFile = baseFile.getParentFile();
    }
    if (baseFile.getName().endsWith("html")) {
      baseFile = baseFile.getParentFile();
    }
    if (!error && baseFile.exists() && baseFile.isDirectory()) {

      // Add formats
      for (String format : available_formats) {
        File report;
        if (format.equals("json") || format.equals("xml") || format.equals("pdf") || format.equals("mets.xml")) {
          report = new File(baseFile.getPath() + "/" + reportId + "-" + filename + "." + format);
        } else {
          report = new File(baseFile.getPath() + "/html/" + reportId + "-" + filename + "." + format);
        }
        if (report.exists() && report.length() > 0) {
          format = format.equals("mets.xml") ? "mets" : format;
          formats.put(format, report.getPath());
        }
      }

      // All OK
      loadedFormats = true;
    }
  }

  /**
   * Getters
   */

  public String getName() {
    return name;
  }
  public String getLowerName() {
    return name.toLowerCase();
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
  public Map<String, String> getFormats() {
    return formats;
  }
  public String getFilename() {
    return filename;
  }
  public Integer getReportVersion() {
    return reportVersion;
  }
  public Configuration getConfig() {
    return config;
  }
  public Integer getId() {
    return id;
  }
  public boolean isLast() {
    return last;
  }
  public String getFilePath() {
    return filePath;
  }
  public boolean isQuick() {
    return quick;
  }
}



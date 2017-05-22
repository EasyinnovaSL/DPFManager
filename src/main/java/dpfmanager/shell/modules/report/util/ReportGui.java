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

import dpfmanager.shell.core.DPFManagerProperties;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Created by easy on 17/09/2015.
 */
public class ReportGui implements Comparable<ReportGui>{
  private String[] available_formats = {"html", "xml", "json", "pdf"};

  private String uuid;
  private String baseDir;
  private String reportDay;
  private String reportDir;
  private Long timestamp;
  private boolean loaded;
  private boolean error;

  private String date;
  private Integer nfiles;
  private String time;
  private String input;
  private Integer errors;
  private Integer warnings;
  private Integer passed;
  private Integer score;
  private Map<String, String> formats;
  private String delete;
  private String deletePath;

  public ReportGui(String baseDir, String reportDay, String reportDir) {
    this.baseDir = baseDir;
    this.reportDay = reportDay;
    this.reportDir = reportDir;
    this.uuid = reportDay + reportDir;
    this.loaded = false;
    this.error = true;
    readTime();
  }

  public void readTime(){
    File reportXml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.xml");
    File reportJson = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.json");
    File reportHtml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.html");
    File reportPdf = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.pdf");
    timestamp = 0L;
    if (reportXml.exists() && reportXml.length() > 0) {
      timestamp = getTimestamp(reportXml.getPath());
    }
    if (reportJson.exists() && reportJson.length() > 0) {
      timestamp = getTimestamp(reportJson.getPath());
    }
    if (reportHtml.exists() && reportHtml.length() > 0) {
      timestamp = getTimestamp(reportHtml.getPath());
    }
    if (reportPdf.exists() && reportPdf.length() > 0) {
      timestamp = getTimestamp(reportPdf.getPath());
    }
  }

  public boolean exists(){
    File reportXml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.xml");
    File reportJson = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.json");
    File reportHtml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.html");
    File reportPdf = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.pdf");
    if (reportXml.exists() && reportXml.length() > 0) {
      return true;
    }
    if (reportJson.exists() && reportJson.length() > 0) {
      return true;
    }
    if (reportHtml.exists() && reportHtml.length() > 0) {
      return true;
    }
    if (reportPdf.exists() && reportPdf.length() > 0) {
      return true;
    }
    return false;
  }

  public void load() {
    if (loaded) return;

    File reportXml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.xml");
    File reportJson = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.json");
    File reportHtml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.html");
    File reportPdf = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.pdf");

    if (reportXml.exists() && reportXml.length() > 0) {
      createRowFromXml(reportDay, reportXml);
    }
    if (error && reportJson.exists() && reportJson.length() > 0) {
      createRowFromJson(reportDay, reportJson);
    }
    if (error && reportHtml.exists() && reportHtml.length() > 0) {
      createRowFromHtml(reportDay, reportHtml);
    }
    if (error && reportPdf.exists() && reportPdf.length() > 0) {
      createRowFromPdf(reportDay, reportPdf);
    }

    if (!error) {
      // Add formats
      for (String format : available_formats) {
        File report;
        if (format.equals("json") || format.equals("xml")) {
          report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary." + format);
        } else {
          report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report." + format);
        }
        if (report.exists() && report.length() > 0) {
          addFormat(format, report.getPath());
        }
      }
      // Add mets
      File folder = new File(baseDir + "/" + reportDay + "/" + reportDir + "/");
      if (folder.exists() && folder.isDirectory()) {
        String[] filter = {"mets.xml"};
        Collection<File> childs = FileUtils.listFiles(folder, filter, false);
        if (childs.size() > 0) {
          addFormat("mets", folder.getPath());
        }
      }
      // All OK
      loaded = true;
    }
  }

  private static int countFiles(File folder, String extension) {
    String[] files = folder.list(new FilenameFilter() {
      @Override
      public boolean accept(File current, String name) {
        File f = new File(current, name);
        return f.isFile() && f.getName().toLowerCase().endsWith(extension);
      }
    });
    return files.length;
  }

  /**
   * Read full file string.
   *
   * @param path     the path
   * @param encoding the encoding
   * @return the string
   */
  static String readFullFile(String path, Charset encoding) {
    try {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, encoding);
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * Create row from xml report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  private void createRowFromXml(String reportDay, File file) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      File parent = new File(file.getParent());
      int n = countFiles(parent, ".xml") - 1 - countFiles(parent, "_fixed.xml") - countFiles(parent, "mets.xml");
      String xml = readFullFile(file.getPath(), Charset.defaultCharset());
      String stime = getStime(timestamp);
      int passed = 0, errors = 0, warnings = 0, score = 0;
      String input = parseInputFiles(file.getParentFile(), file.getAbsolutePath(), ".xml");

      // Passed
      if (xml.contains("<valid_files>")) {
        try {
          String sub = xml.substring(xml.indexOf("<valid_files>"));
          sub = sub.substring(sub.indexOf(">") + 1);
          sub = sub.substring(0, sub.indexOf("<"));
          passed = Integer.parseInt(sub);
        } catch (Exception e) {
          passed = -1;
        }
      }

      // Errors
      if (xml.contains("<invalid_files>")) {
        try {
          String sub = xml.substring(xml.indexOf("<invalid_files>"));
          sub = sub.substring(sub.indexOf(">") + 1);
          sub = sub.substring(0, sub.indexOf("<"));
          errors = Integer.parseInt(sub);
        } catch (Exception e) {
          errors = -1;
        }
      }

      // Warnings
      if (xml.contains("<warnings>")) {
        try {
          String sub = xml.substring(xml.indexOf("<warnings>"));
          sub = sub.substring(sub.indexOf(">") + 1);
          sub = sub.substring(0, sub.indexOf("<"));
          warnings = Integer.parseInt(sub);
        } catch (Exception e) {
          warnings = -1;
        }
      }

      if (n > 0) {
        score = passed * 100 / n;
      }

      setValues(sdate, stime, input, n, errors, warnings, passed, score, file.getAbsolutePath());
    } catch (Exception e) {
      error = true;
    }
  }

  /**
   * Create row from html report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  private void createRowFromHtml(String reportDay, File file) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      File parent = new File(file.getParent() + "/html");
      int n = countFiles(parent, ".html") - countFiles(parent, "_fixed.html");
      String html = readFullFile(file.getPath(), Charset.defaultCharset());
      int passed = 0, errors = 0, warnings = 0, score = 0;
      String stime = getStime(timestamp);
      File folder = new File(file.getParentFile().getAbsolutePath() + "/html");
      String input = parseInputFiles(folder, file.getAbsolutePath(), ".html");

      // Passed
      if (html.contains("id=\"pie-global\"")) {
        try {
          String sub = html.substring(html.indexOf("id=\"pie-global\""));
          sub = sub.substring(sub.indexOf("pie-info"));
          sub = sub.substring(sub.indexOf("success"));
          sub = sub.substring(sub.indexOf(">") + 1);
          sub = sub.substring(0, sub.indexOf("<"));
          if (sub.endsWith(" passed")) {
            passed = Integer.parseInt(sub.substring(0, sub.indexOf(" ")));
          }
        } catch (Exception e) {
          passed = -1;
        }
      }

      // Score
      if (html.contains("id=\"pie-global\"")) {
        try {
          String sub = html.substring(html.indexOf("id=\"pie-global\""));
          sub = sub.substring(sub.indexOf("pie-info"));
          sub = sub.substring(sub.indexOf("info bold"));
          sub = sub.substring(sub.indexOf(">") + 1);
          sub = sub.substring(0, sub.indexOf("<"));
          sub = sub.substring(sub.lastIndexOf(" ") + 1);
          if (sub.endsWith("%")) {
            score = Integer.parseInt(sub.substring(0, sub.indexOf("%")));
          }
        } catch (Exception e) {
          score = -1;
        }
      }

      // Errors
      if (html.contains("id=\"pie-global\"")) {
        try {
          String sub = html.substring(html.indexOf("id=\"pie-global\""));
          sub = sub.substring(sub.indexOf("pie-info"));
          sub = sub.substring(sub.indexOf("error"));
          sub = sub.substring(sub.indexOf(">") + 1);
          sub = sub.substring(0, sub.indexOf("<"));
          if (sub.endsWith(" failed")) {
            errors = Integer.parseInt(sub.substring(0, sub.indexOf(" ")));
          }
        } catch (Exception e) {
          errors = -1;
        }
      }

      // Warnings
      if (html.indexOf("row hover-row") > 0) {
        try {
          String[] list = html.split("row hover-row");
          for (int i = 1; i < list.length; i++) {
            String sub = list[i];
            if (sub.contains("class=\"c2 warning\"")) {
              warnings++;
            }
          }
        } catch (Exception e) {
          warnings = -1;
        }
      }

      setValues(sdate, stime, input, n, errors, warnings, passed, score, file.getAbsolutePath());
    } catch (Exception e) {
      error = true;
    }
  }

  /**
   * Create row from json report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  private void createRowFromJson(String reportDay, File file) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      File parent = new File(file.getParent());
      int n = countFiles(parent, ".json") - 1 - countFiles(parent, "_fixed.json");
      int passed = 0, errors = 0, warnings = 0, score = 0;
      String json = readFullFile(file.getPath(), Charset.defaultCharset());
      JsonObject jObjRoot = new JsonParser().parse(json).getAsJsonObject();
      String stime = getStime(timestamp);
      String input = parseInputFiles(file.getParentFile(), file.getAbsolutePath(), ".json");
      JsonObject jObj = jObjRoot;

      // Passed
      if (jObj.has("stats")) {
        try {
          JsonObject jStats = jObj.get("stats").getAsJsonObject();
          passed = Integer.parseInt(jStats.get("valid_files").getAsString());
        } catch (Exception e) {
          passed = -1;
        }
      }

      // Errors
      if (jObj.has("stats")) {
        try {
          JsonObject jStats = jObj.get("stats").getAsJsonObject();
          errors = Integer.parseInt(jStats.get("invalid_files").getAsString());
        } catch (Exception e) {
          errors = -1;
        }
      }

      // Warnings
      if (jObj.has("stats")) {
        try {
          JsonObject jStats = jObj.get("stats").getAsJsonObject();
          warnings = Integer.parseInt(jStats.get("warnings").getAsString());
        } catch (Exception e) {
          warnings = -1;
        }
      }


      // Score
      if (n > 0) {
        score = passed * 100 / n;
      }


      setValues(sdate, stime, input, n, errors, warnings, passed, score, file.getAbsolutePath());
    } catch (Exception e) {
      error = true;
    }
  }

  /**
   * Create row from pdf report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  private void createRowFromPdf(String reportDay, File file) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      int mWarns = 0;
      Integer n = 0, passed = 0, errors = 0, score = 0;
      String stime = getStime(timestamp);
      String input = parseInputFiles(file.getParentFile(), file.getAbsolutePath(), ".pdf");

      //Reads in pdf document
      PDDocument document = PDDocument.load(file);
      PDFTextStripper pdfStripper = new PDFTextStripper();
      String text = pdfStripper.getText(document);
      int lastWarns = 0;
      for (String line : text.split("\n")){
        line = line.replace("\r","");
        if (line.startsWith("Processed files:")){
          n = Integer.parseInt(line.replace("Processed files: ", ""));
        } else if (line.startsWith("Global score")){
          score = Integer.parseInt(line.replace("Global score ", "").replace("%", ""));
        } else if (line.endsWith("passed")){
          passed = Integer.parseInt(line.split(" ")[0]);
        } else if (line.endsWith("failed")) {
          errors = Integer.parseInt(line.split(" ")[0]);
        } else if (!line.contains("with") && line.endsWith("warnings")) {
          String middle = line.substring(line.indexOf("errors"), line.indexOf("warnings"));
          String strWarn = middle.split(" ")[1];
          if (Integer.parseInt(strWarn) > 0){
            lastWarns++;
          }
        }
        if (line.startsWith("View the full individual")){
          if (lastWarns > 0){
            mWarns++;
          }
          lastWarns = 0;
        }
      }
      document.close();

      setValues(sdate, stime, input, n, errors, mWarns, passed, score, file.getAbsolutePath());
    } catch (Exception e) {
      error = true;
    }
  }

  public void setValues(String sdate, String stime, String input, Integer nFiles, Integer errors, Integer warnings, Integer passed, Integer score, String deletePath) {
    this.date = parseDate2Locale(sdate);
    this.time = stime;
    this.input = input;
    this.nfiles = nFiles;
    this.errors = errors;
    this.warnings = warnings;
    this.passed = passed;
    this.score = score;
    this.formats = new SimpleMapProperty<>(FXCollections.observableHashMap());
    this.uuid = System.currentTimeMillis() + "";
    this.delete = uuid;
    this.deletePath = deletePath;
    this.error = false;
  }

  private String parseDate2Locale(String sdate) {
    try {
      // Convert to date
      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      Date date = df.parse(sdate);
      // Locale date
      DateFormat locDf = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
      sdate = locDf.format(date);
    } catch (Exception e) {
    }
    return sdate;
  }

  private static String parseInputFiles(File folder, String filePath, String ext) {
    String input = "";
    int count = 0;
    for (final File fileEntry : folder.listFiles()) {
      if (!fileEntry.isDirectory() && fileEntry.getPath().toLowerCase().endsWith(ext) && !fileEntry.getAbsolutePath().equals(filePath)) {
        String fname = fileEntry.getName();
        if (fname.toLowerCase().endsWith(ext) && !fname.toLowerCase().endsWith(".mets.xml") && !fname.toLowerCase().endsWith("_fixed" + ext)) {
          fname = fname.substring(fname.indexOf("-") + 1, fname.length() - ext.length());
          input = (input.length() > 0) ? input + ", " + fname : fname;
        }
        if (count == 5) {
          break;
        }
        count++;
      }
    }
    return input;
  }

  /**
   * Setters Getters
   * @return
   */
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public Map<String, String> getFormats() {
    return formats;
  }

  public void setFormats(Map<String, String> formats) {
    this.formats = formats;
  }

  public void addFormat(String format, String filepath) {
    this.formats.put(format, filepath);
  }

  public String getDelete() {
    return delete;
  }

  public void setDelete(String delete) {
    this.delete = delete;
  }

  private static String getStime(Long milis) {
    DateFormat locDf = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault());
    return locDf.format(milis);
  }

  private Long getTimestamp(String path) {
    try {
      BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
      return attr.creationTime().toMillis();
    } catch (Exception e) {
      return 0L;
    }
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getDeletePath() {
    return deletePath;
  }

  public void setDeletePath(String deletePath) {
    this.deletePath = deletePath;
  }

  public boolean isLoaded() {
    return loaded;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public Integer getPassed() {
    return passed;
  }

  public void setPassed(Integer passed) {
    this.passed = passed;
  }

  public Integer getWarnings() {
    return warnings;
  }

  public void setWarnings(Integer warnings) {
    this.warnings = warnings;
  }

  public Integer getErrors() {
    return errors;
  }

  public void setErrors(Integer errors) {
    this.errors = errors;
  }

  public Integer getNfiles() {
    return nfiles;
  }

  public void setNfiles(Integer nfiles) {
    this.nfiles = nfiles;
  }

  /**
   * Overrides
   */

  @Override
  public int compareTo(ReportGui other) {
    return other.timestamp.compareTo(this.timestamp);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ReportGui)) {
      return false;
    }
    ReportGui rg = (ReportGui) o;
    return this.getUuid().equals(rg.getUuid());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getUuid());
  }
}

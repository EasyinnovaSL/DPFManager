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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by easy on 17/09/2015.
 */
public class ReportRow {
  private final SimpleStringProperty date;
  private final SimpleStringProperty nfiles;
  private final SimpleStringProperty time;
  private final SimpleStringProperty input;
  private final SimpleStringProperty errors;
  private final SimpleStringProperty warnings;
  private final SimpleStringProperty passed;
  private final SimpleStringProperty score;
  private final SimpleMapProperty<String, String> formats;
  private final SimpleStringProperty delete;
  private String uuid;
  private String deletePath;

  /**
   * Instantiates a new Report row.
   *
   * @param sdate    the date
   * @param nFiles   the n files
   * @param stime    the time
   * @param input    the input
   * @param errors   the errors
   * @param warnings the warnings
   * @param passed   the passed
   * @param score    the score
   */
  public ReportRow(String sdate, String stime, String input, String nFiles, String errors, String warnings, String passed, String score, String deletePath) {
    this.date = new SimpleStringProperty(parseDate2Locale(sdate));
    this.time = new SimpleStringProperty(stime);
    this.input = new SimpleStringProperty(input);
    this.nfiles = new SimpleStringProperty(nFiles);
    this.errors = new SimpleStringProperty(errors);
    this.warnings = new SimpleStringProperty(warnings);
    this.passed = new SimpleStringProperty(passed);
    this.score = new SimpleStringProperty(score);
    this.formats = new SimpleMapProperty<>(FXCollections.observableHashMap());
    this.uuid = System.currentTimeMillis() + "";
    this.delete = new SimpleStringProperty(uuid);
    this.deletePath = deletePath;
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

  /**
   * Gets nfiles.
   *
   * @return the nfiles
   */
  public String getNfiles() {
    return nfiles.get();
  }

  /**
   * Sets nfiles.
   *
   * @param fName the f name
   */
  public void setNfiles(String fName) {
    nfiles.set(fName);
  }

  /**
   * Gets date.
   *
   * @return the date
   */
  public String getDate() {
    return date.get();
  }

  /**
   * Sets date.
   *
   * @param fName the f name
   */
  public void setDate(String fName) {
    date.set(fName);
  }


  /**
   * Gets result.
   *
   * @return the result
   */
  public String getTime() {
    return time.get();
  }

  /**
   * Sets result.
   *
   * @param fName the f name
   */
  public void setTime(String fName) {
    time.set(fName);
  }

  /**
   * Gets fixed.
   *
   * @return the fixed
   */
  public String getInput() {
    return input.get();
  }

  /**
   * Sets fixed.
   *
   * @param fName the f name
   */
  public void setInput(String fName) {
    input.set(fName);
  }

  /**
   * Gets errors.
   *
   * @return the errors
   */
  public String getErrors() {
    return errors.get();
  }

  /**
   * Sets errors.
   *
   * @param fName the f name
   */
  public void setErrors(String fName) {
    errors.set(fName);
  }

  /**
   * Gets warnings.
   *
   * @return the warnings
   */
  public String getWarnings() {
    return warnings.get();
  }

  /**
   * Sets warnings.
   *
   * @param fName the f name
   */
  public void setWarnings(String fName) {
    warnings.set(fName);
  }

  /**
   * Gets passed.
   *
   * @return the passed
   */
  public String getPassed() {
    return passed.get();
  }

  /**
   * Sets passed.
   *
   * @param fName the f name
   */
  public void setPassed(String fName) {
    passed.set(fName);
  }

  /**
   * Gets score.
   *
   * @return the score
   */
  public String getScore() {
    return score.get();
  }

  /**
   * Sets score.
   *
   * @param fName the f name
   */
  public void setScore(String fName) {
    score.set(fName);
  }

  /**
   * Gets formats.
   *
   * @return the formats
   */
  public ObservableMap<String, String> getFormats() {
    return formats.get();
  }

  /**
   * Sets formats.
   *
   * @param files the files
   */
  public void setFormats(ObservableMap<String, String> files) {
    this.formats.set(files);
  }

  /**
   * Add format.
   *
   * @param format   the format
   * @param filepath the filepath
   */
  public void addFormat(String format, String filepath) {
    this.formats.put(format, filepath);
  }

  /**
   * Gets date.
   *
   * @return the date
   */
  public String getDelete() {
    return delete.get();
  }

  /**
   * Sets date.
   *
   * @param delete the delete file
   */
  public void setDelete(String delete) {
    date.set(delete);
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
  public static ReportRow createRowFromXml(String reportDay, File file, ResourceBundle bundle) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      File parent = new File(file.getParent());
      int n = countFiles(parent, ".xml") - 1 - countFiles(parent, "_fixed.xml") - countFiles(parent, "mets.xml");
      String xml = readFullFile(file.getPath(), Charset.defaultCharset());
      String stime = getStime(file.getPath());
      int passed = 0, errors = 0, warnings = 0, score = 0;
      String input = parseInputFiles(file.getParentFile(), file.getAbsolutePath(), ".xml");

      // Passed
      if (xml.indexOf("<valid_files>") >= 0) {
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
      if (xml.indexOf("<invalid_files>") >= 0) {
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
      if (xml.indexOf("<warnings>") >= 0) {
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

      ReportRow row = new ReportRow(sdate, stime, input, "" + n, bundle.getString("errors").replace("%1", "" + errors), bundle.getString("warnings").replace("%1", "" + warnings), bundle.getString("passed").replace("%1", "" + passed), score + "%", file.getAbsolutePath());
      return row;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Create row from html report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  public static ReportRow createRowFromHtml(String reportDay, File file, ResourceBundle bundle) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      File parent = new File(file.getParent() + "/html");
      int n = countFiles(parent, ".html") - countFiles(parent, "_fixed.html");
      String html = readFullFile(file.getPath(), Charset.defaultCharset());
      int passed = 0, errors = 0, warnings = 0, score = 0;
      String stime = getStime(file.getPath());
      File folder = new File(file.getParentFile().getAbsolutePath() + "/html");
      String input = parseInputFiles(folder, file.getAbsolutePath(), ".html");

      // Passed
      if (html.indexOf("id=\"pie-global\"") >= 0) {
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
      if (html.indexOf("id=\"pie-global\"") >= 0) {
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
      if (html.indexOf("id=\"pie-global\"") >= 0) {
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

      ReportRow row = new ReportRow(sdate, stime, input, "" + n, bundle.getString("errors").replace("%1", "" + errors), bundle.getString("warnings").replace("%1", "" + warnings), bundle.getString("passed").replace("%1", "" + passed), score + "%", file.getAbsolutePath());
      return row;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Create row from json report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  public static ReportRow createRowFromJson(String reportDay, File file, ResourceBundle bundle) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      File parent = new File(file.getParent());
      int n = countFiles(parent, ".json") - 1 - countFiles(parent, "_fixed.json");
      int passed = 0, errors = 0, warnings = 0, score = 0;
      String json = readFullFile(file.getPath(), Charset.defaultCharset());
      JsonObject jObjRoot = new JsonParser().parse(json).getAsJsonObject();
      String stime = getStime(file.getPath());
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


      ReportRow row = new ReportRow(sdate, stime, input, "" + n, bundle.getString("errors").replace("%1", "" + errors), bundle.getString("warnings").replace("%1", "" + warnings), bundle.getString("passed").replace("%1", "" + passed), score + "%", file.getAbsolutePath());
      return row;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Create row from pdf report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  public static ReportRow createRowFromPdf(String reportDay, File file, ResourceBundle bundle) {
    try {
      String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
      int mWarns = 0;
      String n = "?", passed = "?", errors = "?", score = "?";
      String stime = getStime(file.getPath());
      String input = parseInputFiles(file.getParentFile(), file.getAbsolutePath(), ".pdf");

      //Reads in pdf document
      PDDocument document = Loader.loadPDF(file);
      PDFTextStripper pdfStripper = new PDFTextStripper();
      String text = pdfStripper.getText(document);
      int lastWarns = 0;
      for (String line : text.split("\n")){
        line = line.replace("\r","");
        if (line.startsWith("Processed files:")){
          n = line.replace("Processed files: ", "");
        } else if (line.startsWith("Global score")){
          score = line.replace("Global score ", "");
        } else if (line.endsWith("passed")){
          passed = line.split(" ")[0];
        } else if (line.endsWith("failed")) {
          errors = line.split(" ")[0];
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

      ReportRow row = new ReportRow(sdate, stime, input, n, bundle.getString("errors").replace("%1", errors), bundle.getString("warnings").replace("%1", "" + mWarns), bundle.getString("passed").replace("%1", passed), score + "%", file.getAbsolutePath());
      return row;
    } catch (Exception e) {
      return null;
    }
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

  private static String getStime(String path) {
    try {
      BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
      DateFormat locDf = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault());
      return locDf.format(attr.creationTime().toMillis());
    } catch (Exception e) {
      return "";
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
}


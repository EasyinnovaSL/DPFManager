package dpfmanager.shell.modules.classes;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

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

  /**
   * Instantiates a new Report row.
   *
   * @param date     the date
   * @param nFiles   the n files
   * @param time   the time
   * @param input    the input
   * @param errors   the errors
   * @param warnings the warnings
   * @param passed   the passed
   * @param score    the score
   */
  public ReportRow(String date, String time, String input, String nFiles, String errors, String warnings, String passed, String score) {
      this.date = new SimpleStringProperty(date);
      this.time = new SimpleStringProperty(time);
      this.input = new SimpleStringProperty(input);
      this.nfiles = new SimpleStringProperty(nFiles);
      this.errors = new SimpleStringProperty(errors);
      this.warnings = new SimpleStringProperty(warnings);
      this.passed = new SimpleStringProperty(passed);
      this.score = new SimpleStringProperty(score);
      this.formats = new SimpleMapProperty<>(FXCollections.observableHashMap());
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
  static String readFullFile(String path, Charset encoding)
  {
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
  public static ReportRow createRowFromXml(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    String stime = "";
    String input = "";
    File parent = new File(file.getParent());
    int n = countFiles(parent, ".xml") - 1;
    String xml = readFullFile(file.getPath(), Charset.defaultCharset());
    try {
      Path pfile = Paths.get(file.getPath());
      BasicFileAttributes attr = Files.readAttributes(pfile, BasicFileAttributes.class);
      stime = attr.creationTime().toString();
      stime = stime.substring(stime.indexOf("T")+1);
      stime = stime.substring(0, stime.indexOf("."));
    } catch (IOException e) {

    }
    int passed = 0, errors = 0, warnings = 0, score = 0;
    File folder = new File(file.getParentFile().getAbsolutePath());
    for (final File fileEntry : folder.listFiles()) {
      if (!fileEntry.isDirectory()) {
        if (fileEntry.getPath().toLowerCase().endsWith(".xml")) {
          if (!fileEntry.getAbsolutePath().equals(file.getAbsolutePath())) {
            if (input.length() > 0) input += ", ";
            String fname = fileEntry.getName();
            if (fname.toLowerCase().endsWith(".xml")) fname = fname.substring(0, fname.length()-4);
            if (fname.toLowerCase().endsWith(".tif")) fname = fname.substring(0, fname.length()-4);
            if (fname.toLowerCase().endsWith(".tiff")) fname = fname.substring(0, fname.length()-5);
            input += fname;
          }
        }
      }
    }

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

    if(n>0){
      score = passed * 100 / n;
    }

    ReportRow row = new ReportRow(sdate, stime, input, "" + n, errors + " errors", warnings + " warnings", passed + " passed", score + "%");
    return row;
  }

  /**
   * Create row from html report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  public static ReportRow createRowFromHtml(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    String stime = "";
    File parent = new File(file.getParent() + "/html");
    int n = countFiles(parent, ".html");
    String html = readFullFile(file.getPath(), Charset.defaultCharset());
    int passed = 0, errors = 0, warnings = 0, score = 0;
    try {
      Path pfile = Paths.get(file.getPath());
      BasicFileAttributes attr = Files.readAttributes(pfile, BasicFileAttributes.class);
      stime = attr.creationTime().toString();
      stime = stime.substring(stime.indexOf("T")+1);
      stime = stime.substring(0, stime.indexOf("."));
    } catch (IOException e) {

    }
    String input = "";
    File folder = new File(file.getParentFile().getAbsolutePath() + "/html");
    if (folder.exists()) {
      for (final File fileEntry : folder.listFiles()) {
        if (!fileEntry.isDirectory()) {
          if (fileEntry.getPath().toLowerCase().endsWith(".html")) {
            if (!fileEntry.getAbsolutePath().equals(file.getAbsolutePath())) {
              if (input.length() > 0) input += ", ";
              String fname = fileEntry.getName();
              if (fname.toLowerCase().endsWith(".html"))
                fname = fname.substring(0, fname.length() - 5);
              if (fname.toLowerCase().endsWith(".tif"))
                fname = fname.substring(0, fname.length() - 4);
              if (fname.toLowerCase().endsWith(".tiff"))
                fname = fname.substring(0, fname.length() - 5);
              input += fname;
            }
          }
        }
      }
    }

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

    // Errors & warnings
    if (html.indexOf("<td class=\"success\">") >= 0) {
      try {
        String sub = html.substring(html.indexOf("<td class=\"success\">"));
        sub = sub.substring(sub.indexOf(">") + 1);
        sub = sub.substring(0, sub.indexOf("<"));
        if (sub.endsWith(" errors")) {
          errors = Integer.parseInt(sub.substring(0, sub.indexOf(" ")));
        }

        sub = html.substring(html.indexOf("<td class=\"success\">"));
        sub = sub.substring(sub.indexOf("<td class=\"success\">"), 1);
        sub = sub.substring(sub.indexOf(">") + 1);
        sub = sub.substring(0, sub.indexOf("<"));
        if (sub.endsWith(" warnings")) {
          warnings = Integer.parseInt(sub.substring(0, sub.indexOf(" ")));
        }
      } catch (Exception e) {
        warnings = -1;
      }
    }

    ReportRow row = new ReportRow(sdate, stime, input, "" + n, errors + " errors", warnings + " warnings", passed + " passed", score + "%");
    return row;
  }

  /**
   * Create row from json report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  public static ReportRow createRowFromJson(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    File parent = new File(file.getParent());
    int n = countFiles(parent, ".json") - 1;
    int passed = 0, errors = 0, warnings = 0, score = 0;
    String stime = "";
    try {
      Path pfile = Paths.get(file.getPath());
      BasicFileAttributes attr = Files.readAttributes(pfile, BasicFileAttributes.class);
      stime = attr.creationTime().toString();
      stime = stime.substring(stime.indexOf("T")+1);
      stime = stime.substring(0, stime.indexOf("."));
    } catch (IOException e) {

    }
    String input = "";
    File folder = new File(file.getParentFile().getAbsolutePath());
    for (final File fileEntry : folder.listFiles()) {
      if (!fileEntry.isDirectory()) {
        if (fileEntry.getPath().toLowerCase().endsWith(".json")) {
          if (!fileEntry.getAbsolutePath().equals(file.getAbsolutePath())) {
            if (input.length() > 0) input += ", ";
            String fname = fileEntry.getName();
            if (fname.toLowerCase().endsWith(".json")) fname = fname.substring(0, fname.length()-5);
            if (fname.toLowerCase().endsWith(".tif")) fname = fname.substring(0, fname.length()-4);
            if (fname.toLowerCase().endsWith(".tiff")) fname = fname.substring(0, fname.length()-5);
            input += fname;
          }
        }
      }
    }

    ReportRow row = new ReportRow(sdate, stime, input, "" + n, errors + " errors", warnings + " warnings", passed + " passed", score + "%");
    return row;
  }

  /**
   * Create row from pdf report row.
   *
   * @param reportDay the report day
   * @param file      the file
   * @return the report row
   */
  public static ReportRow createRowFromPdf(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    String n = "?";
    String passed = "?", errors = "?", warnings = "?", score = "?";
    String stime = "";
    try {
      Path pfile = Paths.get(file.getPath());
      BasicFileAttributes attr = Files.readAttributes(pfile, BasicFileAttributes.class);
      stime = attr.creationTime().toString();
      stime = stime.substring(stime.indexOf("T")+1);
      stime = stime.substring(0, stime.indexOf("."));
    } catch (IOException e) {

    }
    String input = "";
    File folder = new File(file.getParentFile().getAbsolutePath());
    for (final File fileEntry : folder.listFiles()) {
      if (!fileEntry.isDirectory()) {
        if (fileEntry.getPath().toLowerCase().endsWith(".pdf")) {
          if (!fileEntry.getAbsolutePath().equals(file.getAbsolutePath())) {
            if (input.length() > 0) input += ", ";
            String fname = fileEntry.getName();
            if (fname.toLowerCase().endsWith(".pdf")) fname = fname.substring(0, fname.length()-4);
            if (fname.toLowerCase().endsWith(".tif")) fname = fname.substring(0, fname.length()-4);
            if (fname.toLowerCase().endsWith(".tiff")) fname = fname.substring(0, fname.length()-5);
            input += fname;
          }
        }
      }
    }

    ReportRow row = new ReportRow(sdate, stime, input, n, errors + " errors", warnings + " warnings", passed + " passed", score + "%");
    return row;
  }
}


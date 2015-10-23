package dpfmanager.shell.modules.classes;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by easy on 17/09/2015.
 */
public class ReportRow {
    private final SimpleStringProperty date;
    private final SimpleStringProperty nfiles;
    private final SimpleStringProperty result;
  private final SimpleStringProperty fixed;
  private final SimpleStringProperty errors;
  private final SimpleStringProperty warnings;
  private final SimpleStringProperty passed;
  private final SimpleStringProperty score;
  private String file;

    public ReportRow(String date, String nFiles, String result, String fixed, String errors, String warnings, String passed, String score, String file) {
      this.date = new SimpleStringProperty(date);
      this.nfiles = new SimpleStringProperty(nFiles);
      this.result = new SimpleStringProperty(result);
      this.fixed = new SimpleStringProperty(fixed);
      this.errors = new SimpleStringProperty(errors);
      this.warnings = new SimpleStringProperty(warnings);
      this.passed = new SimpleStringProperty(passed);
      this.score = new SimpleStringProperty(score);
      this.file = file;
    }

  public String getFile() {
    return file;
  }
  public void setFile(String fName) {
    file = fName;
  }

  public String getDate() {
      return date.get();
    }
    public void setDate(String fName) {
      date.set(fName);
    }

    public String getNfiles() {
      return nfiles.get();
    }
    public void setNfiles(String fName) {
      nfiles.set(fName);
    }

    public String getResult() {
      return result.get();
    }
    public void setResult(String fName) {
      result.set(fName);
    }

  public String getFixed() {
    return fixed.get();
  }
  public void setFixed(String fName) {
    fixed.set(fName);
  }

  public String getErrors() {
    return errors.get();
  }
  public void setErrors(String fName) {
    errors.set(fName);
  }

  public String getWarnings() {
    return warnings.get();
  }
  public void setWarnings(String fName) {
    warnings.set(fName);
  }

  public String getPassed() {
    return passed.get();
  }
  public void setPassed(String fName) {
    passed.set(fName);
  }

  public String getScore() {
    return score.get();
  }
  public void setScore(String fName) {
    score.set(fName);
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

  static String readFullFile(String path, Charset encoding)
  {
    try {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, encoding);
    } catch (Exception e) {
      return "";
    }
  }

  public static ReportRow createRowFromXml(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    File parent = new File(file.getParent());
    int n = countFiles(parent, ".xml") - 1;
    String xml = readFullFile(file.getPath(), Charset.defaultCharset());
    int passed = 0, errors = 0, warnings = 0, score = 0;

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

    ReportRow row = new ReportRow(sdate, "" + n, passed + " files passed all checks", errors + " errors " + warnings + " warnings", errors + " errors", warnings + " warnings", passed + " passed", score + "%", file.getPath());
    return row;
  }

  public static ReportRow createRowFromHtml(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    File parent = new File(file.getParent() + "/html");
    int n = countFiles(parent, ".html");
    String html = readFullFile(file.getPath(), Charset.defaultCharset());
    int passed = 0, errors = 0, warnings = 0, score = 0;

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

    ReportRow row = new ReportRow(sdate, "" + n, passed + " files passed all checks", errors + " errors " + warnings + " warnings", errors + " errors", warnings + " warnings", passed + " passed", score + "%", file.getPath());
    return row;
  }

  public static ReportRow createRowFromJson(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    File parent = new File(file.getParent());
    int n = countFiles(parent, ".json") - 1;
    int passed = 0, errors = 0, warnings = 0, score = 0;
    ReportRow row = new ReportRow(sdate, "" + n, passed + " files passed all checks", errors + " errors " + warnings + " warnings", errors + " errors", warnings + " warnings", passed + " passed", score + "%", file.getPath());
    return row;
  }

  public static ReportRow createRowFromPdf(String reportDay, File file) {
    String sdate = reportDay.substring(6, 8) + "/" + reportDay.substring(4, 6) + "/" + reportDay.substring(0, 4);
    String n = "?";
    String passed = "?", errors = "?", warnings = "?", score = "?";
    ReportRow row = new ReportRow(sdate, n, passed + " files passed all checks", errors + " errors " + warnings + " warnings", errors + " errors", warnings + " warnings", passed + " passed", score + "%", file.getPath());
    return row;
  }
}


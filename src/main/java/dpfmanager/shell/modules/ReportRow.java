package dpfmanager.shell.modules;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by easy on 17/09/2015.
 */
public class ReportRow {
    private final SimpleStringProperty date;
    private final SimpleStringProperty nFiles;
    private final SimpleStringProperty result;
  private final SimpleStringProperty fixed;
  private final SimpleStringProperty errors;
  private final SimpleStringProperty warnings;
  private final SimpleStringProperty passed;
  private final SimpleStringProperty score;

    public ReportRow(String date, String nFiles, String result, String fixed, String errors, String warnings, String passed, String score) {
      this.date = new SimpleStringProperty(date);
      this.nFiles = new SimpleStringProperty(nFiles);
      this.result = new SimpleStringProperty(result);
      this.fixed = new SimpleStringProperty(fixed);
      this.errors = new SimpleStringProperty(errors);
      this.warnings = new SimpleStringProperty(warnings);
      this.passed = new SimpleStringProperty(passed);
      this.score = new SimpleStringProperty(score);
    }

    public String getDate() {
      return date.get();
    }
    public void setDate(String fName) {
      date.set(fName);
    }

    public String getnFiles() {
      return nFiles.get();
    }
    public void setnFiles(String fName) {
      nFiles.set(fName);
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
}


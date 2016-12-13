package dpfmanager.shell.modules.report.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by easy on 13/12/2016.
 */
public class SmallIndividualReport implements Comparable {
  int percent;
  boolean isError;
  List<String> checkedIsos;
  HashSet<String> isosCheck;
  Map<String, Integer> nErrors;
  Map<String, Integer> nWarnings;
  boolean containsData;
  String reportPath;
  String filePath;
  String fileName;

  public SmallIndividualReport(IndividualReport ind) {
    this.percent = ind.calculatePercent();
    this.isError = ind.isError();
    this.checkedIsos = new ArrayList<>();
    this.checkedIsos.addAll(ind.getCheckedIsos());
    this.isosCheck = new HashSet<>();
    this.isosCheck.addAll(ind.getIsosCheck());
    this.reportPath = ind.getReportPath();
    this.filePath = ind.getFilePath();
    this.fileName = ind.getFileName();
    this.containsData = ind.containsData();
    this.nErrors = new HashMap<>();
    for (String iso : getCheckedIsos()){
      nErrors.put(iso, ind.getErrors(iso).size());
    }
    this.nWarnings = new HashMap<>();
    for (String iso : getCheckedIsos()){
      nWarnings.put(iso, ind.getOnlyWarnings(iso).size());
    }
  }

  public String getReportPath() {
    return reportPath;
  }

  public String getFilePath() {
    return filePath;
  }

  public String getFileName() {
    return fileName;
  }

  public int getPercent() {
    return percent;
  }

  public boolean getContainsData() {
    return containsData;
  }

  public boolean isError() {
    return isError;
  }

  public List<String> getCheckedIsos() {
    return checkedIsos;
  }

  public HashSet<String> getIsosCheck() {
    return isosCheck;
  }

  public boolean hasValidation(String key) {
    return isosCheck.contains(key);
  }

  public int getNErrors(String iso) {
    if (nErrors.containsKey(iso)) {
      return nErrors.get(iso);
    }
    return 0;
  }

  public int getNWarnings(String iso) {
    if (nWarnings.containsKey(iso)) {
      return nWarnings.get(iso);
    }
    return 0;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof SmallIndividualReport) {
      SmallIndividualReport other = (SmallIndividualReport) o;
      int thisPercent = getPercent();
      int otherPercent = other.getPercent();
      return Integer.compare(thisPercent, otherPercent);
    } else {
      return -1;
    }
  }
}

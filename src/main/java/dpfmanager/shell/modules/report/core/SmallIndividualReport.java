package dpfmanager.shell.modules.report.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by easy on 13/12/2016.
 */
public class SmallIndividualReport implements Comparable, Serializable {

  /**
   * Do not modify!
   */
  private static final long serialVersionUID = 7845L;

  boolean isError;
  Map<String, Integer> nErrors;
  Map<String, Integer> nWarnings;
  Map<String, Integer> nErrorsPolicy;
  Map<String, Integer> nWarningsPolicy;
  boolean containsData;
  String reportPath;
  String filePath;
  String fileName;
  String internalReportFodler;
  Long uuid;
  String imagePath;
  int percentOne;
  int percent;
  boolean quick;

  public SmallIndividualReport(IndividualReport ind) {
    this.isError = ind.isError();
    this.reportPath = ind.getReportPath();
    this.filePath = ind.getFilePath();
    this.fileName = ind.getFileName();
    this.containsData = ind.containsData();
    this.nErrors = new HashMap<>();
    this.internalReportFodler = ind.getInternalReportFodler();
    this.uuid = ind.getUuid();
    this.imagePath = ind.getImagePath();
    this.quick = ind.isQuick();
  }

  public void generate(IndividualReport ind){
    this.nErrors = new HashMap<>();
    this.nWarnings = new HashMap<>();
    this.nErrorsPolicy = new HashMap<>();
    this.nWarningsPolicy = new HashMap<>();
    for (String iso : ind.getCheckedIsos()){
      nErrors.put(iso, ind.getErrors(iso).size());
      nWarnings.put(iso, ind.getOnlyWarnings(iso).size());
      if (ind.hasModifiedIso(iso)){
        nErrorsPolicy.put(iso, ind.getNErrorsPolicy(iso));
        nWarningsPolicy.put(iso, ind.getNWarningsPolicy(iso));
      }
    }
  }

  public void computePercent(GlobalReport global){
    this.percent = calculatePercent(global, global.getErrorValue());
    this.percentOne = calculatePercent(global, 1.0);
  }

  public String getImagePath() {
    return imagePath;
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

  public boolean getContainsData() {
    return containsData;
  }

  public boolean isError() {
    return isError;
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

  public int getNErrorsPolicy(String iso){
    if (nErrorsPolicy.containsKey(iso)) {
      return nErrorsPolicy.get(iso);
    }
    return 0;
  }

  public int getNWarningsPolicy(String iso){
    if (nWarningsPolicy.containsKey(iso)) {
      return nWarningsPolicy.get(iso);
    }
    return 0;
  }

  public int getAllNErrorsPolicy(){
    int count = 0;
    for (String iso : nErrorsPolicy.keySet()){
      count += nErrorsPolicy.get(iso);
    }
    return count;
  }

  public int getPercent() {
    return percent;
  }

  /**
   * Calculate percent.
   *
   * @return the int
   */
  private int calculatePercent(GlobalReport global, double errVal) {
    if (isQuick()){
      int nErrors = 0;
      for (String key : global.getSelectedIsos()) {
        nErrors += global.hasModifiedIso(key) ? getNErrorsPolicy(key) : getNErrors(key);
      }
      int size = global.getSelectedIsos().size();
      int nPassed = size - nErrors;
      Double percentage = (size == 0) ? 0.0 : (nPassed * 1.0) / (size * 1.0) * 100.0;
      return percentage.intValue();
    } else {
      // Full check percentage
      Double rest = 100.0;

      for (String key : global.getSelectedIsos()) {
        int size = global.hasModifiedIso(key) ? getNErrorsPolicy(key) : getNErrors(key);
        rest -= size * errVal; // errVal = 12.5 default
        if (rest < 0 ) break; // Fast break
      }

      if (rest < 0.0) {
        rest = 0.0;
      }
      return rest.intValue();
    }
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof SmallIndividualReport) {
      SmallIndividualReport other = (SmallIndividualReport) o;
      return Integer.compare(this.percent, other.percent);
    } else {
      return -1;
    }
  }

  public String getInternalReportFodler() {
    return internalReportFodler;
  }

  public Long getUuid() {
    return uuid;
  }

  public boolean isQuick() {
    return quick;
  }
}

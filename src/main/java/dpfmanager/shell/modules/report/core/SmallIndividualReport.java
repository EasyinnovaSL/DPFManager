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
  boolean isError;
  List<String> checkedIsos;
  HashSet<String> isosCheck;
  Map<String, Integer> nErrors;
  Map<String, Integer> nWarnings;
  Map<String, Integer> nErrorsPolicy;
  Map<String, Integer> nWarningsPolicy;
  boolean containsData;
  String reportPath;
  String filePath;
  String fileName;
  Map<String, ArrayList<String>> modifiedIsos;
  String internalReportFodler;
  Long uuid;
  String imagePath;

  public SmallIndividualReport(IndividualReport ind) {
    this.isError = ind.isError();
    this.checkedIsos = new ArrayList<>();
    this.checkedIsos.addAll(ind.getCheckedIsos());
    this.isosCheck = new HashSet<>();
    this.isosCheck.addAll(ind.getIsosCheck());
    this.reportPath = ind.getReportPath();
    this.filePath = ind.getFilePath();
    this.fileName = ind.getFileName();
    this.containsData = ind.containsData();
    this.modifiedIsos = ind.getModifiedIsos();
    this.nErrors = new HashMap<>();
    this.internalReportFodler = ind.getInternalReportFodler();
    this.uuid = ind.getUuid();
    this.imagePath = ind.getImagePath();
    for (String iso : getCheckedIsos()){
      nErrors.put(iso, ind.getErrors(iso).size());
    }
    this.nWarnings = new HashMap<>();
    for (String iso : getCheckedIsos()){
      nWarnings.put(iso, ind.getOnlyWarnings(iso).size());
    }
    this.nErrorsPolicy = new HashMap<>();
    for (String iso : getCheckedIsos()){
      if (ind.hasModifiedIso(iso)){
        nErrorsPolicy.put(iso, ind.getNErrorsPolicy(iso));
      }
    }
    this.nWarningsPolicy = new HashMap<>();
    for (String iso : getCheckedIsos()){
      if (ind.hasModifiedIso(iso)){
        nWarningsPolicy.put(iso, ind.getNWarningsPolicy(iso));
      }
    }
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

  public Map<String, ArrayList<String>> getModifiedIsos() {
    return modifiedIsos;
  }

  public boolean hasModifiedIso(String iso) {
    return modifiedIsos.containsKey(iso);
  }

  /**
   * Calculate percent.
   *
   * @return the int
   */
  public int calculatePercent(double errVal) {
    Double rest = 100.0;

    if (isosCheck != null) {
      for (String key : isosCheck) {
        int size = hasModifiedIso(key) ? getNErrorsPolicy(key) : getNErrors(key);
        rest -= size * errVal; // errVal = 12.5 default
        if (rest < 0 ) break; // Fast break
      }
    }

    if (rest < 0.0) {
      rest = 0.0;
    }
    return rest.intValue();
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof SmallIndividualReport) {
      SmallIndividualReport other = (SmallIndividualReport) o;
      Integer thisPercent = calculatePercent(1);
      Integer otherPercent = other.calculatePercent(1);
      return Integer.compare(thisPercent, otherPercent);
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

}

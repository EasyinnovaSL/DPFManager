package dpfmanager.shell.modules.statistics.model;

import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.easyinnova.tiff.model.TagValue;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class StatisticsIso {

  public String iso;
  public String id;
  public Integer count;
  public Integer errors;
  public Integer warnings;
  public Integer passed;

  private StatisticsIsoErrors isoErrors;

  public StatisticsIso(String i, String d){
    iso = i;
    id = d;
    errors = 0;
    warnings = 0;
    passed = 0;
    count = 0;
    isoErrors = new StatisticsIsoErrors(iso, id);
  }

  public StatisticsIsoErrors getIsoErrors() {
    return isoErrors;
  }
}

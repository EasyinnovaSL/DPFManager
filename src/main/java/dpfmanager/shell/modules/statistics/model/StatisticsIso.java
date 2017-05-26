package dpfmanager.shell.modules.statistics.model;

import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.easyinnova.tiff.model.TagValue;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class StatisticsIso {

  public Integer errors;
  public Integer warnings;
  public Integer count;

  public StatisticsIso(){
    errors = 0;
    warnings = 0;
    count = 0;
  }

}

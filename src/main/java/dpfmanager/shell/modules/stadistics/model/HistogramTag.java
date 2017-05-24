package dpfmanager.shell.modules.stadistics.model;

import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.easyinnova.tiff.model.TagValue;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class HistogramTag {

  public TagValue tv;
  public Integer count;

  public HistogramTag(ReportTag tag){
    tv = tag.tv;
    count = 0;
  }

  public void increaseCount(){
    count++;
  }
}

package dpfmanager.shell.modules.statistics.model;

import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.easyinnova.tiff.model.TagValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class ValueTag {

  public String value;
  public Integer main;
  public Integer thumb;


  public ValueTag(String v){
    value = v;
    main = 0;
    thumb = 0;
  }

}

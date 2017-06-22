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
public class HistogramTag {

  public TagValue tv;
  public Integer main;
  public Integer thumb;
  public Integer mainDefault;
  public Integer thumbDefault;
  public Boolean isDefault;

  public Map<String, ValueTag> specificValues;
  public Map<String, ValueTag> specificValuesDefault;

  public HistogramTag(ReportTag tag){
    specificValues = new HashMap<>();
    specificValuesDefault = new HashMap<>();
    tv = tag.tv;
    main = 0;
    mainDefault = 0;
    thumb = 0;
    thumbDefault = 0;
    isDefault = tag.isDefault;
  }

  public void increaseCount(boolean isMain, boolean isDefault){
    if (isMain){
      if (!isDefault) {
        main++;
      }
      mainDefault++;
    } else {
      if (!isDefault) {
        thumb++;
      }
      thumbDefault++;
    }
  }

  public TagValue getValue() {
    return tv;
  }

  public Integer getMainCount(boolean withDefault) {
    if (withDefault){
      return mainDefault;
    } else {
      return main;
    }
  }

  public Integer getThumbCount(boolean withDefault) {
    if (withDefault){
      return thumbDefault;
    } else {
      return thumb;
    }
  }

  public Map<String, ValueTag> getPossibleValues() {
    return specificValues;
  }

  public Map<String, ValueTag> getPossibleValuesDefault() {
    return specificValuesDefault;
  }

  public boolean hasSpecificValues(){
    return !specificValues.isEmpty();
  }

  public boolean hasSpecificValuesDefault(){
    return !specificValuesDefault.isEmpty();
  }

  public void addMainValue(String value, boolean isDefault){
    addValue(value, isDefault, true);
  }

  public void addThumbValue(String value, boolean isDefault){
    addValue(value, isDefault, false);
  }

  private void addValue(String value, boolean isDefault, boolean isMain){
    if (!isDefault){
      ValueTag val = new ValueTag(value);
      if (specificValues.containsKey(value)) val = specificValues.get(value);
      val.increaseCount(isMain);
      specificValues.put(value, val);
    }

    ValueTag val = new ValueTag(value);
    if (specificValuesDefault.containsKey(value)) val = specificValuesDefault.get(value);
    val.increaseCount(isMain);
    specificValuesDefault.put(value, val);

  }

  public Boolean isOnlyDefault() {
    return main == 0 && mainDefault > 0;
  }
}

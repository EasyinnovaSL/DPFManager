/**
 * <h1>HistogramTag.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 */

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

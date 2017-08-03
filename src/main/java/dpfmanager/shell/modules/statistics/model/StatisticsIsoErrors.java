/**
 * <h1>StatisticsIsoErrors.java</h1> <p> This program is free software: you can redistribute it and/or
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

import dpfmanager.shell.interfaces.gui.component.statistics.comparators.IsoComparator;

import com.easyinnova.implementation_checker.rules.RuleResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class StatisticsIsoErrors {

  public String name;
  public String iso;
  public Integer max;
  public Map<String, StatisticsError> errorsMap;

  public StatisticsIsoErrors(String n, String i){
    name = n;
    iso = i;
    max = 0;
    errorsMap = new HashMap<>();
  }

  public void addError(RuleResult rr){
    String id = rr.getRule().getId();
    String description = rr.getDescription();
    StatisticsError sError = (errorsMap.containsKey(id)) ? errorsMap.get(id) : new StatisticsError(id, description, rr.getWarning());
    sError.count++;
    errorsMap.put(id, sError);
    if (sError.count > max) max = sError.count;
  }

  public boolean hasErrors(){
    return !errorsMap.isEmpty();
  }

  public List<StatisticsError> getErrorsList() {
    List<StatisticsError> list = new ArrayList<>(errorsMap.values());
    list.sort(new Comparator<StatisticsError>() {
      @Override
      public int compare(StatisticsError o1, StatisticsError o2) {
        int compare = o2.count.compareTo(o1.count);
        if (compare == 0){
          compare = o1.id.compareTo(o2.id);
        }
        return compare;
      }
    });
    return list;
  }
}

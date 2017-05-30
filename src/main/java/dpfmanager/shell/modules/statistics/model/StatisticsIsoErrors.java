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

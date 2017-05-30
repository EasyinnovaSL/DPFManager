package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.StatisticsIso;
import dpfmanager.shell.modules.statistics.model.StatisticsRule;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class PolicyComparator2 implements  Comparator<StatisticsRule> {

  public enum Mode {
    NAME, TOTAL, FAILED, PERCENTAGE
  }

  private Mode mode;

  public PolicyComparator2(Mode m){
    mode = m;
  }

  @Override
  public int compare(StatisticsRule o1, StatisticsRule o2) {
    Integer compare = 0;
    switch (mode){
      case TOTAL:
        compare = o2.total.compareTo(o1.total);
        break;
      case FAILED:
        compare = o2.count.compareTo(o1.count);
        break;
      case PERCENTAGE:
        compare = o2.computePercentageOne().compareTo(o1.computePercentageOne());
        break;
    }

    if (compare == 0){
      compare = o1.name.compareTo(o2.name);
    }
    return compare;
  }
}

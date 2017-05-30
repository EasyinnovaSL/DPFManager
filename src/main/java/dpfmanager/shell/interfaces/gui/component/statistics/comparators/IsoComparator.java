package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.HistogramTag;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class IsoComparator implements  Comparator<StatisticsIso> {

  public enum Mode {
    ERRORS, WARNINGS, PASSED, NAME, COUNT
  }

  private Mode mode;

  public IsoComparator(Mode m){
    mode = m;
  }

  @Override
  public int compare(StatisticsIso o1, StatisticsIso o2) {
    Integer compare = 0;
    switch (mode){
      case ERRORS:
        compare = o2.errors.compareTo(o1.errors);
        break;
      case WARNINGS:
        compare = o2.warnings.compareTo(o1.warnings);
        break;
      case PASSED:
        compare = o2.passed.compareTo(o1.passed);
        break;
      case COUNT:
        compare = o2.count.compareTo(o1.count);
        break;
    }

    if (compare == 0){
      compare = o1.iso.compareTo(o2.iso);
    }
    return compare;
  }
}

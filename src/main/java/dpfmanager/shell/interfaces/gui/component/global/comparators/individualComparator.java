package dpfmanager.shell.interfaces.gui.component.global.comparators;

import dpfmanager.shell.modules.report.util.ReportIndividualGui;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class IndividualComparator implements Comparator<ReportIndividualGui> {

  public enum Mode {
    ERRORS, WARNINGS, PASSED, NAME
  }

  private Mode mode;

  public IndividualComparator(Mode m){
    mode = m;
  }

  @Override
  public int compare(ReportIndividualGui o1, ReportIndividualGui o2) {
    if (!mode.equals(Mode.NAME)) {
      o1.load();
      o2.load();
    }
    Integer compare = 0;
    switch (mode){
      case ERRORS:
        compare = o2.getErrors().compareTo(o1.getErrors());
        break;
      case WARNINGS:
        compare = o2.getWarnings().compareTo(o1.getWarnings());
        break;
      case PASSED:
        compare = o2.getPassed().compareTo(o1.getPassed());
        break;
    }

    if (compare == 0){
      compare = o1.getName().compareTo(o2.getName());
    }
    return compare;
  }
}

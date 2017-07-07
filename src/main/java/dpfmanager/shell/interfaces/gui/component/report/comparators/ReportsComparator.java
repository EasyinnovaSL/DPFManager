package dpfmanager.shell.interfaces.gui.component.report.comparators;

import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.report.util.ReportIndividualGui;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class ReportsComparator implements  Comparator<ReportGui> {

  public enum Mode {
    ERRORS, WARNINGS, PASSED, RESULT, DATE, NAME
  }

  public enum Order {
    ASC, DESC
  }

  private Mode mode;

  private Order order;

  public ReportsComparator(Mode m, Order o){
    mode = m;
    order = o;
  }

  @Override
  public int compare(ReportGui o1, ReportGui o2) {
    if (!mode.equals(Mode.DATE)) {
      o1.load();
      o2.load();
    }
    Integer compare = 0;
    switch (mode){
      case ERRORS:
        if (order.equals(Order.ASC)){
          compare = o1.getErrors().compareTo(o2.getErrors());
        } else {
          compare = o2.getErrors().compareTo(o1.getErrors());
        }
        break;
      case WARNINGS:
        if (order.equals(Order.ASC)){
          compare = o1.getWarnings().compareTo(o2.getWarnings());
        } else {
          compare = o2.getWarnings().compareTo(o1.getWarnings());
        }
        break;
      case PASSED:
        if (order.equals(Order.ASC)){
          compare = o1.getPassed().compareTo(o2.getPassed());
        } else {
          compare = o2.getPassed().compareTo(o1.getPassed());
        }
        break;
      case RESULT:
        if (order.equals(Order.ASC)){
          compare = o1.getErrors().compareTo(o2.getErrors());
        } else {
          compare = o2.getErrors().compareTo(o1.getErrors());
        }
        break;
      case NAME:
        if (order.equals(Order.ASC)){
          compare = o1.getInputLower().compareTo(o2.getInputLower());
        } else {
          compare = o2.getInputLower().compareTo(o1.getInputLower());
        }
        break;
      case DATE:
        if (order.equals(Order.ASC)){
          compare = o1.getTimestamp().compareTo(o2.getTimestamp());
        } else {
          compare = o2.getTimestamp().compareTo(o1.getTimestamp());
        }
        break;
    }

    // If equals, default NAME ASC
    if (compare == 0 && o1.isLoaded() && o2.isLoaded()){
      compare = o1.getInputLower().compareTo(o2.getInputLower());
    }
    return compare;
  }
}

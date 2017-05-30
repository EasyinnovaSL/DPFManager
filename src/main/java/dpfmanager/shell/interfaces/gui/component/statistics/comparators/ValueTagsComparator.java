package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.HistogramTag;
import dpfmanager.shell.modules.statistics.model.ValueTag;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class ValueTagsComparator implements  Comparator<ValueTag> {
  @Override
  public int compare(ValueTag o1, ValueTag o2) {
    Integer main = o2.main;
    return main.compareTo(o1.main);
  }
}

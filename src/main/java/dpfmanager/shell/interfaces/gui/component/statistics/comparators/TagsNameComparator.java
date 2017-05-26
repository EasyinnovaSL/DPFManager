package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.HistogramTag;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class TagsNameComparator implements  Comparator<HistogramTag> {
  @Override
  public int compare(HistogramTag o1, HistogramTag o2) {
    return o1.getValue().getName().compareTo(o2.getValue().getName());
  }
}

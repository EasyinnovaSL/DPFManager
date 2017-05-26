package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.HistogramTag;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class TagsMainComparator implements  Comparator<HistogramTag> {
  @Override
  public int compare(HistogramTag o1, HistogramTag o2) {
    int compare = o2.getMainCount().compareTo(o1.getMainCount());
    if (compare == 0){
      Integer id1 = o1.getValue().getId();
      return id1.compareTo(o2.getValue().getId());
    }
    return compare;
  }
}

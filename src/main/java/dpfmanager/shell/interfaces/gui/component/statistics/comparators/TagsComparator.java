package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.HistogramTag;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class TagsComparator implements  Comparator<HistogramTag> {

  public enum Mode {
    ID, NAME, MAIN, THUMB
  }

  private Mode mode;

  public TagsComparator(Mode m){
    mode = m;
  }

  @Override
  public int compare(HistogramTag o1, HistogramTag o2) {

    Integer compare = 0;
    switch (mode){
      case NAME:
        compare = o1.getValue().getName().compareTo(o2.getValue().getName());
        break;
      case MAIN:
        compare = o2.getMainCount().compareTo(o1.getMainCount());
        break;
      case THUMB:
        compare = o2.getThumbCount().compareTo(o1.getThumbCount());
        break;
    }

    if (compare == 0){
      Integer id1 = o1.getValue().getId();
      compare = id1.compareTo(o2.getValue().getId());
    }
    return compare;
  }
}

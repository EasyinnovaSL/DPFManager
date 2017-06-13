package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.HistogramTag;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class TagsComparator implements  Comparator<HistogramTag> {

  public enum Mode {
    ID, NAME, MAIN, THUMB, MAIN_DEFAULTS, THUMB_DEFAULTS
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
        compare = o2.getMainCount(false).compareTo(o1.getMainCount(false));
        break;
      case MAIN_DEFAULTS:
        compare = o2.getMainCount(true).compareTo(o1.getMainCount(true));
        break;
      case THUMB:
        compare = o2.getThumbCount(false).compareTo(o1.getThumbCount(false));
        break;
      case THUMB_DEFAULTS:
        compare = o2.getThumbCount(true).compareTo(o1.getThumbCount(true));
        break;
    }

    if (compare == 0){
      Integer id1 = o1.getValue().getId();
      compare = id1.compareTo(o2.getValue().getId());
    }
    return compare;
  }
}

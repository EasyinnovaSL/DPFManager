package dpfmanager.conformancechecker.tiff.reporting;

import com.easyinnova.tiff.model.TagValue;

/**
 * Created by easy on 17/05/2016.
 */
public class ReportTag {
  /**
   * The Index.
   */
  public int index;
  /**
   * The Tv.
   */
  public TagValue tv;
  /**
   * The Dif.
   */
  public int dif = 0;

  public boolean expert = false;
}

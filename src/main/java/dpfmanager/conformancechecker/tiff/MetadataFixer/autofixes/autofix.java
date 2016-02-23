package dpfmanager.conformancechecker.tiff.MetadataFixer.autofixes;

import com.easyinnova.tiff.model.TiffDocument;

/**
 * Created by easy on 26/10/2015.
 */
public interface autofix {
  /**
   * Run.
   *
   * @param td the td
   */
  public void run(TiffDocument td);

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription();
}

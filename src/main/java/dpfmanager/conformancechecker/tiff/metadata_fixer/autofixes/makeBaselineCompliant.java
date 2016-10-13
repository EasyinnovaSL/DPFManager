package dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes;

import com.easyinnova.tiff.model.TiffDocument;

/**
 * Created by easy on 04/10/2016.
 */
public class makeBaselineCompliant implements autofix {
  /**
   * The Definition.
   */
  public String Definition = "Make Baseline Compliant";

  public String getDescription() {
    return Definition;
  }

  public void run(TiffDocument td) {
    // Nothing
    // -> By default, tifflibrary4j writes baseline-compliant tiffs
  }
}

package dpfmanager.shell.modules.autofixes;

import com.easyinnova.tiff.model.TiffDocument;

import java.nio.ByteOrder;

/**
 * Created by easy on 26/10/2015.
 */
public interface autofix {
  public void run(TiffDocument td);

  public String getDescription();
}

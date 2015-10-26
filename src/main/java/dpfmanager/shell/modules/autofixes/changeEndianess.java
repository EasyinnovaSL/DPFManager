package dpfmanager.shell.modules.autofixes;

import com.easyinnova.tiff.model.TiffDocument;

import java.nio.ByteOrder;

/**
 * Created by easy on 23/10/2015.
 */
public class changeEndianess implements autofix {
  public String Definition = "Change Byte Order Endianess";
  private ByteOrder byteOrder;

  public void setByteOrder(ByteOrder byteOrder) {
    this.byteOrder = byteOrder;
  }

  public void run(TiffDocument td) {

  }

  public String getDescription() {
    return Definition;
  }
}

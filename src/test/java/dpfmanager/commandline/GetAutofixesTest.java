package dpfmanager.commandline;

import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by easy on 26/10/2015.
 */
public class GetAutofixesTest extends TestCase {
  public void testAutofixes() throws Exception {
    try {
      TiffConformanceChecker.Logger = new DpfLogger();
      ArrayList<String> classes = TiffConformanceChecker.getAutofixes();

      int nAutofixes = classes.size();

      assertEquals(4, nAutofixes);
    } catch (Exception ex) {
      ex.printStackTrace();
      assertEquals(1, 0);
    }
  }
}

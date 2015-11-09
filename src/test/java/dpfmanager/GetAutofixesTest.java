package dpfmanager;

import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by easy on 26/10/2015.
 */
public class GetAutofixesTest extends TestCase {
  public void testAutofixes() throws Exception {
    try {
      ArrayList<String> classes = TiffConformanceChecker.getAutofixes();

      int nAutofixes = classes.size();

      assertEquals(1, nAutofixes);
    } catch (Exception ex) {
      assertEquals(1, 0);
    }
  }
}

package dpfmanager;

import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;
import junit.framework.TestCase;
import java.util.Set;

/**
 * Created by easy on 26/10/2015.
 */
public class GetAutofixesTest extends TestCase {
  public void testAutofixes() throws Exception {
    try {
      Set<Class<?>> classes = TiffConformanceChecker.getAutofixes();

      int nAutofixes = 0;
      for (Class<?> cl : classes) {
        if (cl.toString().startsWith("class ")) {
          nAutofixes++;
        }
      }

      //assertEquals(2, nAutofixes);
    } catch (Exception ex) {
      assertEquals(1, 0);
    }
  }
}

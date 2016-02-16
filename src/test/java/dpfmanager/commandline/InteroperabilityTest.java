package dpfmanager.commandline;

import dpfmanager.shell.conformancechecker.TiffConformanceChecker;

import junit.framework.TestCase;

/**
 * Created by easy on 08/02/2016.
 */
public class InteroperabilityTest extends TestCase {
  public void testFunctionCall() throws Exception {
    String path = "src/test/resources/Small/Bilevel.tif";
    String report = TiffConformanceChecker.RunTiffConformanceCheckerAndSendReport(path);
    assertEquals(true, report != null);
  }
}

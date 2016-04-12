package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * Created by easy on 08/02/2016.
 */
public class InteroperabilityTest extends CommandLineTest {
  @Test
  public void testFunctionCall() throws Exception {
    String path = "src/test/resources/Small/Bilevel.tif";
    String report = TiffConformanceChecker.RunTiffConformanceCheckerAndSendReport(path);
    assertEquals(true, report != null);
  }
}

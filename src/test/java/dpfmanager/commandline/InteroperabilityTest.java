package dpfmanager.commandline;

import dpfmanager.shell.conformancechecker.TiffConformanceChecker;
import dpfmanager.shell.interfaces.Cli.CommandLine;
import dpfmanager.shell.interfaces.UserInterface;
import javafx.application.Application;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

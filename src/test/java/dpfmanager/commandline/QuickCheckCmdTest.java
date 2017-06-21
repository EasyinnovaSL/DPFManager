package dpfmanager.commandline;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by easy on 13/10/2015.
 */
public class QuickCheckCmdTest extends CommandLineTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/SimpleXml.dpf";
  private String inputFilePath = "src/test/resources/Small.zip";

  @Test
  public void testQuickChecksCmd() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[4];
    args[0] = "check";
    args[1] = "--quick";
    args[2] = "-s";
    args[3] = inputFilePath;

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    // Check only ser report
    File reportFolder = new File(ReportGenerator.getLastReportPath());
    Assert.assertTrue("Report path exists", reportFolder.exists());
    Assert.assertTrue("Report path is directory", reportFolder.isDirectory());
    Assert.assertEquals("Report folder items", 2, reportFolder.list().length);
    File serializedFolder = new File(ReportGenerator.getLastReportPath() + "/serialized");
    Assert.assertTrue("Report path exists", serializedFolder.exists());
    Assert.assertTrue("Report path is directory", serializedFolder.isDirectory());
    Assert.assertEquals("Report folder items", 4, serializedFolder.list().length);
  }

}

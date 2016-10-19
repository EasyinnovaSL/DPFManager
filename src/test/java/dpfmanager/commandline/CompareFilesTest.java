package dpfmanager.commandline;

import dpfmanager.shell.core.app.MainConsoleApp;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by easy on 13/10/2015.
 */
public class CompareFilesTest extends CommandLineTest {


  @Test
  public void fullTestPeriodicalCheck() throws Exception {
    String expected = "19";
    String current = "20";
    String expectedPath = "C:\\Users\\Roser\\DPF Manager\\reports\\20161019\\"+expected+"\\summary.xml";
    String currentPath = "C:\\Users\\Roser\\DPF Manager\\reports\\20161019\\"+current+"\\summary.xml";
    File expectedFile = new File(expectedPath);
    File currentFile = new File(currentPath);

    Assert.assertEquals("The files differ!",
        FileUtils.readFileToString(expectedFile, "utf-8"),
        FileUtils.readFileToString(currentFile, "utf-8"));

  }

}

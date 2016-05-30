package dpfmanager.server;

import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by Adri on 27/05/2016.
 */
public class FullRemoteTest extends ServerTest {

  @Test
  public void testFullRemoteCheck() throws Exception {
    // Get last report path
    String path = ReportGenerator.createReportPath(true);

    // Start server
    System.out.println("Starting server mode...");
    startServer();

    // Start check
    System.out.println("Running full remote check...");

    String[] args = new String[4];
    args[0] = "-url";
    args[1] = "127.0.0.1:9000/dpfmanager";
    args[2] = "-w";
    args[3] = "src/test/resources/Small/Bilevel.tif";
    MainConsoleApp.main(args);

    waitForFinishMultiThred(45);

    // Check report created
    checkReportsCreated(path);
  }

  private void checkReportsCreated(String path){
    int num = 1;
    if (path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
      String number = path.substring(path.lastIndexOf("/") + 1, path.length());
      path = path.substring(0, path.lastIndexOf("/"));
      num = Integer.parseInt(number) + 1;
    }
    String folder1 = path + "/" + num + "/";
    String zip = path + "/" + num + ".zip";
    String folder2 = path + "/" + ++num + "/";


    File f1 = new File(folder1);
    File f2 = new File(folder2);
    File fz = new File(zip);

    Assert.assertEquals(true, f1.exists() && f1.isDirectory());
    Assert.assertEquals(true, f2.exists() && f2.isDirectory());
    Assert.assertEquals(true, fz.exists() && fz.isFile());
  }

}

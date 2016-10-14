package dpfmanager.server;

import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

/**
 * Created by Adri on 27/05/2016.
 */
public class InteractiveRemoteTest extends ServerTest {

  private PrintStream old;
  private ByteArrayOutputStream baos;

  @Test
  public void testInteractiveRemote() throws Exception {
    // Get last report path
    String path = ReportGenerator.createReportPath(true);

    // Custom system out
    old = System.out;
    baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);

    // Start server
    printOut("Starting server mode...");
    startServer();

    // Start check
    printOut("Running interactive remote test...");

    String[] argsCheck = new String[4];
    argsCheck[0] = "remote";
    argsCheck[1] = "-u";
    argsCheck[2] = "127.0.0.1:9000";
    argsCheck[3] = "src/test/resources/Small/Bilevel.tif";
    String job = getOutputJob(argsCheck,"");

    while (job != null) {
      sleep(1000);
      String[] argsJob = new String[5];
      argsJob[0] = "remote";
      argsJob[1] = "--url";
      argsJob[2] = "127.0.0.1:9000";
      argsJob[3] = "--job";
      argsJob[4] = job;
      job = getOutputJob(argsJob, job);
    }

    // Put things back
    System.out.flush();
    System.setOut(old);

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

  private String getOutputJob(String[] args, String lastJob) throws Exception {
    MainConsoleApp.main(args);
    waitForFinishMultiThred(30);
    String output = baos.toString();
    baos.reset();
    String message = "Started job with id: ";
    String still = "This job is still running.";
    String job = null;
    String[] lines = output.split("\n");
    for (String line : lines) {
      if (line.startsWith(message)) {
        job = line.substring(message.length(), line.length());
      } else if (line.startsWith(still)){
        return lastJob;
      }
    }
    if (job != null) {
      job = job.replace("\r", "");
    }
    return job;
  }

  private void printOut(String msg) {
    old.println(msg);
    old.flush();
  }
}

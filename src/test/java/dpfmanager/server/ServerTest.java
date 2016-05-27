package dpfmanager.server;

import dpfmanager.commandline.CommandLineTest;
import dpfmanager.shell.core.app.MainConsoleApp;

import org.junit.Test;

/**
 * Created by Adri on 27/05/2016.
 */
public class ServerTest extends CommandLineTest {

  @Test
  public void testFullRemoteCheck() throws Exception {
    // Start server
    ServerThread server = new ServerThread(9000);
    server.start();
    sleep(6000);

    System.out.println("Running full remote check");

    // Old System.out
//    PrintStream old = System.out;

    // Custom system out
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    PrintStream ps = new PrintStream(baos);
//    System.setOut(ps);

    String[] args = new String[3];
    args[0] = "-url";
    args[1] = "127.0.0.1:9000/dpfmanager";
    args[2] = "D:/Bilevel.tif";
    MainConsoleApp.main(args);

    waitForFinishMultiThred(20);

    // Put things back
//    System.out.flush();
//    System.setOut(old);
  }

  private void sleep(int milis) {
    try {
      Thread.sleep(milis);
    } catch (Exception e) {

    }
  }

}

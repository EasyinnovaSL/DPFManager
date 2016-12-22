package dpfmanager.server;

import dpfmanager.commandline.CommandLineTest;

/**
 * Created by Adrià Llorens on 30/05/2016.
 */
public class ServerTest extends CommandLineTest {

  private ServerThread server;

  public void startServer() {
    server = new ServerThread(9000);
    server.start();
    sleep(10000);
  }

  public void stopServer() {
  }

  public void sleep(int milis) {
    try {
      Thread.sleep(milis);
    } catch (Exception e) {

    }
  }
}

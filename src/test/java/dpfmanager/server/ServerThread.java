package dpfmanager.server;

import dpfmanager.shell.core.app.MainConsoleApp;

/**
 * Created by Adrià Llorens on 27/05/2016.
 */
public class ServerThread extends Thread {

  private Integer port;

  public ServerThread(Integer p) {
    port = p;
  }

  @Override
  public void run() {
    String[] args = new String[3];
    args[0] = "server";
    args[1] = "-p";
    args[2] = port.toString();

    MainConsoleApp.main(args);
  }
}

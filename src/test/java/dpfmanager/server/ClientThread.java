package dpfmanager.server;

import dpfmanager.shell.core.app.MainConsoleApp;

/**
 * Created by Adrià Llorens on 27/05/2016.
 */
public class ClientThread extends Thread {
  @Override
  public void run() {
    String[] args = new String[3];
    args[0] = "-url";
    args[1] = "127.0.0.1:9000/dpfmanager";
    args[2] = "D:/Bilevel.tif";
    MainConsoleApp.main(args);
  }
}

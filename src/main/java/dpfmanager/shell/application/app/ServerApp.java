package dpfmanager.shell.application.app;

import dpfmanager.shell.application.launcher.noui.ServerLauncher;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
public class ServerApp {

  public static void main(String[] args) {
    ServerLauncher cl = new ServerLauncher(args);
    cl.start();
  }

}

package dpfmanager.shell.application.app;

import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;

/**
 * Created by Adrià Llorens on 01/03/2016.
 */
public class CommandLineApp {

  public static void main(String[] args) {
    ConsoleLauncher cl = new ConsoleLauncher(args);
    cl.launch();
  }

}


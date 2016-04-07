package dpfmanager.shell.application.app;

import dpfmanager.shell.interfaces.console.ConsoleLauncher;
import dpfmanager.shell.application.launcher.noui.AppContext;

/**
 * Created by Adrià Llorens on 01/03/2016.
 */
public class CommandLineApp {

  public static void main(String[] args) {
    AppContext.loadContext("DpfSpring.xml");

    ConsoleLauncher cl = new ConsoleLauncher();
    cl.run(args);
  }

}


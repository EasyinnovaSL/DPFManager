package dpfmanager.shell.core;


import dpfmanager.shell.interfaces.CommandLineApp;
import dpfmanager.shell.interfaces.GuiApp;
import dpfmanager.shell.interfaces.cli.CliApp;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
class MainApp {

  public static void main(String[] args) {
    List<String> params = Arrays.asList(args);
    if (params.isEmpty() || params.contains("-gui")){
      GuiApp.main(args);
    }
    else if (params.contains("-test")){
      CliApp.main(args);
    }
    else {
      CommandLineApp cl = new CommandLineApp(params);
      cl.launch();
    }
  }
}

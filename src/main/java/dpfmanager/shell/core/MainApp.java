package dpfmanager.shell.core;


import dpfmanager.shell.interfaces.CommandLineApp;
import dpfmanager.shell.interfaces.GuiApp;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
class MainApp {

  private static final Logger LOG = Logger.getLogger(MainApp.class);

  public static void main(String[] args) {
    List<String> params = Arrays.asList(args);
    if (params.isEmpty() || params.contains("-gui")){
      LOG.info("Starting JavaFX application\n");
      GuiApp.main(args);
    }
    else if (params.contains("-server")){
      LOG.info("Starting server mode\n");
      LOG.error("Not implemented yet, closing app.");
    }
    else {
      LOG.info("Starting command line application\n");
      CommandLineApp cl = new CommandLineApp(params);
      cl.launch();
    }
  }
}

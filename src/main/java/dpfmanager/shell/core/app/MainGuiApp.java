package dpfmanager.shell.core.app;

import dpfmanager.shell.application.app.GuiApp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
public class MainGuiApp {

  public static void main(String[] args) {
    // Initial, set log level to severe (remove JacpFX logs)
    Logger rootLog = Logger.getLogger("");
    rootLog.setLevel(Level.SEVERE);

    GuiApp.main(args);
  }
}

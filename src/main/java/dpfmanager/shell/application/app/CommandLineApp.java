package dpfmanager.shell.application.app;

import dpfmanager.shell.application.launcher.noui.CommandLauncher;
import dpfmanager.shell.interfaces.console.workbench.ConsoleWorkbench;
import javafx.application.Application;
import javafx.stage.Stage;

import org.jacpfx.rcp.workbench.FXWorkbench;

/**
 * Created by Adrià Llorens on 01/03/2016.
 */
public class CommandLineApp extends CommandLauncher {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  protected Class<? extends FXWorkbench> getWorkbenchClass() {
    return ConsoleWorkbench.class;
  }

  @Override
  protected String[] getBasePackages() {
    return new String[]{
        "dpfmanager.shell.modules",                    // Dpf Modules
        "dpfmanager.shell.interfaces.console"          // Console Prespective
    };
  }

  @Override
  protected void postInit(Stage stage) {

  }

  @Override
  public String getXmlConfig() {
    return "DpfSpring.xml";
  }

}

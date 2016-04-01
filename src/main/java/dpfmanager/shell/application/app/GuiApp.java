package dpfmanager.shell.application.app;

import dpfmanager.shell.application.launcher.ui.GuiLauncher;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.jacpfx.rcp.workbench.FXWorkbench;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
public class GuiApp extends GuiLauncher {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  protected void postInit(Stage stage) {
    Image img = new Image("/gui-logo-white.png");
    if (img != null) {
      stage.getIcons().add(img);
    }
  }

  @Override
  protected Class<? extends FXWorkbench> getWorkbenchClass() {
    return GuiWorkbench.class;
  }

  @Override
  protected String[] getBasePackages() {
    return new String[]{
        "dpfmanager.shell.modules",                         // Dpf Modules
        "dpfmanager.shell.interfaces.gui.component",        // UI Components
        "dpfmanager.shell.interfaces.gui.perspective",      // Prespectives
        "dpfmanager.shell.interfaces.gui.fragment"          // Fragments
    };
  }

  @Override
  public String getXmlConfig() {
    return "DpfSpring.xml";
  }
}

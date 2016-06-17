package dpfmanager.shell.application.app;

import dpfmanager.shell.application.launcher.ui.GuiLauncher;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.CustomErrorHandler;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.jacpfx.api.handler.ErrorDialogHandler;
import org.jacpfx.rcp.workbench.FXWorkbench;

import java.util.Locale;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
public class GuiApp extends GuiLauncher {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  protected void postInit(Stage stage) {
    // Update locale
    Locale.setDefault(new Locale(DPFManagerProperties.getLanguage()));
    // Logo img
    Image img = new Image("/gui-logo-white.png");
    if (img != null) {
      stage.getIcons().add(img);
    }
    stage.setTitle("DPF Manager");
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
    return "DpfSpringGui.xml";
  }

  @Override
  public ErrorDialogHandler<Node> getErrorHandler(){
    return new CustomErrorHandler();
  }
}

package dpfmanager.shell.core.app;

import dpfmanager.shell.application.GuiApplication;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
public class GuiApp extends GuiApplication {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  protected void postInit(Stage stage) {

  }
}

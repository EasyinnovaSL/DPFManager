package dpfmanager.shell.interfaces;

import dpfmanager.shell.gui.resources.DpfStyles;
import dpfmanager.shell.gui.ui.main.MainModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.jrebirth.af.api.ui.Model;
import org.jrebirth.af.core.application.DefaultApplication;

/**
 * Created by Adrià Llorens on 23/02/2016.
 */
public final class GuiApp extends DefaultApplication<StackPane> {

  //  private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);
  private static final Logger LOG = Logger.getLogger(GuiApp.class);
  private static Stage thestage;

  /**
   * Application launcher.
   *
   * @param args the command line arguments
   */
  public static void main(final String... args) {
    Application.launch(args);
  }

  @Override
  public Class<? extends Model> getFirstModelClass() {
    return MainModel.class;
  }

  @Override
  protected void customizeScene(final Scene scene) {
    addCSS(scene, DpfStyles.MAIN);
  }

  @Override
  protected void customizeStage(final Stage stage) {
    thestage = stage;
    thestage.setMinWidth(400);
    boolean noDisc = getParameters().getRaw().contains("-noDisc");
    MainModel.setNoDisc(noDisc);
  }

  public static Stage getMyStage() {
    return thestage;
  }

}

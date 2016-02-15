package dpfmanager;

import dpfmanager.jrebirth.resources.DpfStyles;
import dpfmanager.jrebirth.ui.main.MainModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jrebirth.af.api.ui.Model;
import org.jrebirth.af.core.application.DefaultApplication;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public final class RebirthApp extends DefaultApplication<StackPane> {

  private static Stage thestage;

  /**
   * Application launcher.
   *
   * @param args the command line arguments
   */
  public static void main(final String... args) {
    Application.launch(RebirthApp.class, args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends Model> getFirstModelClass() {
    return MainModel.class;
  }

  @Override
  protected void customizeScene(final Scene scene) {
    addCSS(scene, DpfStyles.MAIN);
  }

  @Override
  protected void customizeStage(final Stage stage){
    thestage = stage;
    thestage.setMinWidth(400);
  }

  public static Stage getMyStage() {
    return thestage;
  }

}

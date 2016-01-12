package dpfmanager.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.apache.commons.lang.SystemUtils;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationFixture;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

  //Set properties for headless mode (Windows only)
  static {
    if (SystemUtils.IS_OS_WINDOWS) {
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
    }
  }

  final static int width = 970;
  final static int height = 1200;

  static Stage stage;
  protected Scene scene;
  static SpreadsheetView view;

  public static Stage launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
//    System.out.println("Launch");
    stage = FxToolkit.registerPrimaryStage();
    FxToolkit.setupStage(stage -> {
      view = new SpreadsheetView();
      StackPane sceneRoot = new StackPane(view);

      stage.setScene(new Scene(sceneRoot, width, height));
      stage.setX(0);
      stage.setY(0);

      stage.show();
      stage.toBack();
      stage.toFront();
    });
    FxToolkit.setupApplication(appClass, appArgs);
    return stage;
  }

  @Before
  public final void internalBefore() throws Exception {
    FxToolkit.setupApplication(this);
//    System.out.println("Before");
  }

  @After
  public final void internalAfter() throws Exception {
    FxToolkit.cleanupApplication(this);
//    System.out.println("Afeter");
  }

  @Override
  public void init() throws Exception {
  }

  @Override
  public void start(Stage stage) throws Exception {
    FxToolkit.showStage();
//    System.out.println("Start");
  }

  @Override
  public void stop() throws Exception {
    FxToolkit.hideStage();
//    System.out.println("Stop");
  }

  public void clickOnAndReload(String id){
    clickOn(id);
    scene = stage.getScene();
  }

  public void reloadScene(){
    scene = stage.getScene();
  }

}

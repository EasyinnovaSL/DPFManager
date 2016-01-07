package dpfmanager.gui;

import dpfmanager.MainApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationFixture;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

  //Set properties for headless mode (Windows only)
  static {
//    System.setProperty("testfx.robot", "glass");
//    System.setProperty("testfx.headless", "true");
  }

  final static int width = 970;
  final static int height = 950;

  static Stage stage;
  static SpreadsheetView view;

  public static Stage launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
    if (stage == null) {
      System.out.println("Launch");
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
    }
    return stage;
  }

  @Before
  public final void internalBefore() throws Exception {
    FxToolkit.setupApplication(this);
    System.out.println("Before");
  }

  @After
  public final void internalAfter() throws Exception {
    FxToolkit.cleanupApplication(this);
    System.out.println("Afeter");
  }

  @Override
  public void init() throws Exception { }

  @Override
  public void start(Stage stage) throws Exception {
    FxToolkit.showStage();
    System.out.println("Start");
  }

  @Override
  public void stop() throws Exception {
    FxToolkit.hideStage();
    System.out.println("Stop");
  }

}

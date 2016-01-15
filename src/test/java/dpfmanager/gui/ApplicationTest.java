package dpfmanager.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.commons.lang.SystemUtils;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotException;
import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationFixture;

import java.io.File;
import java.util.List;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

  //Set properties for headless mode (Windows only)
  static {
    if (SystemUtils.IS_OS_WINDOWS) {
//      System.setProperty("testfx.robot", "glass");
//      System.setProperty("testfx.headless", "true");
    }
  }

  final static int width = 970;
  final static int height = 800;
  final static int baseW = 0;
  final static int baseH = 25;

  static Stage stage;
  protected Scene scene;
  static SpreadsheetView view;
  private int scroll = 0;

  public static Stage launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
    stage = FxToolkit.registerPrimaryStage();
    FxToolkit.setupStage(stage -> {
      view = new SpreadsheetView();
      StackPane sceneRoot = new StackPane(view);

      stage.setScene(new Scene(sceneRoot, width, height));
      stage.setX(baseW);
      stage.setY(baseH);

      stage.show();
      stage.toBack();
      stage.toFront();
    });
    FxToolkit.setupApplication(appClass, appArgs);
    FxToolkit.toolkitContext().getRegisteredStage().setWidth(width);
    FxToolkit.toolkitContext().getRegisteredStage().setHeight(height);
    return stage;
  }

  @Before
  public final void internalBefore() throws Exception {
    FxToolkit.setupApplication(this);
  }

  @After
  public final void internalAfter() throws Exception {
    FxToolkit.cleanupStages();
    FxToolkit.cleanupApplication(this);
  }

  @Override
  public void init() throws Exception {
  }

  @Override
  public void start(Stage stage) throws Exception {
    FxToolkit.showStage();
  }

  @Override
  public void stop() throws Exception {
    FxToolkit.hideStage();
  }

  //Main click function + reload
  public void clickOnAndReload(String id){
    clickOnScroll(id);
    reloadScene();
  }

  public void clickOnAndReloadScroll(String id) throws FxRobotException {
    //Move to the window
    moveTo(100 + baseW, 100 + baseH);

    //Click and scroll
    clickOnAndReload(id);
    Node node = scene.lookup(id);
    restartScroll();
    while (node != null && scroll < 100) {
      System.out.println("scroll");
      scroll = scroll + 5;
      robotContext().getScrollRobot().scrollDown(scroll);
      clickOnAndReload(id);
      node = scene.lookup(id);
    }
    if (scroll == 500){
      throw new FxRobotException("Node "+id+" not found! Scroll timeout!");
    }
  }

  public ApplicationTest clickOnScroll(String id) throws FxRobotException {
    //Click without scroll
    boolean ret = clickOnCustom(id);
    if (ret){
      scene = stage.getScene();
      return this;
    }

    // Else Scroll
    int maxScroll = 200;
    moveTo(100+baseW, 100+baseH);
    restartScroll();
    while (!ret && scroll < maxScroll) {
      scroll = scroll + 10;
      robotContext().getScrollRobot().scrollDown(scroll);
      ret = clickOnCustom(id);
    }
    if (scroll == maxScroll){
      throw new FxRobotException("Node "+id+" not found! Scroll timeout!");
    }
    return this;
  }

  private void restartScroll() {
    if (scroll > 0){ //Return to initial scroll
      robotContext().getScrollRobot().scrollUp(scroll);
      scroll = 0;
    }
  }

  private boolean clickOnCustom(String id) {
    try {
      clickOn(id);
      return true;
    } catch (FxRobotException ex) {
      if (ex.getMessage().contains("but no nodes were visible")) {
        return false;
      }
      throw ex;
    }
  }

  public void reloadScene() {
    scene = stage.getScene();
  }

  protected void writeText(String id, String text) {
    TextField txtField = (TextField) scene.lookup(id);
    int length = txtField.getText().length();
    clickOnScroll(id).eraseText(length).write(text);
  }

  protected void waitForCheckFiles(int maxTimeout) {
    sleep(1000);
    int timeout = 0;
    boolean finish = false;
    while (!finish && timeout < maxTimeout) {
      reloadScene();
      Node node = scene.lookup("#loadingPane");
      if (node != null) {
        timeout++;
        sleep(1000);
      } else {
        finish = true;
      }
    }
    sleep(1000);
    Assert.assertNotEquals("Check files reached timeout! (" + maxTimeout + "s)", maxTimeout, timeout);
  }

  protected int getCurrentReports(){
    int nReports = 0;
    String path = ReportGenerator.getReportsFolder();
    File reports = new File(path);

    File[] dates = reports.listFiles();
    for (File date : dates){
      if (date.isDirectory()){
        File[] ids = date.listFiles();
        nReports = nReports + ids.length;
      }
    }
    return nReports;
  }

  protected void clickOnImportedConfig(String path) {
    AnchorPane ap = (AnchorPane) scene.lookup("#pane1");  //Get Anchor Pane
    VBox vbox = (VBox) ap.getChildren().get(0);           //Get VBox
    String idToClick = "#";
    String search = path.replaceAll("/", "_").replaceAll("\\\\", "_");
    for (Node node : vbox.getChildren()) {
      RadioButton rb = (RadioButton) node;
      String text = rb.getText().replaceAll("/", "_").replaceAll("\\\\", "_");
      if (text.endsWith(search)) {
        idToClick += rb.getId();
      }
    }
    clickOnScroll(idToClick);
  }
}

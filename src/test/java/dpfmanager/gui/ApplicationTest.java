package dpfmanager.gui;

import dpfmanager.shell.reporting.ReportGenerator;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
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

import java.awt.*;
import java.io.File;

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
  final static int height = 500;
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
    // Wait for application to start
    Thread.sleep(3000);
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
    sleep(250);
    if (id.equals("#butReports")){
      sleep(250);
    }
    reloadScene();
  }

  public ApplicationTest clickOnScroll(String id) throws FxRobotException {
    return clickOnScroll(id,true);
  }

  public ApplicationTest clickOnScroll(String id, boolean restart) throws FxRobotException {
    // Check if the node if at limit. Position < height + base Height - 5
    // If it is, make one scroll and finish
    if (moveToCustom(id)){
      int y = getMousePositionY();
      int minH = height + baseH -5;
      int maxH = height +baseH +5;
      if (minH < y && y < maxH){
        makeScroll(1,true);
      }
    }

    //Click without scroll
    boolean ret = clickOnCustom(id);
    if (ret){
      scene = stage.getScene();
      return this;
    }

    // Else Scroll
    int maxScroll = 150;
    if (restart) {
      restartScroll();
    }
    while (!ret && scroll < maxScroll) {
      makeScroll(10,false);
      ret = clickOnCustom(id);
    }
    if (scroll == maxScroll){
      throw new FxRobotException("Node "+id+" not found! Scroll timeout!");
    }
    return this;
  }

  private void restartScroll() {
    moveTo(100 + baseW, 100 + baseH);
    if (scroll > 0){ //Return to initial scroll
      robotContext().getScrollRobot().scrollUp(scroll);
      scroll = 0;
    }
  }

  private void makeScroll(int x, boolean move){
    if (move) {
      moveTo(100 + baseW, 100 + baseH);
    }
    scroll = scroll + x;
    robotContext().getScrollRobot().scrollDown(scroll);
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

  private boolean moveToCustom(String id) {
    try {
      moveTo(id);
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
      Node node = scene.lookup("#loadingVbox");
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

  private int getMousePositionY(){
    Point point = MouseInfo.getPointerInfo().getLocation();
    return (int) point.getY();
  }

  private int getMousePositionX(){
    Point point = MouseInfo.getPointerInfo().getLocation();
    return (int) point.getX();
  }

  protected int getCurrentReports(){
    int nReports = 0;
    String path = ReportGenerator.getReportsFolder();
    File reports = new File(path);

    File[] dates = reports.listFiles();
    if (dates == null || dates.length == 0){
      return 0;
    }
    for (File date : dates){
      if (date.isDirectory()){
        File[] ids = date.listFiles();
        nReports = nReports + ids.length;
      }
    }
    return nReports;
  }

  protected void clickOnImportedConfig(String path) {
    VBox vbox = (VBox) scene.lookup("#vBoxConfig");  //Get VBox
    String idToClick = "#";
    String search = path.replaceAll("/", "_").replaceAll("\\\\", "_");
    for (Node node : vbox.getChildren()) {
      RadioButton rb = (RadioButton) node;
      String text = rb.getText().replaceAll("/", "_").replaceAll("\\\\", "_");
      if (text.endsWith(search)) {
        idToClick += rb.getId();
      }
    }
    Assert.assertNotEquals("Import config file failed!", "#", idToClick);

    // Move inside pane
    moveTo("#vBoxConfig");
    clickOnScroll(idToClick,false);
  }
}

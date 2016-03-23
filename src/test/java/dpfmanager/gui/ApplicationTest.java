package dpfmanager.gui;

import dpfmanager.shell.modules.report.ReportGenerator;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
import java.util.logging.Level;
import java.util.logging.Logger;

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
  static int baseW = 0;
  static int baseH = 0;

  static Stage stage;
  protected Scene scene;
  static SpreadsheetView view;
  private int scroll = 0;

  public static Stage launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
    stage = FxToolkit.registerPrimaryStage();
    FxToolkit.setupApplication(appClass, appArgs);

    //Set the base width and height
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    baseW = (int) primaryScreenBounds.getMinX();
    baseH = (int) primaryScreenBounds.getMinY();

    //Custom size
    stage.setWidth(width);
    stage.setHeight(height);
    stage.setX(baseW);
    stage.setY(baseH);

    // Wait for application to start
    Thread.sleep(2000);
    return stage;
  }

  @Before
  public final void internalBefore() throws Exception {
    // Initial, set log level to severe (remove JacpFX logs)
    Logger rootLog = Logger.getLogger("");
    rootLog.setLevel(Level.SEVERE);

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

  public void clickOnAndReload(String id){
    clickOnAndReload(id, 250);
  }

  public void clickOnAndReloadTop(String id){
    clickOnAndReloadTop(id, 250);
  }

  //Main click function + wait + reload
  public void clickOnAndReload(String id, int milis){
    clickOnScroll(id);
    sleep(milis);
    reloadScene();
  }

  //Main click function + wait + reload (top pane)
  public void clickOnAndReloadTop(String id, int milis){
    clickOnScroll(id, true, true);
    sleep(milis);
    reloadScene();
  }

  public ApplicationTest clickOnScroll(String id) throws FxRobotException {
    return clickOnScroll(id,true, false);
  }

  public ApplicationTest clickOnScroll(String id, boolean restart) throws FxRobotException {
    return clickOnScroll(id,restart, false);
  }

  public ApplicationTest clickOnScroll(String id, boolean restart, boolean topItems ) throws FxRobotException {
    // First go to the button and decide if we need scroll or not
    if (!moveToCustom(id)){
      // We need scroll
      boolean ret = false;
      int maxScroll = 150;
      if (restart) {
        restartScroll();
      }
      while (!ret && scroll < maxScroll) {
        makeScroll(10,false);
        ret = moveToCustom(id);
      }
      if (scroll == maxScroll){
        throw new FxRobotException("Node "+id+" not found! Scroll timeout!");
      }
    }

    // Now check if we are under top bar
    int y = getMousePositionY();
    if (y < baseH+50 && !topItems) {
      restartScroll();
      return clickOnScroll(id, false, topItems);
    }

    // Now we can move to the button, lets check if it is at bounds of scene
    int minH = height + baseH -25;
    if (minH < y){
      // We are at limit, so make one scroll more
      makeScroll(1,true);
    }

    // Finally we can click the button
    clickOnCustom(id);
    scene = stage.getScene();
    return this;
  }

  public ApplicationTest clickOnScrollFUNCIONAL(String id, boolean restart, boolean topItems ) throws FxRobotException {
    // Check if the node if at limit. Position < height + base Height - 5
    // If it is, make one scroll and finish
    if (moveToCustom(id)){
      int y = getMousePositionY();
      int minH = height + baseH -5;
      // Check limits
      if (minH < y){
        makeScroll(1,true);
      }
      // Check under top bar
      if (y < 50 && !topItems) {
        restartScroll();
        restart = false;
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
      ret = checkAndClick(id);
    }
    if (scroll == maxScroll){
      throw new FxRobotException("Node "+id+" not found! Scroll timeout!");
    }
    return this;
  }

  //Try to click checking bounds
  private boolean checkAndClick(String id){
    if (moveToCustom(id)){
      int y = getMousePositionY();
      int minH = height + baseH -5;
      // Check limits
      if (minH < y){
        makeScroll(1,true);
      }
      return clickOnCustom(id);
    }
    return false;
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
    txtField.clear();
    txtField.setText(text);
  }

  protected void waitForCheckFiles(int maxTimeout) {
    sleep(1000);
    int timeout = 0;

    // Wait processing pane
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

    // Wait load report
    finish = false;
    while (!finish && timeout < maxTimeout) {
      reloadScene();
      Node node = scene.lookup("#butDessign");
      if (node == null) {
        timeout++;
        sleep(1000);
      } else {
        finish = true;
      }
    }

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
    VBox vbox = (VBox) scene.lookup("#vBoxConfig");                         //Get VBox
    ScrollPane scrollPane = (ScrollPane) scene.lookup("#configScroll");   //Get ScrollPane
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

    // Move inside configuration pane
    moveTo("#vBoxConfig");

    // Check button in limit
    moveTo(idToClick);
    int limitY = (int) (scrollPane.localToScreen(scrollPane.getBoundsInLocal()).getMinY() + scrollPane.getHeight());
    int currentY = getMousePositionY();
    if (currentY > limitY-2){
      // Move inside configuration pane and scroll down
      moveTo("#vBoxConfig");
      makeScroll(1, false);
    }

    // Now click and scroll
    clickOnScroll(idToClick,false);
  }
}

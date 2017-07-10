package dpfmanager.gui;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.gui.component.report.ReportsController;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotException;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.toolkit.ApplicationFixture;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
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

  boolean feedback;
  String lastReport;
  private Integer scrollUnit = 10;

  /**
   * The constant width.
   */
  final static int width = 970;
  /**
   * The constant height.
   */
  final static int height = 500;
  /**
   * The Base w.
   */
  static int baseW = 0;
  /**
   * The Base h.
   */
  static int baseH = 0;

  /**
   * The Max timeout.
   */
  static int maxTimeout = 120;

  /**
   * The Stage.
   */
  static Stage stage;
  /**
   * The Scene.
   */
  protected Scene scene;
  /**
   * The View.
   */
  static SpreadsheetView view;
  private int scroll = 0;

  /**
   * Launch stage.
   *
   * @param appClass the app class
   * @param appArgs  the app args
   * @return the stage
   * @throws Exception the exception
   */
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

  /**
   * Internal before.
   *
   * @throws Exception the exception
   */
  @Before
  public final void internalBefore() throws Exception {
    // Initial, set log level to severe (remove JacpFX logs)
    Logger rootLog = Logger.getLogger("");
    rootLog.setLevel(Level.SEVERE);
    // Save feedback
    feedback = DPFManagerProperties.getFeedback();
    DPFManagerProperties.setFeedback(false);
    // Last report
    lastReport = ReportGenerator.getLastReportPath();
    // Setup application
    FxToolkit.setupApplication(this);
    // Custom pre test
    customPreTest();
  }

  public void customPreTest() throws Exception {

  }

  /**
   * Internal after.
   *
   * @throws Exception the exception
   */
  @After
  public final void internalAfter() throws Exception {
    FxToolkit.cleanupStages();
    FxToolkit.cleanupApplication(this);

    // Set feedback
    DPFManagerProperties.setFeedback(feedback);
    // Delete all reports
    deleteReports();
    // Custom post test
    customPostTest();
  }

  public void customPostTest() throws Exception {

  }

  private void deleteReports() {
    try {
      if (endsWithDate(lastReport)) {
        // No reports before
        FileUtils.deleteDirectory(new File(lastReport));
      } else {
        // Delete new reports
        int index = getIndex(lastReport);
        String folder = getReportFolder(lastReport);
        index++;
        File file = new File(folder + index);
        while (file.exists()) {
          FileUtils.deleteDirectory(file);
          index++;
          file = new File(folder + index);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean endsWithDate(String path) {
    String last8 = path.substring(path.length() - 8);
    try {
      Integer.parseInt(last8);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private int getIndex(String path) {
    path = path.substring(0, path.length() - 1);
    int last = path.lastIndexOf("/");
    String aux = path.substring(last + 1);
    return Integer.parseInt(aux);
  }

  private String getReportFolder(String path) {
    path = path.substring(0, path.length() - 1);
    int last = path.lastIndexOf("/");
    String aux = path.substring(0, last + 1);
    return aux;
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

  /**
   * Click on and reload.
   *
   * @param id the id
   */
  public void clickOnAndReload(String id){
    clickOnAndReload(id, 250);
  }

  /**
   * Click on and reload top.
   *
   * @param id the id
   */
  public void clickOnAndReloadTop(String id){
    clickOnAndReloadTop(id, 250);
  }

  /**
   * Click on and reload bot.
   *
   * @param id the id
   */
  public void clickOnAndReloadBot(String id){
    clickOnAndReloadBot(id, 250);
  }

  /**
   * Click on and reload.
   *
   * @param id     the id
   * @param search the search
   */
//Main click function + wait for node + reload
  public void clickOnAndReload(String id, String search){
    // Click first
    clickOnScroll(id);

    // Reload until node search is visible
    reloadScene();
    Node node = scene.lookup(search);
    int count = 0;
    while (node == null && count < maxTimeout *4){
      sleep(250);
      count++;
      reloadScene();
      node = scene.lookup(search);
    }
    sleep(250);
  }

  /**
   * Click on and reload.
   *
   * @param id    the id
   * @param milis the milis
   */
//Main click function + wait + reload
  public void clickOnAndReload(String id, int milis){
    clickOnScroll(id);
    sleep(milis);
    reloadScene();
  }

  /**
   * Click on and reload top.
   *
   * @param id     the id
   * @param search the search
   */
//Main click function + wait for node + reload (top pane)
  public void clickOnAndReloadTop(String id, String search){
    // Click first
    clickOnScroll(id, true, true, false);

    // Reload until node search is visible
    reloadScene();
    Node node = scene.lookup(search);
    int count = 0;
    while (node == null && count < maxTimeout *4){
      sleep(250);
      count++;
      reloadScene();
      node = scene.lookup(search);
    }
    sleep(250);
  }

  /**
   * Wait until exists.
   *
   * @param id     the id
   */
  public void waitUntilExists(String id) {
    reloadScene();
    Node node = scene.lookup(id);
    int count = 0;
    while (node == null && count < maxTimeout *4){
      sleep(250);
      count++;
      reloadScene();
      node = scene.lookup(id);
    }
    sleep(500);
  }

  /**
   * Wait until has children.
   *
   * @param id     the id
   */
  public void waitUntilHasChilds(String id) {
    reloadScene();
    int count = 0;
    Node node = scene.lookup(id);
    if (node instanceof VBox) {
      VBox vbox = (VBox) node;
      while (vbox.getChildren().size() == 0 && count < maxTimeout *4) {
        sleep(250);
        count++;
        reloadScene();
        vbox = (VBox) scene.lookup(id);;
      }
    } else if (node instanceof HBox) {
      HBox hbox = (HBox) node;
      while (hbox.getChildren().size() == 0 && count < maxTimeout *4) {
        sleep(250);
        count++;
        reloadScene();
        hbox = (HBox) scene.lookup(id);;
      }
    }
    sleep(500);
  }

  /**
   * Wait until exists.
   *
   * @param id     the id
   */
  public void waitForTable(String id) {
    reloadScene();
    Node node = scene.lookup(id);
    int count = 0;
    while ((node == null || !node.isVisible()) && count < maxTimeout *4){
      sleep(250);
      count++;
      reloadScene();
      node = scene.lookup(id);
    }
  }

  /**
   * Click on and reload top.
   *
   * @param id    the id
   * @param milis the milis
   */
//Main click function + wait + reload (top pane)
  public void clickOnAndReloadTop(String id, int milis){
    clickOnScroll(id, true, true, false);
    sleep(milis);
    reloadScene();
  }

  /**
   * Click on and reload bot.
   *
   * @param id    the id
   * @param milis the milis
   */
//Main click function + wait + reload (bot pane)
  public void clickOnAndReloadBot(String id, int milis){
    clickOnScroll(id, true, false, true);
    sleep(milis);
    reloadScene();
  }

  /**
   * Click on scroll application test.
   *
   * @param id the id
   * @return the application test
   * @throws FxRobotException the fx robot exception
   */
  public ApplicationTest clickOnScroll(String id) throws FxRobotException {
    return clickOnScroll(id,true, false, false);
  }

  /**
   * Click on scroll application test.
   *
   * @param id      the id
   * @param restart the restart
   * @return the application test
   * @throws FxRobotException the fx robot exception
   */
  public ApplicationTest clickOnScroll(String id, boolean restart) throws FxRobotException {
    return clickOnScroll(id,restart, false, false);
  }

  /**
   * Click on scroll application test.
   *
   * @param id       the id
   * @param restart  the restart
   * @param topItems the top items
   * @return the application test
   * @throws FxRobotException the fx robot exception
   */
  public ApplicationTest clickOnScroll(String id, boolean restart, boolean topItems, boolean botItems ) throws FxRobotException {
    // Top buttons or bot buttons dont need scroll never
    if (topItems || botItems){
      clickOnCustom(id);
      scene = stage.getScene();
      return this;
    }

    // First go to the button and decide if we need scroll or not
    if (!moveToCustom(id)){
      // We need scroll
      boolean ret = false;
      int maxScroll = 150;
      if (restart) {
        restartScroll();
      }
      while (!ret && scroll < maxScroll) {
        makeScroll(scrollUnit, false);
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
      return clickOnScroll(id, false, topItems, botItems);
    }

    // Now we can move to the button, lets check if it is at bounds of scene
    int minH = height + baseH -25;
    if (minH < y){
      // We are at limit, so make one scroll more
      makeScroll(1, true);
    }

    // Finally we can click the button
    clickOnCustom(id);
    scene = stage.getScene();
    return this;
  }

  /**
   * Click on scroll funcional application test.
   *
   * @param id       the id
   * @param restart  the restart
   * @param topItems the top items
   * @return the application test
   * @throws FxRobotException the fx robot exception
   */
  public ApplicationTest clickOnScrollFUNCIONAL(String id, boolean restart, boolean topItems ) throws FxRobotException {
    // Check if the node if at limit. Position < height + base Height - 5
    // If it is, make one scroll and finish
    if (moveToCustom(id)){
      int y = getMousePositionY();
      int minH = height + baseH -5;
      // Check limits
      if (minH < y){
        makeScroll(1, true);
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

  public void makeScroll(int x, boolean move){
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

  public boolean moveToCustom(String id) {
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

  /**
   * Reload scene.
   */
  public void reloadScene() {
    scene = stage.getScene();
  }

  /**
   * Write text.
   *
   * @param id   the id
   * @param text the text
   */
  protected void writeText(String id, String text) {
    TextField txtField = (TextField) scene.lookup(id);
    txtField.clear();
    txtField.setText(text);
  }

  /**
   * Wait for check files.
   */
  protected void waitForCheckFiles(int count) {
    sleep(1000);
    int timeout = 0;

    // Wait task appear
    boolean finish = false;
    while (!finish && timeout < maxTimeout) {
      reloadScene();
      VBox vbox = (VBox) scene.lookup("#taskVBox");
      if (vbox != null && vbox.getChildren().size() == count) {
        ObservableList<Node> childs = vbox.getChildren();
        // Wait for all tasks finish
        for (Node child : childs) {
          ProgressBar pb = (ProgressBar) child.lookup("#progress");
          boolean childFinish = false;
          while(!childFinish && timeout < maxTimeout){
            if (pb.getProgress() == 1.0){
              childFinish = true;
            } else{
              timeout++;
              sleep(1000);
            }
          }
        }
        finish = true;
      } else {
        timeout++;
        sleep(1000);
      }
    }

    Assert.assertNotEquals("Check files reached timeout! (" + maxTimeout + "s)", maxTimeout, timeout);
  }

  /**
   * Wait for cancel check
   */
  public void waitForCancelChecks(){
    sleep(1000);
    int timeout = 0;

    // Wait task appear
    boolean finish = false;
    while (!finish && timeout < maxTimeout) {
      reloadScene();
      VBox vbox = (VBox) scene.lookup("#taskVBox");
      if (vbox != null && vbox.getChildren().size() > 0) {
        timeout++;
        sleep(1000);
      }
      finish = vbox.getChildren().size() == 0;
    }

    Assert.assertNotEquals("Wait for cancel checks reached timeout! (" + maxTimeout + "s)", maxTimeout, timeout);
  }

  private int getMousePositionY(){
    Point point = MouseInfo.getPointerInfo().getLocation();
    return (int) point.getY();
  }

  private int getMousePositionX(){
    Point point = MouseInfo.getPointerInfo().getLocation();
    return (int) point.getX();
  }

  /**
   * Get current reports int.
   *
   * @return the int
   */
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
        for (File id : date.listFiles()){
          if (id.isDirectory() && containsSummary(id.listFiles())){
            nReports++;
          }
        }
      }
    }
    return nReports;
  }

  private boolean containsSummary(File[] files){
    List<String> summarys = Arrays.asList("report.html", "report.pdf", "summary.json", "summary.xml");
    for (File file : files){
      if (summarys.contains(file.getName())){
        return true;
      }
    }
    return false;
  }

  /**
   * Click on imported config.
   *
   * @param path the path
   */
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
    moveTo("#configScroll");

    // Check need scroll
    if (!moveToCustom(idToClick)){
      // We need scroll
      boolean ret = false;
      int maxScroll = 150;
      while (!ret && scroll < maxScroll) {
        makeScroll(10,false);
        ret = moveToCustom(idToClick);
      }
      if (scroll == maxScroll){
        throw new FxRobotException("Node "+idToClick+" not found! Scroll timeout!");
      }
    }

    // Check if button is in at limits
    int limitY = (int) (scrollPane.localToScreen(scrollPane.getBoundsInLocal()).getMinY() + scrollPane.getHeight());
    int currentY = getMousePositionY();
    if (currentY > limitY-2){
      // Move inside configuration pane and scroll down
      moveTo("#configScroll");
      makeScroll(1, false);
    }

    // Finally we can click the button
    clickOnCustom(idToClick);
    scene = stage.getScene();
  }

  public void clickOnSpecificScrollPane(String idToClick, String scrollPaneId) {
    ScrollPane scrollPane = (ScrollPane) scene.lookup(scrollPaneId);

    moveTo(scrollPaneId);
    boolean ret = moveToCustom(idToClick);
    while (!ret) {
      robotContext().getScrollRobot().scrollDown(1);
      ret = moveToCustom(idToClick);
    }

    // Check if button is in at limits
    int limitY = (int) (scrollPane.localToScreen(scrollPane.getBoundsInLocal()).getMinY() + scrollPane.getHeight());
    int currentY = getMousePositionY();
    while (currentY > limitY-2){
      moveTo(scrollPaneId);
      robotContext().getScrollRobot().scrollDown(1);
      moveTo(idToClick);
      currentY = getMousePositionY();
    }

    clickOnCustom(idToClick);
    scene = stage.getScene();
  }

  public File getBackupFile(String path){
    path = path + ".bak";
    File file = new File(path);
    int count = 1;
    while (file.exists()){
      file = new File(path+count);
      count++;
    }
    return file;
  }

  public void checkFormatsAsserts(int nReports) throws Exception {
    //Check table view
    clickOnAndReloadTop("#butReports", "#pane-reports");
    waitUntilExists("#lastReportRow");
    VBox mainVBox = (VBox) scene.lookup("#vboxReports0");
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsController.itemsPerPage), mainVBox.getChildren().size());
    AnchorPane row = (AnchorPane) mainVBox.getChildren().get(0);
    GridPane grid = (GridPane) row.getChildren().get(0);
    Assert.assertEquals("Report row N files", "3", ((javafx.scene.control.Label) grid.getChildren().get(1)).getText());
    Assert.assertEquals("Report row N passed", "3 passed", ((javafx.scene.control.Label) grid.getChildren().get(7)).getText());
    Assert.assertEquals("Report row N errors", "0 errors", ((javafx.scene.control.Label) grid.getChildren().get(5)).getText());
    Assert.assertEquals("Report row N warnings", "0 warnings", ((javafx.scene.control.Label) grid.getChildren().get(6)).getText());

    // Go to global report
//    clickOnAndReload("#butGlobal", "#pane-global");

    //Check html
    reloadScene();
    waitUntilExists("#mainVBox #buthtml");
    clickOnAndReload("#mainVBox #buthtml", "#pane-show");
    waitUntilExists("#webView");
    FxAssert.verifyThat("#webView", NodeMatchers.isNotNull());

    //Check pdf
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butpdf", "#pane-show");
    waitUntilExists("#pdfPagesVBox");
    waitUntilHasChilds("#pdfPagesVBox");
    FxAssert.verifyThat("#pdfPagesVBox", NodeMatchers.isNotNull());
    VBox pdfPagesVBox = (VBox) scene.lookup("#pdfPagesVBox");
    Assert.assertEquals(1, pdfPagesVBox.getChildren().size());

    //Check xml
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butxml", "#pane-show");
    waitUntilExists("#textArea");
    waitUntilHasText("#textArea");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    javafx.scene.control.TextArea textArea = (javafx.scene.control.TextArea) scene.lookup("#textArea");
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"";
    String initial = textArea.getText().substring(0,expected.length());
    Assert.assertEquals("Report xml", expected, initial);

    //Check mets
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butmets", "#pane-show");
    waitUntilExists("#textArea");
    waitUntilHasText("#textArea");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    textArea = (javafx.scene.control.TextArea) scene.lookup("#textArea");
    expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"";
    initial = textArea.getText().substring(0,expected.length());
    Assert.assertEquals("Report mets", expected, initial);

    //Check json
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butjson", "#pane-show");
    waitUntilExists("#textArea");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    textArea = (javafx.scene.control.TextArea) scene.lookup("#textArea");
    JsonObject jObj = new JsonParser().parse(textArea.getText()).getAsJsonObject();
    Assert.assertTrue("Report json", (jObj.has("individualreports") && jObj.has("stats")));
  }

  public void waitUntilHasText(String id){
    TextArea textArea = (TextArea) scene.lookup(id);
    int timeout = 0;
    while (textArea.getText().isEmpty() && timeout < maxTimeout * 4){
      sleep(250);
      timeout++;
    }
    Assert.assertNotEquals("Wait for text reached timeout! (" + maxTimeout + "s)", maxTimeout, timeout);
    sleep(250);
  }

  public void setScrollUnit(Integer x) {
    scrollUnit = x;
  }
}

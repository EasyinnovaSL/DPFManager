package dpfmanager;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.function.Consumer;

/**
 * Created by Adria Llorens on 05/10/2015.
 */
public class GuiFXTest extends ApplicationTest {

  final static int width = 970;
  final static int height = 950;
  SpreadsheetView view;

  //Set properties for headless mode
  static {
//    System.setProperty("testfx.robot", "glass");
//    System.setProperty("glass.platform", "Monocle");
//    System.setProperty("monocle.platform", "Headless");

//    System.setProperty("java.awt.headless", "true");
//    System.setProperty("testfx.robot", "glass");
//    System.setProperty("testfx.headless", "true");
//    System.setProperty("prism.order", "sw");
//    System.setProperty("prism.text", "t2k");
  }

  Stage stage;
  Scene scene;

  @Override
  public void init() throws Exception{
    launch(MainApp.class, null);
    stage = MainApp.getStage();
//    Thread.sleep(5000);
  }
  @Override
  public void start(Stage stage2) throws Exception {
    stage.setWidth(width);
    stage.setHeight(height);
    stage.setX(0);
    stage.setY(0);
    scene = stage.getScene();
    FxToolkit.showStage();
  }
  @Override
  public void stop() throws Exception{
    FxToolkit.hideStage();
  }

//  @BeforeClass
//  public static void setupSpec() throws Exception {
//    FxToolkit.registerPrimaryStage();
//  }

//  @Before
//  public void before() throws Exception {
//    FxToolkit.setupStage(stage -> {
////      view = new SpreadsheetView();
////      StackPane sceneRoot = new StackPane(view);
////
////      stage.setScene(new Scene(sceneRoot, width, height));
////      stage.setX(0);
////      stage.setY(0);
////      stage.show();
//      Parent rootNode = new Region();
//      stage.setScene(new Scene(rootNode, width, height));
//      stage.show();
//      stage.toBack();
//      stage.toFront();
//    });
//    FxToolkit.setupApplication(MainApp.class);
//    FxToolkit.showStage();
//    Thread.sleep(2000);
//  }

//  @Test
//  public void  launchApplication() throws Exception {
//    WaitForAsyncUtils.waitForFxEvents();
//    Button gitinit = (Button)scene.lookup("#gitinit");
//    Assert.assertEquals("\uf04b",gitinit.getText());
//    Button gitclone = (Button)scene.lookup("#gitclone");
//    Assert.assertEquals("\uF0C5", gitclone.getText());
//    ToolBar toolbar = (ToolBar)scene.lookup("#gitToolBar");
//    AnchorPane anchor = (AnchorPane)toolbar.getParent();
//    Double anchorPaneConstraint = new Double(0.0);
//    Assert.assertEquals("Left Anchor Test",anchorPaneConstraint, anchor.getLeftAnchor(toolbar));
//    Assert.assertEquals("Right Anchor Test",anchorPaneConstraint,anchor.getRightAnchor(toolbar));
//    Assert.assertEquals("Top Anchor Test", anchorPaneConstraint, anchor.getTopAnchor(toolbar));
//  }

  @Test
  public void testFirstScreen() throws Exception {
    //Wait for buttons clicks
    WaitForAsyncUtils.waitForFxEvents();

    FxAssert.verifyThat("#txtBox1", NodeMatchers.hasText("Select a file"));
  }

  @Test
  public void testFX() throws Exception {
    //Wait for buttons clicks
    WaitForAsyncUtils.waitForFxEvents();

    clickOn("#butAbout");//.moveTo("New").clickOn("Text Document");
    FxAssert.verifyThat("#aboutTitle", NodeMatchers.hasText("About DPF Manager"));
  }

  @Test
  public void testFail() throws Exception {
    //Wait for buttons clicks
    WaitForAsyncUtils.waitForFxEvents();

    clickOn("#butReport");//.moveTo("New").clickOn("Text Document");
    FxAssert.verifyThat("#txtBox1", NodeMatchers.hasText("Select a file"));
  }

  @After
  public void after() throws Exception {
    FxToolkit.hideStage();
  }
}

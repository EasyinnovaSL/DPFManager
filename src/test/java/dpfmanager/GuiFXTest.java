package dpfmanager;

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
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.api.FxToolkit;

/**
 * Created by Adria Llorens on 05/10/2015.
 */
public class GuiFXTest extends ApplicationTest {

  final static int width = 970;
  final static int height = 950;
  static SpreadsheetView view;

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

  @BeforeClass
  public static void setupSpec() throws Exception {
    FxToolkit.registerPrimaryStage();
  }

  @Before
  public void before() throws Exception {
    FxToolkit.setupStage(stage -> {
//      view = new SpreadsheetView();
//      StackPane sceneRoot = new StackPane(view);
//
//      stage.setScene(new Scene(sceneRoot, width, height));
//      stage.setX(0);
//      stage.setY(0);
//      stage.show();
      Parent rootNode = new Region();
      stage.setScene(new Scene(rootNode, width, height));
      stage.show();
      stage.toBack();
      stage.toFront();
    });
    FxToolkit.setupApplication(MainApp.class);
    FxToolkit.showStage();
    Thread.sleep(2000);
  }

  @Test
  public void testFirstScreen() throws Exception {
    // given:

    // when:

    // then:
    FxAssert.verifyThat("#txtBox1", NodeMatchers.hasText("Select a file"));
  }

  @Test
  public void testFX() throws Exception {
    // given:
    clickOn("#butAbout");//.moveTo("New").clickOn("Text Document");
//    write("myTextfile.txt").push(ENTER);

    // when:
//    drag(".file").dropTo("#trash-can");

    // then:
//    verifyThat("#aboutTitle", containsText("About DPF Manager"));
//    Thread.sleep(2000);

//    FxAssert.verifyThat("#txtBox1", NodeMatchers.hasText("Select a file"));
    FxAssert.verifyThat("#aboutTitle", NodeMatchers.hasText("About DPF Manager"));
  }

  @Test
  public void testFail() throws Exception {
    // given:

    // when:
    clickOn("#butReport");//.moveTo("New").clickOn("Text Document");

    // then:
    FxAssert.verifyThat("#txtBox1", NodeMatchers.hasText("Select a file"));
  }

  @After
  public void after() throws Exception {
    FxToolkit.hideStage();
  }

  @Override
  public void start(Stage stage) throws Exception {

  }
}

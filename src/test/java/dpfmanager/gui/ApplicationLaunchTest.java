package dpfmanager.gui;

import dpfmanager.MainApp;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public class ApplicationLaunchTest extends ApplicationTest {

  Stage stage = null;
  Scene scene;

  @Override
  public void init() throws Exception{
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
    System.out.println("Init");
  }

//  @Override
//  public void start(Stage stage) throws Exception {
//    scene=stage.getScene();
//    System.out.println("Start");
//    FxToolkit.showStage();
//  }

  @Override
  public void stop() throws Exception{
    System.out.println("Stop");
    FxToolkit.hideStage();
  }

//  @Test
//  public void testFirstScreen() throws Exception {
//    WaitForAsyncUtils.waitForFxEvents();
//    FxAssert.verifyThat("#welcomeText", NodeMatchers.hasText("Welcome to DPF Manager!"));
//  }

  @Test
  public void  testDesignScreen() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();

    //Check for buttons
    Button butAbout = (Button)scene.lookup("#butAbout");
    Assert.assertEquals("butAbout", butAbout.getId());
    Button butReport = (Button)scene.lookup("#butReport");
    Assert.assertEquals("butReport", butReport.getId());
  }

//  @Test
//  public void testButAbout() throws Exception {
//    //Wait for async events
//    WaitForAsyncUtils.waitForFxEvents();
//
//    //Check buton about
//    clickOn("#butAbout");
//    FxAssert.verifyThat("#aboutTitle", NodeMatchers.hasText("About DPF Manager"));
//  }

//  @Test
//  public void testFail() throws Exception {
//    //Wait for buttons clicks
//    WaitForAsyncUtils.waitForFxEvents();
//
//    clickOn("#butReport");//.moveTo("New").clickOn("Text Document");
//    FxAssert.verifyThat("#txtBox1", NodeMatchers.hasText("Select a file"));
//  }

}

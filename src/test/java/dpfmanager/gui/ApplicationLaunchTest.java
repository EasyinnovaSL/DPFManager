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

  Stage stage;
  Scene scene;

  @Override
  public void init()
      throws Exception{
    stage=launch(MainApp.class,null);
  }

  @Override
  public void start(Stage stage) throws Exception {
    scene=stage.getScene();
    FxToolkit.showStage();
  }

  @Override
  public void stop() throws Exception{
    FxToolkit.hideStage();
  }

  @Test
  public void  launchApplicationTest() throws Exception {
    WaitForAsyncUtils.waitForFxEvents();

    //Check for buttons
    Button butAbout = (Button)scene.lookup("#butAbout");
    Assert.assertEquals("butAbout", butAbout.getId());
    Button butReport = (Button)scene.lookup("#butReport");
    Assert.assertEquals("butReport", butReport.getId());
  }

  @Test
  public void  launchApplicationTest2() throws Exception {
    WaitForAsyncUtils.waitForFxEvents();
    FxAssert.verifyThat("#txtBox1", NodeMatchers.hasText("Select a file"));
  }

}

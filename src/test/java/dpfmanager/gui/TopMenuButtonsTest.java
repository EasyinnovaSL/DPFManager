package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public class TopMenuButtonsTest extends ApplicationTest {

  Stage stage = null;
  Scene scene;

  @Override
  public void init() throws Exception{
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Test
  public void testTopMenuButtons() throws Exception {
    // Wait for async events
    WaitForAsyncUtils.waitForFxEvents();

    // checker -- about
    clickOnAndReloadTop("#butAbout",1000);
    FxAssert.verifyThat("#pane2", NodeMatchers.isNotNull());
    // about -- reports
    clickOnAndReloadTop("#butReports",4000);
    FxAssert.verifyThat("#pane1", NodeMatchers.isNotNull());
    // reports -- checker
    clickOnAndReloadTop("#butDessign",1000);
    FxAssert.verifyThat("#pane0", NodeMatchers.isNotNull());
    // checker -- reports
    clickOnAndReloadTop("#butReports",4000);
    FxAssert.verifyThat("#pane1", NodeMatchers.isNotNull());
    // reports -- about
    clickOnAndReloadTop("#butAbout",1000);
    FxAssert.verifyThat("#pane2", NodeMatchers.isNotNull());
    // about -- checker
    clickOnAndReloadTop("#butDessign",1000);
    FxAssert.verifyThat("#pane0", NodeMatchers.isNotNull());
  }

}

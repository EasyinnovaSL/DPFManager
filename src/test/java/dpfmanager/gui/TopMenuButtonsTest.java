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
    clickOnAndReloadTop("#butAbout","#pane-about");
    FxAssert.verifyThat("#pane-about", NodeMatchers.isNotNull());
    // about -- reports
    clickOnAndReloadTop("#butReports","#pane-reports");
    FxAssert.verifyThat("#pane-reports", NodeMatchers.isNotNull());
    // reports -- checker
    waitUntilExists("#butDessign");
    clickOnAndReloadTop("#butDessign","#pane-design");
    FxAssert.verifyThat("#pane-design", NodeMatchers.isNotNull());
    // checker -- reports
    clickOnAndReloadTop("#butReports","#pane-reports");
    FxAssert.verifyThat("#pane-reports", NodeMatchers.isNotNull());
    // reports -- about
    clickOnAndReloadTop("#butAbout","#pane-about");
    FxAssert.verifyThat("#pane-about", NodeMatchers.isNotNull());
    // about -- checker
    clickOnAndReloadTop("#butDessign","#pane-design");
    FxAssert.verifyThat("#pane-design", NodeMatchers.isNotNull());
  }

}

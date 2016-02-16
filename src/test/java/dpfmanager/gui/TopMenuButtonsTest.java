package dpfmanager.gui;

import dpfmanager.shell.MainApp;
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
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testTopMenuButtons() throws Exception {
    // Wait for async events
    WaitForAsyncUtils.waitForFxEvents();

    // checker -- about
    clickOnAndReload("#butAbout");
    FxAssert.verifyThat("#pane2", NodeMatchers.isNotNull());
    // about -- reports
    clickOnAndReload("#butReports");
    FxAssert.verifyThat("#pane1", NodeMatchers.isNotNull());
    // reports -- checker
    clickOnAndReload("#butDessign");
    FxAssert.verifyThat("#pane0", NodeMatchers.isNotNull());
    // checker -- reports
    clickOnAndReload("#butReports");
    FxAssert.verifyThat("#pane1", NodeMatchers.isNotNull());
    // reports -- about
    clickOnAndReload("#butAbout");
    FxAssert.verifyThat("#pane2", NodeMatchers.isNotNull());
    // about -- checker
    clickOnAndReload("#butDessign");
    FxAssert.verifyThat("#pane0", NodeMatchers.isNotNull());
  }

}

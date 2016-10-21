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

    // About
    goTo("#butAbout", "#pane-about");
    // Reports
    goTo("#butReports", "#pane-reports");
    // File Check
    goTo("#butDessign", "#pane-design");
    // Conformance checkers
    goTo("#butInterop", "#pane-interop");
    // Periodical
    goTo("#butPeriodical", "#pane-periodical");
    // Reports
    goTo("#butReports", "#pane-reports");
    // Conformance checkers
    goTo("#butInterop", "#pane-interop");
    // File Check
    goTo("#butDessign", "#pane-design");
    // About
    goTo("#butAbout", "#pane-about");
    // Periodical
    goTo("#butPeriodical", "#pane-periodical");
    // File Check
    goTo("#butDessign", "#pane-design");
  }

  private void goTo(String id, String pane){
    waitUntilExists(id);
    clickOnAndReloadTop(id, pane);
    FxAssert.verifyThat(pane, NodeMatchers.isNotNull());
  }

}

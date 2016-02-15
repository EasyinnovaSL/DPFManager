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
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();

    // checker -- about
    clickOnAndReload("#butAbout");
    FxAssert.verifyThat("#butAbout", NodeMatchers.isNull());
    // about -- reports
    clickOnAndReload("#butReport");
    FxAssert.verifyThat("#butReport", NodeMatchers.isNull());
    // reports -- checker
    clickOnAndReload("#butChecker");
    FxAssert.verifyThat("#butChecker", NodeMatchers.isNull());
    // checker -- reports
    clickOnAndReload("#butReport");
    FxAssert.verifyThat("#butReport", NodeMatchers.isNull());
    // reports -- about
    clickOnAndReload("#butAbout");
    FxAssert.verifyThat("#butAbout", NodeMatchers.isNull());
    // about -- checker
    clickOnAndReload("#butChecker");
    FxAssert.verifyThat("#butChecker", NodeMatchers.isNull());
  }

}

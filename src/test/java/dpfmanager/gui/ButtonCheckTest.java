package dpfmanager.gui;

import dpfmanager.MainApp;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Created by Adrià Llorens on 13/01/2016.
 */
public class ButtonCheckTest extends ApplicationTest {

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testFileButtonCheck() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running check file button test...");

    //import config file
    clickOnAndReload("#checkFilesButton");
    clickOnAndReload("#butReport");
    FxAssert.verifyThat("#tab_reports", NodeMatchers.isNull());
  }
}


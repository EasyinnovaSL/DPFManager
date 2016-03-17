package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.junit.Assert;
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
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Test
  public void testButtonsBelow() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running check buttons test...");

    // Continue 3 button
    clickOnAndReload("#newButton",1000);
    Assert.assertTrue("New button fail.", scene.lookup("#step1").getStyleClass().contains("blue-but"));
    clickOnAndReload("#continueButton");
    Assert.assertTrue("Continue of step 1 fail.", scene.lookup("#step2").getStyleClass().contains("blue-but"));
    clickOnAndReload("#continueButton");
    Assert.assertTrue("Continue of step 2 fail.", scene.lookup("#step2").getStyleClass().contains("blue-but"));
    clickOnAndReload("#continueButton");
    Assert.assertTrue("Continue of step 3 fail.", scene.lookup("#step4").getStyleClass().contains("blue-but"));

    // Check files button
    clickOnAndReloadTop("#butDessign");
    clickOnAndReload("#checkFilesButton");
    clickOnAndReloadTop("#butReports");
    FxAssert.verifyThat("#tabReports", NodeMatchers.isNull());
  }
}


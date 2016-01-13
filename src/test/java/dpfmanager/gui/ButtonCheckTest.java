package dpfmanager.gui;

import dpfmanager.MainApp;
import javafx.geometry.VerticalDirection;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.*;

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
  public void testButtonsBelow() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running check buttons test...");

    // Continue 3 button
//    clickOnAndReload("#newButton");
//    clickOnAndReload("#continue1");
//    clickOnAndReload("#continue2");
//    scroll(10, VerticalDirection.DOWN);
//    moveTo("#continue3");
////    clickOnAndReload("#continue3");
//    this.press(MouseButton.PRIMARY);
//    sleep(250);
//    this.release(MouseButton.PRIMARY);
//    FxAssert.verifyThat("#continue4", NodeMatchers.isNotNull());

    // Check files button
//    clickOnAndReload("#butChecker");
    clickOnAndReload("#checkFilesButton");
    clickOnAndReload("#butReport");
    FxAssert.verifyThat("#tab_reports", NodeMatchers.isNull());
  }
}


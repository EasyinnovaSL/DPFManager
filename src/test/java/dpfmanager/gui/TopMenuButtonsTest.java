package dpfmanager.gui;

import dpfmanager.MainApp;
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
  public void testButAbout() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();

    //Check buton about
    clickOn("#butAbout");
    FxAssert.verifyThat("#aboutTitle", NodeMatchers.hasText("About DPF Manager"));
  }

}

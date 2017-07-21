package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Created by Adrià Llorens on 11/01/2016.
 */
public class FileCheckFormatsTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/configFormats.dpf";
  private String inputFilePath = "src/test/resources/SmallGui.zip";
//  private String inputFilePath = "src/test/resources/Small/Bilevel.tif";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Test
  public void testCheckFormats() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running file check test...");

    //Get the current reports number
    int nReports = getCurrentReports();

    //import config file and check files
    GuiWorkbench.setTestParam("import", inputConfigPath);
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(inputConfigPath);
    writeText("#inputText", inputFilePath);
    clickOnAndReload("#checkFilesButton");

    //Wait for the checks
    waitForCheckFiles(1);
    clickOnAndReloadBot("#taskBut");

    // Full formats asserts
    checkFormatsAsserts(nReports);
  }
}

package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.util.ReportRow;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Created by Adrià Llorens on 09/05/2016.
 */
public class JobsTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/configAll.dpf";
  private String inputFilePath = "src/test/resources/SmallGui.zip";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(GuiApp.class, "-gui", "-test", "-t2");
    scene = stage.getScene();
  }

  @Test
  public void testButtonsBelow() throws Exception {
    // Wait for async events
    WaitForAsyncUtils.waitForFxEvents();

    /**
     * Cancel checks test
     */
    System.out.println("Running  cancel jobs test...");

    // Get the current reports number
    int nReports = getCurrentReports();

    // Import config file and check files
    GuiWorkbench.setTestParam("import", inputConfigPath);
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(inputConfigPath);
    writeText("#inputText", inputFilePath);
    clickOnAndReload("#checkFilesButton");

    // After 2 seconds, cancel the check
    Thread.sleep(2000);
    clickOnAndReload("#cancelImage");

    // Wait for cancel request and check that there isn't the report
    waitForCancelChecks();
    clickOnAndReloadTop("#butReports", "#pane-reports");
    waitForTable("#tabReports");
    TableView<ReportRow> table = (TableView) scene.lookup("#tabReports");
    org.junit.Assert.assertEquals("Reports table rows", Math.min(nReports, ReportsModel.reports_loaded), table.getItems().size());

    /**
     * Pause checks test
     */
    System.out.println("Running pause jobs test...");

    // Init new check
    clickOnAndReloadTop("#butDessign", "#pane-design");
    clickOnAndReloadBot("#taskBut");
    GuiWorkbench.setTestParam("import", inputConfigPath);
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(inputConfigPath);
    clickOnAndReload("#checkFilesButton");

    // After 2 seconds, pause the check
    Thread.sleep(2000);
    clickOnAndReload("#resumePauseImage");

    // Wait 20 seconds and resume it
    Thread.sleep(20000);
    ImageView imgPause = (ImageView) scene.lookup("#resumePauseImage");
    ImageView imgCancel = (ImageView) scene.lookup("#cancelImage");
    Assert.assertEquals(true, imgPause.isVisible());
    Assert.assertEquals(true, imgCancel.isVisible());
    clickOnAndReload("#resumePauseImage");

    // Wait for finish
    waitForCheckFiles(1);

    // Pause and cancel are not visible
    imgPause = (ImageView) scene.lookup("#resumePauseImage");
    imgCancel = (ImageView) scene.lookup("#cancelImage");
    Assert.assertEquals(false, imgPause.isVisible());
    Assert.assertEquals(false, imgCancel.isVisible());
  }

}

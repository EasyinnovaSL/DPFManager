package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.util.ReportRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Created by Adrià Llorens on 11/01/2016.
 */
public class MultiFileCheckTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/SimpleXml.dpf";
  private String inputFilePath = "src/test/resources/Small/Bilevel.tif;src/test/resources/SmallGui.zip";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Test
  public void testFileCheck() throws Exception {
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
    clickOnAndReloadBot("#taskButInConsole");

    //Check table view
    clickOnAndReloadTop("#butReports","#pane-reports");
    waitForTable("#tabReports");
    TableView<ReportRow> table = (TableView) scene.lookup("#tabReports");
    ReportRow row = table.getItems().get(0);
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsModel.reports_loaded), table.getItems().size());
    Assert.assertEquals("Report row N files", "4", row.getNfiles());
    Assert.assertEquals("Report row N passed", "4 passed", row.getPassed());
  }
}


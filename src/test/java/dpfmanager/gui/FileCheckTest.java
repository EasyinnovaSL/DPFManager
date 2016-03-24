package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.core.ReportRow;
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
public class FileCheckTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/configAll.dpf";
  private String inputFilePath = "src/test/resources/SmallGui.zip";
//  private String inputFilePath = "src/test/resources/Small/Bilevel.tif";

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
    clickOnImportedConfig(inputConfigPath);
    writeText("#inputText", inputFilePath);
    clickOnAndReload("#checkFilesButton");
    FxAssert.verifyThat("#loadingVbox", NodeMatchers.isVisible()); //Check loading screen
    waitForCheckFiles();

    //Check table view
    clickOnAndReloadTop("#butReports","#pane-reports");
    TableView<ReportRow> table = (TableView) scene.lookup("#tabReports");
    ReportRow row = table.getItems().get(0);
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsModel.reports_loaded), table.getItems().size());
    Assert.assertEquals("Report row N files", "3", row.getNfiles());
    Assert.assertEquals("Report row N passed", "1 passed", row.getPassed());
    Assert.assertEquals("Report row N errors", "2 errors", row.getErrors());
    Assert.assertEquals("Report row N warnings", "0 warnings", row.getWarnings());

    //Check html && pdf exists
    FxAssert.verifyThat("#tabReports #buthtml", NodeMatchers.isNotNull());
    clickOnAndReload("#tabReports #buthtml", "#pane-show");
    FxAssert.verifyThat("#webView", NodeMatchers.isNotNull());

    //Check xml
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#tabReports #butxml", "#pane-show");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    TextArea textArea = (TextArea) scene.lookup("#textArea");
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String initial = textArea.getText().substring(0,expected.length());
    Assert.assertEquals("Report xml", expected, initial);

    //Check json
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#tabReports #butjson", "#pane-show");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    textArea = (TextArea) scene.lookup("#textArea");
    JsonObject jObj = new JsonParser().parse(textArea.getText()).getAsJsonObject();
    Assert.assertTrue("Report json", (jObj.has("individualreports") && jObj.has("stats")));
  }
}


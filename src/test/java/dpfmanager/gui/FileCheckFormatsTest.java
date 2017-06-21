package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.util.ReportRow;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

  @Test
  public void testQuickCheckFormats() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running quick file check test...");

    //Get the current reports number
    int nReports = getCurrentReports();

    //import config file and check files
    GuiWorkbench.setTestParam("import", inputConfigPath);
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(inputConfigPath);
    writeText("#inputText", inputFilePath);
    clickOnAndReload("#quickCheckFilesButton");

    //Wait for the checks
    waitForCheckFiles(1);
    clickOnAndReloadBot("#taskBut");

    // Full formats asserts
    checkFormatsAsserts(nReports);
  }

  public void checkFormatsAsserts(int nReports) throws Exception {
    //Check table view
    clickOnAndReloadTop("#butReports", "#pane-reports");
    waitUntilExists("#lastReportRow");
    VBox mainVBox = (VBox) scene.lookup("#mainVBox");
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsModel.reports_to_load), mainVBox.getChildren().size());
    AnchorPane row = (AnchorPane) mainVBox.getChildren().get(0);
    GridPane grid = (GridPane) row.getChildren().get(0);
    Assert.assertEquals("Report row N files", "3", ((Label) grid.getChildren().get(2)).getText());
    Assert.assertEquals("Report row N passed", "3 passed", ((Label) grid.getChildren().get(6)).getText());
    Assert.assertEquals("Report row N errors", "0 errors", ((Label) grid.getChildren().get(4)).getText());
    Assert.assertEquals("Report row N warnings", "0 warnings", ((Label) grid.getChildren().get(5)).getText());

    //Check html
    reloadScene();
    waitUntilExists("#mainVBox #buthtml");
    clickOnAndReload("#mainVBox #buthtml", "#pane-show");
    waitUntilExists("#webView");
    FxAssert.verifyThat("#webView", NodeMatchers.isNotNull());

    //Check pdf
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butpdf", "#pane-show");
    waitUntilExists("#pdfPagesVBox");
    waitUntilHasChilds("#pdfPagesVBox");
    FxAssert.verifyThat("#pdfPagesVBox", NodeMatchers.isNotNull());
    VBox pdfPagesVBox = (VBox) scene.lookup("#pdfPagesVBox");
    Assert.assertEquals(1, pdfPagesVBox.getChildren().size());

    //Check xml
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butxml", "#pane-show");
    waitUntilExists("#textArea");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    TextArea textArea = (TextArea) scene.lookup("#textArea");
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"";
    String initial = textArea.getText().substring(0,expected.length());
    Assert.assertEquals("Report xml", expected, initial);

    //Check mets
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butmets", "#pane-show");
    waitUntilExists("#textArea");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    textArea = (TextArea) scene.lookup("#textArea");
    expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"";
    initial = textArea.getText().substring(0,expected.length());
    Assert.assertEquals("Report mets", expected, initial);

    //Check json
    clickOnAndReloadTop("#butReports", "#pane-reports");
    clickOnAndReload("#mainVBox #butjson", "#pane-show");
    waitUntilExists("#textArea");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    textArea = (TextArea) scene.lookup("#textArea");
    JsonObject jObj = new JsonParser().parse(textArea.getText()).getAsJsonObject();
    Assert.assertTrue("Report json", (jObj.has("individualreports") && jObj.has("stats")));
  }
}


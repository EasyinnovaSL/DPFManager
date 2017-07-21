package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.component.report.ReportsController;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.util.ReportRow;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 12/01/2016.
 */
public class ReportParserTest extends ApplicationTest {

  private String configHtml = "src/test/resources/ConfigFiles/SimpleHtml.dpf";
  private String inputFiles = "src/test/resources/OneOffEach.zip";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Test
  public void testReportParser() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running report parser test...");

    //Get the current reports number
    int nReports = getCurrentReports();

    // --
    // First Run the 3 check
    // --
    GuiWorkbench.setTestParam("import", configHtml);
    waitUntilExists("#importButton");
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(configHtml);
    writeText("#inputText", inputFiles);
    clickOnAndReload("#checkFilesButton");

    // Now wait for the 4 checks
    waitForCheckFiles(1);
    clickOnAndReloadBot("#taskBut");

    // Go to reports and check them
    clickOnAndReloadTop("#butReports", "#pane-reports");
    waitUntilExists("#lastReportRow");
    VBox mainVBox = (VBox) scene.lookup("#vboxReports0");
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsController.itemsPerPage), mainVBox.getChildren().size());
    AnchorPane row = (AnchorPane) mainVBox.getChildren().get(0);
    checkValidRow((GridPane) row.getChildren().get(0));
  }

  private void checkValidRow(GridPane grid) {
    Assert.assertEquals("Report row N files", "2", ((Label) grid.getChildren().get(1)).getText());
    Assert.assertEquals("Report row N passed", "1 passed", ((Label) grid.getChildren().get(8)).getText());
    Assert.assertEquals("Report row N errors", "1 errors", ((Label) grid.getChildren().get(6)).getText());
    Assert.assertEquals("Report row N warnings", "1 warnings", ((Label) grid.getChildren().get(7)).getText());
  }
}


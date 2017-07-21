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
    clickOnAndReloadBot("#taskBut");

    //Check table view
    clickOnAndReloadTop("#butReports","#pane-reports");
    waitUntilExists("#lastReportRow");
    VBox mainVBox = (VBox) scene.lookup("#vboxReports0");
    AnchorPane row = (AnchorPane) mainVBox.getChildren().get(0);
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsController.itemsPerPage), mainVBox.getChildren().size());
    GridPane grid = (GridPane) row.getChildren().get(0);
    checkValidRow(grid);
  }

  private void checkValidRow(GridPane grid) {
    Assert.assertEquals("Report row N files", "4", ((Label) grid.getChildren().get(1)).getText());
    Assert.assertEquals("Report row N passed", "4 passed", ((Label) grid.getChildren().get(8)).getText());
    Assert.assertEquals("Report row N errors", "0 errors", ((Label) grid.getChildren().get(6)).getText());
    Assert.assertEquals("Report row N warnings", "0 warnings", ((Label) grid.getChildren().get(7)).getText());
  }

}


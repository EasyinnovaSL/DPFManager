package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.component.report.ReportsController;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public class QuickCheckTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/SimpleXml.dpf";
  private String inputFilePath = "src/test/resources/OneOffEach.zip";

  @Override
  public void init() throws Exception{
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Test
  public void testQuickChecksGui() throws Exception {
    // Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running quick check test...");
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

    //Check table view
    clickOnAndReloadTop("#butReports","#pane-reports");
    waitUntilExists("#lastReportRow");
    VBox mainVBox = (VBox) scene.lookup("#mainVBox");
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsController.itemsPerPage), mainVBox.getChildren().size());
    AnchorPane row = (AnchorPane) mainVBox.getChildren().get(0);
    GridPane grid = (GridPane) row.getChildren().get(0);
    checkValidRow(grid);

    // Check semi transparent formats
    HBox formatsBox = (HBox) grid.getChildren().get(8);
    for (Node n : formatsBox.getChildren()){
      ImageView iv = (ImageView) n;
      Assert.assertEquals("Formats buttons semi-transparent", 0.4, iv.getOpacity(), 0.0);
    }

    // Check only ser report
    File reportFolder = new File(ReportGenerator.getLastReportPath());
    Assert.assertTrue("Report path exists", reportFolder.exists());
    Assert.assertTrue("Report path is directory", reportFolder.isDirectory());
    Assert.assertEquals("Report folder items", 2, reportFolder.list().length);
  }

  private void checkValidRow(GridPane grid) {
    Assert.assertEquals("Report row N files", "2", ((Label) grid.getChildren().get(2)).getText());
    Assert.assertEquals("Report row N passed", "1 passed", ((Label) grid.getChildren().get(6)).getText());
    Assert.assertEquals("Report row N errors", "1 errors", ((Label) grid.getChildren().get(4)).getText());
    Assert.assertEquals("Report row N warnings", "0 warnings", ((Label) grid.getChildren().get(5)).getText());
  }

}

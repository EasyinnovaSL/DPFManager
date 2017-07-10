package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
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
public class RecursiveTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/SimpleXml.dpf";
  private String inputFilePath = "src/test/resources/S2";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Test
  public void recursiveGuiTest() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running recursive gui test...");

    //import config file
    GuiWorkbench.setTestParam("import", inputConfigPath);
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(inputConfigPath);
    writeText("#inputText", inputFilePath);

    // Check without recursive
    clickOnAndReload("#checkFilesButton");
    clickOnAndReloadBot("#taskBut");

    // Check with recursive
    clickOnAndReload("#comboChoice");
    clickOnAndReload("Folder");
    clickOnAndReload("#recursiveCheck");
    writeText("#inputText", inputFilePath);
    clickOnAndReload("#checkFilesButton");

    // Wait for checks
    waitForCheckFiles(2);
    clickOnAndReloadBot("#taskBut");

    //Check table view
    clickOnAndReloadTop("#butReports", "#pane-reports");
    waitUntilExists("#lastReportRow");

    VBox mainVBox = (VBox) scene.lookup("#vboxReports0");
    AnchorPane row1 = (AnchorPane) mainVBox.getChildren().get(0);
    GridPane grid1 = (GridPane) row1.getChildren().get(0);
    AnchorPane row2 = (AnchorPane) mainVBox.getChildren().get(1);
    GridPane grid2 = (GridPane) row2.getChildren().get(0);
    int n1 = Integer.parseInt(((Label) grid1.getChildren().get(1)).getText());
    int n2 = Integer.parseInt(((Label) grid2.getChildren().get(1)).getText());
    Assert.assertEquals("Reports count doesn't match ("+n1+", "+n2+")", true, (n1 == 2 || n2 == 2) && (n1 == 4 || n2 == 4));
  }
}


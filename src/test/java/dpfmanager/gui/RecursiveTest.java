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
    clickOnAndReloadBot("#taskButInConsole");

    // Check with recursive
    clickOnAndReload("#comboChoice");
    clickOnAndReload("Folder");
    clickOnAndReload("#recursiveCheck");
    writeText("#inputText", inputFilePath);
    clickOnAndReload("#checkFilesButton");

    // Wait for checks
    waitForCheckFiles(2);
    clickOnAndReloadBot("#taskButInConsole");

    //Check table view
    clickOnAndReloadTop("#butReports", "#pane-reports");
    waitForTable("#tabReports");
    TableView<ReportRow> table = (TableView) scene.lookup("#tabReports");
    int n1 = Integer.parseInt(table.getItems().get(0).getNfiles());
    int n2 = Integer.parseInt(table.getItems().get(1).getNfiles());
    Assert.assertEquals("Reports count doesn't match ("+n1+", "+n2+")", true, (n1 == 2 || n2 == 2) && (n1 == 4 || n2 == 4));
  }
}


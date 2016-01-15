package dpfmanager.gui;

import dpfmanager.MainApp;
import dpfmanager.shell.modules.reporting.ReportRow;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Site;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
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
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testFileCheck() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running file check test...");

    //Get the current reports number
    int nReports = getCurrentReports();

    //import config file
    MainApp.setTestParam("import", inputConfigPath);
    clickOnScroll("#importButton");
    clickOnImportedConfig(inputConfigPath);
    writeText("#txtBox1", inputFilePath);
    clickOnAndReload("#checkFilesButton");
    FxAssert.verifyThat("#loadingPane", NodeMatchers.isNotNull()); //Check loading screen
    waitForCheckFiles(60);

    //Check table view
    clickOnAndReload("#butReport");
    TableView<ReportRow> table = (TableView) scene.lookup("#tab_reports");
    ReportRow row = table.getItems().get(0);
    Assert.assertEquals("Reports table rows", nReports+1, table.getItems().size());
    Assert.assertEquals("Report row N files", "3", row.getNfiles());
    Assert.assertEquals("Report row N passed", "1 passed", row.getPassed());
    Assert.assertEquals("Report row N errors", "2 errors", row.getErrors());
    Assert.assertEquals("Report row N warnings", "0 warnings", row.getWarnings());

    //Check html && pdf exists
    FxAssert.verifyThat("#tab_reports #buthtml", NodeMatchers.isNotNull());
    clickOnAndReload("#tab_reports #buthtml");
    FxAssert.verifyThat("#webViewReport", NodeMatchers.isNotNull());

    //Check xml
    clickOnAndReload("#butReport");
    clickOnAndReload("#tab_reports #butxml");
    FxAssert.verifyThat("#textAreaReport", NodeMatchers.isNotNull());
    TextArea textArea = (TextArea) scene.lookup("#textAreaReport");
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String initial = textArea.getText().substring(0,expected.length());
    Assert.assertEquals("Report xml", expected, initial);

    //Check json
    clickOnAndReload("#butReport");
    clickOnAndReload("#tab_reports #butjson");
    FxAssert.verifyThat("#textAreaReport", NodeMatchers.isNotNull());
    textArea = (TextArea) scene.lookup("#textAreaReport");
    JsonObject jObj = new JsonParser().parse(textArea.getText()).getAsJsonObject();
    Assert.assertTrue("Report json", (jObj.has("individualreports") && jObj.has("stats")));
  }
}


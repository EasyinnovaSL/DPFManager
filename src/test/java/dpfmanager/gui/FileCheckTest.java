package dpfmanager.gui;

import dpfmanager.MainApp;
import dpfmanager.shell.modules.reporting.ReportRow;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

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

  private String inputConfigPath = "src/test/resources/TestFiles/configAll.dpf";
  private String inputFilePath = "src/test/resources/Small.zip";
//  private String inputFilePath = "src/test/resources/Small/Bilevel.tif";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testImportConfigFile() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running check file test...");

    //import config file
//    MainApp.setTestParam("import", inputConfigPath);
//    clickOn("#importButton");
//    clickOn("#radioConfig1");

    //Check files
//    clickOn("#txtBox1").eraseText(13).write(inputFilePath);
//    clickOnAndReload("#checkFilesButton");

    //Wait for check files
//    int timeout = waitForCheckFiles();

    //Check timeout && HTML report
//    Assert.assertNotEquals("Check files reached timeout! (60s)", 60, timeout);
//    FxAssert.verifyThat("#webViewReport", NodeMatchers.isNotNull());

    //Check table view
    clickOnAndReload("#butReport");
    TableView<ReportRow> table = (TableView) scene.lookup("#tab_reports");
//    Assert.assertEquals("Reports table rows", 1, table.getItems().size());
    ReportRow row = table.getItems().get(0);
    Assert.assertEquals("Number of reports", "8", row.getNfiles());
    clickOnAndReload("#tab_reports #butxml");
    sleep(5000);
//    row.get
//    row.getNfiles()

  }

  private int waitForCheckFiles(){
    sleep(1000);
    int timeout = 0;
    boolean finish = false;
    while (!finish && timeout < 60) {
      reloadScene();
      Node node = scene.lookup("#loadingPane");
      if (node != null) {
        timeout++;
        sleep(1000);
      } else {
        finish = true;
      }
    }
    sleep(1000);
    return timeout;
  }

}


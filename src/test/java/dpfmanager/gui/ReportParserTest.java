package dpfmanager.gui;

import dpfmanager.MainApp;
import dpfmanager.shell.modules.reporting.ReportRow;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 12/01/2016.
 */
public class ReportParserTest extends ApplicationTest {

  private String configHtml = "src/test/resources/ConfigFiles/SimpleHtml.dpf";
  private String configXml = "src/test/resources/ConfigFiles/SimpleXml.dpf";
  private String configJson = "src/test/resources/ConfigFiles/SimpleJson.dpf";
  private String inputFiles = "src/test/resources/OneOffEach.zip";

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
    System.out.println("Running report parser test...");

    // --
    // First Run the 3 check
    // --
//    List<String> list = Arrays.asList(configHtml, configJson, configXml);
//    for (String configFile : list){
//      MainApp.setTestParam("import", configFile);
//      clickOn("#importButton");
//      clickOnImportedConfig(configFile);
//      writeText("#txtBox1", inputFiles);
//      clickOnAndReload("#checkFilesButton");
//      waitForCheckFiles();
//      clickOnAndReload("#butChecker");
//    }

    // Go to reports and check them
    clickOnAndReload("#butReport");
    TableView<ReportRow> table = (TableView) scene.lookup("#tab_reports");
    Assert.assertEquals("Reports table rows", 10, table.getItems().size());
    checkValidRow(table.getItems().get(0), "XML"); //Xml
    checkValidRow(table.getItems().get(1), "JSON"); //Json
    checkValidRow(table.getItems().get(2), "HTML"); //Html
  }

  private void waitForCheckFiles(){
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
    Assert.assertNotEquals("Check files reached timeout! (60s)", 60, timeout);
  }

  private void writeText(String id, String text){
    TextField txtField = (TextField) scene.lookup(id);
    int length = txtField.getText().length();
    clickOn(id).eraseText(length).write(text);
  }

  private void checkValidRow(ReportRow row, String type){
    Assert.assertEquals("Report row N files ("+type+")", "2", row.getNfiles());
    Assert.assertEquals("Report row N passed ("+type+")", "1 passed", row.getPassed());
    Assert.assertEquals("Report row N errors ("+type+")", "1 errors", row.getErrors());
    Assert.assertEquals("Report row N warnings ("+type+")", "1 warnings", row.getWarnings());
    Assert.assertEquals("Report row score ("+type+")", "50%", row.getScore());
  }

  private void clickOnImportedConfig(String path){
    AnchorPane ap = (AnchorPane) scene.lookup("#pane1");  //Get Anchor Pane
    VBox vbox = (VBox) ap.getChildren().get(0);           //Get VBox
    String idToClick = "#";
    String search = path.replaceAll("/","_").replaceAll("\\\\","_");
    for (Node node : vbox.getChildren()){
      RadioButton rb = (RadioButton) node;
      String text = rb.getText().replaceAll("/", "_").replaceAll("\\\\","_");
      if (text.endsWith(search)){
        idToClick += rb.getId();
      }
    }
    clickOn(idToClick);
  }

}


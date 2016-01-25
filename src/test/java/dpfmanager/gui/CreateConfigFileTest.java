package dpfmanager.gui;

import dpfmanager.MainApp;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.util.WaitForAsyncUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import com.google.common.base.Predicate;

import javax.xml.bind.SchemaOutputResolver;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public class CreateConfigFileTest extends ApplicationTest {

  private String outputPath = "temp/config.dpf";
  private String expectedPath = "src/test/resources/ConfigFiles/config.dpf";

  Stage stage = null;
  private int uniqueId = 0;

  @Override
  public void init() throws Exception {
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testCreateConfigFile() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running create config file test...");

    // 1 - Click New Button
    clickOnAndReload("#newButton");

    // 2 - Deselect tiff baseline and Select Tiff/EP & TIFF/IT-P1
    // 1: Baseline   2: EP   3: IT   4: ITP1   5: IT-P2
    clickOnScroll("#radProf1");
    clickOnScroll("#radProf2");
    clickOnScroll("#radProf4");
    clickOnAndReload("#continue1");

    // 3 - Add Rule
    addRule("ImageWidth", ">", "500");
    addRule("ImageHeight", "<", "1000");
    clickOnScroll("#ID0 #removeButton");
    clickOnAndReload("#continue2");

    // 4 - Repot format
    clickOnScroll("#chkHtml");
    clickOnScroll("#chkPdf");
    clickOnAndReload("#continue3");

    // 5 - Add Fix
    addFix("Add Tag", "Artist", "EasyTest");
    addFix("Remove Tag", "Copyright", "Easyinnova");
    clickOnScroll("#ID3 #removeButton");
    clickOnAndReload("#continue4");

    // Create temp folder
    createTempFolder();

    // 6 - Save the report
    MainApp.setTestParam("saveConfig",outputPath);
    clickOnScroll("#saveReportButton");

    // Print generated file
//    System.out.println("\nOutput file:");
//    printFile(outputPath);
//    System.out.println("\nExpected file:");
//    printFile(expectedPath);

    // Compare Result
    String expected = FileUtils.readFileToString(new File(expectedPath), "utf-8").replace("\r", "");
    String output = FileUtils.readFileToString(new File(outputPath), "utf-8").replace("\r", "");
    Assert.assertEquals("Config files differ!", expected, output);

    // Delete temp folder
    FileUtils.deleteDirectory(new File("temp"));
  }

  private void addRule(String tag, String op, String text) {
    // First click Add Rule Button
    clickOn("#addRule");

    //Set the rule parameters
    clickOnScroll("#ID" + uniqueId + " #comboBoxTag").clickOn(tag);
    clickOnScroll("#ID" + uniqueId + " #comboBoxOp").clickOn(op);
    clickOnScroll("#ID" + uniqueId + " #textField").write(text);

    //Check combobox items size
    ComboBox comboBoxTag = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxTag");
    ComboBox comboBoxOp = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxOp");
    Assert.assertEquals("ComboBox Tag inside 'Add Rule' failed", 4, comboBoxTag.getItems().size());
    Assert.assertEquals("ComboBox Operator inside 'Add Rule' failed", 3, comboBoxOp.getItems().size());

    uniqueId++;
  }

  private void addFix(String action, String field, String text) {
    // First click Add Fix Button
    clickOnScroll("#addFix");

    //Set the rule parameters
    clickOnScroll("#ID" + uniqueId + " #comboBoxAction").clickOn(action);
    clickOnScroll("#ID" + uniqueId + " #comboBoxField").clickOn(field);
    if (action.equals("Add Tag")) {
      clickOnScroll("#ID" + uniqueId + " #textField").write(text);
    }

    //Check combobox items size
    ComboBox comboBoxAction = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxAction");
    ComboBox comboBoxFix = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxField");
    Assert.assertEquals("ComboBox Action inside 'Add Fix' failed", 2, comboBoxAction.getItems().size());
    Assert.assertEquals("ComboBox Field inside 'Add Fix' failed", 3, comboBoxFix.getItems().size());

    uniqueId++;
  }

  private void createTempFolder(){
    File folder = new File("temp");
    if (!folder.exists()){
      folder.mkdirs();
    }
  }

  private void printFile(String outputPath) throws Exception {
    System.out.println("\n\n\nConfig File:\n");
    FileReader fr = new FileReader(outputPath);
    BufferedReader br = new BufferedReader(fr);
    String s;
    while ((s = br.readLine()) != null) {
      System.out.println(s);
    }
    fr.close();
  }

  //Unused
  private class isChecked implements Predicate<Node> {
    @Override
    public boolean apply(Node node) {
      CheckBox checkbox = (CheckBox) node;
      return checkbox.isSelected();
    }
  }

  private class isNotChecked implements Predicate<Node> {
    @Override
    public boolean apply(Node node) {
      CheckBox checkbox = (CheckBox) node;
      return !checkbox.isSelected();
    }
  }
}

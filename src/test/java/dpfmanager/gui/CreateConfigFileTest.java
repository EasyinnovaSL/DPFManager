package dpfmanager.gui;

import dpfmanager.MainApp;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import com.sun.javafx.robot.FXRobot;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.matcher.base.GeneralMatchers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.function.Predicate;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public class CreateConfigFileTest extends ApplicationTest {

  Stage stage = null;
  private int uniqueId = 0;

  @Override
  public void init() throws Exception {
    stage = launch(MainApp.class, "-gui", "-noDisc", "-test");
    scene = stage.getScene();
  }

  private class isChecked implements Predicate {
    @Override
    public boolean test(Object o) {
      CheckBox checkbox = (CheckBox) o;
      return checkbox.isSelected();
    }
  }

  private class isNotChecked implements Predicate {
    @Override
    public boolean test(Object o) {
      CheckBox checkbox = (CheckBox) o;
      return !checkbox.isSelected();
    }
  }

  @Test
  public void testCreateConfigFile() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();

    // 1 - Click New Button
    clickOnAndReload("#newButton");

    // 2 - Deselect tiff baseline and Select Tiff/EP & TIFF/IT-P1
    // 1: Baseline   2: EP   3: IT   4: ITP1   5: IT-P2
    clickOn("#radProf1");
    clickOn("#radProf2");
    clickOn("#radProf4");
    clickOnAndReload("#continue1");

    // 3 - Add Rule
    addRule("ImageWidth", ">", "500");
    addRule("ImageHeight", "<", "1000");
    clickOn("#ID0 #removeButton");
    clickOnAndReload("#continue2");

    // 4 - Repot format
    clickOn("#chkPdf");
    clickOnAndReload("#continue3");

    // 5 - Add Fix
    addFix("Add Tag", "Artist", "EasyTest");
    addFix("Remove Tag", "Copyright", "Easyinnova");
    clickOn("#ID3 #removeButton");
    clickOnAndReload("#continue4");

    // Test the steps buttons

    // 6 - Save the report
    String outputPath = "D:/tmp/config.dpf";
    clickOn("#outputTextField").write(outputPath);
    clickOn("#saveReportButton");

    // Compare Result
    String expectedPath = "src/test/resources/TestFiles/config.dpf";
    File expected = new File(expectedPath);
    File output = new File(outputPath);
    Assert.assertEquals("Config files differ!", FileUtils.readFileToString(expected, "utf-8"), FileUtils.readFileToString(output, "utf-8"));

    System.out.println("\n\n\nConfig File:\n");
    FileReader fr = new FileReader(outputPath);
    BufferedReader br = new BufferedReader(fr);
    String s;
    while ((s = br.readLine()) != null) {
      System.out.println(s);
    }
    fr.close();
  }

  private void addRule(String tag, String op, String text) {
    // First click Add Rule Button
    clickOn("#addRule");

    //Set the rule parameters
    clickOn("#ID" + uniqueId + " #comboBoxTag").clickOn(tag);
    clickOn("#ID" + uniqueId + " #comboBoxOp").clickOn(op);
    clickOn("#ID" + uniqueId + " #textField").write(text);

    //Check combobox items size
    ComboBox comboBoxTag = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxTag");
    ComboBox comboBoxOp = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxOp");
    Assert.assertEquals("ComboBox Tag inside 'Add Rule' failed", 4, comboBoxTag.getItems().size());
    Assert.assertEquals("ComboBox Operator inside 'Add Rule' failed", 3, comboBoxOp.getItems().size());

    uniqueId++;
  }

  private void addFix(String action, String field, String text) {
    // First click Add Fix Button
    clickOn("#addFix");

    //Set the rule parameters
    clickOn("#ID" + uniqueId + " #comboBoxAction").clickOn(action);
    clickOn("#ID" + uniqueId + " #comboBoxField").clickOn(field);
    if (action.equals("Add Tag")) {
      clickOn("#ID" + uniqueId + " #textField").write(text);
    }

    //Check combobox items size
    ComboBox comboBoxAction = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxAction");
    ComboBox comboBoxFix = (ComboBox) scene.lookup("#ID" + uniqueId + " #comboBoxField");
    Assert.assertEquals("ComboBox Action inside 'Add Fix' failed", 2, comboBoxAction.getItems().size());
    Assert.assertEquals("ComboBox Field inside 'Add Fix' failed", 3, comboBoxFix.getItems().size());

    uniqueId++;
  }
}

package dpfmanager.shell.modules.classes;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by easy on 09/10/2015.
 */
public class Fixes {
  private ArrayList<Fix> fixes;

  /**
   * Instantiates a new Fixes.
   */
  public Fixes() {
    fixes = new ArrayList<Fix>();
  }

  /**
   * Gets fixes.
   *
   * @return the fixes
   */
  public ArrayList<Fix> getFixes() {
    return fixes;
  }

  /**
   * Read fixes.
   *
   * @param scene the scene
   */
  public void ReadFixes(Scene scene) {
    Boolean wrong_format = false;
    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    for (Node node : ap2.getChildren()){
      if(node instanceof HBox) {
        HBox hBox1 = (HBox)node;
        String tag = null, operator = null, value = null;
        for (Node nodeIn : hBox1.getChildren()){
          if(nodeIn instanceof ComboBox) {
            ComboBox comboBox = (ComboBox)nodeIn;
            if (comboBox.getValue() != null){
              if (operator == null) operator = comboBox.getValue().toString();
              else tag = comboBox.getValue().toString();
            }
            else{
              wrong_format = true;
            }
          } else if(nodeIn instanceof TextField) {
            TextField textField = (TextField)nodeIn;
            String nodeInValue = textField.getText();
            if (!nodeInValue.isEmpty()){
              value = textField.getText();
            }
            else{
              wrong_format = true;
            }
          }
        }
        if (!wrong_format) {
          Fix fix = new Fix(tag, operator, value);
          fixes.add(fix);
        }
      }
      wrong_format=false;
    }
  }

  /**
   * Read autofixes.
   *
   * @param scene the scene
   */
  public void ReadAutofixes(Scene scene) {
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    for (Node node : ap2.getChildren()) {
      if (node instanceof CheckBox) {
        CheckBox check = (CheckBox)node;
        if (check.isSelected()) {
          addFixFromTxt(check.getId() + ",Yes");
        }
      }
    }
  }

  /**
   * Write.
   *
   * @param pw the pw
   */
  public void Write(PrintWriter pw) {
    for (Fix fix : fixes) {
      pw.println("FIX\t" + fix.Txt());
    }
  }

  /**
   * Add fix from txt.
   *
   * @param txt the txt
   */
  public void addFixFromTxt(String txt) {
    Fix fix = new Fix();
    fix.ReadTxt(txt);
    fixes.add(fix);
  }
}

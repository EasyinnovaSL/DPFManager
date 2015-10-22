package dpfmanager.shell.modules.classes;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by easy on 09/10/2015.
 */
public class Fixes {
  private ArrayList<Fix> fixes;

  public Fixes() {
    fixes = new ArrayList<Fix>();
  }

  public ArrayList<Fix> getFixes() {
    return fixes;
  }

  public void Read(Scene scene) {
    Boolean wrong_format = false;
    VBox tabPane = ((VBox) ((SplitPane) scene.getRoot().getChildrenUnmodifiable().get(0)).getItems().get(1));
    AnchorPane ap = (AnchorPane)(tabPane.getChildren().get(0));
    ScrollPane sp = (ScrollPane)(ap.getChildren().get(0));
    AnchorPane ap2 = (AnchorPane)(sp.getContent());
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

  public void Write(PrintWriter pw) {
    for (Fix fix : fixes) {
      pw.println("FIX\t" + fix.Txt());
    }
  }

  public void addFixFromTxt(String txt) {
    Fix fix = new Fix();
    fix.ReadTxt(txt);
    fixes.add(fix);
  }
}

package dpfmanager.shell.modules;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by easy on 06/10/2015.
 */
public class Rules {
  private ArrayList<Rule> rules;

  public Rules() {
    rules = new ArrayList<Rule>();
  }

  public void Read(Scene scene) {
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
              if (tag == null) tag = comboBox.getValue().toString();
              else operator = comboBox.getValue().toString();
            }
          } else if(nodeIn instanceof TextField) {
            TextField textField = (TextField)nodeIn;
            if (textField.getText() != null){
              value = textField.getText();
            }
          }
        }
        Rule rule = new Rule(tag, operator, value);
        rules.add(rule);
      }
    }
  }
}

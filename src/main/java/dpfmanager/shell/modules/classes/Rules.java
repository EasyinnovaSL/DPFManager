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
import java.util.regex.Pattern;

/**
 * Created by easy on 06/10/2015.
 */
public class Rules {
  private ArrayList<Rule> rules;

  public Rules() {
    rules = new ArrayList<Rule>();
  }

  public ArrayList<Rule> getRules() {
    return rules;
  }

  public void Read(Scene scene) {
    Boolean wrong_format = false;
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    for (Node node : ap2.getChildren()){
      if(node instanceof HBox) {
        HBox hBox1 = (HBox)node;
        String tag = null, operator = null, value = null;
        for (Node nodeIn : hBox1.getChildren()){
          if(nodeIn instanceof ComboBox) {
            ComboBox comboBox = (ComboBox)nodeIn;
            if (comboBox.getValue() != null){
              String comboBoxVal = comboBox.getValue().toString();
              if (tag == null) tag = comboBoxVal;
              else if (operator == null) operator = comboBoxVal;
              else value = comboBoxVal;
            } else{
              wrong_format = true;
            }
          } else if(nodeIn instanceof TextField) {
            TextField textField = (TextField)nodeIn;
            String nodeInValue = textField.getText();
            if (!nodeInValue.isEmpty()){
              if (!Pattern.matches("[a-zA-Z ]+", nodeInValue)) {
                value = nodeInValue;
              }else{
                wrong_format = true;
              }
            } else{
              wrong_format = true;
            }
          }
        }
        if (!wrong_format) {
          Rule rule = new Rule(tag, operator, value);
          rules.add(rule);
        }
      }
      wrong_format = false;
    }
  }

  public void Write(PrintWriter pw) {
    for (Rule rule : rules) {
      pw.println("RULE\t" + rule.Txt());
    }
  }

  public void addRuleFromTxt(String txt) {
    Rule rule = new Rule();
    rule.ReadTxt(txt);
    rules.add(rule);
  }
}

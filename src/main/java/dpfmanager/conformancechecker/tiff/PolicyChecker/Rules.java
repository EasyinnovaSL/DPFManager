package dpfmanager.conformancechecker.tiff.PolicyChecker;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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

  /**
   * Instantiates a new Rules.
   */
  public Rules() {
    rules = new ArrayList<Rule>();
  }

  /**
   * Gets rules.
   *
   * @return the rules
   */
  public ArrayList<Rule> getRules() {
    return rules;
  }

  /**
   * Read.
   *
   * @param scene the scene
   */
  public void Read(Scene scene) {
    // TODO eliminar
    rules.clear();
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

  /**
   * Read.
   *
   * @param rulesBox the vbox node
   */
  public void Read(VBox rulesBox) {
    rules.clear();
    boolean wrong_format = false;
    for (Node n : rulesBox.getChildren()){
      HBox hbox = (HBox) n;
      String tag = null, operator = null, value = null;
      if (hbox.getChildren().size() != 4){
        wrong_format = true;
      }
      else {
        ComboBox comboBox = (ComboBox) hbox.getChildren().get(0);
        if (comboBox.getValue() != null) {
          tag = comboBox.getValue().toString();
        }
        else{
          wrong_format = true;
        }

        ComboBox comboOp = (ComboBox) hbox.getChildren().get(1);
        if (comboOp.getValue() != null) {
          operator = comboOp.getValue().toString();
        }
        else{
          wrong_format = true;
        }

        Node nodeVal = hbox.getChildren().get(2);
        if (nodeVal instanceof ComboBox) {
          ComboBox comboVal = (ComboBox) nodeVal;
          value = comboVal.getValue().toString();
        } else if (nodeVal instanceof TextField) {
          TextField textVal = (TextField) nodeVal;
          value = textVal.getText();
          if (value.isEmpty() || Pattern.matches("[a-zA-Z ]+", value)) {
            wrong_format = true;
          }
        }
      }

      if (!wrong_format) {
        Rule rule = new Rule(tag, operator, value);
        rules.add(rule);
      }
    }
  }

  /**
   * Write.
   *
   * @param pw the pw
   */
  public void Write(PrintWriter pw) {
    for (Rule rule : rules) {
      pw.println("RULE\t" + rule.Txt());
    }
  }

  /**
   * Add rule from txt.
   *
   * @param txt the txt
   */
  public void addRuleFromTxt(String txt) {
    Rule rule = new Rule();
    rule.ReadTxt(txt);
    rules.add(rule);
  }
}

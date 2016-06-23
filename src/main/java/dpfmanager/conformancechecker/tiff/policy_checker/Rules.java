package dpfmanager.conformancechecker.tiff.policy_checker;

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

  public void set(ArrayList<Rule> rules) {
    this.rules = rules;
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

  /**
   * Add rule from parameters.
   *
   * @param tag the tag
   * @param operator the operator
   * @param value the value
   * @param warning the warning
   */
  public void addRule(String tag, String operator, String value, boolean warning) {
    Rule rule = new Rule();
    rule.setTag(tag);
    rule.setOperator(operator);
    rule.setValue(value);
    rule.setWarning(warning);
    rules.add(rule);
  }
}

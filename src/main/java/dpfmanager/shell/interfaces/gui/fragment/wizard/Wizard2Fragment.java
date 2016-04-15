package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.conformancechecker.tiff.policy_checker.Rule;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.util.NumberTextField;
import dpfmanager.shell.interfaces.gui.component.config.ConfigModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.controlsfx.control.CheckComboBox;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_2,
    viewLocation = "/fxml/config/subconfig2.fxml",
    localeID = "en_US",
    scope = Scope.SINGLETON)
public class Wizard2Fragment {

  @Resource
  private Context context;

  @FXML
  private VBox rulesBox;

  private ConfigModel model;

  public Wizard2Fragment() {
  }

  @FXML
  protected void addRule() {
    addRule(null, null, null);
  }

  public void clear() {
    rulesBox.getChildren().clear();
  }

  public void saveRules(Configuration config) {
    Rules rules = config.getRules();
    rules.set(readFromGui(rulesBox));
  }

  public ArrayList<Rule> readFromGui(VBox rulesBox) {
    ArrayList<Rule> rules = new ArrayList<Rule>();
    boolean wrong_format = false;
    for (Node n : rulesBox.getChildren()){
      HBox hbox = (HBox) n;
      String tag = null, operator = null, value = null;
      boolean bwarning = false;
      if (hbox.getChildren().size() != 5){
        wrong_format = true;
      } else {
        CheckBox warning = (CheckBox) hbox.getChildren().get(0);
        bwarning = warning.isSelected();

        ComboBox comboBox = (ComboBox) hbox.getChildren().get(1);
        if (comboBox.getValue() != null) {
          tag = comboBox.getValue().toString();
        }
        else{
          wrong_format = true;
        }

        ComboBox comboOp = (ComboBox) hbox.getChildren().get(2);
        if (comboOp.getValue() != null) {
          operator = comboOp.getValue().toString();
        }
        else{
          wrong_format = true;
        }

        Node nodeVal = hbox.getChildren().get(3);
        if (nodeVal instanceof CheckComboBox) {
          CheckComboBox comboVal = (CheckComboBox) nodeVal;
          value = "";
          for (int idx = 0; idx < comboVal.getCheckModel().getCheckedIndices().size(); idx++) {
            int selindex = (Integer)comboVal.getCheckModel().getCheckedIndices().get(idx);
            if (value.length() > 0) value += ";";
            value += comboVal.getCheckModel().getItem(selindex);
          }
        } else if (nodeVal instanceof TextField) {
          TextField textVal = (TextField) nodeVal;
          value = textVal.getText();
          if (value.isEmpty() || Pattern.matches("[a-zA-Z ]+", value)) {
            wrong_format = true;
          }
        } else if (nodeVal instanceof ComboBox) {
          ComboBox comboVal = (ComboBox) nodeVal;
          value = comboVal.getValue().toString();
        }
      }

      if (!wrong_format) {
        Rule rule = new Rule(tag, operator, value, bwarning);
        rules.add(rule);
      }
    }
    return rules;
  }

  public void loadRules(Configuration config) {
    rulesBox.getChildren().clear();
    Rules rules = config.getRules();
    for (Rule r : rules.getRules()) {
      String value = r.getValue();
      String tag = r.getTag();
      String operator = r.getOperator();
      addRule(tag, operator, value);
    }
  }

  private void addRule(String tag, String operator, String value) {
    // HBox container
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setId("ID" + getModel().getNextId());
    hbox.setSpacing(5);

    // Checkboc
    CheckBox warning = new CheckBox();
    warning.setText("Warning");
    warning.getStyleClass().add("checkreport");

    // Add combobox
    ComboBox comboBox = new ComboBox();
    comboBox.setId("comboBoxTag");
    comboBox.getStyleClass().add("combo-box-white");
    for (Field field : getModel().getFields()) {
      comboBox.getItems().add(field.getName());
    }
    if (tag != null) {
      comboBox.setValue(tag);
    }

    // Add remove button
    Button remove = new Button();
    remove.setText("X");
    remove.getStyleClass().add("but-remove");
    remove.setId("removeButton");
    remove.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        rulesBox.getChildren().remove(hbox);
      }
    });

    // Combo Box listener
    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String item) {
        if (hbox.getChildren().size() >= 3) {
          addOperator(item, hbox, remove, null, null);
        }
      }
    });

    //Add to view
    hbox.getChildren().addAll(warning, comboBox, remove);
    rulesBox.getChildren().add(hbox);
    rulesBox.setMargin(hbox, new Insets(0, 0, 15, 0));

    if (operator != null) {
      addOperator(tag, hbox, remove, operator, value);
    }
  }

  private void addOperator(String item, HBox hbox, Button remove, String operatorLoad, String valueLoad) {
    ArrayList<String> operators = getModel().getOperators(item);
    ArrayList<String> values = getModel().getValues(item);

    // Remove
    while (hbox.getChildren().size() > 2) hbox.getChildren().remove(2);

    // Add operator combo box
    if (operators != null) {
      ComboBox comboOp = new ComboBox();
      comboOp.setId("comboBoxOp");
      comboOp.getStyleClass().add("combo-box-white");
      for (String operator : operators) {
        comboOp.getItems().add(operator);
      }
      if (operatorLoad != null) {
        comboOp.setValue(operatorLoad);
      }
      hbox.getChildren().add(comboOp);

      // Combo Box listener
      comboOp.valueProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue ov, String t, String itemchanged) {
          if (hbox.getChildren().size() >= 3) {
            addOperator(item, hbox, remove, comboOp.getValue().toString(), null);
          }
        }
      });
    }

    // Value combo box or text field
    if (values == null || (operatorLoad != null && !operatorLoad.equals("="))) {
      TextField value = new NumberTextField();
      value.setId("textField");
      value.getStyleClass().add("txtRule");
      if (valueLoad != null) {
        value.setText(valueLoad);
      }
      hbox.getChildren().add(value);
    } else {
      CheckComboBox comboVal = new CheckComboBox();
      comboVal.getStyleClass().add("combo-box-white");
      for (String value : values) {
        comboVal.getItems().add(value);
      }
      if (valueLoad != null) {
        for (String valueLoad1 : valueLoad.split(";")) {
          comboVal.getCheckModel().check(comboVal.getCheckModel().getItemIndex(valueLoad1));
        }
      }
      hbox.getChildren().add(comboVal);
    }

    // Add remove button
    hbox.getChildren().add(remove);
  }

  public ConfigModel getModel() {
    return model;
  }

  public void setModel(ConfigModel model) {
    this.model = model;
  }
}

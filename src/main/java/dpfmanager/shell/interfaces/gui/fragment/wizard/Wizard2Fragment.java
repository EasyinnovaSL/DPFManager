package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.tiff.Configuration;
import dpfmanager.conformancechecker.tiff.Field;
import dpfmanager.conformancechecker.tiff.PolicyChecker.Rule;
import dpfmanager.conformancechecker.tiff.PolicyChecker.Rules;
import dpfmanager.shell.interfaces.gui.reimplemented.NumberTextField;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.component.config.ConfigModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_2,
    viewLocation = "/fxml-jr/config/subconfig2.fxml",
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
    rules.Read(rulesBox);
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
        if (hbox.getChildren().size() == 2) {
          addOperator(item, hbox, remove, null, null);
        }
      }
    });

    //Add to view
    hbox.getChildren().addAll(comboBox, remove);
    rulesBox.getChildren().add(hbox);
    rulesBox.setMargin(hbox, new Insets(0, 0, 15, 0));

    if (operator != null) {
      addOperator(tag, hbox, remove, operator, value);
    }
  }

  private void addOperator(String item, HBox hbox, Button remove, String operatorLoad, String valueLoad) {
    ArrayList<String> operators = getModel().getOperators(item);
    ArrayList<String> values = getModel().getValues(item);

    //Remove button X
    hbox.getChildren().remove(remove);

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
    }

    // Value combo box or text field
    if (values == null) {
      TextField value = new NumberTextField();
      value.setId("textField");
      value.getStyleClass().add("txtRule");
      if (valueLoad != null) {
        value.setText(valueLoad);
      }
      hbox.getChildren().add(value);
    } else {
      ComboBox comboVal = new ComboBox();
      comboVal.getStyleClass().add("combo-box-white");
      for (String value : values) {
        comboVal.getItems().add(value);
      }
      if (valueLoad != null) {
        comboVal.setValue(valueLoad);
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

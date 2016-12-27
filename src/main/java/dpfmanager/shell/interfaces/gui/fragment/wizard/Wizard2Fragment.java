/**
 * <h1>Wizard2Fragment.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.configuration.Configuration;
import com.easyinnova.policy_checker.model.Field;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.util.NumberTextField;
import dpfmanager.shell.interfaces.gui.component.config.ConfigController;
import dpfmanager.shell.interfaces.gui.component.config.ConfigModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.policy_checker.model.Rule;
import com.easyinnova.policy_checker.model.Rules;

import org.controlsfx.control.CheckComboBox;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_2,
    viewLocation = "/fxml/config/subconfig2.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class Wizard2Fragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private VBox rulesBox;
  @FXML
  private VBox isosBox;

  private ConfigController controller;
  private ConfigModel model;

  public Wizard2Fragment() {
  }

  public void setController(ConfigController controller) {
    this.controller = controller;
  }

  @FXML
  protected void addRule() {
    addRule(null, null, null, false);
  }

  public void clear() {
    rulesBox.getChildren().clear();
  }

  public void saveRules(Configuration config) {
    Rules rules = config.getRules();
    rules.set(readFromGui(rulesBox));
  }

  public void saveIsos(Configuration config) {
    for (CheckBox chk : getCheckBoxs()) {
      if (!chk.isSelected()) {
        config.removeModifiedIso(chk.getId().replace("w2", ""));
      }
    }
  }

  public ArrayList<Rule> readFromGui(VBox rulesBox) {
    ArrayList<Rule> rules = new ArrayList<Rule>();
    boolean wrong_format = false;
    for (Node n : rulesBox.getChildren()) {
      HBox hbox = (HBox) n;
      String tag = null, operator = null, value = null;
      boolean bwarning = false;
      if (hbox.getChildren().size() != 5) {
        wrong_format = true;
      } else {
        CheckBox warning = (CheckBox) hbox.getChildren().get(0);
        bwarning = warning.isSelected();

        ComboBox comboBox = (ComboBox) hbox.getChildren().get(1);
        if (comboBox.getValue() != null) {
          tag = comboBox.getValue().toString();
        } else {
          wrong_format = true;
        }

        ComboBox comboOp = (ComboBox) hbox.getChildren().get(2);
        if (comboOp.getValue() != null) {
          operator = comboOp.getValue().toString();
        } else {
          wrong_format = true;
        }

        Node nodeVal = hbox.getChildren().get(3);
        if (nodeVal instanceof CheckComboBox) {
          CheckComboBox comboVal = (CheckComboBox) nodeVal;
          value = "";
          for (int idx = 0; idx < comboVal.getCheckModel().getCheckedIndices().size(); idx++) {
            int selindex = (Integer) comboVal.getCheckModel().getCheckedIndices().get(idx);
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
      addRule(tag, operator, value, r.getWarning());
    }
  }

  public void loadIsos(Configuration config) {
    isosBox.getChildren().clear();
    for (String iso : config.getIsos()) {
      addIsoBox(iso, config.getModifiedIso(iso).size());
    }
  }

  private void addIsoBox(String iso, Integer nRules) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);

    String name = ImplementationCheckerLoader.getIsoName(iso);
    if (nRules > 0) {
      name += " " + bundle.getString("w2Invalidated").replace("%1", nRules + "");
    }
    Label label = new Label(name);
    label.setId("w2" + iso);
    label.getStyleClass().add("checkreport");
    hbox.getChildren().add(label);

    // EDIT
    Button edit = new Button();
    edit.getStyleClass().addAll("edit-img", "action-img-16");
    edit.setCursor(Cursor.HAND);
    edit.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        String id = label.getId();
        controller.editIso(id.replace("w2", ""));
      }
    });
    hbox.getChildren().add(edit);
    HBox.setMargin(edit, new Insets(0, 0, 0, 10));

    isosBox.getChildren().add(hbox);
  }

  public void check(String iso) {
    for (CheckBox chk : getCheckBoxs()) {
      if (chk.getId().equals("w2" + iso)) {
        chk.setSelected(true);
      }
    }
  }

  private List<CheckBox> getCheckBoxs() {
    List<CheckBox> boxs = new ArrayList<>();
    for (Node node : isosBox.getChildren()) {
      HBox hbox = (HBox) node;
      if (hbox.getChildren().get(0) instanceof CheckBox) {
        boxs.add((CheckBox) hbox.getChildren().get(0));
      }
    }
    return boxs;
  }

  private void addRule(String tag, String operator, String value, boolean bwarning) {
    // HBox container
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setId("ID" + getModel().getNextId());
    hbox.setSpacing(5);

    // Checkboc
    CheckBox warning = new CheckBox();
    warning.setText(bundle.getString("w2Warning"));
    warning.getStyleClass().add("checkreport");
    warning.setSelected(bwarning);

    // Add combobox
    ComboBox comboBox = new ComboBox();
    comboBox.setId("comboBoxTag");
    comboBox.getStyleClass().addAll("combo-box-white", "dpf-bar");
    for (Field field : getModel().getFields()) {
      comboBox.getItems().add(field.getName());
    }
    if (tag != null) {
      comboBox.setValue(tag);
    }
    comboBox.setCellFactory(param -> {
      return new ListCell<String>() {

        @Override
        public void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);

          if (item != null) {
            setText(item);
            String toolTipText = getModel().getFieldToolTip(item);
            Tooltip tt = new Tooltip(toolTipText);
            setTooltip(tt);
          } else {
            setText(null);
            setTooltip(null);
          }
        }
      };
    });

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
      comboOp.getStyleClass().addAll("combo-box-white", "dpf-bar");
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
      comboVal.getStyleClass().addAll("combo-box-white", "dpf-bar");
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

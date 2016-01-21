/**
 * <h1>ReportGenerator.java</h1>
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Easy Innova
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.interfaces;

import dpfmanager.shell.modules.autofixes.autofix;
import dpfmanager.shell.modules.classes.Configuration;
import dpfmanager.shell.modules.classes.Field;
import dpfmanager.shell.modules.classes.Fix;
import dpfmanager.shell.modules.classes.Fixes;
import dpfmanager.shell.modules.classes.NumberTextField;
import dpfmanager.shell.modules.classes.Rule;
import dpfmanager.shell.modules.classes.Rules;
import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * The Class Gui.
 */
public class Gui extends UserInterface {
  private int uniqueId = 0;
  private double defaultLineYlayout = 564.0;

  public void addRule(Scene scene) {
    addRule(scene, null, null, null);
  }

  public void addRule(Scene scene, String tag, String operator, String value) {
    Button addRule = (Button) scene.lookup("#addRule");
    Button continueButton = (Button) scene.lookup("#continue2");
    Line line = (Line) scene.lookup("#line");
    int dif = 50;
    double xRule = addRule.getLayoutX() - 100;
    double yRule = addRule.getLayoutY();

    // Add combobox
    ComboBox comboBox = new ComboBox();
    comboBox.setId("comboBoxTag");
    for (Field field : getFields()) {
      comboBox.getItems().add(field.getName());
    }
    if (tag != null) {
      comboBox.setValue(tag);
    }

    // Remove button
    String styleButton = "-fx-background-color: transparent;\n" +
        "\t-fx-border-width     : 0px   ;\n" +
        "\t-fx-border-radius: 0 0 0 0;\n" +
        "\t-fx-background-radius: 0 0 0; -fx-text-fill: WHITE; -fx-font-weight:bold;";
    String styleButtonPressed = "-fx-border-width: 0px; -fx-background-color: rgba(255, 255, 255, 0.2);";
    Button remove = new Button();
    remove.setText("X");
    remove.styleProperty().bind(
        Bindings
            .when(remove.pressedProperty())
            .then(new SimpleStringProperty(styleButtonPressed))
            .otherwise(new SimpleStringProperty(styleButton)
            )
    );
    remove.setId("removeButton");
    remove.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        deleteRule(remove.getParent().getId(), scene);
      }
    });
    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String item) {
        addOperator(item, scene);
      }
    });
    HBox hBox = new HBox(comboBox, remove);
    hBox.setId("ID" + uniqueId++);
    hBox.setSpacing(5);
    hBox.setLayoutX(xRule);
    hBox.setLayoutY(yRule);

    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    ap2.getChildren().add(hBox);

    addRule.setLayoutY(addRule.getLayoutY() + dif);

    // Move bottom elements if necessary
    if (addRule.getLayoutY() + addRule.getHeight() > line.getLayoutY()) {
      line.setLayoutY(line.getLayoutY() + dif);
      continueButton.setLayoutY(continueButton.getLayoutY() + dif);
    }

    if (operator != null) {
      addOperator(tag, scene, operator, value);
    }
  }

  private void addOperator(String item, Scene scene) {
    addOperator(item, scene, null, null);
  }

  private void addOperator(String item, Scene scene, String selectedOperator, String selectedValue) {
    ArrayList<String> operators = null;
    ArrayList<String> values = null;
    for (Field field : getFields()) {
      if (field.getName().equals(item)) {
        operators = field.getOperators();
        values = field.getValues();
      }
    }
    if (operators != null) {
      AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
      for (Node node : ap2.getChildren()) {
        Boolean delete_combobox = true;
        if (node instanceof HBox) {
          HBox hBox1 = (HBox) node;
          for (Node nodeIn : hBox1.getChildren()) {
            if (nodeIn instanceof ComboBox) {
              ComboBox comboBox = (ComboBox) nodeIn;
              if (comboBox.getValue() != null && comboBox.getValue().equals(item)) {
                ComboBox comboOp = new ComboBox();
                comboOp.setId("comboBoxOp");
                for (String operator : operators) {
                  comboOp.getItems().add(operator);
                }
                if (selectedOperator != null) {
                  comboOp.setValue(selectedOperator);
                }
                Button bRemove = null;
                if (hBox1.getChildren().get(1) instanceof ComboBox) {
                  ComboBox bla = (ComboBox) hBox1.getChildren().get(1);
                  String val = (String) bla.getValue();
                  if (val != null) {
                    delete_combobox = false;
                  }
                }
                if (delete_combobox) {
                  while (hBox1.getChildren().size() > 1) {
                    for (int i = 1; i < hBox1.getChildren().size(); i++) {
                      if (hBox1.getChildren().get(i) instanceof Button) {
                        bRemove = (Button) hBox1.getChildren().get(i);
                      }
                      hBox1.getChildren().remove(i);
                    }
                  }

                  hBox1.getChildren().add(comboOp);
                  if (values == null) {
                    TextField value = new NumberTextField();
                    value.setId("textField");
                    value.getStyleClass().add("txtRule");
                    if (selectedValue != null) {
                      value.setText(selectedValue);
                    }
                    hBox1.getChildren().add(value);
                  } else {
                    ComboBox comboVal = new ComboBox();
                    for (String value : values) {
                      comboVal.getItems().add(value);
                    }
                    if (selectedValue != null) {
                      comboVal.setValue(selectedValue);
                    }
                    hBox1.getChildren().add(comboVal);
                  }
                  hBox1.getChildren().add(bRemove);
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  void deleteRule(String id, Scene scene) {
    Button addRule = (Button) scene.lookup("#addRule");
    Button continueButton = (Button) scene.lookup("#continue2");
    Line line = (Line) scene.lookup("#line");
    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    ArrayList<Node> lRemove = new ArrayList<Node>();
    for (Node node : ap2.getChildren()) {
      if (node instanceof HBox) {
        HBox hBox1 = (HBox) node;
        if (hBox1.getId().equals(id)) {
          lRemove.add(node);
        } else {
          if (Integer.parseInt(hBox1.getId().substring(2)) > Integer.parseInt(id.substring(2))) {
            hBox1.setLayoutY(hBox1.getLayoutY() - 50);
          }
        }
      }
    }
    for (Node node : lRemove) {
      ap2.getChildren().remove(node);
    }
    if (addRule.getLayoutY() > 339) {
      addRule.setLayoutY(addRule.getLayoutY() - 50);
      if (line.getLayoutY() > defaultLineYlayout) {
        line.setLayoutY(line.getLayoutY() - 50);
        continueButton.setLayoutY(continueButton.getLayoutY() - 50);
      }
    }
  }

  public void loadIsos(Scene scene, Configuration config) {
    CheckBox radProf1 = (CheckBox) scene.lookup("#radProf1");
    CheckBox radProf2 = (CheckBox) scene.lookup("#radProf2");
    CheckBox radProf3 = (CheckBox) scene.lookup("#radProf3");
    CheckBox radProf4 = (CheckBox) scene.lookup("#radProf4");
    CheckBox radProf5 = (CheckBox) scene.lookup("#radProf5");
    radProf1.setSelected(config.getIsos().contains("Baseline"));
    radProf2.setSelected(config.getIsos().contains("Tiff/EP"));
    radProf3.setSelected(config.getIsos().contains("Tiff/IT"));
    radProf4.setSelected(config.getIsos().contains("Tiff/IT-1"));
    radProf5.setSelected(config.getIsos().contains("Tiff/IT-2"));
  }

  public void saveIsos(Scene scene, Configuration config) {
    CheckBox radProf1 = (CheckBox)scene.lookup("#radProf1");
    CheckBox radProf2 = (CheckBox)scene.lookup("#radProf2");
    CheckBox radProf3 = (CheckBox)scene.lookup("#radProf3");
    CheckBox radProf4 = (CheckBox)scene.lookup("#radProf4");
    CheckBox radProf5 = (CheckBox)scene.lookup("#radProf5");
    config.getIsos().clear();
    if (radProf1.isSelected()) config.addISO("Baseline");
    if (radProf2.isSelected()) config.addISO("Tiff/EP");
    if (radProf3.isSelected()) config.addISO("Tiff/IT");
    if (radProf4.isSelected()) config.addISO("Tiff/IT-1");
    if (radProf5.isSelected()) config.addISO("Tiff/IT-2");
  }

  public void saveRules(Scene scene, Configuration config) {
    Rules rules = config.getRules();
    rules.Read(scene);
  }

  public void loadRules(Scene scene, Configuration config) {
    Rules rules = config.getRules();
    for (Rule r : rules.getRules()) {
      String value = r.getValue();
      String tag = r.getTag();
      String operator = r.getOperator();
      addRule(scene, tag, operator, value);
    }
  }

  public void loadFormats(Scene scene, Configuration config) {
    CheckBox chkHtml = (CheckBox) scene.lookup("#chkHtml");
    CheckBox chkXml = (CheckBox) scene.lookup("#chkXml");
    CheckBox chkJson = (CheckBox) scene.lookup("#chkJson");
    CheckBox chkPdf = (CheckBox) scene.lookup("#chkPdf");
    chkHtml.setSelected(config.getFormats().contains("HTML"));
    chkXml.setSelected(config.getFormats().contains("XML"));
    chkJson.setSelected(config.getFormats().contains("JSON"));
    chkPdf.setSelected(config.getFormats().contains("PDF"));
  }

  public void loadOutput(Scene scene, Configuration config) {
    CheckBox chkOutput = (CheckBox) scene.lookup("#chkDefaultOutput");
    TextField txtOutput = (TextField) scene.lookup("#txtOutput");
    if (config.getOutput() != null) {
      chkOutput.setSelected(false);
      txtOutput.setText(config.getOutput());
    } else {
      chkOutput.setSelected(true);
    }
  }

  public void saveFormats(Scene scene, Configuration config) {
    CheckBox chkHtml = (CheckBox)scene.lookup("#chkHtml");
    CheckBox chkXml = (CheckBox)scene.lookup("#chkXml");
    CheckBox chkJson = (CheckBox)scene.lookup("#chkJson");
    CheckBox chkPdf = (CheckBox)scene.lookup("#chkPdf");
    config.getFormats().clear();
    if (chkHtml.isSelected()) config.addFormat("HTML");
    if (chkXml.isSelected()) config.addFormat("XML");
    if (chkJson.isSelected()) config.addFormat("JSON");
    if (chkPdf.isSelected()) config.addFormat("PDF");
  }

  public void saveOutput(Scene scene, Configuration config) {
    CheckBox chkDefaultOutput = (CheckBox) scene.lookup("#chkDefaultOutput");
    TextField txtOutput = (TextField) scene.lookup("#txtOutput");
    if (!chkDefaultOutput.isSelected()) {
      config.setOutput(txtOutput.getText());
    }
  }

  public void getAutofixes(Scene scene) throws Exception {
    ArrayList<String> classes = TiffConformanceChecker.getAutofixes();

    int ypos = 320;
    int xpos = 252;
    int dify = 50;
    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    boolean first = true;
    for (String className : classes) {
      autofix fix = (autofix) Class.forName("dpfmanager.shell.modules.autofixes." + className).newInstance();

      CheckBox check = new CheckBox();
      check.setText(fix.getDescription());
      check.setId(className);
      check.setLayoutY(ypos);
      check.setLayoutX(xpos);
      check.getStyleClass().add("checkreport");
      check.setTextFill(Paint.valueOf("white"));
      ap2.getChildren().add(check);

      ypos += dify;
      if (!first) {
        Button but = (Button) scene.lookup("#addFix");
        but.setLayoutY(but.getLayoutY() + dify);
        Line lin = (Line) scene.lookup("#line");
        lin.setLayoutY(lin.getLayoutY() + dify);
      } else {
        first = false;
      }
    }
  }

  public void loadFixes(Scene scene, Configuration config) {
    Fixes fixes = config.getFixes();
    for (Fix fix : fixes.getFixes()) {
      String value = fix.getValue();
      String tag = fix.getTag();
      String operator = fix.getOperator();
      if (operator != null) {
        addFix(scene, operator, tag, value);
      } else {
        addAutoFix(scene, tag, value);
      }
    }
  }

  public void saveFixes(Scene scene, Configuration config) {
    Fixes fixes = config.getFixes();
    /*if (chkAutoFixLE != null && chkAutoFixLE.isSelected())
      fixes.addFixFromTxt("ByteOrder,LittleEndian");
    if (chkAutoFixBE != null && chkAutoFixBE.isSelected())
      fixes.addFixFromTxt("ByteOrder,BigEndian");
    if (chkAutoFixPersonal != null && chkAutoFixPersonal.isSelected())
      fixes.addFixFromTxt("PrivateData,Clear");*/

    fixes.ReadFixes(scene);
    fixes.ReadAutofixes(scene);
  }

  public void addFix(Scene scene) {
    addFix(scene, null, null, null);
  }

  public void addAutoFix(Scene scene, String tag, String value) {
    CheckBox autoFix = (CheckBox) scene.lookup("#" + tag);
    autoFix.setSelected(value.equals("Yes"));
  }

  public void addFix(Scene scene, String tag, String operator, String value) {
    Button addFix = (Button) scene.lookup("#addFix");
    Button continueButton = (Button) scene.lookup("#continue2");
    Line line = (Line) scene.lookup("#line");

    int dif = 50;
    double xRule = addFix.getLayoutX() - 100;
    double yRule = addFix.getLayoutY();

    // Add combobox
    ComboBox comboBox = new ComboBox();
    comboBox.setId("comboBoxAction");
    for (String fix : getFixes()) {
      comboBox.getItems().add(fix);
    }
    if (tag != null) {
      comboBox.setValue(tag);
    }
    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String item) {
        addFixValue(item, scene);
      }
    });

    // Remove button
    String styleButton = "-fx-background-color: transparent;\n" +
        "\t-fx-border-width     : 0px   ;\n" +
        "\t-fx-border-radius: 0 0 0 0;\n" +
        "\t-fx-background-radius: 0 0 0; -fx-text-fill: WHITE; -fx-font-weight:bold;";
    String styleButtonPressed = "-fx-border-width: 0px; -fx-background-color: rgba(255, 255, 255, 0.2);";
    Button remove = new Button();
    remove.setText("X");
    remove.styleProperty().bind(
        Bindings
            .when(remove.pressedProperty())
            .then(new SimpleStringProperty(styleButtonPressed))
            .otherwise(new SimpleStringProperty(styleButton)
            )
    );
    remove.setId("removeButton");
    remove.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        deleteFix(remove.getParent().getId(), scene);
      }
    });

    HBox hBox = new HBox(comboBox, remove);
    hBox.setId("ID" + uniqueId++);
    hBox.setSpacing(5);
    hBox.setLayoutX(xRule);
    hBox.setLayoutY(yRule);

    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    ap2.getChildren().add(hBox);

    addFix.setLayoutY(addFix.getLayoutY() + dif);

    // Move bottom elements if necessary
    if (addFix.getLayoutY() + addFix.getHeight() > line.getLayoutY()) {
      line.setLayoutY(line.getLayoutY() + dif);
      continueButton.setLayoutY(continueButton.getLayoutY() + dif);
    }

    if (operator != null) {
      addFixValue(tag, scene, operator, value);
    }
  }

  private void addFixValue(String item, Scene scene) {
    addFixValue(item, scene, null, null);
  }

  private void addFixValue(String item, Scene scene, String selectedOperator, String selectedValue) {
    Boolean delete_combobox;
    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    for (Node node : ap2.getChildren()) {
      delete_combobox = true;
      if (node instanceof HBox) {
        HBox hBox1 = (HBox) node;
        for (Node nodeIn : hBox1.getChildren()) {
          if (nodeIn instanceof ComboBox) {
            ComboBox comboBox = (ComboBox) nodeIn;
            if (comboBox.getValue() != null && comboBox.getValue().equals(item)) {
              ComboBox comboOp = new ComboBox();
              comboOp.setId("comboBoxField");
              for (String field : getFixFields()) {
                comboOp.getItems().add(field);
              }
              if (selectedOperator != null) {
                comboOp.setValue(selectedOperator);
              }
              Button bRemove = null;
              if (hBox1.getChildren().get(1) instanceof ComboBox) {
                ComboBox bla = (ComboBox) hBox1.getChildren().get(1);
                String val = (String) bla.getValue();
                if (val != null) {
                  delete_combobox = false;
                }
              }
              if (delete_combobox) {
                while (hBox1.getChildren().size() > 1) {
                  for (int i = 1; i < hBox1.getChildren().size(); i++) {
                    if (hBox1.getChildren().get(i) instanceof Button) {
                      bRemove = (Button) hBox1.getChildren().get(i);
                    }
                    hBox1.getChildren().remove(i);
                  }
                }

                hBox1.getChildren().add(comboOp);

                if (item.equals("Add Tag")) {
                  TextField value = new TextField();
                  value.setId("textField");
                  value.getStyleClass().add("txtFix");
                  if (selectedValue != null) {
                    value.setText(selectedValue);
                  }
                  hBox1.getChildren().add(value);
                }

                hBox1.getChildren().add(bRemove);
                break;
              }
            }
          }
        }
      }
    }
  }

  void deleteFix(String id, Scene scene) {
    Button addFix = (Button) scene.lookup("#addFix");
    Button continueButton = (Button) scene.lookup("#continue2");
    Line line = (Line) scene.lookup("#line");
    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    ArrayList<Node> lRemove = new ArrayList<Node>();
    for (Node node : ap2.getChildren()) {
      if (node instanceof HBox) {
        HBox hBox1 = (HBox) node;
        if (hBox1.getId().equals(id)) {
          lRemove.add(node);
        } else {
          if (Integer.parseInt(hBox1.getId().substring(2)) > Integer.parseInt(id.substring(2))) {
            hBox1.setLayoutY(hBox1.getLayoutY() - 50);
          }
        }
      }
    }
    for (Node node : lRemove) {
      ap2.getChildren().remove(node);
    }
    if (addFix.getLayoutY() > 339) {
      addFix.setLayoutY(addFix.getLayoutY() - 50);
      if (line.getLayoutY() > defaultLineYlayout) {
        line.setLayoutY(line.getLayoutY() - 50);
        continueButton.setLayoutY(continueButton.getLayoutY() - 50);
      }
    }
  }

  void setLabel(String labelName, Scene scene, String txt) {
    Label lab = (Label) scene.lookup("#" + labelName);
    lab.setText(txt);
  }

  public void setReport(Scene scene, Configuration config) {
    setLabel("labIsos", scene, config.getTxtIsos());
    setLabel("labReports", scene, config.getTxtFormats());
    setLabel("labRules", scene, config.getTxtRules());
    setLabel("labFixes", scene, config.getTxtFixes());
  }
}


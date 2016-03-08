package dpfmanager.shell.interfaces.gui.ui.config.sub;

import dpfmanager.shell.interfaces.gui.ui.config.ConfigController;
import dpfmanager.shell.interfaces.gui.ui.config.ConfigModel;
import dpfmanager.shell.interfaces.gui.ui.config.ConfigView;
import dpfmanager.conformancechecker.tiff.Configuration;
import dpfmanager.conformancechecker.tiff.MetadataFixer.Fix;
import dpfmanager.conformancechecker.tiff.MetadataFixer.Fixes;
import dpfmanager.conformancechecker.tiff.MetadataFixer.autofixes.autofix;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
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

import org.jrebirth.af.core.ui.fxml.AbstractFXMLController;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 10/02/2016.
 */
public class SubConfig4Controller extends AbstractFXMLController<ConfigModel, ConfigView> {

  @FXML
  private VBox autoFixesBox;

  @FXML
  private VBox fixesBox;

  private Map<String,CheckBox> autoFixesMap;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(final URL url, final ResourceBundle resource) {
    ConfigController.setSubController4(this);
    initAutoFixes();
  }

  public void clear(){
    fixesBox.getChildren().clear();
    for (Node node : autoFixesBox.getChildren()){
      if (node instanceof CheckBox){
        CheckBox check = (CheckBox) node;
        check.setSelected(false);
      }
    }
  }

  public void saveFixes(Configuration config) {
    Fixes fixes = config.getFixes();
    fixes.ReadFixes(fixesBox);
    fixes.ReadAutofixes(autoFixesBox);
  }

  public void loadFixes(Configuration config) {
    fixesBox.getChildren().clear();
    Fixes fixes = config.getFixes();
    for (Fix fix : fixes.getFixes()) {
      String value = fix.getValue();
      String tag = fix.getTag();
      String action = fix.getOperator();
      if (action != null) {
        addFix(action, tag, value);
      } else {
        autoFixesMap.get(tag).setSelected(value.equals("Yes"));
      }
    }
  }

  public void initAutoFixes() {
    ArrayList<String> classes = TiffConformanceChecker.getAutofixes();
    autoFixesMap = new HashMap<>();
    for (String className : classes) {
      autofix fix = null;
      try {
        fix = (autofix) Class.forName("dpfmanager.shell.services.conformancechecker.MetadataFixer.autofixes." + className).newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (fix != null) {
        CheckBox check = new CheckBox();
        check.setText(fix.getDescription());
        check.setId(className);
        check.getStyleClass().add("checkreport");

        autoFixesMap.put(className,check);
        autoFixesBox.getChildren().add(check);
      }
    }
  }

  @FXML
  protected void addFix(ActionEvent event) throws Exception {
    addFix(null, null, null);
  }

  private void addFix(String action, String tag, String value) {
    ConfigModel model = ConfigController.getMyModel();

    // HBox container
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setId("ID" + model.getNextId());
    hbox.setSpacing(5);

    // Add combobox
    ComboBox comboBox = new ComboBox();
    comboBox.setId("comboBoxAction");
    comboBox.getStyleClass().add("combo-box-white");
    for (String fix : model.getFixes()) {
      comboBox.getItems().add(fix);
    }
    if (action != null){
      comboBox.setValue(action);
    }

    // Add remove button
    Button remove = new Button();
    remove.setText("X");
    remove.getStyleClass().add("but-remove");
    remove.setId("removeButton");
    remove.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        fixesBox.getChildren().remove(hbox);
      }
    });

    // Combo Box listener
    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String item) {
        addTag(item, hbox, remove, null, null);
      }
    });

    //Add to view
    hbox.getChildren().addAll(comboBox,remove);
    fixesBox.getChildren().add(hbox);
    fixesBox.setMargin(hbox, new Insets(0,0,15,0));

    if (tag != null){
      addTag(action, hbox, remove, tag, value);
    }
  }

  private void addTag(String item, HBox hbox, Button remove, String tagLoad, String valueLoad){
    ConfigModel model = ConfigController.getMyModel();
    ArrayList<String> tags = model.getFixFields();

    //Remove button X
    hbox.getChildren().remove(remove);

    if (hbox.getChildren().size() > 1){
      Node aux = hbox.getChildren().get(0);
      hbox.getChildren().clear();
      hbox.getChildren().add(aux);
    }

    // Add tags combo box
    if (tags != null) {
      ComboBox comboOp = new ComboBox();
      comboOp.setId("comboBoxOp");
      comboOp.getStyleClass().add("combo-box-white");
      for (String tag : tags) {
        comboOp.getItems().add(tag);
      }
      if (tagLoad != null){
        comboOp.setValue(tagLoad);
      }
      hbox.getChildren().add(comboOp);
    }

    // Value combo box or text field
    if (item.equals("Add Tag")) {
      TextField value = new TextField();
      value.setId("textField");
      value.getStyleClass().add("txtFix");
      if (valueLoad != null){
        value.setText(valueLoad);
      }
      hbox.getChildren().add(value);
    }

    // Add remove button
    hbox.getChildren().add(remove);
  }

}

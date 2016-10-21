/**
 * <h1>Wizard1Fragment.java</h1> <p> This program is free software: you can redistribute it and/or
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
import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import org.apache.commons.io.FileUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_1,
    viewLocation = "/fxml/config/subconfig1.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class Wizard1Fragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private VBox vboxRadios;

  private Integer count;

  public Wizard1Fragment() {
    count = 0;
  }

  public void init() {
    List<String> errors = new ArrayList<>();

    // Internal
    List<String> paths = ImplementationCheckerLoader.getPathsList();
    for (String path : paths) {
      if (ImplementationCheckerLoader.isValid(path)) {
        addinternalCheckBox(path, false);
      } else {
        errors.add(path.substring(path.indexOf("/") + 1, path.length() - 4));
      }
    }

    // User configs
    File folder = new File(DPFManagerProperties.getIsosDir());
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isFile()) {
        if (fileEntry.getName().toLowerCase().endsWith(".xml")) {
          if (ImplementationCheckerLoader.isValid(fileEntry.getName())) {
            addConfigCheckBox(fileEntry.getName(), false);
          } else {
            errors.add(fileEntry.getName());
          }
        }
      }
    }

    // Inform errors
    if (errors.size() == 1) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, DPFManagerProperties.getBundle().getString("w1errorReadingIso").replace("%1", errors.get(0))));
    } else if (errors.size() > 1) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, DPFManagerProperties.getBundle().getString("w1errorReadingMultipleIso"), String.join(", ", errors)));
    }
  }

  private void addinternalCheckBox(String iso, boolean selected) {
    addCheckBox(ImplementationCheckerLoader.getFileName(iso), ImplementationCheckerLoader.getIsoName(iso), false, false);
  }

  private void addExternalCheckBox(String path, boolean selected) {
    addCheckBox(getNextId("external"), path, selected, true);
  }

  private void addConfigCheckBox(String name, boolean selected) {
    addCheckBox("config" + name, ImplementationCheckerLoader.getIsoName(name), selected, true);
  }

  private void addCheckBox(String id, String name, boolean selected, boolean delete) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);

    CheckBox chk = new CheckBox(name);
    chk.setId(id);
    chk.getStyleClass().add("checkreport");
    chk.setSelected(selected);
    hbox.getChildren().add(chk);

    if (delete) {
      Button icon = new Button();
      icon.getStyleClass().addAll("delete-img", "action-img-16");
      icon.setCursor(Cursor.HAND);
      icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          if (chk.getId().startsWith("external")) {
            // Only from gui
            vboxRadios.getChildren().remove(hbox);
          } else if (chk.getId().startsWith("config")) {
            // From system
            String name = chk.getId().replace("config", "");
            File file = new File(DPFManagerProperties.getIsosDir() + "/" + name);
            if (file.exists() && file.isFile()) {
              file.delete();
            }
            vboxRadios.getChildren().remove(hbox);
          }
        }
      });
      hbox.getChildren().add(icon);
      HBox.setMargin(icon, new Insets(0, 0, 0, 10));
    }

    vboxRadios.getChildren().add(hbox);
  }

  public void clear() {
    List<HBox> toDelete = new ArrayList<>();
    for (Node node : vboxRadios.getChildren()) {
      HBox hbox = (HBox) node;
      boolean delete = false;
      for (Node hNode : hbox.getChildren()) {
        if (hNode instanceof CheckBox) {
          CheckBox chk = (CheckBox) hNode;
          chk.setSelected(false);
          delete = chk.getId().startsWith("external");
        }
      }
      if (delete){
        toDelete.add(hbox);
      }
    }
    vboxRadios.getChildren().removeAll(toDelete);
  }

  public void saveIsos(Configuration config) {
    config.getIsos().clear();
    for (CheckBox chk : getCheckBoxs()) {
      if (chk.isSelected()) {
        if (chk.getId().startsWith("external")) {
          // Path
          config.addISO(chk.getText());
        } else if (chk.getId().startsWith("config")) {
          // Config
          config.addISO(chk.getId().replace("config", ""));
        } else {
          // Internal
          config.addISO(chk.getId());
        }
      }
    }
  }

  public void loadIsos(Configuration config) {
    for (String iso : config.getIsos()) {
      CheckBox chk = getCheckById(iso);
      if (chk != null) {
        // Internal
        chk.setSelected(true);
      } else {
        // External
        File file = new File(iso);
        File fileConf = new File(DPFManagerProperties.getIsosDir() + "/" + iso);
        if (file.exists() && file.isFile()) {
          addExternalCheckBox(iso, true);
        } else if (fileConf.exists() && fileConf.isFile()) {
          CheckBox chk2 = getCheckById("config" + fileConf.getName());
          if (chk2 != null) {
            chk2.setSelected(true);
          }
        }
      }
    }
  }

  private CheckBox getCheckById(String id) {
    for (CheckBox chk : getCheckBoxs()) {
      if (chk.getId().equals(id)) {
        return chk;
      }
    }
    return null;
  }

  private List<CheckBox> getCheckBoxs() {
    List<CheckBox> boxs = new ArrayList<>();
    for (Node node : vboxRadios.getChildren()) {
      HBox hbox = (HBox) node;
      if (hbox.getChildren().get(0) instanceof CheckBox){
        boxs.add((CheckBox) hbox.getChildren().get(0));
      }
    }
    return boxs;
  }

  @FXML
  protected void importIso() {
    File file;
    String value = GuiWorkbench.getTestParams("importIso");
    if (value != null) {
      //Test mode
      file = new File(value);
    } else {
      //Ask for file
      String configDir = DPFManagerProperties.getDefaultDirConfig();
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(bundle.getString("w1Import"));
      fileChooser.setInitialDirectory(new File(configDir));
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(bundle.getString("w1XmlFiles"), "*.xml");
      fileChooser.getExtensionFilters().add(extFilter);
      file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
    }

    addIsoFile(file, true);
  }

  public void addIsoFile(File file, boolean ask) {
    if (file == null) {
      return;
    }

    // Check valid config
    ImplementationCheckerObjectType rules = ImplementationCheckerLoader.getRules(file.getPath());
    if (rules == null) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, DPFManagerProperties.getBundle().getString("w1errorReadingIso").replace("%1", file.getPath())));
      return;
    }

    if (ask) {
      DPFManagerProperties.setDefaultDirConfig(file.getParent());
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(bundle.getString("w1copyTitle"));
      alert.setHeaderText(bundle.getString("w1copyHeader"));
      alert.setContentText(bundle.getString("w1copyContent"));
      ButtonType buttonTypeYes = new ButtonType(bundle.getString("yes"));
      ButtonType buttonTypeNo = new ButtonType(bundle.getString("no"));
      alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == buttonTypeYes) {
        // Copy the file
        boolean needAdd = true, error = false;
        File configFile = new File(DPFManagerProperties.getIsosDir() + "/" + file.getName());
        if (configFile.exists()) {
          configFile.delete();
          needAdd = false;
        }
        try {
          FileUtils.copyFile(file, configFile);
        } catch (IOException e) {
          error = true;
        }
        if (error) {
          // Add source file
          addExternalCheckBox(file.getAbsolutePath(), true);
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.WARNING, bundle.getString("w1errorCopyConfig")));
        } else if (needAdd) {
          addConfigCheckBox(file.getName(), true);
        }
      } else {
        addExternalCheckBox(file.getAbsolutePath(), true);
      }
    } else {
      addExternalCheckBox(file.getAbsolutePath(), true);
    }
  }

  private String getNextId(String prefix) {
    return prefix + (++count);
  }

}

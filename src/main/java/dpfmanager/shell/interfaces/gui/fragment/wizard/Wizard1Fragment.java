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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.configuration.Configuration;
import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.component.config.ConfigController;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 08/03/2016.
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

  public Wizard1Fragment() {
  }

  public void init() {
    List<String> errors = new ArrayList<>();

    // Internal
    List<String> paths = ImplementationCheckerLoader.getPathsList();
    for (String path : paths) {
      if (ImplementationCheckerLoader.isValid(path)) {
        addInternalCheckBox(path);
      } else {
        errors.add(path.substring(path.indexOf("/") + 1, path.length() - 4));
      }
    }

    // Inform errors
    if (errors.size() == 1) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, DPFManagerProperties.getBundle().getString("w1errorReadingIso").replace("%1", errors.get(0))));
    } else if (errors.size() > 1) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, DPFManagerProperties.getBundle().getString("w1errorReadingMultipleIso"), String.join(", ", errors)));
    }
  }

  private void addInternalCheckBox(String iso) {
    addCheckBox(ImplementationCheckerLoader.getFileName(iso), ImplementationCheckerLoader.getIsoName(iso), bundle.getString("w6BuiltIn"), false, false);
  }

  private void addCheckBox(String id, String name, String path, boolean selected, boolean delete) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);

    CheckBox chk = new CheckBox(name);
    chk.setId(id);
    chk.getStyleClass().add("checkreport");
    chk.setSelected(selected);
    chk.setEllipsisString(" ... ");
    chk.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
    chk.setTooltip(new Tooltip(path));
    hbox.getChildren().add(chk);

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
      if (delete) {
        toDelete.add(hbox);
      }
    }
    vboxRadios.getChildren().removeAll(toDelete);
  }

  public void saveIsos(Configuration config) {
    config.getIsos().clear();
    for (CheckBox chk : getCheckBoxs()) {
      if (chk.isSelected()) {
        config.addISO(chk.getId());
      }
    }
  }

  public void loadIsos(Configuration config) {
    for (String iso : config.getIsos()) {
      CheckBox chk = getCheckById(iso);
      if (chk != null) {
        chk.setSelected(true);
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
      if (hbox.getChildren().get(0) instanceof CheckBox) {
        boxs.add((CheckBox) hbox.getChildren().get(0));
      }
    }
    return boxs;
  }

}

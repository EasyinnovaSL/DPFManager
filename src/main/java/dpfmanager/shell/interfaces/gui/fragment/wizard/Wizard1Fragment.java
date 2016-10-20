/**
 * <h1>Wizard1Fragment.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
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
import dpfmanager.shell.core.config.GuiConfig;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @FXML
  private VBox vboxRadios;

  public Wizard1Fragment() {
  }

  public void init() {
    List<String> paths = ImplementationCheckerLoader.getPathsList();
    for (String path : paths){
      addCheckBox(ImplementationCheckerLoader.getFileName(path), ImplementationCheckerLoader.getIsoName(path));
    }
  }

  private void addCheckBox(String id, String name){
    CheckBox chk = new CheckBox(name);
    chk.setId(id);
    chk.getStyleClass().add("checkreport");
    vboxRadios.getChildren().add(chk);
  }

  public void clear() {
    List<CheckBox> toDelete = new ArrayList<>();
    for (Node node : vboxRadios.getChildren()){
      CheckBox chk = (CheckBox) node;
      chk.setSelected(false);
      if (chk.getId().startsWith("external")){
        toDelete.add(chk);
      }
    }
    vboxRadios.getChildren().removeAll(toDelete);
  }

  public void saveIsos(Configuration config) {
    config.getIsos().clear();
    for (Node node : vboxRadios.getChildren()){
      CheckBox chk = (CheckBox) node;
      if (chk.isSelected()) {
        if (chk.getId().startsWith("external")){
          // Path
          config.addISO(chk.getText());
        } else {
          // Internal
          config.addISO(chk.getId());
        }
      }
    }
  }

  public void loadIsos(Configuration config) {
    int c = 0;
    for (String iso : config.getIsos()){
      CheckBox chk = getCheckById(iso);
      if (chk != null){
        // Internal
        chk.setSelected(true);
      } else {
        // Path
        addCheckBox("external"+c, iso);
        c++;
      }
    }
  }

  private CheckBox getCheckById(String id) {
    for (Node node : vboxRadios.getChildren()){
      CheckBox chk = (CheckBox) node;
      if (chk.getId().equals(id)){
        return chk;
      }
    }
    return null;
  }
}

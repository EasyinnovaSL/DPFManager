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
import dpfmanager.shell.core.config.GuiConfig;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

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
  private CheckBox radProf1, radProf2, radProf3, radProf4, radProf5;

  public Wizard1Fragment() {
  }

  public void clear() {
    radProf1.setSelected(false);
    radProf2.setSelected(false);
    radProf3.setSelected(false);
    radProf4.setSelected(false);
    radProf5.setSelected(false);
  }

  public void saveIsos(Configuration config) {
    config.getIsos().clear();
    if (radProf1.isSelected()) config.addISO("Baseline");
    if (radProf2.isSelected()) config.addISO("Tiff/EP");
    if (radProf3.isSelected()) config.addISO("Tiff/IT");
    if (radProf4.isSelected()) config.addISO("Tiff/IT-1");
    if (radProf5.isSelected()) config.addISO("Tiff/IT-2");
  }

  public void loadIsos(Configuration config) {
    radProf1.setSelected(config.getIsos().contains("Baseline"));
    radProf2.setSelected(config.getIsos().contains("Tiff/EP"));
    radProf3.setSelected(config.getIsos().contains("Tiff/IT"));
    radProf4.setSelected(config.getIsos().contains("Tiff/IT-1"));
    radProf5.setSelected(config.getIsos().contains("Tiff/IT-2"));
  }
}

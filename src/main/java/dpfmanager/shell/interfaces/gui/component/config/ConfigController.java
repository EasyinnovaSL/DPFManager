/**
 * <h1>ConfigController.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.config;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard1Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard2Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard3Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard4Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard5Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard6Fragment;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;

import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;

import java.io.File;
import java.util.List;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ConfigController extends DpfController<ConfigModel, ConfigView> {

  private ManagedFragmentHandler<Wizard1Fragment> fragment1;
  private ManagedFragmentHandler<Wizard2Fragment> fragment2;
  private ManagedFragmentHandler<Wizard3Fragment> fragment3;
  private ManagedFragmentHandler<Wizard4Fragment> fragment4;
  private ManagedFragmentHandler<Wizard5Fragment> fragment5;
  private ManagedFragmentHandler<Wizard6Fragment> fragment6;

  public void clearAllSteps() {
    fragment1.getController().clear();
    fragment2.getController().clear();
    fragment3.getController().clear();
    fragment4.getController().clear();
    fragment5.getController().clear();
    getView().clear();
  }

  public void saveConfig() {
    // Save description
    String description = getView().getDescription();
    getModel().getConfiguration().setDescription(description);
    // Save to file
    File file = null;
    String path = getModel().getPath();
    if (path != null) {
      // When editing, save directly in same file
      file = new File(path);
    } else {
      String value = GuiWorkbench.getTestParams("saveConfig");
      if (value != null) {
        // Save in specified output (TEST)
        file = new File(value);
      } else {
        // Get save name
        String name = getView().getSaveFilename();
        if (name.isEmpty()) {
          // Empty input
          getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("enterFilename")));
        } else {
          name = checkInput(name);
          if (name == null) {
            // Alert incorrect name format
            getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("noEnterPath")));
          } else {
            file = new File(DPFManagerProperties.getConfigDir() + "/" + name);
            if (file.exists()) {
              // Alert name exists
              getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("enterDuplicate")));
              file = null;
            }
          }
        }
      }
    }

    if (file != null) {
      try {
        getModel().saveConfig(file.getAbsolutePath());
        getContext().send(GuiConfig.PERSPECTIVE_DESSIGN, new UiMessage(UiMessage.Type.SHOW));
      } catch (Exception ex) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(getBundle().getString("exception"), ex));
      }
    }
  }

  public String checkInput(String name) {
    if (name.contains("/") || name.contains("\\") ||
        name.lastIndexOf(".") != name.indexOf(".") ||    // More than 2 '.'
        (name.contains(".") && !name.endsWith(".dpf"))) {  // Has some '.' but no .dpf
      return null;
    } else if (!name.endsWith(".dpf")) {
      return name + ".dpf";
    } else {
      return name;
    }
  }

  public void saveSettings(int step) {
    switch (step) {
      case 1:
        fragment1.getController().saveIsos(getModel().getConfiguration());
        break;
      case 2:
        fragment2.getController().saveRules(getModel().getConfiguration());
        fragment2.getController().saveIsos(getModel().getConfiguration());
        break;
      case 3:
        fragment3.getController().saveFormats(getModel().getConfiguration());
        fragment3.getController().saveOutput(getModel().getConfiguration());
        break;
      case 4:
        fragment4.getController().saveFixes(getModel().getConfiguration());
        break;
    }
  }

  public void loadSettings(int step) {
    switch (step) {
      case 1:
        fragment1.getController().loadIsos(getModel().getConfiguration());
        break;
      case 2:
        fragment2.getController().loadIsos(getModel().getConfiguration());
        fragment2.getController().loadRules(getModel().getConfiguration());
        break;
      case 3:
        fragment3.getController().loadFormats(getModel().getConfiguration());
        fragment3.getController().loadOutput(getModel().getConfiguration());
        break;
      case 4:
        fragment4.getController().loadFixes(getModel().getConfiguration());
        break;
      case 5:
        fragment5.getController().loadSummary(getModel().getConfiguration());
        break;
    }
  }

  public void editIso(String iso){
    fragment6.getController().edit(iso, getModel().getConfiguration().getModifiedIso(iso));
    getView().gotoEdit();
  }

  public void editIsoSuccess(String isoId, List<String> rules){
    getModel().getConfiguration().addModifiedIso(isoId, rules);
    fragment2.getController().check(isoId);
    getView().gotoConfig(2);
  }

  public void editIsoCancelled(){
    getView().gotoConfig(2);
  }

  /**
   * Setters
   */

  public void setFragment1(ManagedFragmentHandler<Wizard1Fragment> fragment1) {
    this.fragment1 = fragment1;
  }

  public void setFragment2(ManagedFragmentHandler<Wizard2Fragment> fragment2) {
    this.fragment2 = fragment2;
  }

  public void setFragment3(ManagedFragmentHandler<Wizard3Fragment> fragment3) {
    this.fragment3 = fragment3;
  }

  public void setFragment4(ManagedFragmentHandler<Wizard4Fragment> fragment4) {
    this.fragment4 = fragment4;
  }

  public void setFragment5(ManagedFragmentHandler<Wizard5Fragment> fragment5) {
    this.fragment5 = fragment5;
  }

  public void setFragment6(ManagedFragmentHandler<Wizard6Fragment> fragment6) {
    this.fragment6 = fragment6;
  }

}

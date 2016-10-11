/**
 * <h1>GuiApp.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.application.app;

import dpfmanager.shell.application.launcher.ui.GuiLauncher;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.CustomErrorHandler;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.jacpfx.api.handler.ErrorDialogHandler;
import org.jacpfx.rcp.workbench.FXWorkbench;

import java.util.Locale;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
public class GuiApp extends GuiLauncher {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  protected void postInit(Stage stage) {
    // Logo img
    Image img = new Image("/gui-logo-white.png");
    if (img != null) {
      stage.getIcons().add(img);
    }
    stage.setTitle("DPF Manager");
  }

  @Override
  protected Class<? extends FXWorkbench> getWorkbenchClass() {
    return GuiWorkbench.class;
  }

  @Override
  protected String[] getBasePackages() {
    return new String[]{
        "dpfmanager.shell.modules",                         // Dpf Modules
        "dpfmanager.shell.interfaces.gui.component",        // UI Components
        "dpfmanager.shell.interfaces.gui.perspective",      // Prespectives
        "dpfmanager.shell.interfaces.gui.fragment"          // Fragments
    };
  }

  @Override
  public String getXmlConfig() {
    // Update locale
    Locale.setDefault(new Locale(DPFManagerProperties.getLanguage()));
    return "DpfSpringGui.xml";
  }

  @Override
  public ErrorDialogHandler<Node> getErrorHandler(){
    return new CustomErrorHandler();
  }
}

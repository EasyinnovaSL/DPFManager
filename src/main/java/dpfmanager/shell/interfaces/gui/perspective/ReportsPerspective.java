/**
 * <h1>ReportsPerspective.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.perspective;

import dpfmanager.shell.core.adapter.DpfAbstractPerspective;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.NavMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@Perspective(id = GuiConfig.PERSPECTIVE_REPORTS,
    name = GuiConfig.PERSPECTIVE_REPORTS,
    active = false,
    components = {
        GuiConfig.COMPONENT_TOP,
        GuiConfig.COMPONENT_NAV,
        GuiConfig.COMPONENT_REPORTS,
        GuiConfig.COMPONENT_PANE,
        GuiConfig.COMPONENT_BAR,
        BasicConfig.MODULE_MESSAGE,
        BasicConfig.MODULE_THREADING,
        BasicConfig.MODULE_REPORT
    }
)
public class ReportsPerspective extends DpfAbstractPerspective {

  @Resource
  public Context context;

  @Override
  public void handleMessage(DpfMessage dpfMessage, PerspectiveLayout layout) {
  }

  @Override
  public void onShowCustom() {
    context.send(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_NAV, new NavMessage(NavMessage.Selected.TABLE));
    context.send(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
  }

  @PostConstruct
  public void onStartPerspective(PerspectiveLayout perspectiveLayout, FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Top Buttons component
    StackPane topPane = new StackPane();

    // Center Component
    StackPane centerPane = new StackPane();
    centerPane.setAlignment(Pos.TOP_CENTER);

    // Bottom Component
    bottomPane = new StackPane();
    bottomPane.setAlignment(Pos.BOTTOM_CENTER);

    // Bottom Bar Component
    bottomBar = new StackPane();
    bottomBar.setAlignment(Pos.BOTTOM_CENTER);

    // Navigation Bar
    StackPane navPane = new StackPane();
    centerPane.setAlignment(Pos.TOP_CENTER);

    // Attach to PERSPECTIVE
    mainSplit = constructSplitPane(constructScrollPane(centerPane), bottomPane);
    mainPane = constructMainPaneWithBread(mainSplit, navPane, bottomBar);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_TOP, topPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_NAV, navPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_REPORTS, centerPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_PANE, bottomPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_BAR, bottomBar);

    // Define main pane
    borderPane = constructBorderPane(perspectiveLayout, topPane, mainPane);
  }

  @Override
  public Context getContext() {
    return context;
  }
}

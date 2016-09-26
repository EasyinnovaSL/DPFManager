/**
 * <h1>PeriodicalPerspective.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.perspective;

import dpfmanager.shell.core.adapter.DpfAbstractPerspective;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Perspective(id = GuiConfig.PERSPECTIVE_PERIODICAL,
    name = GuiConfig.PERSPECTIVE_PERIODICAL,
    active = false,
    components = {
        GuiConfig.COMPONENT_TOP,
        GuiConfig.COMPONENT_PERIODICAL,
        GuiConfig.COMPONENT_PANE,
        GuiConfig.COMPONENT_BAR,
        BasicConfig.MODULE_MESSAGE,
        BasicConfig.MODULE_PERIODICAL,
        BasicConfig.MODULE_DATABASE
    }
)
public class PeriodicalPerspective extends DpfAbstractPerspective {

  @Resource
  public Context context;

  private StackPane topPane;

  @Override
  public void handleMessage(DpfMessage dpfMessage, PerspectiveLayout layout) {

  }

  @PostConstruct
  public void onStartPerspective(PerspectiveLayout perspectiveLayout, FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Top Buttons component
    topPane = new StackPane();

    // Center Component
    StackPane centerPane = new StackPane();
    centerPane.setAlignment(Pos.TOP_CENTER);

    // Bottom Pane Component
    bottomPane = new StackPane();
    bottomPane.setAlignment(Pos.BOTTOM_CENTER);

    // Bottom Bar Component
    bottomBar = new StackPane();
    bottomBar.setAlignment(Pos.BOTTOM_CENTER);

    // Attach to PERSPECTIVE
    mainSplit = constructSplitPane(constructScrollPane(centerPane), bottomPane);
    mainPane = constructMainPane(mainSplit, bottomBar);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_TOP, topPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_PERIODICAL, centerPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_PANE, bottomPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_BAR, bottomBar);

    // Define main pane
    borderPane = constructBorderPane(perspectiveLayout, topPane, mainPane);

    // Escape character to quit
    mainPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent t) {
        if(t.getCode()== KeyCode.ESCAPE) {
          ((Stage) topPane.getScene().getWindow()).close();//use any one object
        } else if(t.getCode()== KeyCode.F1) {
          context.send(GuiConfig.PERSPECTIVE_ABOUT, new UiMessage(UiMessage.Type.SHOW));
        }
      }
    });
  }

  @Override
  public Context getContext() {
    return context;
  }

  @Override
  public void onReloadCustom() {
  }

  @Override
  public void onShowCustom() {
  }
}

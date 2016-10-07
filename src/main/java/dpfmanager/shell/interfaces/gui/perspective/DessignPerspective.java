/**
 * <h1>DessignPerspective.java</h1> <p> This program is free software: you can redistribute it
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
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Perspective(id = GuiConfig.PERSPECTIVE_DESSIGN,
    name = GuiConfig.PERSPECTIVE_DESSIGN,
    active = false,
    components = {
        GuiConfig.COMPONENT_TOP,
        GuiConfig.COMPONENT_DESIGN,
        GuiConfig.COMPONENT_PANE,
        GuiConfig.COMPONENT_BAR,
        BasicConfig.MODULE_MESSAGE,
        BasicConfig.MODULE_CONFORMANCE,
        BasicConfig.MODULE_REPORT,
        BasicConfig.MODULE_THREADING,
        BasicConfig.MODULE_DATABASE,
        BasicConfig.MODULE_TIMER,
        BasicConfig.MODULE_INTEROPERABILITY
    }
)
public class DessignPerspective extends DpfAbstractPerspective {

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
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_DESIGN, centerPane);
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
    getContext().send(GuiConfig.COMPONENT_DESIGN, new UiMessage(UiMessage.Type.RELOAD));
  }

  @Override
  public void onShowCustom() {
    getContext().send(GuiConfig.COMPONENT_DESIGN, new UiMessage(UiMessage.Type.RELOAD));
  }
}

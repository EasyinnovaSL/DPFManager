/**
 * <h1>GuiWorkbench.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.workbench;

import dpfmanager.shell.application.launcher.ui.GuiLauncher;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.messages.messages.CloseMessage;
import javafx.application.Application.Parameters;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.workbench.Workbench;
import org.jacpfx.api.componentLayout.WorkbenchLayout;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.workbench.FXWorkbench;

import java.util.HashMap;
import java.util.Map;

@Workbench(id = BasicConfig.WORKBENCH, name = BasicConfig.WORKBENCH,
    perspectives = {
        GuiConfig.PERSPECTIVE_FIRST,
        GuiConfig.PERSPECTIVE_DESSIGN,
        GuiConfig.PERSPECTIVE_REPORTS,
        GuiConfig.PERSPECTIVE_ABOUT,
        GuiConfig.PERSPECTIVE_CONFIG,
        GuiConfig.PERSPECTIVE_SHOW,
        GuiConfig.PERSPECTIVE_PERIODICAL,
        GuiConfig.PERSPECTIVE_INTEROPERABILITY
    }
)
public class GuiWorkbench implements FXWorkbench {

  private static Stage thestage;
  private static Parameters parameters;

  static Map<String, String> testValues;

  @Resource
  private Context context;

  @Override
  public void handleInitialLayout(Message<Event, Object> action, WorkbenchLayout<Node> layout, Stage stage) {
    parameters = GuiLauncher.getMyParameters();
    testValues = new HashMap<>();
    thestage = stage;
    thestage.setMinWidth(DpFManagerConstants.MIN_WIDTH);
    layout.setWorkbenchXYSize(DpFManagerConstants.WIDTH, DpFManagerConstants.HEIGHT);
    if (parameters.getRaw().contains("-test")) {
      layout.setStyle(StageStyle.UNDECORATED);
    } else {
      layout.setStyle(StageStyle.DECORATED);
    }

    EventHandler<WindowEvent> closeHandler = thestage.getOnCloseRequest();

    thestage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        if (!(event instanceof DpfCloseEvent)) {
          context.send(GuiConfig.PERSPECTIVE_DESSIGN + "." + BasicConfig.MODULE_THREADING, new CloseMessage(CloseMessage.Type.THREADING));
          event.consume();
        } else {
          closeHandler.handle(event);
        }
      }
    });

    // Escape character to quit
    thestage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent t) {
        if (t.getCode() == KeyCode.H && t.isMetaDown()) {
          if (thestage.isShowing()){
            thestage.setIconified(true);
          }
        } if (t.getCode() == KeyCode.ESCAPE) {
          context.send(GuiConfig.PERSPECTIVE_DESSIGN + "." + BasicConfig.MODULE_THREADING, new CloseMessage(CloseMessage.Type.THREADING));
        } else if (t.getCode() == KeyCode.F1) {
          context.send(GuiConfig.PERSPECTIVE_ABOUT, new UiMessage(UiMessage.Type.SHOW));
        }
      }
    });

  }

  @Override
  public void postHandle(final FXComponentLayout layout) {
  }

  public static Stage getMyStage() {
    return thestage;
  }

  public static void setTestParam(String key, String value) {
    testValues.put(key, value);
  }

  public static String getTestParams(String key) {
    return testValues.get(key);
  }

  public static boolean getFirstTime() {
    return !parameters.getRaw().contains("-test") && DPFManagerProperties.getFirstTime();
  }

  public static boolean isTestMode() {
    return parameters.getRaw().contains("-test");
  }

  public static Parameters getAppParams() {
    return parameters;
  }

}

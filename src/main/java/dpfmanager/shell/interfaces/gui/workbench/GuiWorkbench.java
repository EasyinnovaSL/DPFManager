/************************************************************************
 * Copyright (C) 2010 - 2012
 *
 * [JacpFXWorkbench.java] AHCP Project (http://jacp.googlecode.com) All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ************************************************************************/
package dpfmanager.shell.interfaces.gui.workbench;

import dpfmanager.shell.application.launcher.ui.GuiLauncher;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.modules.messages.messages.CloseMessage;
import javafx.application.Application.Parameters;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
        GuiConfig.PERSPECTIVE_SHOW
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
          context.send(GuiConfig.PERSPECTIVE_DESSIGN + "." + BasicConfig.MODULE_THREADING, new CloseMessage(true));
          event.consume();
        } else {
          closeHandler.handle(event);
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

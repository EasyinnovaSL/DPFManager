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
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import javafx.application.Application.Parameters;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacpfx.api.annotations.workbench.Workbench;
import org.jacpfx.api.componentLayout.WorkbenchLayout;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.workbench.FXWorkbench;

import java.util.HashMap;
import java.util.Map;

@Workbench(id = BasicConfig.WORKBENCH, name = BasicConfig.WORKBENCH,
    perspectives = {
        GuiConfig.PRESPECTIVE_FIRST,
        GuiConfig.PRESPECTIVE_DESSIGN,
        GuiConfig.PRESPECTIVE_REPORTS,
        GuiConfig.PRESPECTIVE_ABOUT,
        GuiConfig.PRESPECTIVE_CONFIG,
        GuiConfig.PRESPECTIVE_SHOW
    }
)
public class GuiWorkbench implements FXWorkbench {

  private static Stage thestage;

  private static Parameters parameters;

  static Map<String, String> testValues;

  @Override
  public void handleInitialLayout(Message<Event, Object> action, WorkbenchLayout<Node> layout, Stage stage) {
    parameters = GuiLauncher.getMyParameters();
    testValues = new HashMap<>();
    thestage = stage;
    layout.setWorkbenchXYSize(970, 950);
    if (parameters.getRaw().contains("-test")) {
      layout.setStyle(StageStyle.UNDECORATED);
    }
    else{
      layout.setStyle(StageStyle.DECORATED);
    }
  }

  @Override
  public void postHandle(final FXComponentLayout layout) {
  }

  public static Stage getMyStage(){
    return thestage;
  }

  public static void setTestParam(String key, String value) {
    testValues.put(key, value);
  }

  public static String getTestParams(String key) {
    return testValues.get(key);
  }

  public static boolean getFirstTime(){
    return !parameters.getRaw().contains("-test") && DPFManagerProperties.getFirstTime();
  }
}

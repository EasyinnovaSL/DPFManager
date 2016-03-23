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
package dpfmanager.shell.interfaces.console.workbench;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.ConsoleConfig;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;

import org.jacpfx.api.annotations.workbench.Workbench;
import org.jacpfx.api.componentLayout.WorkbenchLayout;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.workbench.FXWorkbench;

@Workbench(id = BasicConfig.WORKBENCH, name = BasicConfig.WORKBENCH,
    perspectives = {ConsoleConfig.PRESPECTIVE})
public class ConsoleWorkbench implements FXWorkbench {

  @Override
  public void handleInitialLayout(Message<Event, Object> action, WorkbenchLayout<Node> layout, Stage stage) {
    layout.setWorkbenchXYSize(100, 100);
  }

  @Override
  public void postHandle(final FXComponentLayout layout) {
  }
}

/**
 * <h1>ConformanceCheckerModule.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.conformancechecker;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_CONFORMANCE,
    name = BasicConfig.MODULE_CONFORMANCE,
    active = true)
public class ConformanceCheckerModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private ConformanceCheckerService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(ConformanceMessage.class)) {
      ConformanceMessage cm = dpfMessage.getTypedMessage(ConformanceMessage.class);
      if (cm.askOverwrite() && checkOverwriteOutput(cm.getPath())) {
        cm.setAskOverwrite(false);
        ResourceBundle bundle = DPFManagerProperties.getBundle();
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.CONFIRMATION, bundle.getString("confirmOverwrite"), bundle.getString("path") + ": " + getOutputFromPath(cm.getPath()), cm, GuiConfig.PERSPECTIVE_DESSIGN + "." + BasicConfig.MODULE_CONFORMANCE));
      } else if (cm.isGui()) {
        String path = cm.getPath();
        String input = cm.getInput();
        Configuration config = null;
        if (!path.isEmpty()) {
          config = service.readConfig(path);
          config.setQuick(cm.isQuick());
          if (config == null) {
            getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, DPFManagerProperties.getBundle().getString("errorReadingConfFile")));
            return;
          }
        }
        service.initProcessInputRun(input, config, cm.getRecursive());
      }
    } else if (dpfMessage.isTypeOf(ProcessInputMessage.class)) {
      service.tractProcessInputMessage(dpfMessage.getTypedMessage(ProcessInputMessage.class));
    }
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    GuiContext guiContext = new GuiContext(context);
    ConformanceChecker.setLogger(new DpfLogger(guiContext, false));
    service.setContext(guiContext);
  }

  private boolean checkOverwriteOutput(String path) {
    String output = getOutputFromPath(path);
    if (output != null && !output.isEmpty()) {
      File tmp = new File(output);
      return tmp.exists() && tmp.isDirectory() && tmp.listFiles().length > 0;
    } else {
      return false;
    }
  }

  private String getOutputFromPath(String path) {
    if (path != null && !path.isEmpty()) {
      Configuration config = service.readConfig(path);
      if (config != null) {
        return config.getOutput();
      }
    }
    return null;
  }

  @Override
  public Context getContext() {
    return context;
  }

}

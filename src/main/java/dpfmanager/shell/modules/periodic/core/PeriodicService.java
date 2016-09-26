/**
 * <h1>PeriodicService.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.periodic.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;

import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.ErrorManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_PERIODIC)
@Scope("singleton")
public class PeriodicService extends DpfService {

  private Controller controller;
  private ResourceBundle bundle;

  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
  }

  @Override
  protected void handleContext(DpfContext context) {
    if (System.getProperty("os.name").startsWith("Windows")) {
      controller = new ControllerWindows(context, DPFManagerProperties.getBundle());
    } else {
      controller = new ControllerLinux(context, DPFManagerProperties.getBundle());
    }
  }

  public void savePeriocicalCheck(PeriodicCheck check) {
    boolean result = controller.savePeriodicalCheck(check);
    if (!result){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorSavePeriodic")));
    } else {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("successSavePeriodic").replace("%1", check.getUuid())));
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, check.toString(bundle)));
    }
    context.sendGui(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.SAVE, check.getUuid(), result));
  }

  public void editPeriocicalCheck(PeriodicCheck check) {
    boolean result = controller.editPeriodicalCheck(check);
    if (!result){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorEditPeriodic")));
    } else {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("successEditPeriodic").replace("%1", check.getUuid())));
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, check.toString(bundle)));
    }
    context.sendGui(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.EDIT, check.getUuid(), result));
  }

  public void deletePeriocicalCheck(String uuid) {
    boolean result = controller.deletePeriodicalCheck(uuid);
    if (!result){
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorDeleteCron")));
    } else {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("successRemovePeriodic").replace("%1",uuid)));
    }
    context.sendGui(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, uuid, result));
  }

  public void readPeriodicalChecksGui() {
    List<PeriodicCheck> list = controller.readPeriodicalChecks();
    context.sendGui(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.LIST, list));
  }

  public void readPeriodicalChecksCmd(){
    List<PeriodicCheck> list = controller.readPeriodicalChecks();
    String text = "";
    for (PeriodicCheck check : list){
      text += check.toString(bundle) + "\n";
    }
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, text));
  }

  @PreDestroy
  public void finish() {
  }

}

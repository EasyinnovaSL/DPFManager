/**
 * <h1>ThreadingController.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.threading;

import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.interfaces.console.CheckController;
import dpfmanager.shell.modules.threading.core.ThreadingService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Adria Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_THREADING)
public class ThreadingController extends DpfSpringController {

  @Autowired
  private ThreadingService service;

  @Autowired
  private ApplicationContext appContext;

  @Resource(name = "parameters")
  private Map<String, String> parameters;

  @Override
  synchronized public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(IndividualStatusMessage.class)) {
      IndividualStatusMessage sm = dpfMessage.getTypedMessage(IndividualStatusMessage.class);
      service.finishIndividual(sm.getIndividual(), sm.getUuid(), sm.getConfig());
    } else if (dpfMessage.isTypeOf(GlobalStatusMessage.class)) {
      GlobalStatusMessage gm = dpfMessage.getTypedMessage(GlobalStatusMessage.class);
      boolean noOpen = !parameters.containsKey(CheckController.showReport) || parameters.get("mode").equals("SERVER");
      service.handleGlobalStatus(gm, noOpen);
      // Now close application only if it is not server mode
      if (gm.isFinish() && parameters.get("mode").equals("CMD")) {
        AppContext.close();
      }
    } else if (dpfMessage.isTypeOf(RunnableMessage.class)) {
      RunnableMessage rm = dpfMessage.getTypedMessage(RunnableMessage.class);
      service.run(rm.getRunnable(), rm.getUuid());
    } else if (dpfMessage.isTypeOf(ThreadsMessage.class)) {
      ThreadsMessage tm = dpfMessage.getTypedMessage(ThreadsMessage.class);
      service.processThreadMessage(tm);
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage message) {
    return null;
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }

}

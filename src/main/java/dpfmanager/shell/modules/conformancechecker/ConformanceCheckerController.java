/**
 * <h1>ConformanceCheckerController.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.conformancechecker;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_CONFORMANCE)
public class ConformanceCheckerController extends DpfSpringController {

  @Autowired
  private ConformanceCheckerService service;

  @Autowired
  private ApplicationContext appContext;

  @Resource(name = "parameters")
  private Map<String, String> parameters;

  @Override
  public void handleMessage(DpfMessage message) {
    if (message.isTypeOf(ConformanceMessage.class)) {
      ConformanceMessage cm = message.getTypedMessage(ConformanceMessage.class);
      if (!cm.hasPaths()) {
        // From console
        Integer recursive = null;
        if (parameters.containsKey("-r")){
          recursive = Integer.parseInt(parameters.get("-r"));
        }
        service.setParameters(cm.getConfig(), recursive, null);
        service.initMultiProcessInputRun(cm.getFiles());
      } else {
        // From server
        String path = cm.getPath();
        String input = cm.getInput();
        if (!service.readConfig(path)) {
          // Error reading config file
        } else {
          service.setParameters(null, null, cm.getUuid());
          service.initProcessInputRun(input);
        }
      }
    } else if (message.isTypeOf(ProcessInputMessage.class)){
      service.tractProcessInputMessage(message.getTypedMessage(ProcessInputMessage.class));
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage message) {
    return null;
  }

  @PostConstruct
  public void init() {
    ConsoleContext context = new ConsoleContext(appContext);
    ConformanceChecker.setLogger(new DpfLogger(context, true));
    service.setContext(context);
  }
}

/**
 * <h1>ConformanceCheckerController.java</h1> <p> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version; or, at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+
 * or MPL-2.0+. </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
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
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

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

  private DpfContext context;

  @Resource(name = "parameters")
  private Map<String, String> parameters;

  @Override
  public void handleMessage(DpfMessage message) {
    if (message.isTypeOf(ConformanceMessage.class)) {
      ConformanceMessage cm = message.getTypedMessage(ConformanceMessage.class);
      if (cm.isConsole()) {
        // From console
        Integer recursive = 1;
        if (parameters.containsKey("-r")) {
          recursive = Integer.parseInt(parameters.get("-r"));
        }
        if (checkOverwriteOutput(cm.getConfig().getOutput())) {
          service.initMultiProcessInputRun(cm.getFiles(), cm.getConfig(), recursive);
        }
      } else if (cm.isServer()) {
        // From server
        String path = cm.getPath();
        String input = cm.getInput();
        Configuration config = service.readConfig(path);
        if (config != null) {
          service.initProcessInputRun(input, config, cm.getUuid());
        }
      }
    } else if (message.isTypeOf(ProcessInputMessage.class)) {
      service.tractProcessInputMessage(message.getTypedMessage(ProcessInputMessage.class));
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage message) {
    return null;
  }

  @PostConstruct
  public void init() {
    context = new ConsoleContext(appContext);
    ConformanceChecker.setLogger(new DpfLogger(context, true));
    service.setContext(context);
  }

  private boolean checkOverwriteOutput(String output) {
    if (output == null) {
      return true;
    }
    File tmp = new File(output);
    if (tmp.exists() && tmp.isDirectory() && tmp.listFiles().length > 0) {
      // Ask for overwrite
      String ok = "yes";
      ResourceBundle bundle = DPFManagerProperties.getBundle();
      if (!parameters.containsKey("overwrite")) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("confirmOverwrite") + " Y/N"));
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "(" + bundle.getString("path") + ": " + output + ")"));
        Scanner sc = new Scanner(System.in);
        ok = sc.nextLine();
        sc.close();
      }
      List<String> accepted = Arrays.asList("YES", "Y", "y", "yes");
      if (accepted.contains(ok)) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, ""));
        try {
          FileUtils.deleteDirectory(tmp);
        } catch (IOException e) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("cannotOverwrite")));
          return false;
        }
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("canceledCheck")));
        return false;
      }
    }
    return true;
  }
}

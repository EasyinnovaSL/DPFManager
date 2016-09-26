/**
 * <h1>DatabaseController.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.database;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.core.DatabaseService;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.server.messages.PostMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Controller(BasicConfig.MODULE_DATABASE)
public class DatabaseController extends DpfSpringController {

  @Autowired
  private DatabaseService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  synchronized public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(JobsMessage.class)){
      service.handleJobsMessage(dpfMessage.getTypedMessage(JobsMessage.class));
    }
  }

  @Override
  public Object handleMessageWithResponse(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(PostMessage.class)){
      PostMessage pm = dpfMessage.getTypedMessage(PostMessage.class);
      if (pm.isAsk() && pm.getHash() == null){
        return service.getJobStatus(pm.getId());
      } else if (pm.isAsk()){
        return service.getJobStatusByHash(pm.getHash());
      }
    }
    return null;
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }
}

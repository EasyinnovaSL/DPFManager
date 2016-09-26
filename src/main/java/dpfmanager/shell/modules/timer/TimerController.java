/**
 * <h1>TimerController.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.timer;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.timer.core.TimerService;
import dpfmanager.shell.modules.timer.messages.TimerMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
@Controller(BasicConfig.MODULE_TIMER)
public class TimerController extends DpfSpringController {

  @Autowired
  private TimerService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(TimerMessage.class)) {
      TimerMessage tm = dpfMessage.getTypedMessage(TimerMessage.class);
      if (tm.isPlay()){
        service.playTask(tm.getClazz());
      } else if (tm.isStop()){
        service.stopTask(tm.getClazz());
      } else if (tm.isFinish()){
        service.runTask(tm.getClazz());
      }
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

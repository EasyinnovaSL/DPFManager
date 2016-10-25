/**
 * <h1>ThreadingModule.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.threading;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.messages.messages.CloseMessage;
import dpfmanager.shell.modules.threading.core.ThreadingService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_THREADING,
    name = BasicConfig.MODULE_THREADING,
    active = true)
public class ThreadingModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private ThreadingService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(IndividualStatusMessage.class)) {
      IndividualStatusMessage sm = dpfMessage.getTypedMessage(IndividualStatusMessage.class);
      service.finishIndividual(sm.getIndividual(), sm.getUuid(), sm.getConfig());
    } else if (dpfMessage.isTypeOf(GlobalStatusMessage.class)) {
      GlobalStatusMessage gm = dpfMessage.getTypedMessage(GlobalStatusMessage.class);
      service.handleGlobalStatus(gm, false);
    } else if (dpfMessage.isTypeOf(RunnableMessage.class)) {
      RunnableMessage rm = dpfMessage.getTypedMessage(RunnableMessage.class);
      service.run(rm.getRunnable(), rm.getUuid());
    } else if (dpfMessage.isTypeOf(ThreadsMessage.class)){
      ThreadsMessage tm = dpfMessage.getTypedMessage(ThreadsMessage.class);
      service.processThreadMessage(tm);
    } else if (dpfMessage.isTypeOf(CloseMessage.class)){
      service.closeRequested();
    }
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    service.setContext(new GuiContext(context));
    if (context.getParentId().equals(GuiConfig.PERSPECTIVE_DESSIGN)){
      service.reSetContext(new GuiContext(context));
    }
  }

  @PreDestroy
  public void onPreDestroyComponent() {
    service.finish();
  }

  @Override
  public Context getContext() {
    return context;
  }

}

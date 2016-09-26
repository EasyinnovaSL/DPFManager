/**
 * <h1>BarComponent.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.bottom;

import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.SwitchMessage;
import dpfmanager.shell.core.messages.WidgetMessage;
import dpfmanager.shell.interfaces.gui.fragment.BarFragment;
import dpfmanager.shell.interfaces.gui.fragment.BottomFragment;
import dpfmanager.shell.modules.timer.messages.TimerMessage;
import dpfmanager.shell.modules.timer.tasks.JobsStatusTask;
import javafx.event.Event;
import javafx.scene.Node;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.View;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 21/03/2016.
 */
@View(id = GuiConfig.COMPONENT_BAR,
    name = GuiConfig.COMPONENT_BAR,
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_BAR)
public class BarComponent extends DpfSimpleView {

  @Resource
  private Context context;

  private ManagedFragmentHandler<BarFragment> fragment;

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message.isTypeOf(WidgetMessage.class)){
      WidgetMessage wm = message.getTypedMessage(WidgetMessage.class);
      if (wm.isTasks() && wm.isShow() && !fragment.getController().isTasksvisible()){
        fragment.getController().showHideTasks();
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    return fragment.getFragmentNode();
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    fragment = context.getManagedFragmentHandler(BarFragment.class);
    fragment.getController().init();
  }

  @Override
  public void sendMessage(String target, Object message) {
    context.send(target, message);
  }

}

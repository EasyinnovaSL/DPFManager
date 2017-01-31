/**
 * <h1>PaneComponent.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.bottom;

import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.WidgetMessage;
import dpfmanager.shell.interfaces.gui.fragment.BottomFragment;
import dpfmanager.shell.interfaces.gui.fragment.TaskFragment;
import dpfmanager.shell.interfaces.gui.fragment.TopFragment;
import dpfmanager.shell.modules.database.messages.CheckTaskMessage;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.timer.messages.TimerMessage;
import dpfmanager.shell.modules.timer.tasks.JobsStatusTask;
import javafx.scene.Node;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.View;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 03/03/2016.
 */
@View(id = GuiConfig.COMPONENT_PANE,
    name = GuiConfig.COMPONENT_PANE,
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_PANE)
public class PaneComponent extends DpfSimpleView {

  @Resource
  private Context context;

  private ManagedFragmentHandler<BottomFragment> fragment;

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message.isTypeOf(WidgetMessage.class)){
      WidgetMessage wm = message.getTypedMessage(WidgetMessage.class);
      if (wm.isConsole() && wm.isShow()){
        fragment.getController().showConsole();
        if(!fragment.getController().isTasksVisible()){
          showInPrespective();
        }
      } else if (wm.isConsole() && wm.isHide()){
        fragment.getController().hideConsole();
        if(!fragment.getController().isTasksVisible()){
          hideInPrespective();
        }
      } else if (wm.isTasks() && wm.isShow()){
        fragment.getController().showTasks();
        if(!fragment.getController().isConsoleVisible()){
          showInPrespective();
        }
      } else if (wm.isTasks() && wm.isHide()) {
        fragment.getController().hideTasks();
        if(!fragment.getController().isConsoleVisible()){
          hideInPrespective();
        }
      }

      // Notify timer
      if (fragment.getController().isTasksVisible()){
        context.send(BasicConfig.MODULE_TIMER, new TimerMessage(TimerMessage.Type.PLAY, JobsStatusTask.class));
      } else {
        context.send(BasicConfig.MODULE_TIMER, new TimerMessage(TimerMessage.Type.STOP, JobsStatusTask.class));
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(CheckTaskMessage.class)){
      CheckTaskMessage ctm = message.getTypedMessage(CheckTaskMessage.class);
      if (!ctm.isFinishActions()){
        handleJobs(ctm);
      } else {
        fragment.getController().finishActions(ctm);
      }
    }
    return fragment.getFragmentNode();
  }

  private void handleJobs(CheckTaskMessage ctm){
    for (Jobs job : ctm.getJobs()){
      if (!fragment.getController().containsJob(job.getId()) && job.getState() != 3){
        // Init new fragment
        ManagedFragmentHandler<TaskFragment> taskFragment = context.getManagedFragmentHandler(TaskFragment.class);
        fragment.getController().addTask(taskFragment, job, ctm.getPid());
      } else {
        // Update existing one
        fragment.getController().updateTask(job);
      }
    }
    // Notify finish task
    context.send(BasicConfig.MODULE_TIMER, new TimerMessage(TimerMessage.Type.FINISH, JobsStatusTask.class));
  }

  private void showInPrespective(){
    String current = context.getManagedFragmentHandler(TopFragment.class).getController().getCurrentId();
    context.send(current,new WidgetMessage(WidgetMessage.Action.SHOW));
  }

  private void hideInPrespective(){
    String current = context.getManagedFragmentHandler(TopFragment.class).getController().getCurrentId();
    context.send(current,new WidgetMessage(WidgetMessage.Action.HIDE));
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    fragment = context.getManagedFragmentHandler(BottomFragment.class);
    fragment.getController().init();
  }

  @Override
  public void sendMessage(String target, Object message) {
    context.send(target, message);
  }

}

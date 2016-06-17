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

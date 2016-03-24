package dpfmanager.shell.interfaces.gui.component.bottom;

import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.WidgetMessage;
import dpfmanager.shell.interfaces.gui.fragment.BottomFragment;
import dpfmanager.shell.interfaces.gui.fragment.TopFragment;
import javafx.scene.Node;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.View;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 03/03/2016.
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
    }
  }

  private void showInPrespective(){
    String current = context.getManagedFragmentHandler(TopFragment.class).getController().getCurrentId();
    context.send(current,new WidgetMessage(WidgetMessage.Action.SHOW));
  }

  private void hideInPrespective(){
    String current = context.getManagedFragmentHandler(TopFragment.class).getController().getCurrentId();
    context.send(current,new WidgetMessage(WidgetMessage.Action.HIDE));
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    return fragment.getFragmentNode();
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

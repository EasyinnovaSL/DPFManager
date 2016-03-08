package dpfmanager.shell.interfaces.gui.component.bottom;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.fragment.BottomFragment;
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
 * Created by Adrià Llorens on 03/03/2016.
 */
@View(id = GuiConfig.COMPONENT_BOTTOM,
    name = GuiConfig.COMPONENT_BOTTOM,
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_BOTTOM)
public class BottomComponent implements FXComponent {

  @Resource
  private Context context;

  private ManagedFragmentHandler<BottomFragment> fragment;

  @Override
  public Node handle(Message<Event, Object> message) {
    return null;
  }

  @Override
  public Node postHandle(Node node, Message<Event, Object> message) {
    return fragment.getFragmentNode();
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    fragment = context.getManagedFragmentHandler(BottomFragment.class);
    fragment.getController().init();
  }

}

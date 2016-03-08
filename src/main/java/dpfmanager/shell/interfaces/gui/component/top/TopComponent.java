package dpfmanager.shell.interfaces.gui.component.top;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.fragment.TopFragment;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.View;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@View(id = GuiConfig.COMPONENT_TOP,
    name = "TopPane",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_TOP)
public class TopComponent implements FXComponent {

  private FlowPane flowPane;

  @Resource
  private Context context;

  @Override
  public Node handle(final Message<Event, Object> message) {
    // runs in worker thread
    return null;
  }

  @Override
  public Node postHandle(Node arg0, Message<Event, Object> message) {
      // runs in FX application thread
      return flowPane;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    flowPane = (FlowPane) context.getManagedFragmentHandler(TopFragment.class).getFragmentNode();
  }

}

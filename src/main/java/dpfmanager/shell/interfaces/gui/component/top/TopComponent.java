package dpfmanager.shell.interfaces.gui.component.top;

import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
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
public class TopComponent extends DpfSimpleView {

  private FlowPane flowPane;

  @Resource
  private Context context;

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    return flowPane;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    flowPane = (FlowPane) context.getManagedFragmentHandler(TopFragment.class).getFragmentNode();
  }

  @Override
  public void sendMessage(String target, Object message) {
    context.send(target, message);
  }

}

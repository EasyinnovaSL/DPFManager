package dpfmanager.shell.jacp.interfaces.gui.component.about;

import dpfmanager.shell.jacp.core.config.GuiConfig;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_ABOUT,
    name = GuiConfig.COMPONENT_ABOUT,
    viewLocation = "/fxml-jr/about.fxml",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_ABOUT)
public class AboutView implements FXComponent {

  @Resource
  private Context context;

  @Override
  public Node handle(final Message<Event, Object> message) {
    return null;
  }

  @Override
  public Node postHandle(Node node, Message<Event, Object> message) {
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
  }

}

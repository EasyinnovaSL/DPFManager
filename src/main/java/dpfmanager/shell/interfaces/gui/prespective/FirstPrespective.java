package dpfmanager.shell.interfaces.gui.prespective;

import dpfmanager.shell.core.config.GuiConfig;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.perspective.FXPerspective;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Perspective(id = GuiConfig.PRESPECTIVE_FIRST,
    name = GuiConfig.PRESPECTIVE_FIRST,
    components = { GuiConfig.COMPONENT_FIRST }
)
public class FirstPrespective implements FXPerspective {

  @Resource
  public Context context;

  @Override
  public void handlePerspective(Message<Event, Object> message, PerspectiveLayout perspectiveLayout) {

  }

  @PostConstruct
  public void onStartPerspective(PerspectiveLayout perspectiveLayout, FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Center Component
    StackPane centerPane = new StackPane();
    centerPane.getStylesheets().add("/styles-jr/main.css");
    centerPane.setAlignment(Pos.TOP_CENTER);

    // Attach to prespective
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_FIRST, centerPane);

    // Define main pane
    perspectiveLayout.registerRootComponent(centerPane);
  }

}

package dpfmanager.shell.interfaces.gui.perspective;

import dpfmanager.shell.core.adapter.DpfAbstractPerspective;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Perspective(id = GuiConfig.PERSPECTIVE_FIRST,
    name = GuiConfig.PERSPECTIVE_FIRST,
    components = {
        GuiConfig.COMPONENT_FIRST,
        BasicConfig.MODULE_MESSAGE
    }
)
public class FirstPerspective extends DpfAbstractPerspective {

  @Resource
  public Context context;

  @Override
  public void handleMessage(DpfMessage dpfMessage, PerspectiveLayout layout) {
  }

  @PostConstruct
  public void onStartPerspective(PerspectiveLayout perspectiveLayout, FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Center Component
    StackPane centerPane = new StackPane();
    centerPane.getStylesheets().add("/styles/main.css");
    centerPane.setAlignment(Pos.TOP_CENTER);

    // Attach to PERSPECTIVE
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_FIRST, centerPane);

    // Define main pane
    perspectiveLayout.registerRootComponent(centerPane);
  }

  @Override
  public Context getContext() {
    return context;
  }

}

package dpfmanager.shell.interfaces.gui.perspective;

import dpfmanager.shell.core.adapter.DpfAbstractPerspective;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Perspective(id = GuiConfig.PERSPECTIVE_DESSIGN,
    name = GuiConfig.PERSPECTIVE_DESSIGN,
    active = false,
    components = {
        GuiConfig.COMPONENT_TOP,
        GuiConfig.COMPONENT_DESIGN,
        GuiConfig.COMPONENT_PANE,
        GuiConfig.COMPONENT_BAR,
        BasicConfig.MODULE_MESSAGE,
        BasicConfig.MODULE_CONFORMANCE,
        BasicConfig.MODULE_REPORT,
        BasicConfig.MODULE_THREADING,
        BasicConfig.MODULE_DATABASE,
        BasicConfig.MODULE_TIMER
    }
)
public class DessignPerspective extends DpfAbstractPerspective {

  @Resource
  public Context context;

  private StackPane topPane;

  @Override
  public void handleMessage(DpfMessage dpfMessage, PerspectiveLayout layout) {

  }

  @PostConstruct
  public void onStartPerspective(PerspectiveLayout perspectiveLayout, FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Top Buttons component
    topPane = new StackPane();

    // Center Component
    StackPane centerPane = new StackPane();
    centerPane.setAlignment(Pos.TOP_CENTER);

    // Bottom Pane Component
    bottomPane = new StackPane();
    bottomPane.setAlignment(Pos.BOTTOM_CENTER);

    // Bottom Bar Component
    bottomBar = new StackPane();
    bottomBar.setAlignment(Pos.BOTTOM_CENTER);

    // Attach to PERSPECTIVE
    mainSplit = constructSplitPane(constructScrollPane(centerPane), bottomPane);
    mainPane = constructMainPane(mainSplit, bottomBar);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_TOP, topPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_DESIGN, centerPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_PANE, bottomPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_BAR, bottomBar);

    // Define main pane
    borderPane = constructBorderPane(perspectiveLayout, topPane, mainPane);
  }

  @Override
  public Context getContext() {
    return context;
  }

  @Override
  public void onReloadCustom() {
    getContext().send(GuiConfig.COMPONENT_DESIGN, new UiMessage(UiMessage.Type.RELOAD));
  }

  @Override
  public void onShowCustom() {
    getContext().send(GuiConfig.COMPONENT_DESIGN, new UiMessage(UiMessage.Type.RELOAD));
  }
}

package dpfmanager.shell.interfaces.gui.prespective;

import dpfmanager.shell.core.adapter.DpfAbstractPrespective;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.interfaces.gui.fragment.BarFragment;
import dpfmanager.shell.interfaces.gui.fragment.TopFragment;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.OnShow;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Perspective(id = GuiConfig.PRESPECTIVE_REPORTS,
    name = GuiConfig.PRESPECTIVE_REPORTS,
    active = false,
    components = {
        GuiConfig.COMPONENT_TOP,
        GuiConfig.COMPONENT_REPORTS,
        GuiConfig.COMPONENT_PANE,
        GuiConfig.COMPONENT_BAR,
        BasicConfig.MODULE_LOGS
    }
)
public class ReportsPrespective extends DpfAbstractPrespective {

  @Resource
  public Context context;

  private BorderPane borderPane;
  private ScrollPane scrollPane;

  private boolean firsttime;

  @Override
  public void handleMessage(DpfMessage dpfMessage, PerspectiveLayout layout) {
  }

  @Override
  public void onShowCustom() {
    context.send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.SHOW));
  }

  @PostConstruct
  public void onStartPerspective(PerspectiveLayout perspectiveLayout, FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Top Buttons component
    StackPane topPane = new StackPane();

    // Center Component
    StackPane centerPane = new StackPane();
    centerPane.setAlignment(Pos.TOP_CENTER);

    // Bottom Component
    bottomPane = new StackPane();
    bottomPane.setAlignment(Pos.BOTTOM_CENTER);

    // Bottom Bar Component
    bottomBar = new StackPane();
    bottomBar.setAlignment(Pos.BOTTOM_CENTER);

    // Bottom Bar
    StackPane botPane = new StackPane();
    botPane.setAlignment(Pos.BOTTOM_CENTER);

    // Attach to prespective
    mainSplit = constructSplitPane(constructScrollPane(centerPane), bottomPane);
    mainPane = constructMainPane(mainSplit, bottomBar);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_TOP, topPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_REPORTS, centerPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_PANE, bottomPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_BAR, bottomBar);

    // Define main pane
    borderPane = constructBorderPane(perspectiveLayout, topPane, mainPane);

    firsttime = true;
  }

  @Override
  public Context getContext() {
    return context;
  }
}

package dpfmanager.shell.jacp.interfaces.gui.prespective;

import dpfmanager.shell.jacp.core.config.GuiConfig;
import dpfmanager.shell.jacp.core.messages.ConsoleMessage;
import dpfmanager.shell.jacp.core.messages.DpfMessage;
import dpfmanager.shell.jacp.core.messages.ReportsMessage;
import dpfmanager.shell.jacp.interfaces.gui.fragment.BottomFragment;
import dpfmanager.shell.jacp.interfaces.gui.fragment.TopFragment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
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
@Perspective(id = GuiConfig.PRESPECTIVE_REPORTS,
    name = GuiConfig.PRESPECTIVE_REPORTS,
    active = false,
    components = {
        GuiConfig.COMPONENT_TOP,
        GuiConfig.COMPONENT_REPORTS,
        GuiConfig.COMPONENT_BOTTOM
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
    if (dpfMessage instanceof ConsoleMessage) {
      ConsoleMessage consMessage = (ConsoleMessage) dpfMessage;
      if (consMessage.isShow()) {
        showBottomPane(context);
      } else {
        hideBottomPane(context);
      }
    }
  }

  @Override
  public void sendMessage(String target, DpfMessage dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void onShow() {
    showHideBottomPane(context, context.getManagedFragmentHandler(BottomFragment.class).getController().isVisible());
    context.getManagedFragmentHandler(TopFragment.class).getController().setCurrentToggle(TopFragment.ButReports);
    if (firsttime) {
      context.send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.SCROLL, scrollPane));
      firsttime = false;
    }
    context.send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.TABLE));
  }

  @Override
  public void onReload() {
    context.send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.TABLE));
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

    // Attach to prespective
    showButton = constructShowButton();
    scrollPane = constructScrollPane(centerPane);
    mainSplit = constructSplitPane(scrollPane, bottomPane);
    mainPane = constructMainPane(mainSplit, showButton);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_TOP, topPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_REPORTS, centerPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_BOTTOM, bottomPane);

    // Define main pane
    borderPane = constructBorderPane(perspectiveLayout, topPane, mainPane);

    // Button handlers
    showButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        context.send(GuiConfig.PRESPECTIVE_REPORTS, new ConsoleMessage(ConsoleMessage.Type.SHOW));
      }
    });
    firsttime = true;
  }

}

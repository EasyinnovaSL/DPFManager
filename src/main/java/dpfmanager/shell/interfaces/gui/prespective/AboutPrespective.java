package dpfmanager.shell.interfaces.gui.prespective;

import dpfmanager.shell.core.messages.ConsoleMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.fragment.BottomFragment;
import dpfmanager.shell.interfaces.gui.fragment.TopFragment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
@Perspective(id = GuiConfig.PRESPECTIVE_ABOUT,
    name = GuiConfig.PRESPECTIVE_ABOUT,
    active = false,
    components = {
        GuiConfig.COMPONENT_TOP,
        GuiConfig.COMPONENT_ABOUT,
        GuiConfig.COMPONENT_BOTTOM
    }
)
public class AboutPrespective extends DpfAbstractPrespective {

  @Resource
  public Context context;

  private BorderPane borderPane;

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
    context.getManagedFragmentHandler(TopFragment.class).getController().setCurrentToggle(TopFragment.ButAbout);
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
    mainSplit = constructSplitPane(constructScrollPane(centerPane), bottomPane);
    mainPane = constructMainPane(mainSplit, showButton);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_TOP, topPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_ABOUT, centerPane);
    perspectiveLayout.registerTargetLayoutComponent(GuiConfig.TARGET_CONTAINER_BOTTOM, bottomPane);

    // Define main pane
    borderPane = constructBorderPane(perspectiveLayout, topPane, mainPane);

    // Button handlers
    showButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        context.send(GuiConfig.PRESPECTIVE_ABOUT, new ConsoleMessage(ConsoleMessage.Type.SHOW));
      }
    });
  }

}

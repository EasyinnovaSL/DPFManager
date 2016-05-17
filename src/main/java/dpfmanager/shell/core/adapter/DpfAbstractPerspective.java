package dpfmanager.shell.core.adapter;

import static javafx.scene.layout.Priority.ALWAYS;

import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.messages.WidgetMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.fragment.BarFragment;
import dpfmanager.shell.interfaces.gui.fragment.TopFragment;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.perspective.FXPerspective;
import org.jacpfx.rcp.util.LayoutUtil;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
public abstract class DpfAbstractPerspective implements FXPerspective {

  protected Node divider;

  protected AnchorPane mainPane;
  protected StackPane bottomPane;
  protected SplitPane mainSplit;
  protected StackPane bottomBar;

  protected ManagedFragmentHandler<BarFragment> fragmentBar;

  /**
   * Handle default messages
   */
  @Override
  public void handlePerspective(Message<Event, Object> message, PerspectiveLayout perspectiveLayout) {
    if (message.isMessageBodyTypeOf(ArrayMessage.class)) {
      // Array message
      ArrayMessage aMessage = message.getTypedMessageBody(ArrayMessage.class);
      tractMessage(aMessage.getFirstMessage(), perspectiveLayout);
      if (aMessage.hasNext()) {
        aMessage.removeFirst();
        getContext().send(aMessage.getFirstTarget(), aMessage);
      }
    } else if (message.isMessageBodyTypeOf(DpfMessage.class)) {
      // Single message
      DpfMessage dpfMessage = message.getTypedMessageBody(DpfMessage.class);
      tractMessage(dpfMessage, perspectiveLayout);
    }
  }

  private void tractMessage(DpfMessage dpfMessage, PerspectiveLayout perspectiveLayout) {
    if (dpfMessage.isTypeOf(UiMessage.class)) {
      // On Show Perspective
      UiMessage um = dpfMessage.getTypedMessage(UiMessage.class);
      if (um.isShow()){
        onShow();
      } else{
        onReload();
      }
    }else if (dpfMessage.isTypeOf(WidgetMessage.class)) {
      WidgetMessage wm = dpfMessage.getTypedMessage(WidgetMessage.class);
      showHideBottomPane(wm.isShow());
    } else{
      handleMessage(dpfMessage, perspectiveLayout);
    }
  }

  public void onShow(){
    getContext().getManagedFragmentHandler(TopFragment.class).getController().setCurrentToggle(getContext().getId());
    showHideBottomPane(getContext().getManagedFragmentHandler(BarFragment.class).getController().isVisible());
    onShowCustom();
  }

  public void onReload(){
    onReloadCustom();
  }

  /**
   * Methods to override
   */
  public abstract void handleMessage(DpfMessage dpfMessage, PerspectiveLayout layout);

  public abstract Context getContext();

  public void onShowCustom(){}

  public void onReloadCustom(){}

  /**
   * Construct UI methods
   */
  protected BorderPane constructBorderPane(PerspectiveLayout perspectiveLayout, Node top, Node center) {
    BorderPane borderPane = new BorderPane();
    borderPane.getStylesheets().add("/styles/main.css");
    borderPane.getStyleClass().add("background-main");
    borderPane.setTop(top);
    borderPane.setCenter(center);
    LayoutUtil.GridPaneUtil.setFullGrow(ALWAYS, borderPane);
    perspectiveLayout.registerRootComponent(borderPane);
    return borderPane;
  }

  protected ScrollPane constructScrollPane(Node content) {
    content.getStyleClass().add("background-main");

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.getStyleClass().add("background-main");
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setMaxWidth(DpFManagerConstants.WIDTH);

    // Center content
    scrollPane.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (scrollPane.getWidth() < DpFManagerConstants.WIDTH) {
          scrollPane.setHvalue(0.5);
        }
      }
    });
    scrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (scrollPane.getWidth() < DpFManagerConstants.WIDTH) {
          scrollPane.setHvalue(0.5);
        }
      }
    });

    scrollPane.setContent(content);
    return scrollPane;
  }

  protected SplitPane constructSplitPane(Node n1, Node n2) {
    SplitPane mainSplit = new SplitPane();
    mainSplit.setId("mainSplit");
    mainSplit.setOrientation(Orientation.VERTICAL);
    mainSplit.setDividerPositions(0.8);
    mainSplit.getStyleClass().add("background-main");
    mainSplit.getItems().addAll(n1, n2);
    return mainSplit;
  }

  protected AnchorPane constructMainPane(SplitPane mainSplit, Node bar) {
    AnchorPane mainPane = new AnchorPane();
    mainPane.setCenterShape(true);
    mainPane.setId("mainPane");
    mainPane.getStyleClass().add("background-main");
    mainPane.getChildren().add(mainSplit);
    AnchorPane.setTopAnchor(mainSplit, 0.0);
    AnchorPane.setBottomAnchor(mainSplit, 21.0);
    AnchorPane.setLeftAnchor(mainSplit, 0.0);
    AnchorPane.setRightAnchor(mainSplit, 0.0);

    mainPane.getChildren().add(bar);
    AnchorPane.setBottomAnchor(bar, 0.0);
    AnchorPane.setRightAnchor(bar, 0.0);
    AnchorPane.setLeftAnchor(bar, 0.0);

    return mainPane;
  }

  protected Node getDivider() {
    if (divider == null) {
      divider = mainPane.lookup("#mainSplit > .split-pane-divider");
    }
    return divider;
  }

  /**
   * Bottom Widget methods
   */
  public void showHideBottomPane(boolean toShow) {
    if (toShow) {
      showBottomPane();
    } else {
      hideBottomPane();
    }
  }

  public void showBottomPane() {
    // Show stack pane
    NodeUtil.showStack(bottomPane);

    // Show divider
    if (getDivider() != null) {
      if (!getDivider().getStyleClass().contains("show-divider")) {
        getDivider().getStyleClass().add("show-divider");
      }
      NodeUtil.showNode(getDivider());
    }
    mainSplit.setDividerPositions(0.8);
  }

  public void hideBottomPane() {
    // Hide stack pane
    NodeUtil.hideStack(bottomPane);

    // Hide divider
    if (getDivider() != null) {
      if (getDivider().getStyleClass().contains("show-divider")) {
        getDivider().getStyleClass().remove("show-divider");
      }
      NodeUtil.hideNode(getDivider());
    }
  }
}

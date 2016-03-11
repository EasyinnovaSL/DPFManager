package dpfmanager.shell.interfaces.gui.prespective;

import static javafx.scene.layout.Priority.ALWAYS;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.fragment.BottomFragment;
import javafx.event.Event;
import javafx.geometry.Insets;
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
public abstract class DpfAbstractPrespective implements FXPerspective {

  protected Node divider;

  protected AnchorPane mainPane;
  protected StackPane bottomPane;
  protected SplitPane mainSplit;
  protected Button showButton;

  /**
   * Handle default messages
   */
  @Override
  public void handlePerspective(Message<Event, Object> message, PerspectiveLayout perspectiveLayout) {
    // Only tract Dpf Messages
    if (message.getMessageBody() instanceof DpfMessage) {
      DpfMessage dpfMessage = (DpfMessage) message.getMessageBody();
      if (dpfMessage instanceof UiMessage) {
        UiMessage uiMessage = (UiMessage) dpfMessage;
        switchUiActions(uiMessage);
      } else {
        // Send to overriden method
        handleMessage(dpfMessage, perspectiveLayout);
      }
    }
  }

  private void switchUiActions(UiMessage uiMessage) {
    switch (uiMessage.getType()) {
      case INIT:
        onInit();
        break;
      case RELOAD:
        onReload();
        break;
      case SHOW:
        onShow();
        break;
      case HIDE:
        onHide();
        break;
    }

    if (uiMessage.hasNext()){
      sendMessage(uiMessage.getNext(), uiMessage.getMessage());
    }
  }

  /**
   * Methods to override
   */
  public abstract void handleMessage(DpfMessage dpfMessage, PerspectiveLayout layout);

  public abstract void sendMessage(String target, DpfMessage dpfMessage);

  public void onInit() {
  }

  public void onReload() {
  }

  public void onShow() {
  }

  public void onHide() {
  }

  protected BorderPane constructBorderPane(PerspectiveLayout perspectiveLayout, Node top, Node center) {
    BorderPane borderPane = new BorderPane();
    borderPane.getStylesheets().add("/styles/main.css");
    borderPane.getStyleClass().add("background-main");
    LayoutUtil.GridPaneUtil.setFullGrow(ALWAYS, borderPane);
    perspectiveLayout.registerRootComponent(borderPane);
    borderPane.setTop(top);
    borderPane.setCenter(center);
    return borderPane;
  }

  protected ScrollPane constructScrollPane(Node content) {
    content.getStyleClass().add("background-main");
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setMaxWidth(970);
    scrollPane.getStyleClass().add("background-main");
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
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

  protected AnchorPane constructMainPane(SplitPane mainSplit, Button show) {
    AnchorPane mainPane = new AnchorPane();
    mainPane.setId("mainPane");
    mainPane.getStyleClass().add("background-main");
    mainPane.getChildren().add(mainSplit);
    AnchorPane.setTopAnchor(mainSplit, 0.0);
    AnchorPane.setBottomAnchor(mainSplit, 0.0);
    AnchorPane.setLeftAnchor(mainSplit, 0.0);
    AnchorPane.setRightAnchor(mainSplit, 0.0);

    mainPane.getChildren().add(show);
    AnchorPane.setBottomAnchor(show, 0.0);
    AnchorPane.setRightAnchor(show, 0.0);

    return mainPane;
  }

  protected Button constructShowButton() {
    Button showBottom = new Button();
    showBottom.setText("Show");
    showBottom.getStyleClass().addAll("bot-button");
    showBottom.setPadding(new Insets(4, 5, 4, 5));
    return showBottom;
  }

  protected Node getDivider() {
    if (divider == null) {
      divider = mainPane.lookup("#mainSplit > .split-pane-divider");
    }
    return divider;
  }

  public void showHideBottomPane(Context context, boolean toShow) {
    if (toShow) {
      showBottomPane(context);
    } else {
      hideBottomPane(context);
    }
  }

  public void showBottomPane(Context context) {
    // Show stack pane
    NodeUtil.showStack(bottomPane);

    // Show divider
    mainSplit.setDividerPositions(0.8);
    if (getDivider() != null) {
      if (!getDivider().getStyleClass().contains("show-divider")) {
        getDivider().getStyleClass().add("show-divider");
      }
      NodeUtil.showNode(getDivider());
    }

    // Hide button
    NodeUtil.hideNode(showButton);

    // Save it
    context.getManagedFragmentHandler(BottomFragment.class).getController().setVisible(true);
    context.getManagedFragmentHandler(BottomFragment.class).getController().setCurrentPrespective(context.getId());
  }

  public void hideBottomPane(Context context) {
    // Hide stack pane
    NodeUtil.hideStack(bottomPane);

    // Hide divider
    if (getDivider() != null) {
      if (getDivider().getStyleClass().contains("show-divider")) {
        getDivider().getStyleClass().remove("show-divider");
      }
      NodeUtil.hideNode(getDivider());
    }

    // Show button
    NodeUtil.showNode(showButton);

    // Save it
    context.getManagedFragmentHandler(BottomFragment.class).getController().setVisible(false);
  }
}

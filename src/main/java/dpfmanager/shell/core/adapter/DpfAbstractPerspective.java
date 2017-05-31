/**
 * <h1>DpfAbstractPerspective.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.core.adapter;

import static javafx.scene.layout.Priority.ALWAYS;

import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ScrollMessage;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import com.sun.javafx.scene.control.skin.TableColumnHeader;

import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.perspective.FXPerspective;
import org.jacpfx.rcp.util.LayoutUtil;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Adria Llorens on 03/03/2016.
 */
public abstract class DpfAbstractPerspective implements FXPerspective {

  protected Node divider;

  protected BorderPane borderPane;
  protected AnchorPane mainPane;
  protected StackPane bottomPane;
  protected SplitPane mainSplit;
  protected StackPane bottomBar;
  protected ScrollPane scrollPane;

  protected ManagedFragmentHandler<BarFragment> fragmentBar;

  protected Locale currentLocale = Locale.getDefault();

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
      } else if (um.isReload()){
        onReload();
      }
    } else if (dpfMessage.isTypeOf(WidgetMessage.class)) {
      WidgetMessage wm = dpfMessage.getTypedMessage(WidgetMessage.class);
      showHideBottomPane(wm.isShow());
    } else if (dpfMessage.isTypeOf(ScrollMessage.class)) {
      ScrollMessage sm = dpfMessage.getTypedMessage(ScrollMessage.class);
      ensureVisible(sm.getNdoe());
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

    scrollPane = new ScrollPane();
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

  private void ensureVisible(Node node) {
//    double width = scrollPane.getContent().getBoundsInLocal().getWidth();
//    double height = scrollPane.getContent().getBoundsInLocal().getHeight();
//
//    double x = node.getBoundsInParent().getMaxX();
//    double y = node.getBoundsInParent().getMaxY();
//
//    // scrolling values range from 0 to 1
//    scrollPane.setVvalue(y/height);
//    scrollPane.setHvalue(x / width);
//
//    // just for usability
//    node.requestFocus();
  }
}

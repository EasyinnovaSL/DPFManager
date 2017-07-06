package dpfmanager.shell.interfaces.gui.component.global;

import dpfmanager.shell.core.util.NodeUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import com.sun.javafx.scene.control.skin.PaginationSkin;

/**
 * Created by Adrià Llorens on 06/07/2017.
 */
public class PaginationBetterSkin extends PaginationSkin {

  private HBox controlBox;
  private Label pageInformation;
  private Node controlNav;
  private Button prev;
  private Button next;
  private Button first;
  private Button last;

  /**
   * @param pagination
   */
  public PaginationBetterSkin(Pagination pagination) {
    super(pagination);
    patchNavigation();
  }

  private void patchNavigation() {
    Pagination pagination = getSkinnable();
    Node control = pagination.lookup(".control-box");
    Node navControl = pagination.lookup(".pagination-control");
    if (!(control instanceof HBox) || navControl == null) return;

    controlBox = (HBox) control;
    controlNav = navControl;
    prev = (Button) controlBox.getChildren().get(0);
    next = (Button) controlBox.getChildren().get(controlBox.getChildren().size() - 1);

    // Remove label
    pageInformation = (Label) pagination.lookup(".page-information");
    pageInformation.setMinHeight(0.0);
    pageInformation.setPrefHeight(0.0);
    pageInformation.setMaxHeight(0.0);

    first = new Button("");
    first.getStyleClass().add("arrows-buttons");
    first.setGraphic(createArrows("left-arrow"));
    first.setOnAction(e -> {
      pagination.setCurrentPageIndex(0);
    });
    first.disableProperty().bind(pagination.currentPageIndexProperty().isEqualTo(0));

    last = new Button("");
    last.getStyleClass().add("arrows-buttons");
    last.setGraphic(createArrows("right-arrow"));
    last.setOnAction(e -> {
      pagination.setCurrentPageIndex(pagination.getPageCount());
    });
    last.disableProperty().bind(pagination.currentPageIndexProperty().isEqualTo(Bindings.add(-1,pagination.pageCountProperty())));

    ListChangeListener childrenListener = c -> {
      while (c.next()) {
        // implementation detail: when nextButton is added, the setup is complete
        if (c.wasAdded() && !c.wasRemoved() // real addition
            && c.getAddedSize() == 1 // single addition
            && c.getAddedSubList().get(0) == next) {
          addCustomNodes();
        }
      }
    };
    controlBox.getChildren().addListener(childrenListener);
    addCustomNodes();
  }

  private Node createArrows(String clazz){
    StackPane arrow1 = new StackPane();
    arrow1.getStyleClass().add(clazz);
    StackPane arrow2 = new StackPane();
    arrow2.getStyleClass().add(clazz);
    HBox hbox = new HBox();
    hbox.setMinHeight(9.0);
    hbox.setPrefHeight(9.0);
    hbox.setMaxHeight(9.0);
    hbox.getChildren().addAll(arrow1, arrow2);
    return hbox;
  }

  protected void addCustomNodes() {
    if (getSkinnable().getPageCount() == 1) {
      NodeUtil.hideNode(controlNav);
    } else {
      NodeUtil.showNode(controlNav);
      NodeUtil.hideNode(pageInformation);
    }

    if (first.getParent() != controlBox) {
      controlBox.getChildren().add(0, first);
      controlBox.getChildren().add(last);
    }
  }

}

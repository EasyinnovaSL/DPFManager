/**
 * <h1>PaginationBetterSkin.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 */
package dpfmanager.shell.interfaces.gui.component.global;

import com.sun.javafx.scene.control.skin.PaginationSkin;
import dpfmanager.shell.core.util.NodeUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

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

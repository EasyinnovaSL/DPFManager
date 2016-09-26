/**
 * <h1>NodeUtil.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.core.util;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
public class NodeUtil {

  public static void hideNode(Node node) {
    node.setVisible(false);
    node.setManaged(false);
  }

  public static void showNode(Node node) {
    node.setVisible(true);
    node.setManaged(true);
  }

  public static void hideAnchor(AnchorPane ap) {
    ap.setVisible(false);
    ap.setManaged(false);
    ap.setMinWidth(0.0);
    ap.setMaxWidth(0.0);
  }

  public static void showAnchor(AnchorPane ap) {
    ap.setVisible(true);
    ap.setManaged(true);
    ap.setMinWidth(AnchorPane.USE_COMPUTED_SIZE);
    ap.setMaxWidth(AnchorPane.USE_COMPUTED_SIZE);
  }

  public static void hideStack(StackPane sp) {
    sp.setVisible(false);
    sp.setManaged(false);
    sp.setMinWidth(0.0);
    sp.setMaxWidth(0.0);
    sp.setMinHeight(0.0);
    sp.setMaxHeight(0.0);
  }

  public static void showStack(StackPane sp) {
    sp.setVisible(true);
    sp.setManaged(true);
    sp.setMinWidth(StackPane.USE_COMPUTED_SIZE);
    sp.setMaxWidth(StackPane.USE_COMPUTED_SIZE);
    sp.setMinHeight(StackPane.USE_COMPUTED_SIZE);
    sp.setMaxHeight(StackPane.USE_COMPUTED_SIZE);
  }

}

package dpfmanager.shell.jacp.core.util;

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

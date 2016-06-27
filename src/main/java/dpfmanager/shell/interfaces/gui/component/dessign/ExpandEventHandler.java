package dpfmanager.shell.interfaces.gui.component.dessign;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.controlsfx.control.CheckTreeView;

/**
 * Created by Adrià Llorens on 23/06/2016.
 */
public class ExpandEventHandler implements EventHandler {

  public static Image folderExpandImage = new Image("images/folder-open.png");

  private CheckTreeView<String> myTreeView;

  public ExpandEventHandler(CheckTreeView<String> treeView){
    myTreeView = treeView;
  }

  @Override
  public void handle(Event event) {
    ObservableList childs;
    if (event.getSource() instanceof FilePathTreeItem){
      FilePathTreeItem source = (FilePathTreeItem) event.getSource();
      childs = source.getChildren();
      // Image
      if (source.isDirectory() && source.isExpanded()) {
        ImageView iv = (ImageView) source.getGraphic();
        iv.setImage(folderExpandImage);
      }
    } else {
      // Root node
      CheckBoxTreeItem root = (CheckBoxTreeItem) event.getSource();
      childs = root.getChildren();
    }

    // Set childs
    for (Object obj : childs){
      FilePathTreeItem item = (FilePathTreeItem) obj;
      item.loadChildren(myTreeView);
    }
  }
}

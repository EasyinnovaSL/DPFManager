package dpfmanager.shell.interfaces.gui.component.dessign;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Adrià Llorens on 23/06/2016.
 */
public class CollapseEventHandler implements EventHandler {

  public static Image folderCollapseImage = new Image("images/folder.png");

  @Override
  public void handle(Event event) {
    if (event.getSource() instanceof FilePathTreeItem) {
      FilePathTreeItem source = (FilePathTreeItem) event.getSource();
      if (source.isDirectory() && !source.isExpanded()) {
        ImageView iv = (ImageView) source.getGraphic();
        iv.setImage(folderCollapseImage);
      }
    }
  }
}

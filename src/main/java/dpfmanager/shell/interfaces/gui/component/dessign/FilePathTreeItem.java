/**
 * <h1>FilePathTreeItem.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.dessign;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.controlsfx.control.CheckTreeView;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathTreeItem extends CheckBoxTreeItem<String> {

  // Images
  public static Image folderCollapseImage = new Image("images/folder.png");
  public static Image fileImage = new Image("images/text-x-generic.png");

  private String fullPath;
  private boolean isDirectory;

  public FilePathTreeItem(Path file) {
    super(file.toString());
    fullPath = file.toString();

    // Test if this is a directory and set the icon
    if (Files.isDirectory(file)) {
      isDirectory = true;
      setGraphic(new ImageView(folderCollapseImage));
    } else {
      isDirectory = false;
      setGraphic(new ImageView(fileImage));
    }

    // Set the value
    if (!fullPath.endsWith(File.separator)) {
      String value = file.toString();
      int indexOf = value.lastIndexOf(File.separator);
      if (indexOf > 0) {
        this.setValue(value.substring(indexOf + 1));
      } else {
        this.setValue(value);
      }
    }
  }

  public void loadChildren(CheckTreeView<String> myTreeView){
    try {
      if (getChildren().size() == 0) {
        DirectoryStream<Path> directories = Files.newDirectoryStream(Paths.get(fullPath));
        for (Path name : directories) {
          FilePathTreeItem treeNode = new FilePathTreeItem(name);
          if (isSelected()) {
            myTreeView.getCheckModel().check(treeNode);
          }
          getChildren().add(treeNode);
        }
      }
    } catch (Exception e){

    }
  }

  public boolean isFullSelected(){
    if (!isDirectory()){
      return isSelected();
    } else {
      if (isSelected()){
        for (TreeItem<String> item : getChildren()) {
          if (item instanceof FilePathTreeItem) {
            FilePathTreeItem fpItem = (FilePathTreeItem) item;
            if (!fpItem.isFullSelected()) {
              return false;
            }
          }
        }
        return true;
      } else {
        return false;
      }
    }
  }

  public boolean isDirectory() {
    return (this.isDirectory);
  }

  public String getFullPath() {
    return fullPath;
  }

  public String getParentPath() {
    return Paths.get(fullPath).getParent().toString();
  }

}

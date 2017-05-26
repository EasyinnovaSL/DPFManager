/**
 * <h1>PeriodicFragment.java</h1> <p> This program is free software: you can redistribute it and/or
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
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.fragment.statics;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.modules.statistics.core.StatisticsObject;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_STATISTICS,
    viewLocation = "/fxml/statics-fragments/statistics.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class StatisticsFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private VBox mainVBoxStatics;

  public void addChild(String title, Node node) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.TOP_LEFT);

    ImageView imageView = new ImageView();
    imageView.setImage(new Image("file:src/main/resources/images/tab-buttons/tab-1-blue.png"));

    Label label = new Label(title);
    label.getStyleClass().add("config-title");

    hbox.getChildren().addAll(imageView, label);
    HBox.setMargin(imageView, new Insets(0, 10, 0, 0));

    ImageView line = new ImageView(new Image("file:src/main/resources/images/line.png"));
    VBox.setMargin(line, new Insets(3,0,5,0));

    mainVBoxStatics.getChildren().addAll(hbox, line, node);
  }

}
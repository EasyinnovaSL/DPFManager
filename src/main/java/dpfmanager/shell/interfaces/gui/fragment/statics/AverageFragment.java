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
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.modules.statistics.core.StatisticsObject;
import dpfmanager.shell.modules.statistics.model.HistogramTag;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_AVERAGE,
    viewLocation = "/fxml/statics-fragments/empty.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class AverageFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private AnchorPane mainPane;

  private VBox vboxPane = new VBox();

  public void init(StatisticsObject so) {


    // Add VBox to view
    mainPane.getChildren().add(vboxPane);
    AnchorPane.setBottomAnchor(vboxPane, 0.0);
    AnchorPane.setLeftAnchor(vboxPane, 0.0);
    AnchorPane.setRightAnchor(vboxPane, 0.0);
    AnchorPane.setTopAnchor(vboxPane, 0.0);
  }

}
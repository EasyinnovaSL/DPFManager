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
import dpfmanager.shell.interfaces.gui.component.statistics.StatisticsView;
import dpfmanager.shell.modules.statistics.model.StatisticsError;
import dpfmanager.shell.modules.statistics.model.StatisticsIsoErrors;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_ERRORS_LIST,
    viewLocation = "/fxml/statics-fragments/errors-list.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class ErrorsListFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private VBox mainVBox;
  @FXML
  private VBox vboxRows;
  @FXML
  private Label labelTitle;

  /** the internal Statistics Error object */
  private StatisticsIsoErrors sIsoErrors;

  public void init(StatisticsIsoErrors s, List<Node> rows) {
    sIsoErrors = s;
    labelTitle.setText(sIsoErrors.name);
    vboxRows.getChildren().addAll(rows);
    hide();
  }

  public void show(){
    NodeUtil.showNode(mainVBox);
  }

  public void hide(){
    NodeUtil.hideNode(mainVBox);
  }

  public void setVisible(boolean show){
    if (show){
      NodeUtil.showNode(mainVBox);
    } else {
      NodeUtil.hideNode(mainVBox);
    }
  }

  public String getId() {
    return sIsoErrors.iso;
  }

}
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
import dpfmanager.shell.interfaces.gui.component.statistics.messages.ShowHideErrorsMessage;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_ISO,
    viewLocation = "/fxml/statics-fragments/iso.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class IsoFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private Label isoName;
  @FXML
  private Label isoN;
  @FXML
  private Label isoErr;
  @FXML
  private Label isoWar;
  @FXML
  private Label isoOk;

  @FXML
  private GridPane gridPercent;
  @FXML
  private StackPane paneErr;
  @FXML
  private StackPane paneWar;
  @FXML
  private StackPane panePas;

  @FXML
  private GridPane gridPane;

  /** the internal Statistics ISO object */
  private StatisticsIso sIso;

  public void init(StatisticsIso s) {
    sIso = s;
    isoName.setText(s.iso);
    isoN.setText(s.count + "");
    isoErr.setText(s.errors + "");
    isoWar.setText(s.warnings + "");
    isoOk.setText(s.passed + "");
    calculatePercentages();
    if (sIso.getIsoErrors().hasErrors()){
      gridPane.getStyleClass().addAll("hoverRow","clickable");
    }
  }

  private void calculatePercentages(){
    ColumnConstraints cErr = gridPercent.getColumnConstraints().get(0);
    ColumnConstraints cWar = gridPercent.getColumnConstraints().get(1);
    ColumnConstraints cPas = gridPercent.getColumnConstraints().get(2);
    Double errPercent = ((sIso.errors * 1.0) / (sIso.count * 1.0)) * 100.0;
    Double warPercent = ((sIso.warnings * 1.0) / (sIso.count * 1.0)) * 100.0;
    Double okPercent = 100.0 - errPercent - warPercent;

    cErr.setPercentWidth(errPercent);
    if (errPercent == 100.0){
      paneErr.getStyleClass().add("round-both");
    } else {
      paneErr.getStyleClass().add("round-left");
    }

    cWar.setPercentWidth(warPercent);
    if (warPercent == 100.0){
      paneWar.getStyleClass().add("round-both");
    } else if (errPercent == 0.0) {
      paneWar.getStyleClass().add("round-left");
    } else if (okPercent == 0.0) {
      paneWar.getStyleClass().add("round-right");
    }

    cPas.setPercentWidth(okPercent);
    if (okPercent == 100.0){
      panePas.getStyleClass().add("round-both");
    } else {
      panePas.getStyleClass().add("round-right");
    }
  }

  @FXML
  protected void onGridPaneClicked(MouseEvent event) throws Exception {
    if (gridPane.getStyleClass().contains("active")){
      // Hide errors list
      context.send(GuiConfig.COMPONENT_STATISTICS, new ShowHideErrorsMessage(ShowHideErrorsMessage.Type.HIDE, sIso.id));
    } else {
      // Show errors list
      context.send(GuiConfig.COMPONENT_STATISTICS, new ShowHideErrorsMessage(ShowHideErrorsMessage.Type.SHOW, sIso.id));
    }
  }

  public void setSelected(boolean selected){
    if (selected){
      gridPane.getStyleClass().add("active");
    } else {
      gridPane.getStyleClass().remove("active");
    }
  }

  public String getId() {
    return sIso.id;
  }

}
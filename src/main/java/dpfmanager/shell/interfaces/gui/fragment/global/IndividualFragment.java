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

package dpfmanager.shell.interfaces.gui.fragment.global;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.report.util.ReportIndividualGui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import org.apache.commons.io.FileUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_INDIVIDUAL,
    viewLocation = "/fxml/fragments/individual.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class IndividualFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private Label input;
  @FXML
  private Label nfiles;
  @FXML
  private Label errors;
  @FXML
  private Label warnings;
  @FXML
  private Label passed;
  @FXML
  private HBox scoreBox;
  @FXML
  private HBox formatsBox;

  /* Report Row info */
  private ReportIndividualGui info;

  public void init(ReportIndividualGui r) {
    info = r;
    loadReportRow();
  }

  public void updateIcons(){
    info.readFormats();
    formatsBox.getChildren().clear();
    addFormatIcons(info.getFormats(), info.getReportVersion());
  }

  private void loadReportRow() {
    info.load();
    input.setText(info.getFilename());
    String isotext = bundle.getString("oneIso").replace("%1", info.getSelectedIsos() + "");
    if (info.getSelectedIsos() > 1) {
      isotext = bundle.getString("multipleIso").replace("%1", info.getSelectedIsos() + "");
    }
    nfiles.setText(isotext);
    errors.setText(bundle.getString("errors").replace("%1", info.getErrors() + ""));
    warnings.setText(bundle.getString("warnings").replace("%1", "" + info.getWarnings() + ""));
    passed.setText(bundle.getString("passed").replace("%1", "" + info.getPassed() + ""));
    addChartScore(info.getScore());
    addFormatIcons(info.getFormats(), info.getReportVersion());
  }

  private void addChartScore(Integer scoreInt) {
    Double score = scoreInt * 1.0;

    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Correct", score),
            new PieChart.Data("Error", 100 - score));

    PieChart chart = new PieChart(pieChartData);
    chart.setId("pie_chart");
    chart.setMinSize(22, 22);
    chart.setMaxSize(22, 22);

    Label score_label = new Label(score + "%");
    score_label.setTextFill(Color.LIGHTGRAY);

    scoreBox.getChildren().add(chart);
    scoreBox.getChildren().add(score_label);
  }

  private void addFormatIcons(Map<String, String> itemRead, Integer version) {
    List<String> sortedFormats = Arrays.asList("html","pdf","xml","mets", "json");
    Map<String, String> item = new HashMap<>();
    if (version > 0) {
      // Transform reports
      for (String format : sortedFormats){
        if (!item.containsKey(format)) item.put(format, (itemRead.containsKey(format)) ? itemRead.get(format) : null);
      }
    } else {
      item = itemRead;
    }
    for (String i : sortedFormats) {
      if (!item.containsKey(i)) continue;
      ImageView icon = new ImageView();
      icon.setId("but" + i);
      icon.setFitHeight(20);
      icon.setFitWidth(20);
      icon.setCursor(Cursor.HAND);
      icon.setImage(new Image("images/formats/" + i + ".png"));
      Tooltip.install(icon, new Tooltip(i.toUpperCase()));

      String path = item.get(i);
      ShowMessage sMessage = null;
      if (path != null && new File(path).exists()){
        // Show directly
        sMessage = new ShowMessage(i, path);
      } else if (version > 1){
        // Transformation need
        icon.setOpacity(0.4);
        icon.setOnMouseEntered(event -> icon.setOpacity(1.0));
        icon.setOnMouseExited(event -> icon.setOpacity(0.4));
//        Long formatUuid = Long.parseLong(info.getUuid()+Character.getNumericValue(i.charAt(0)));
//        sMessage = new ShowMessage(formatUuid, i, gr, info.getInternalReportFolder());
      }
      if (sMessage != null){
        final ShowMessage finalSMessage = sMessage;
        icon.setOnMouseClicked(event -> {
          ArrayMessage am = new ArrayMessage();
          am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
          am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, finalSMessage);
          context.send(GuiConfig.PERSPECTIVE_SHOW, am);
        });

        formatsBox.getChildren().add(icon);
      }
    }
  }

}
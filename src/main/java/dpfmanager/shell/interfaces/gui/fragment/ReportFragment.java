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

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.interfaces.gui.component.statistics.messages.ShowHideErrorsMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.util.ReportGui;
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
@Fragment(id = GuiConfig.FRAGMENT_REPORT,
    viewLocation = "/fxml/fragments/report.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class ReportFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private Label date;
  @FXML
  private ImageView okImage;
  @FXML
  private ImageView koImage;
  @FXML
  private Label files;
  @FXML
  private Label input;
  @FXML
  private Label type;
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
  @FXML
  private HBox actionsBox;

  /* Report Row info */
  private ReportGui info;

  public void init(ReportGui reportRow) {
    info = reportRow;
    loadReportRow();
  }

  public void updateIcons(){
  }

  private void loadReportRow() {
    info.load();
    date.setText(info.getDate() + " " + info.getTime());
    files.setText(info.getNfiles() + "");
    input.setText(info.getInput());
    errors.setText(bundle.getString("errors").replace("%1", info.getErrors() + ""));
    if (info.getErrors() == 0){
      NodeUtil.showNode(okImage);
      NodeUtil.hideNode(koImage);
    } else {
      NodeUtil.hideNode(okImage);
      NodeUtil.showNode(koImage);
    }
    if (info.getGlobalReport().getConfig().isQuick()) {
      type.setText(bundle.getString("typeQuick"));
      warnings.setText("");
    } else {
      type.setText(bundle.getString("typeFull"));
      warnings.setText(bundle.getString("warnings").replace("%1", "" + info.getWarnings() + ""));
    }
    passed.setText(bundle.getString("passed").replace("%1", "" + info.getPassed() + ""));
    addChartScore(info.getScore());
    addActionsIcons(info.getDelete());
    addLastItem(info.isLast());
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

    Label score_label = new Label(score.intValue() + "%");
    score_label.setTextFill(Color.LIGHTGRAY);

    scoreBox.getChildren().add(chart);
    scoreBox.getChildren().add(score_label);
  }

  public void addActionsIcons(String item) {
    String path = info.getDeletePath();

    // Open folder button
    Button iconFolder = new Button();
    iconFolder.setMinHeight(20);
    iconFolder.setPrefHeight(20);
    iconFolder.setMaxHeight(20);
    iconFolder.setMinWidth(20);
    iconFolder.setPrefWidth(20);
    iconFolder.setMaxWidth(20);
    iconFolder.getStyleClass().addAll("folder-img", "periodic-img");
    iconFolder.setCursor(Cursor.HAND);
    iconFolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // Open folder
        File file = new File(path);
        File dir = new File(file.getParent());
        if (Desktop.isDesktopSupported()) {
          new Thread(() -> {
            try {
              Desktop.getDesktop().open(dir);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }).start();
        }
      }
    });
    actionsBox.getChildren().add(iconFolder);

    // Trash button
    Button icon = new Button();
    icon.setMinHeight(20);
    icon.setPrefHeight(20);
    icon.setMaxHeight(20);
    icon.setMinWidth(20);
    icon.setPrefWidth(20);
    icon.setMaxWidth(20);
    icon.getStyleClass().addAll("delete-img", "periodic-img");
    icon.setCursor(Cursor.HAND);
    icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // Send action to controller
        context.send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.DELETE, getUuid()));
        // Delete report
        File file = new File(path);
        File dir = new File(file.getParent());
        try {
          FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
          e.printStackTrace();
        }

        // TODO
//        getModel().removeItem(item);
      }
    });
    actionsBox.getChildren().add(icon);
  }

  public void addLastItem(boolean isLast){
    if (isLast){
      StackPane stack = new StackPane();
      stack.setMaxWidth(0.0);
      stack.setMaxHeight(0.0);
      stack.setId("lastReportRow");
      actionsBox.getChildren().add(stack);
    }
  }

  @FXML
  protected void onGridPaneClicked(MouseEvent event) throws Exception {
    context.send(GuiConfig.PERSPECTIVE_GLOBAL, new UiMessage(UiMessage.Type.SHOW));
    context.send(GuiConfig.PERSPECTIVE_GLOBAL + "." + GuiConfig.COMPONENT_GLOBAL, new GuiGlobalMessage(GuiGlobalMessage.Type.INIT, info));
  }

  public String getUuid() {
    return info.getUuid();
  }

  public ReportGui getInfo() {
    return info;
  }
}
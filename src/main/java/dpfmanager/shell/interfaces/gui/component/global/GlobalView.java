/**
 * <h1>ReportsView.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.interfaces.gui.component.global;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.interfaces.gui.fragment.ReportFragment;
import dpfmanager.shell.interfaces.gui.fragment.global.IndividualFragment;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.report.util.ReportIndividualGui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.apache.commons.io.FileUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
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
 * Created by Adria Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_GLOBAL,
    name = GuiConfig.COMPONENT_GLOBAL,
    viewLocation = "/fxml/global.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_GLOBAL)
public class GlobalView extends DpfView<GlobalModel, GlobalController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  /**
   * Global report elements
   */
  @FXML
  private VBox globalVBox;
  @FXML
  private Label globalDate;
  @FXML
  private Label globalTime;
  @FXML
  private Label globalFiles;
  @FXML
  private Label globalErrors;
  @FXML
  private Label globalWarnings;
  @FXML
  private Label globalPassed;
  @FXML
  private HBox globalScoreBox;
  @FXML
  private HBox globalFormatsBox;
  @FXML
  private HBox globalActionsBox;

  /**
   * Individual reports elements
   */
//  @FXML
//  private ProgressIndicator indicator;
  @FXML
  private VBox individualsVBox;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  public Context getContext() {
    return context;
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(GuiGlobalMessage.class)){
      GuiGlobalMessage gMessage = message.getTypedMessage(GuiGlobalMessage.class);
      if (gMessage.isInit()) {
        getController().readIndividualReports(gMessage.getReportGui().getInternalReportFolder());
      } else if (gMessage.isAddIndividual()) {
        gMessage.getReportIndividualGui().load();
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(GuiGlobalMessage.class)){
      GuiGlobalMessage gMessage = message.getTypedMessage(GuiGlobalMessage.class);
      if (gMessage.isInit()) {
        initGlobalReport(gMessage.getReportGui());
        initIndividualsReports();
      } else if (gMessage.isAddIndividual()) {
        addIndividualReport(gMessage.getReportIndividualGui());
      }
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new GlobalModel(context));
    setController(new GlobalController());
    getModel().setResourcebundle(bundle);
  }

  /**
   * Global report
   */
  private void initGlobalReport(ReportGui info) {
    globalDate.setText(info.getDate());
    globalTime.setText(info.getTime());
    globalFiles.setText(info.getNfiles() + "");
    globalErrors.setText(bundle.getString("errors").replace("%1", info.getErrors() + ""));
    globalWarnings.setText(bundle.getString("warnings").replace("%1", "" + info.getWarnings() + ""));
    globalPassed.setText(bundle.getString("passed").replace("%1", "" + info.getPassed() + ""));
    addChartScore(info);
    addFormatIcons(info);
    addActionsIcons(info);
  }

  private void addChartScore(ReportGui info) {
    Double score = info.getScore() * 1.0;

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

    globalScoreBox.getChildren().clear();
    globalScoreBox.getChildren().add(chart);
    globalScoreBox.getChildren().add(score_label);
  }

  private void addFormatIcons(ReportGui info) {
    Map<String, String> itemRead = info.getFormats();
    Integer version = info.getReportVersion();
    GlobalReport gr = info.getGlobalReport();
    globalFormatsBox.getChildren().clear();
    List<String> sortedFormats = Arrays.asList("html", "pdf", "xml", "mets", "json");
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
      } else if (gr.getVersion() > 1){
        // Transformation need
        icon.setOpacity(0.4);
        icon.setOnMouseEntered(event -> icon.setOpacity(1.0));
        icon.setOnMouseExited(event -> icon.setOpacity(0.4));
        Long formatUuid = Long.parseLong(info.getUuid()+Character.getNumericValue(i.charAt(0)));
        sMessage = new ShowMessage(formatUuid, i, gr, info.getInternalReportFolder(), true);
      }
      if (sMessage != null){
        final ShowMessage finalSMessage = sMessage;
        icon.setOnMouseClicked(event -> {
          ArrayMessage am = new ArrayMessage();
          am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
          am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, finalSMessage);
          context.send(GuiConfig.PERSPECTIVE_SHOW, am);
        });

//        ContextMenu contextMenu = new ContextMenu();
//        javafx.scene.control.MenuItem download = new javafx.scene.control.MenuItem("Download report");
//        contextMenu.getItems().add(download);
//        icon.setOnContextMenuRequested(e -> contextMenu.show(icon, e.getScreenX(), e.getScreenY()));
        globalFormatsBox.getChildren().add(icon);
      }
    }
  }

  public void addActionsIcons(ReportGui info) {
    globalActionsBox.getChildren().clear();
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
    globalActionsBox.getChildren().add(iconFolder);

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
        context.send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.DELETE, info.getUuid()));
        // Delete report
        File file = new File(path);
        File dir = new File(file.getParent());
        try {
          FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    globalActionsBox.getChildren().add(icon);
  }

  /**
   * Individual reports
   */

  private void initIndividualsReports(){
    individualsVBox.getChildren().clear();
    getController().loadAndPrintIndividuals();
  }

  private void addIndividualReport(ReportIndividualGui rig){
    ManagedFragmentHandler<IndividualFragment> handler = context.getManagedFragmentHandler(IndividualFragment.class);
    handler.getController().init(rig);
    individualsVBox.getChildren().add(handler.getFragmentNode());
  }

}

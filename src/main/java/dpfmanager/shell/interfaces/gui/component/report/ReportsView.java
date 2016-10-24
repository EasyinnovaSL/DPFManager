/**
 * <h1>ReportsView.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.modules.report.util.ReportRow;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.PropertySheet;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_REPORTS,
    name = GuiConfig.COMPONENT_REPORTS,
    viewLocation = "/fxml/summary.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_REPORTS)
public class ReportsView extends DpfView<ReportsModel, ReportsController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  // View elements
  @FXML
  private VBox reportsVbox;
  @FXML
  private Button loadMore;
  @FXML
  private TableView<ReportRow> tabReports;
  @FXML
  private Label labelEmpty;
  @FXML
  private ProgressIndicator indicator;

  private TableColumn<ReportRow, String> colScore;
  private TableColumn colFormats;
  private TableColumn colDelete;

  private int prefWidth = 840;
  private int prefHeight = 470;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(ReportsMessage.class)) {
      ReportsMessage rMessage = message.getTypedMessage(ReportsMessage.class);
      if (rMessage.isRead()) {
        getModel().setReload(true);
        readData();
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(ReportsMessage.class)) {
      ReportsMessage rMessage = message.getTypedMessage(ReportsMessage.class);
      if (rMessage.isReload()) {
        showLoading();
        context.send(new ReportsMessage(ReportsMessage.Type.READ));
      } else if (rMessage.isRead()) {
        addData();
        hideLoading();
      }
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new ReportsModel());
    setController(new ReportsController());
    getModel().setResourcebundle(bundle);
    addHeaders();
  }

  private void addHeaders() {
    TableColumn colDate = new TableColumn(bundle.getString("colDate"));
    setMinMaxWidth(colDate, 75);
    colDate.setCellValueFactory(new PropertyValueFactory<ReportRow, String>("date"));

    TableColumn colTime = new TableColumn(bundle.getString("colTime"));
    setMinMaxWidth(colTime, 75);
    colTime.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("time")
    );

    TableColumn colN = new TableColumn(bundle.getString("colN"));
    setMinMaxWidth(colN, 70);
    colN.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("nfiles")
    );

    TableColumn colFile = new TableColumn(bundle.getString("colFile"));
    setMinMaxWidth(colFile, 160);
    colFile.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("input")
    );

    TableColumn colErrors = new TableColumn(bundle.getString("colErrors"));
    setMinMaxWidth(colErrors, 65);
    colErrors.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("errors")
    );

    TableColumn colWarnings = new TableColumn(bundle.getString("colWarnings"));
    setMinMaxWidth(colWarnings, 85);
    colWarnings.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("warnings")
    );

    TableColumn colPassed = new TableColumn(bundle.getString("colPassed"));
    setMinMaxWidth(colPassed, 65);
    colPassed.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("passed")
    );

    colScore = new TableColumn(bundle.getString("colScore"));
    setMinMaxWidth(colScore, 75);
    colScore.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("score")
    );

    colFormats = new TableColumn(bundle.getString("colFormats"));
    setMinMaxWidth(colFormats, 120);
    colFormats.setCellValueFactory(
        new PropertyValueFactory<ReportRow, ObservableMap<String, String>>("formats")
    );

    colDelete = new TableColumn(bundle.getString("colDelete"));
    setMinMaxWidth(colDelete, 30);
    colDelete.setVisible(true);
    colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

    tabReports.skinProperty().addListener((obs, oldSkin, newSkin) -> {
      TableHeaderRow header = (TableHeaderRow) tabReports.lookup("TableHeaderRow");
      header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
    });

    tabReports.setPrefWidth(840.0);
    tabReports.setFixedCellSize(28.0);
    tabReports.getColumns().addAll(colDate, colTime, colN, colFile, colErrors, colWarnings, colPassed, colScore, colFormats, colDelete);
    tabReports.setCursor(Cursor.DEFAULT);
    tabReports.setEditable(false);
    tabReports.setMaxHeight(470.0);

    // Change column colors
    changeColumnTextColor(colDate, Color.LIGHTGRAY);
    changeColumnTextColor(colTime, Color.LIGHTGRAY);
    changeColumnTextColor(colFile, Color.LIGHTGRAY);
    changeColumnTextColor(colN, Color.CYAN);
    changeColumnTextColor(colErrors, Color.RED);
    changeColumnTextColor(colWarnings, Color.ORANGE);
    changeColumnTextColor(colPassed, Color.GREENYELLOW);
    changeColumnTextColor(colScore, Color.LIGHTGRAY);
  }

  private void setMinMaxWidth(TableColumn column, int width) {
    column.setMinWidth(width);
    column.setPrefWidth(width);
    column.setMaxWidth(width);
    column.setResizable(false);
    column.setSortable(false);
    column.setEditable(false);
  }

  private void readData() {
    // Read data
    getModel().readIfNeed();
  }

  private void addData() {
    // Add data
    ObservableList<ReportRow> data = getModel().getData();
    tabReports.setItems(data);

    // Resize
    resizeTable();
    Bindings.size(tabReports.getItems()).addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        resizeTable();
      }
    });

    addChartScore();
    addFormatIcons();
    addDeleteIcon();
  }

  private void resizeTable() {
    double height = tabReports.getFixedCellSize() * tabReports.getItems().size() + tabReports.getFixedCellSize();
    if (height > 470) {
      height = 470.0;
    }
    tabReports.setMinHeight(height);
    tabReports.setPrefHeight(height);
  }

  private void changeColumnTextColor(TableColumn column, Color color) {
    column.setCellFactory(new Callback<TableColumn, TableCell>() {
      public TableCell call(TableColumn param) {
        return new TableCell<ReportRow, String>() {
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
              this.setTextFill(color);
              setText(item);
            }
          }
        };
      }
    });
  }

  public void addChartScore() {
    colScore.setCellFactory(new Callback<TableColumn<ReportRow, String>, TableCell<ReportRow, String>>() {
      @Override
      public TableCell<ReportRow, String> call(TableColumn<ReportRow, String> param) {
        TableCell<ReportRow, String> cell = new TableCell<ReportRow, String>() {
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty && item != null) {

              Double score = item.indexOf("%") < 0 || item.indexOf("?") >= 0 ? 0 : Double.parseDouble(item.substring(0, item.indexOf('%')));

              ObservableList<PieChart.Data> pieChartData =
                  FXCollections.observableArrayList(
                      new PieChart.Data("Correct", score),
                      new PieChart.Data("Error", 100 - score));

              PieChart chart = new PieChart(pieChartData);
              chart.setId("pie_chart");
              chart.setMinSize(22, 22);
              chart.setMaxSize(22, 22);

              HBox box = new HBox();
              box.setSpacing(8);
              box.setAlignment(Pos.CENTER_LEFT);

              Label score_label = new Label(item);
              score_label.setTextFill(Color.LIGHTGRAY);

              box.getChildren().add(chart);
              box.getChildren().add(score_label);

              setGraphic(box);
            } else {
              setGraphic(null);
            }
          }
        };
        return cell;
      }
    });
  }

  public void addFormatIcons() {
    colFormats.setCellFactory(new Callback<TableColumn<ReportRow, ObservableMap<String, String>>, TableCell<ReportRow, ObservableMap<String, String>>>() {
      @Override
      public TableCell<ReportRow, ObservableMap<String, String>> call(TableColumn<ReportRow, ObservableMap<String, String>> param) {
        TableCell<ReportRow, ObservableMap<String, String>> cell = new TableCell<ReportRow, ObservableMap<String, String>>() {
          @Override
          public void updateItem(ObservableMap<String, String> item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty && item != null) {

              HBox box = new HBox();
              box.setSpacing(3);
              box.setAlignment(Pos.CENTER_LEFT);

              for (String i : item.keySet()) {
                ImageView icon = new ImageView();
                icon.setId("but" + i);
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                icon.setImage(new Image("images/formats/" + i + ".png"));
                icon.setCursor(Cursor.HAND);

                String type = i;
                String path = item.get(i);
                icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                    ArrayMessage am = new ArrayMessage();
                    am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
                    am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
                    getContext().send(GuiConfig.PERSPECTIVE_SHOW, am);
                  }
                });

                ContextMenu contextMenu = new ContextMenu();
                javafx.scene.control.MenuItem download = new javafx.scene.control.MenuItem("Download report");
                contextMenu.getItems().add(download);
                icon.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                  public void handle(ContextMenuEvent e) {
                    contextMenu.show(icon, e.getScreenX(), e.getScreenY());
                  }
                });
                box.getChildren().add(icon);
              }

              setGraphic(box);
            } else {
              setGraphic(null);
            }
          }
        };
        return cell;
      }
    });
  }

  public void addDeleteIcon() {
    colDelete.setCellFactory(new Callback<TableColumn<ReportRow, String>, TableCell<ReportRow, String>>() {
      @Override
      public TableCell<ReportRow, String> call(TableColumn<ReportRow, String> param) {
        TableCell<ReportRow, String> cell = new TableCell<ReportRow, String>() {
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            HBox box = new HBox();
            box.setSpacing(3);
            box.setAlignment(Pos.CENTER_LEFT);

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
                // Delete report
                File file = new File(item);
                File dir = new File(file.getParent());
                try {
                  FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                  e.printStackTrace();
                }

                final ObservableList items = getTableView().getItems();
                if( items != null && items.size() > 0) {
                  Object item = items.get(getTableRow().getIndex());
                  items.remove(item);
                }
              }
            });

            box.getChildren().add(icon);
            setGraphic(box);
          }
        };
        return cell;
      }
    });
  }

  void refreshTable(TableView tableView) {
    final ObservableList items = tableView.getItems();
    if( items == null || items.size() == 0) return;

    Object item = tableView.getItems().get(0);
    items.remove(item);
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        items.add(0, item);
      }
    });
  }

  private void showLoading() {
    indicator.setProgress(-1.0);
    NodeUtil.showNode(indicator);
    NodeUtil.hideNode(tabReports);
    NodeUtil.hideNode(labelEmpty);
    NodeUtil.hideNode(loadMore);
  }

  private void hideLoading() {
    NodeUtil.hideNode(indicator);
    NodeUtil.showNode(tabReports);

    if (getModel().isEmpty()) {
      NodeUtil.hideNode(loadMore);
      NodeUtil.showNode(labelEmpty);
    } else if (getModel().isAllReportsLoaded()) {
      NodeUtil.hideNode(labelEmpty);
      NodeUtil.hideNode(loadMore);
    } else {
      NodeUtil.showNode(loadMore);
      NodeUtil.hideNode(labelEmpty);
    }
  }

  @FXML
  protected void loadMoreReports(ActionEvent event) throws Exception {
    getController().loadMoreReports();
  }

  public void hideLoadMore() {
    NodeUtil.hideNode(loadMore);
  }

  public Context getContext() {
    return context;
  }

}

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_REPORTS,
    name = GuiConfig.COMPONENT_REPORTS,
    viewLocation = "/fxml/summary.fxml",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_REPORTS)
public class ReportsView extends DpfView<ReportsModel, ReportsController> {

  @Resource
  private Context context;

  // View elements
  @FXML
  private Button loadMore;
  @FXML
  private TableView<ReportRow> tabReports;

  private TableColumn<ReportRow, String> colScore;
  private TableColumn colFormats;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message instanceof ReportsMessage) {
      ReportsMessage rMessage = (ReportsMessage) message;
      if (rMessage.isReload()) {
        getModel().setReload(true);
      } else if (rMessage.isShow()){
        readData();
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message instanceof ReportsMessage) {
      ReportsMessage rMessage = (ReportsMessage) message;
      if (rMessage.isShow()){
        addData();
      }
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new ReportsModel());
    setController(new ReportsController());
    addHeaders();
  }

  private void addHeaders() {
    TableColumn colDate = new TableColumn("Date");
    setMinMaxWidth(colDate, 85);
    colDate.setCellValueFactory(new PropertyValueFactory<ReportRow, String>("date"));

    TableColumn colTime = new TableColumn("Time");
    setMinMaxWidth(colTime, 75);
    colTime.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("time")
    );

    TableColumn colN = new TableColumn("#Files");
    setMinMaxWidth(colN, 50);
    colN.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("nfiles")
    );

    TableColumn colFile = new TableColumn("Input");
    setMinMaxWidth(colFile, 200);
    colFile.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("input")
    );

    TableColumn colErrors = new TableColumn("Errors");
    setMinMaxWidth(colErrors, 65);
    colErrors.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("errors")
    );

    TableColumn colWarnings = new TableColumn("Warnings");
    setMinMaxWidth(colWarnings, 65);
    colWarnings.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("warnings")
    );

    TableColumn colPassed = new TableColumn("Passed");
    setMinMaxWidth(colPassed, 65);
    colPassed.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("passed")
    );

    colScore = new TableColumn("Score");
    setMinMaxWidth(colScore, 75);
    colScore.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("score")
    );

    colFormats = new TableColumn("Formats");
    setMinMaxWidth(colFormats, 150);
    colFormats.setCellValueFactory(
        new PropertyValueFactory<ReportRow, ObservableMap<String, String>>("formats")
    );

    tabReports.getColumns().addAll(colDate, colTime, colN, colFile, colErrors, colWarnings, colPassed, colScore, colFormats);
    tabReports.setPrefHeight(470.0);
    tabReports.setMinHeight(470.0);
    tabReports.setPrefWidth(840.0);
    tabReports.setCursor(Cursor.DEFAULT);
    tabReports.setEditable(false);

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

  private void setMinMaxWidth(TableColumn column, int width){
    column.setMinWidth(width);
    column.setPrefWidth(width);
    column.setMaxWidth(width);
  }

  private void readData() {
    // Read data
    getModel().readIfNeed();
  }

  private void addData() {
    // Add data
    ObservableList<ReportRow> data = getModel().getData();
    tabReports.setItems(data);
    addChartScore();
    addFormatIcons();

    if (getModel().isAllReportsLoaded()) {
      NodeUtil.hideNode(loadMore);
    }
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
                icon.setImage(new Image("images/format_" + i + ".png"));
                icon.setCursor(Cursor.HAND);

                String type = i;
                String path = item.get(i);
                icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                    ArrayMessage am = new ArrayMessage();
                    am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
                    am.add(GuiConfig.PERSPECTIVE_SHOW+"."+GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
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

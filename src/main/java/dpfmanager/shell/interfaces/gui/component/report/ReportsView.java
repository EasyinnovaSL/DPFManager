package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.modules.report.ReportRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_REPORTS,
    name = GuiConfig.COMPONENT_REPORTS,
    viewLocation = "/fxml/summary.fxml",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_REPORTS)
public class ReportsView extends DpfView<ReportsModel, ReportsController> implements FXComponent {

  @Resource
  private Context context;

  // View elements
  @FXML
  private Button loadMore;
  @FXML
  private TableView<ReportRow> tabReports;
  @FXML
  private VBox reportsVbox;
  @FXML
  private TextArea textArea;
  @FXML
  private WebView webView;

  private TableColumn<ReportRow, String> colScore;
  private TableColumn colFormats;
  private ScrollPane scrollPane = null;
  private boolean firsttime = true;

  private ChangeListener<Number> listenerHeigth;
  final private double DEFAULT_SCROLL_WIDTH = 970;

  @Override
  public Node handle(Message<Event, Object> message) {
    if (message.getMessageBody() instanceof ReportsMessage) {
      ReportsMessage rMessage = (ReportsMessage) message.getMessageBody();
      if (rMessage.isTable()) {
        showReports();
        addData();
      } else if (rMessage.isReport()) {
        getModel().setReload(true);
        showSingleReport(rMessage.getReportType(), rMessage.getPath());
      } else if (rMessage.isScroll()) {
        scrollPane = rMessage.getScrollPane();
        attachListeners();
      }
    }
    return null;
  }

  @Override
  public Node postHandle(Node node, Message<Event, Object> message) {
    if (message.getMessageBody() instanceof ReportsMessage) {
      ReportsMessage rMessage = (ReportsMessage) message.getMessageBody();
      if (rMessage.isReport()) {
        if (rMessage.getReportType().equals("html")) {
          showWebView(rMessage.getPath());
        }
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
    getController().initButtonsHandlers();
  }

  private void attachListeners() {
    //Declare listeners
    listenerHeigth = new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        webView.setMinHeight((Double) newValue);
        webView.setPrefHeight((Double) newValue);
        webView.setMaxHeight((Double) newValue);
        textArea.setMinHeight((Double) newValue);
        textArea.setPrefHeight((Double) newValue);
        textArea.setMaxHeight((Double) newValue);
      }
    };
    scrollPane.heightProperty().addListener(listenerHeigth);
  }

  private void addHeaders() {
    TableColumn colDate = new TableColumn("Date");
    colDate.setMinWidth(85);
    colDate.setCellValueFactory(new PropertyValueFactory<ReportRow, String>("date"));

    TableColumn colTime = new TableColumn("Time");
    colTime.setMinWidth(75);
    colTime.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("time")
    );

    TableColumn colFile = new TableColumn("Input");
    colFile.setMinWidth(200);
    colFile.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("input")
    );

    TableColumn colN = new TableColumn("#Files");
    colN.setMinWidth(50);
    colN.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("nfiles")
    );

    TableColumn colErrors = new TableColumn("Errors");
    colErrors.setMinWidth(60);
    colErrors.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("errors")
    );

    TableColumn colWarnings = new TableColumn("Warnings");
    colWarnings.setMinWidth(60);
    colWarnings.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("warnings")
    );

    TableColumn colPassed = new TableColumn("Passed");
    colPassed.setMinWidth(60);
    colPassed.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("passed")
    );

    colScore = new TableColumn("Score");
    colScore.setMinWidth(80);
    colScore.setCellValueFactory(
        new PropertyValueFactory("score")
    );

    colFormats = new TableColumn("Formats");
    colFormats.setMinWidth(150);
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

  public void addData() {
    // Add data
    getModel().readIfNeed();
    ObservableList<ReportRow> data = getModel().getData();
    tabReports.setItems(data);
    addChartScore();
    addFormatIcons();

    if (getModel().isAllReportsLoaded()) {
      loadMore.setVisible(false);
      loadMore.setManaged(false);
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
              setText(null);

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
                    getContext().send(new ReportsMessage(ReportsMessage.Type.REPORT, type, path));
                  }
                });

                ContextMenu contextMenu = new ContextMenu();
                javafx.scene.control.MenuItem download = new javafx.scene.control.MenuItem("Download report");
//                download.setOnAction(new EventHandler<ActionEvent>() {
//                  @Override
//                  public void handle(ActionEvent event) {
//                    FileChooser fileChooser = new FileChooser();
//                    fileChooser.setTitle("Save Report");
//                    fileChooser.setInitialFileName(new File(item.get(i)).getName());
//                    File file = fileChooser.showSaveDialog(getLocalFacade().getGlobalFacade().getApplication().getStage());
//                    if (file != null) {
//                      try {
//                        Files.copy(Paths.get(new File(item.get(i)).getAbsolutePath()), Paths.get(file.getAbsolutePath()));
//                      } catch (Exception ex) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Error");
//                        alert.setHeaderText("An error ocurred");
//                        alert.setContentText("There was an error while saving the report file");
//                        alert.showAndWait();
//                      }
//                    }
//                  }
//                });
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

  private void showSingleReport(String type, String path) {
    System.out.println("Showing report...");
    switch (type) {
      case "xml":
        showTextArea(getContent(path));
        break;
      case "json":
        String content = getContent(path);
        content = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(content));
        showTextArea(content);
        break;
      case "pdf":
        try {
          Desktop.getDesktop().open(new File(path));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      default:
        break;
    }
  }

  private String getContent(String path) {
    String content = "";
    try {
      BufferedReader input = new BufferedReader(new FileReader(path));
      try {
        String line;
        while ((line = input.readLine()) != null) {
          if (!content.equals("")) {
            content += "\n";
          }
          content += line;
        }
      } finally {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return content;
  }

  /**
   * Show Hide
   */

  public void showTextArea(String content) {
    showHideChilds(false);

    textArea.setText(content);
    textArea.setVisible(true);
    textArea.setManaged(true);
    textArea.setPrefHeight(reportsVbox.getHeight());
  }

  public void hideTextArea() {
    textArea.setVisible(false);
    textArea.setManaged(false);
    textArea.clear();
  }

  public void showWebView(String path) {
    showHideChilds(false);

    webView.getEngine().load("file:///" + path.replace("\\", "/"));
    NodeUtil.showNode(webView);
    if (scrollPane != null) {
      resizeScrollPane();
    }
  }

  private void resizeScrollPane(){
    webView.setPrefHeight(scrollPane.getHeight());
    webView.setPrefWidth(scrollPane.getWidth());
    scrollPane.setMaxWidth(Double.MAX_VALUE);
  }

  public void hideWebView() {
    NodeUtil.hideNode(webView);
  }

  public void showReports() {
    showHideChilds(true);
    hideTextArea();
    hideWebView();
    if (scrollPane != null) {
      scrollPane.setMaxWidth(DEFAULT_SCROLL_WIDTH);
    }
  }

  private void showHideChilds(boolean visible) {
    for (Node child : reportsVbox.getChildren()) {
      child.setVisible(visible);
      child.setManaged(visible);
    }
  }

  /** Getters */

  public Button getLoadMore(){
    return loadMore;
  }

  public Context getContext(){
    return context;
  }

}

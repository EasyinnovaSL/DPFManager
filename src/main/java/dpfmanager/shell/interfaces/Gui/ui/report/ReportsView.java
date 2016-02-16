package dpfmanager.shell.interfaces.Gui.ui.report;

import dpfmanager.shell.interfaces.Gui.ui.main.MainModel;
import dpfmanager.shell.reporting.ReportRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import org.jrebirth.af.api.resource.fxml.FXMLItem;
import org.jrebirth.af.core.resource.Resources;
import org.jrebirth.af.core.resource.fxml.FXML;
import org.jrebirth.af.core.ui.DefaultView;

/**
 * Created by Adrià Llorens on 02/02/2016.
 */
public class ReportsView extends DefaultView<ReportsModel, ScrollPane, ReportsController> {

  private static FXMLItem ROOT_EMBEDDED_FXML = Resources.create(new FXML("fxml-jr", "summary"));

  // View elements
  private Node rootNode;
  private ScrollPane scrollPane;
  private Button loadMore;
  private TableView<ReportRow> tabReports;
  private VBox reportsVbox;
  private WebView webView = null;
  private TextArea textArea;
  private AnchorPane anchorPane;
  private WebEngine webEngine;

  private TableColumn<ReportRow, String> colScore;
  private TableColumn colFormats;

  private ChangeListener<Number> listenerWidth;
  private ChangeListener<Number> listenerHeigth;

  final private double DEFAULT_SCROLL_WIDTH = 970;

  public ReportsView(final ReportsModel model) {
    super(model);
  }

  @Override
  protected void initView() {
    // Save the buttons
    rootNode = getFXMLNode();
    loadMore = (Button) rootNode.lookup("#button_load");
    reportsVbox = (VBox) rootNode.lookup("#reportsVbox");
    textArea = (TextArea) rootNode.lookup("#textArea");
    textArea.setEditable(false);
    tabReports = (TableView<ReportRow>) rootNode.lookup("#tab_reports");
    anchorPane = (AnchorPane) rootNode.lookup("#pane1");

    //Declare listeners
    listenerHeigth = new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        webView.setPrefHeight((Double) newValue);
        anchorPane.setPrefHeight((Double) newValue);
      }
    };
    listenerWidth = new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        webView.setPrefWidth((Double) newValue);
        anchorPane.setPrefWidth((Double) newValue);
      }
    };
  }

  @Override
  public void start() {
    try {
      GridPane grid = new GridPane();
      grid.getStyleClass().add("background-main");
      grid.setAlignment(Pos.TOP_CENTER);
      grid.getChildren().add(rootNode);

      scrollPane = getRootNode();
      scrollPane.setFitToHeight(true);
      scrollPane.setFitToWidth(true);
      scrollPane.setMaxWidth(DEFAULT_SCROLL_WIDTH);
      scrollPane.getStyleClass().add("background-main");
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setContent(grid);

      //Empty table view
      if (tabReports.getItems().size() != 0) {
        tabReports.getItems().clear();
        tabReports.getColumns().clear();
        getModel().getData().clear();
      }

      addHeaders();
      addData();
      showReports();

//      getModel().callCommand(ParseReportsCommand.class);

      // Show report from check
      getController().shoReportIfNeeded();
    } catch (Exception ex) {
      ex.printStackTrace();
      getModel().getModel(MainModel.class).getErrorCommand().setMessage(ex.toString());
      getModel().getModel(MainModel.class).getErrorCommand().run();
    }
  }

  @Override
  public void reload() {
    showReports();
  }

  private void addHeaders(){
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

  public void addData(){
    // Add data
    getModel().readReports();
    ObservableList<ReportRow> data = getModel().getData();
    tabReports.setItems(data);
    addChartScore();
    addFormatIcons();

    if (getModel().isAllReportsLoaded()) {
      loadMore.setVisible(false);
      loadMore.setManaged(false);
    }
  }

  private Node getFXMLNode() {
    Node rootNode = ROOT_EMBEDDED_FXML.get().getNode();
    return rootNode;
  }

  public Button getLoadButton() {
    return loadMore;
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

                String itemGetI = item.get(i);
                String strI = i;
                getController().linkFormatIcon(icon, i, item.get(i));

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

  public void showTextArea(String content) {
    showHideChilds(false);

    double h = getModel().getLocalFacade().getGlobalFacade().getApplication().getScene().getHeight();
    textArea.setText(content);
    textArea.setVisible(true);
    textArea.setManaged(true);
    textArea.setMinHeight(h);
    textArea.setMaxHeight(h);
  }

  public void hideTextArea() {
    textArea.setVisible(false);
    textArea.setManaged(false);
    textArea.clear();
  }

  public void showWebView(String path) {
    showHideChilds(false);

    if (webView == null) {
      webView = new WebView();
      webView.setId("webViewReport");
      reportsVbox.getChildren().add(webView);
      webEngine = webView.getEngine();
    }

    webEngine.load("file:///" + path.replace("\\", "/"));
    webView.setPrefHeight(scrollPane.getHeight());
    webView.setPrefWidth(scrollPane.getWidth());
    webView.setVisible(true);
    webView.setManaged(true);

    scrollPane.setMaxWidth(Double.MAX_VALUE);
    scrollPane.widthProperty().addListener(listenerWidth);
    scrollPane.heightProperty().addListener(listenerHeigth);
  }

  public void hideWebView() {
    if (webView != null) {
      webView.setVisible(false);
      webView.setManaged(false);
      webEngine.load("Empty");
    }
  }

  public void showReports() {
    showHideChilds(true);
    hideTextArea();
    hideWebView();
    scrollPane.setMaxWidth(DEFAULT_SCROLL_WIDTH);
    scrollPane.widthProperty().removeListener(listenerWidth);
    scrollPane.heightProperty().removeListener(listenerHeigth);
  }

  private void showHideChilds(boolean visible) {
    for (Node child : reportsVbox.getChildren()) {
      child.setVisible(visible);
      child.setManaged(visible);
    }
  }

}

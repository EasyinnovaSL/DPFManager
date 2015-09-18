/**
 * <h1>ReportGenerator.java</h1>
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2115 Easy Innova, SL
 * </p>
 *
 * @author Easy Innova
 * @version 1.0
 * @since 23/6/2115
 */

package dpfmanager;

import dpfmanager.shell.modules.ProcessInput;
import dpfmanager.shell.modules.ReportRow;
import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirectoryManager;
import javax.swing.text.TableView;

/**
 * The Class MainApp.
 */
public class MainApp extends Application {
  private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);

  @FXML private TextField txtFile;
  @FXML private RadioButton radBL;
  @FXML private RadioButton radEP;
  @FXML private RadioButton radIT;
  @FXML private RadioButton radAll;
  private static Stage thestage;
  final int width = 970;
  final int height = 1000;

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws Exception the exception
   */
  public static void main(final String[] args) throws Exception {
    launch(args);
  }

  /**
   * The main method.
   *
   * @param stage the stage
   * @throws Exception the exception
   */
  @Override
  public final void start(final Stage stage) throws Exception {
    Parameters params = getParameters();
    if (params.getRaw().size() == 0 || (params.getRaw().size() == 1 && params.getRaw().get(0).equals("-gui"))) {
      thestage = stage;
      LOG.info("Starting JavaFX application");

      // GUI
      LoadGui();
    } else {
      // Command Line
      CommandLine cl = new CommandLine(params);
      cl.launch();
      Platform.exit();
    }
  }

  private boolean FirstTime() {
    return false;
  }

  private void LoadGui() throws Exception
  {
    if (FirstTime()) {
      String fxmlFile = "/fxml/design3.fxml";

      FXMLLoader loader = new FXMLLoader();
      Parent rootNode1 = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
      Scene scenemain = new Scene(rootNode1, width, height);

      thestage.setTitle("DPFManager");
      thestage.setScene(scenemain);
      thestage.setMaxHeight(height);
      thestage.setMinHeight(height);
      thestage.setMaxWidth(width);
      thestage.setMinWidth(width);
      thestage.show();
    } else {
      ShowMain();
    }
  }

  private void ShowMain() throws Exception {
    String fxmlFile = "/fxml/design.fxml";
    LOG.debug("Loading FXML for main view from: {}", fxmlFile);

    FXMLLoader loader = new FXMLLoader();
    Parent rootNode1 = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
    Scene scenemain = new Scene(rootNode1, width, height);
    scenemain.getStylesheets().add("/styles/style.css");

    thestage.setTitle("DPFManager");
    thestage.setScene(scenemain);
    thestage.setMaxHeight(height);
    thestage.setMinHeight(height);
    thestage.setMaxWidth(width);
    thestage.setMinWidth(width);
    thestage.show();

    ObservableList<Node> nodes=scenemain.getRoot().getChildrenUnmodifiable();
    SplitPane splitPa1=(SplitPane)nodes.get(0);
    splitPa1.lookupAll(".split-pane-divider").stream()
        .forEach(
            div -> div.setMouseTransparent(true));
  }

  @FXML
  protected void acceptConditions(ActionEvent event) throws Exception {
    try {
      ShowMain();
    } catch (Exception ex) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("An error occured");
      alert.setContentText(ex.toString());
      alert.showAndWait();
    }
  }


  @FXML
  protected void openFile(ActionEvent event) throws Exception {
    List<String> allowedExtensions = new ArrayList<String>();
    ArrayList<String> files = new ArrayList<String>();

    try {
      allowedExtensions.add(".tif");
      files.add(txtFile.getText());
      boolean bl = radBL.isSelected() || radAll.isSelected();
      boolean ep = radEP.isSelected() || radAll.isSelected();
      boolean it = radIT.isSelected() || radAll.isSelected();

      ProcessInput pi = new ProcessInput(allowedExtensions, bl, ep, it);
      String filename = pi.ProcessFiles(files, false, false, true, "", true);

      ShowReport(filename);
    } catch (Exception ex) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("An error occured");
      alert.setContentText(ex.toString());
      alert.showAndWait();
    }
  }

  private void ShowReport(String filename) {
    String styleBackground = "-fx-background-image: url('/images/topMenu.png'); " +
        "-fx-background-position: center center; " +
        "-fx-background-repeat: repeat-x;";
    String styleButton = "-fx-background-color: transparent;\n" +
        "\t-fx-border-width     : 0px   ;\n" +
        "\t-fx-border-radius: 0 0 0 0;\n" +
        "\t-fx-background-radius: 0 0 0";
    String styleButtonPressed = "-fx-background-color: rgba(255, 255, 255, 0.2);";

    Scene sceneReport = new Scene(new Group(), width, height);

    VBox root = new VBox();
    SplitPane splitPa = new SplitPane();
    splitPa.setOrientation(Orientation.VERTICAL);

    Pane topImg = new Pane();
    topImg.setStyle(styleBackground);
    topImg.setMinWidth(width);
    topImg.setMinHeight(50);
    //topImg.setMaxWidth(width);
    topImg.setMaxHeight(50);

    Button checker = new Button();
    checker.setMinWidth(170);
    checker.setMinHeight(30);
    checker.setLayoutY(5.0);

    checker.setCursor(Cursor.HAND);
    checker.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          gotoReport(event);
          gotoMain(event);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    checker.styleProperty().bind(
        Bindings
            .when(checker.pressedProperty())
            .then(
                new SimpleStringProperty(styleButtonPressed)
            ).otherwise(
            new SimpleStringProperty(styleButton)
        )
    );

    Button report = new Button();
    report.setMinWidth(80);
    report.setMinHeight(30);
    report.setLayoutY(5.0);

    report.setCursor(Cursor.HAND);
    report.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          gotoReport(event);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    report.styleProperty().bind(
        Bindings
            .when(report.pressedProperty())
            .then(
                new SimpleStringProperty(styleButtonPressed)
            ).otherwise(
            new SimpleStringProperty(styleButton)
        )

    );

    topImg.getChildren().addAll(checker, report);

    final WebView browser = new WebView();
    //double w = width-topImg.getWidth();
    double h = height - topImg.getHeight() - 50;
    browser.setMinWidth(width);
    browser.setMinHeight(h);
    //browser.setMaxWidth(width);
    browser.setMaxHeight(h);
    final WebEngine webEngine = browser.getEngine();
    webEngine.load("file:///" + System.getProperty("user.dir") + "/" + filename);

    splitPa.getItems().addAll(topImg);
    splitPa.getItems().addAll(browser);
    splitPa.setDividerPosition(0, 0.5f);
    root.getChildren().addAll(splitPa);
    sceneReport.setRoot(root);

    thestage.setScene(sceneReport);

    //Set invisible the divisor line
    splitPa.lookupAll(".split-pane-divider").stream()
        .forEach(
            div -> div.setStyle("-fx-padding: 0;\n" +
                "    -fx-background-color: transparent;\n" +
                "    -fx-background-insets: 0;\n" +
                "    -fx-shape: \" \";"));
    splitPa.lookupAll(".split-pane-divider").stream()
        .forEach(
            div -> div.setMouseTransparent(true));

    thestage.setMaxHeight(Double.MAX_VALUE);
    thestage.setMinHeight(height);
    thestage.setMaxWidth(Double.MAX_VALUE);
    thestage.setResizable(true);
    thestage.setMinWidth(width);

    thestage.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
        if (newSceneWidth.doubleValue() < 989) {
          report.setLayoutX(470.0);
          checker.setLayoutX(290.0);
        } else {
          double dif = (newSceneWidth.doubleValue() - width) / 2;
          report.setLayoutX(463.0 + dif);
          checker.setLayoutX(283.0 + dif);
        }
      }
    });

    thestage.sizeToScene();
  }

  @FXML
  protected void gotoMain(ActionEvent event) throws Exception {
    ShowMain();
  }

  @FXML
  protected void gotoReport(ActionEvent event) throws Exception {
    try {
    FXMLLoader loader2 = new FXMLLoader();
    String fxmlFile2 = "/fxml/design2.fxml";
    Parent rootNode2 = (Parent) loader2.load(getClass().getResourceAsStream(fxmlFile2));
    Scene scenereport = new Scene(rootNode2, width, height);
    scenereport.getStylesheets().add("/styles/style.css");

    thestage.setMaxHeight(height);
    thestage.setMinHeight(height);
    thestage.setMaxWidth(width);
    thestage.setMinWidth(width);

    thestage.setScene(scenereport);
    thestage.sizeToScene();
    ObservableList<Node> nodes=scenereport.getRoot().getChildrenUnmodifiable();
    SplitPane splitPa1=(SplitPane)nodes.get(0);
    splitPa1.lookupAll(".split-pane-divider").stream()
        .forEach(
            div -> div.setMouseTransparent(true));

      ObservableList<ReportRow> data = ReadReports();

    javafx.scene.control.TableView<ReportRow> tabReports = new javafx.scene.control.TableView<ReportRow>();

    tabReports.setEditable(true);
    TableColumn colDate = new TableColumn("Date");
    colDate.setMinWidth(90);
    colDate.setCellValueFactory(new PropertyValueFactory<ReportRow, String>("date"));

    TableColumn colN = new TableColumn("Files Processed");
    colN.setMinWidth(100);
    colN.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("nfiles")
    );

    TableColumn colResult = new TableColumn("Result");
    colResult.setMinWidth(165);
    colResult.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("result")
    );

    TableColumn colFixed= new TableColumn("Fixed");
      colFixed.setMinWidth(120);
    colFixed.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("fixed")
    );

    TableColumn colErrors = new TableColumn("Errors");
      colErrors.setMinWidth(75);
    colErrors.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("errors")
    );

    TableColumn colWarnings = new TableColumn("Warnings");
      colWarnings.setMinWidth(85);
    colWarnings.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("warnings")
    );

    TableColumn colPassed = new TableColumn("Passed");
      colPassed.setMinWidth(75);
    colPassed.setCellValueFactory(
        new PropertyValueFactory<ReportRow, String>("passed")
    );

    TableColumn colScore = new TableColumn("Score");
      colScore.setMinWidth(100);
      colScore.setCellValueFactory(
          new PropertyValueFactory<ReportRow, String>("score")
      );

      tabReports.getColumns().addAll(colDate, colN, colResult, colFixed, colErrors, colWarnings, colPassed, colScore);
      tabReports.setItems(data);

      tabReports.setLayoutX(82.0);
      tabReports.setLayoutY(270.0);
      tabReports.setPrefHeight(270.0);
      tabReports.setPrefWidth(835.0);

      changeColumnTextColor(colDate, Color.LIGHTGRAY);
      changeColumnTextColor(colN, Color.CYAN);
      changeColumnTextColor(colResult, Color.LIGHTGRAY);
      changeColumnTextColor(colFixed, Color.LIGHTGRAY);
      changeColumnTextColor(colErrors, Color.RED);
      changeColumnTextColor(colWarnings, Color.ORANGE);
      changeColumnTextColor(colPassed, Color.GREENYELLOW);
      changeColumnTextColor(colScore, Color.LIGHTGRAY);

      tabReports.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
            ReportRow row = tabReports.getSelectionModel().getSelectedItem();
            ShowReport(row.getFile());
          }
        }
      });

      VBox tabPane = ((VBox) ((SplitPane) nodes.get(0)).getItems().get(1));
      AnchorPane ap = (AnchorPane)(tabPane.getChildren().get(0));
      ScrollPane sp = (ScrollPane)(ap.getChildren().get(0));
      AnchorPane ap2 = (AnchorPane)(sp.getContent());
      ap2.getChildren().add(tabReports);

    } catch (Exception ex) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("An error occured");
      alert.setContentText(ex.toString());
      alert.showAndWait();
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

  @FXML
  protected void browseFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    File file = fileChooser.showOpenDialog(thestage);
    txtFile.setText(file.getPath());
  }

  private ObservableList<ReportRow> ReadReports() {
    List<ReportRow> list = new ArrayList<ReportRow>();
    ObservableList<ReportRow> data = FXCollections.observableArrayList(list);

    String baseDir = "reports";
    File reportsDir = new File(baseDir);
    if (reportsDir.exists()) {
      String[] directories = reportsDir.list(new FilenameFilter() {
        @Override
        public boolean accept(File current, String name) {
          return new File(current, name).isDirectory();
        }
      });
      for (String reportDay : directories) {
        File reportsDay = new File(baseDir + "/" + reportDay);
        String[] directories2 = reportsDay.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        for (String reportDir : directories2) {
          File reportHtml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.html");
          if (reportHtml.exists()) {
            ReportRow rr = ReportRow.createRowFromHtml(reportDay, reportHtml);
            data.add(rr);
          } else {
            File report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.xml");
            if (report.exists()) {
              ReportRow rr = ReportRow.createRowFromXml(reportDay, report);
              data.add(rr);
            }  else {
              File reportJson = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.json");
              if (reportJson.exists()) {
                ReportRow rr = ReportRow.createRowFromJson(reportDay, reportJson);
                data.add(rr);
              }
            }
          }
        }
      }
    }

    return data;
  }
}

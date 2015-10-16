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

import dpfmanager.shell.modules.classes.Configuration;
import dpfmanager.shell.modules.classes.Field;
import dpfmanager.shell.modules.classes.Fixes;
import dpfmanager.shell.modules.classes.ProcessInput;
import dpfmanager.shell.modules.classes.ReportRow;
import dpfmanager.shell.modules.classes.Rules;
import dpfmanager.shell.modules.interfaces.CommandLine;
import dpfmanager.shell.modules.interfaces.Gui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * The Class MainApp.
 */
public class MainApp extends Application {
  private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);
  public enum RuleFields { ImageWidth, ImageHeight, PixelDensity };
  private static Stage thestage;
  final int width = 970;
  final int height = 950;
  private static Configuration config;
  private static String dropped;

  @FXML private TextField txtFile;
  @FXML private CheckBox radProf1, radProf2, radProf3, radProf4, radProf5;
  @FXML private Line line;
  @FXML private CheckBox chkFeedback, chkSubmit;
  @FXML private CheckBox chkHtml, chkXml, chkJson;
  @FXML private TextField txtName, txtSurname, txtEmail, txtJob, txtOrganization, txtCountry, txtWhy;
  @FXML private Button addRule, continueButton, addFix;

  private static Gui gui;

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
    if (params==null || params.getRaw().size() == 0 || (params.getRaw().size() == 1 && params.getRaw().get(0).equals("-gui"))) {
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

  public static class AsNonApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
      // noop
    }
  }

  private boolean FirstTime() {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "first_time";
    String defaultValue = "1";
    String propertyValue = prefs.get(PREF_NAME, defaultValue);
    return propertyValue.equals("1");
  }

  private void LoadGui() throws Exception
  {
    if (FirstTime()) {
      ShowDisclaimer();
    } else {
      gui = new Gui();
      gui.LoadConformanceChecker();

      ShowMain();
    }
  }

  private void ShowDisclaimer() throws Exception {
    String fxmlFile = "/fxml/legal.fxml";

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
  }

  @FXML
  private void ShowMain() throws Exception {
    String fxmlFile = "/fxml/design.fxml";
    LOG.debug("Loading FXML for main view from: {}", fxmlFile);

    FXMLLoader loader = new FXMLLoader();
    Parent rootNode1 = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
    Scene scenemain = new Scene(rootNode1, width, height);
    scenemain.getStylesheets().add("/styles/style.css");

    // Tree View
    /*RootDirItem rootDirItem = ResourceItem.createObservedPath(Paths.get("/"));
    DirectoryTreeView tv = new DirectoryTreeView();
    tv.setIconSize(IconSize.MEDIUM);
    tv.setRootDirectories(FXCollections.observableArrayList(rootDirItem));
    DirectoryView v = new DirectoryView();
    v.setIconSize(IconSize.MEDIUM);
    tv.getSelectedItems().addListener( (Observable o) -> {
      if( ! tv.getSelectedItems().isEmpty() ) {
        v.setDir(tv.getSelectedItems().get(0));
      } else {
        v.setDir(null);
      }
    });
    SplitPane p = new SplitPane(tv,v);
    p.setDividerPositions(0.3);*/

    thestage.setTitle("DPFManager");
    thestage.setScene(scenemain);
    thestage.setMaxHeight(height);
    thestage.setMinHeight(height);
    thestage.setMaxWidth(width);
    thestage.setMinWidth(width);
    thestage.sizeToScene();
    thestage.show();

    ObservableList<Node> nodes=scenemain.getRoot().getChildrenUnmodifiable();
    SplitPane splitPa1=(SplitPane)nodes.get(0);
    splitPa1.lookupAll(".split-pane-divider").stream()
        .forEach(
            div -> div.setMouseTransparent(true));

    scenemain.setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
          event.acceptTransferModes(TransferMode.MOVE);
        } else {
          event.consume();
        }
      }
    });

    // Dropping over surface
    scenemain.setOnDragDropped(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
          success = true;
          String filePath = null;
          for (File file:db.getFiles()) {
            filePath = file.getAbsolutePath();
            dropped = filePath;
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                SetFile();
              }
            });
            break;
          }
        }
        event.setDropCompleted(success);
        event.consume();
      }
    });

    // Read config files
    VBox vBox = new VBox();
    vBox.setSpacing(3);
    vBox.setPadding(new Insets(5));
    File folder = new File(".");
    final ToggleGroup group = new ToggleGroup();
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isFile()) {
        if (fileEntry.getName().toLowerCase().endsWith(".cfg")) {
          RadioButton radio = new RadioButton();
          radio.setText(fileEntry.getName());
          radio.setToggleGroup(group);
          vBox.getChildren().add(radio);
        }
      }
    }

    Scene scene = thestage.getScene();
    AnchorPane pan = (AnchorPane)scene.lookup("#pane1");
    pan.getChildren().add(vBox);
  }

  protected void SetFile() {
    Scene scene = thestage.getScene();
    TextField txtField = (TextField)scene.lookup("#txtBox1");
    txtField.setText(dropped);
  }

  @FXML
  protected void proceed(ActionEvent event) throws Exception {
    try {
      Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);

      if (chkFeedback.isSelected()){
        final String PREF_NAME = "feedback";
        String newValue = "1";
        prefs.put(PREF_NAME, newValue);
      } else {
        final String PREF_NAME = "feedback";
        String newValue = "0";
        prefs.put(PREF_NAME, newValue);
      }
      if (chkSubmit.isSelected()){
        boolean ok = true;
        if (txtName.getText().length() == 0) ok = false;
        if (txtSurname.getText().length() == 0) ok = false;
        if (txtEmail.getText().length() == 0) ok = false;
        if (txtJob.getText().length() == 0) ok = false;
        if (txtOrganization.getText().length() == 0) ok = false;
        if (txtCountry.getText().length() == 0) ok = false;
        if (txtWhy.getText().length() == 0) ok = false;
        if (!ok) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Missing data");
          alert.setContentText("Please fill in all the fields");
          alert.showAndWait();
          return;
        }
        if (txtEmail.getText().indexOf("@") < 0 || txtEmail.getText().indexOf("@") > txtEmail.getText().lastIndexOf(".")) {
          ok = false;
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Incorrect email");
          alert.setContentText("Please write your email correctly");
          alert.showAndWait();
        }
        if (ok) {
          String url = "http://dpfmanager.org/form.php";
          URL obj = new URL(url);
          java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

          //add reuqest header
          con.setRequestMethod("POST");
          con.setRequestProperty("User-Agent", "Mozilla/5.0");
          con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

          String urlParameters = "email=" + txtEmail.getText();
          urlParameters += "&name=" + txtName.getText();
          urlParameters += "&surname=" + txtSurname.getText();
          urlParameters += "&jobrole=" + txtJob.getText();
          urlParameters += "&organization=" + txtOrganization.getText();
          urlParameters += "&country=" + txtCountry.getText();
          urlParameters += "&comments=" + txtWhy.getText();
          urlParameters += "&formtype=DPFManagerApp";

          // Send post request
          con.setDoOutput(true);
          DataOutputStream wr = new DataOutputStream(con.getOutputStream());
          wr.writeBytes(urlParameters);
          wr.flush();
          wr.close();

          boolean getok = false;
          int responseCode = con.getResponseCode();
          if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
              if (inputLine == "OK") getok = true;
            }
            in.close();
          }
          if (getok) {
            final String PREF_NAME = "first_time";
            String newValue = "0";
            prefs.put(PREF_NAME, newValue);
          } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error ocurred");
            alert.setContentText("Sorry, we could not proceed your request. Try again the next time you run DPFmanager");
            alert.showAndWait();

            final String PREF_NAME = "first_time";
            String newValue = "1";
            prefs.put(PREF_NAME, newValue);
          }
        }
      } else {
        final String PREF_NAME = "first_time";
        String newValue = "0";
        prefs.put(PREF_NAME, newValue);
      }

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
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<String> extensions = this.gui.getExtensions();

    if (txtFile.getText().equals("Select a file")) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Alert");
      alert.setHeaderText("Please select a file");
      alert.showAndWait();
      return;
    }

    Scene scene = thestage.getScene();
    AnchorPane ap3 = (AnchorPane)scene.lookup("#pane1");
    boolean oneChecked = false;
    for (Node node : ap3.getChildren()){
      if(node instanceof VBox) {
        VBox vBox1 = (VBox)node;
        for (Node nodeIn : vBox1.getChildren()){
          if(nodeIn instanceof RadioButton) {
            RadioButton radio = (RadioButton)nodeIn;
            if (radio.isSelected()) {
              config = new Configuration();
              config.ReadFile(radio.getText());
              oneChecked = true;
              break;
            }
          }
        }
      }
    }

    if (!oneChecked) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Alert");
      alert.setHeaderText("Please select a configuration file");
      alert.showAndWait();
      return;
    }

    ShowLoading();

    // Create a background task, because otherwise the loading message is not shown
    Task<Integer> task = new Task<Integer>(){
      @Override
      protected Integer call() throws Exception{
        try {
          files.add(txtFile.getText());
          boolean bl = config.getIsos().contains("Baseline");
          boolean ep = config.getIsos().contains("Tiff/EP");
          int it = -1;
          if (config.getIsos().contains("Tiff/IT")) it = 0;
          if (config.getIsos().contains("Tiff/IT-1")) it = 1;
          if (config.getIsos().contains("Tiff/IT-2")) it = 2;

          ProcessInput pi = new ProcessInput(extensions, bl, ep, it, config.getRules().getRules().size() > 0);
          ArrayList<String> formats = config.getFormats();
          String filename = pi.ProcessFiles(files, formats.contains("XML"), formats.contains("JSON"), formats.contains("HTML"), "", true, config.getRules(), config.getFixes());

          ShowReport(filename);
        } catch (Exception ex) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("An error occured");
          alert.setContentText(ex.toString());
          alert.showAndWait();
        }
        return 0;
      }
    };

    //start the background task
    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  private void ShowReport(String filename) {
    System.out.println("Showing report...");
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

    // Button go to main
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

    // Button go to reports
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

    // Button go to about
    Button about = new Button();
    about.setMinWidth(55);
    about.setMinHeight(30);
    about.setLayoutY(5.0);

    about.setCursor(Cursor.HAND);
    about.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          gotoAbout(event);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    about.styleProperty().bind(
        Bindings
            .when(about.pressedProperty())
            .then(
                new SimpleStringProperty(styleButtonPressed)
            ).otherwise(
            new SimpleStringProperty(styleButton)
        )
    );

    topImg.getChildren().addAll(checker, report, about);

    // Create a task to be run later, in order to avoid conflicts between threads
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        WebView browser = new WebView();
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
              about.setLayoutX(560.0);
            } else {
              double dif = (newSceneWidth.doubleValue() - width) / 2;
              report.setLayoutX(463.0 + dif);
              checker.setLayoutX(283.0 + dif);
              about.setLayoutX(560.0 + dif);
            }
          }
        });

        thestage.sizeToScene();
      }
    });
  }

  @FXML
  protected void gotoMain(ActionEvent event) throws Exception {
    ShowMain();
  }

  void LoadSceneXml(String fxmlFile) throws Exception {
    LOG.debug("Loading FXML for main view from: {}", fxmlFile);

    FXMLLoader loader = new FXMLLoader();
    Parent rootNode1 = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
    Scene scene = new Scene(rootNode1, width, height);
    scene.getStylesheets().add("/styles/style.css");

    thestage.setTitle("DPFManager");
    thestage.setScene(scene);
    thestage.setMaxHeight(height);
    thestage.setMinHeight(height);
    thestage.setMaxWidth(width);
    thestage.setMinWidth(width);
    thestage.sizeToScene();
    thestage.show();

    ObservableList<Node> nodes=scene.getRoot().getChildrenUnmodifiable();
    SplitPane splitPa1=(SplitPane)nodes.get(0);
    splitPa1.lookupAll(".split-pane-divider").stream()
        .forEach(
            div -> div.setMouseTransparent(true));
  }

  @FXML
  protected void newConfig(ActionEvent event) throws Exception {
    config = new Configuration();
    LoadSceneXml("/fxml/config1.fxml");
  }

  @FXML
  protected void gotoConfig2(ActionEvent event) throws Exception {
    if (radProf1.isSelected()) config.getIsos().add("Baseline");
    if (radProf2.isSelected()) config.getIsos().add("Tiff/EP");
    if (radProf3.isSelected()) config.getIsos().add("Tiff/IT");
    if (radProf4.isSelected()) config.getIsos().add("Tiff/IT-1");
    if (radProf5.isSelected()) config.getIsos().add("Tiff/IT-2");
    LoadSceneXml("/fxml/config2.fxml");
  }

  @FXML
  protected void gotoConfig3(ActionEvent event) throws Exception {
    Rules rules = config.getRules();
    Scene scene = thestage.getScene();
    rules.Read(scene);
    LoadSceneXml("/fxml/config3.fxml");
  }

  @FXML
  protected void gotoConfig4(ActionEvent event) throws Exception {
    if (chkHtml.isSelected()) config.getFormats().add("HTML");
    if (chkXml.isSelected()) config.getFormats().add("XML");
    if (chkJson.isSelected()) config.getFormats().add("JSON");
    LoadSceneXml("/fxml/config4.fxml");
  }

  @FXML
  protected void gotoConfig6(ActionEvent event) throws Exception {
    Fixes fixes = config.getFixes();
    Scene scene = thestage.getScene();
    fixes.Read(scene);
    LoadSceneXml("/fxml/config6.fxml");
    setLabel("labIsos", config.getTxtIsos());
    setLabel("labReports", config.getTxtFormats());
    setLabel("labRules", config.getTxtRules());
    setLabel("labFixes", config.getTxtFixes());
  }

  void setLabel(String labelName, String txt) {
    Scene scene = thestage.getScene();
    Label lab = (Label)scene.lookup("#" + labelName);
    lab.setText(txt);
  }

  @FXML
  protected void saveConfig(ActionEvent event) throws Exception {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("."));
    fileChooser.setInitialFileName("config.cfg");
    fileChooser.setTitle("Save Config");
    File file = fileChooser.showSaveDialog(thestage);
    if (file != null) {
      try {
        SaveConfig(file.getAbsolutePath());
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
    ShowMain();
  }

  void SaveConfig(String filename) throws Exception {
    config.SaveFile(filename);
  }

  @FXML
  protected void gotoAbout(ActionEvent event) throws Exception {
    LoadSceneXml("/fxml/about.fxml");
  }

  @FXML
  protected void gotoReport(ActionEvent event) throws Exception {
    try {
    FXMLLoader loader2 = new FXMLLoader();
    String fxmlFile2 = "/fxml/summary.fxml";
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

    TableColumn<ReportRow, ReportRow> colScore = new TableColumn("Score");
      colScore.setMinWidth(100);
      colScore.setCellValueFactory(
          new PropertyValueFactory("score")
      );
      colScore.setCellFactory((TableColumn<ReportRow, ReportRow> param) -> {
        return new TableCell<ReportRow, ReportRow>(){

          @Override
          protected void updateItem(ReportRow item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) setGraphic (null);
            else {
              double tot = 50 + 100;
              double ratio1 = 40 / tot;
              double ratio2 = 60 / tot;

              Rectangle r1 = new Rectangle();
              //the param is the column, bind so rects resize with column
              r1.widthProperty().bind(param.widthProperty().multiply(ratio1));
              r1.heightProperty().bind(this.getTableRow().heightProperty().multiply(0.5));
              r1.setStyle("-fx-fill:#f3622d;");
              Rectangle r2 = new Rectangle(0, 20);
              r2.widthProperty().bind(param.widthProperty().multiply(ratio2));
              r2.setStyle("-fx-fill:#fba71b;");

              HBox hbox = new HBox(r1,r2);
              hbox.setAlignment(Pos.CENTER_LEFT);
              setGraphic(hbox);
              setText(null);
            }
          }

        };
      });

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
            if (row.getFile().toLowerCase().endsWith(".html")) {
              ShowReport(row.getFile());
            }
          }
        }
      });

      AnchorPane ap2 = (AnchorPane)scenereport.lookup("#pane1");
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

  private void addNumericOperator(String item) {
    ArrayList<String> operators = null;
    for (Field field : gui.getFields()) {
      if (field.getName().equals(item)) {
        operators = field.getOperators();
      }
    }
    if (operators != null) {
      Scene scene = thestage.getScene();
      AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
      for (Node node : ap2.getChildren()) {
        if (node instanceof HBox) {
          HBox hBox1 = (HBox) node;
          for (Node nodeIn : hBox1.getChildren()) {
            if (nodeIn instanceof ComboBox) {
              ComboBox comboBox = (ComboBox) nodeIn;
              if (comboBox.getValue() != null && comboBox.getValue().equals(item)) {
                ComboBox comboOp = new ComboBox();
                for (String operator : operators) {
                  comboOp.getItems().add(operator);
                }
                while (hBox1.getChildren().size() > 1)
                  hBox1.getChildren().remove(1);
                TextField value = new TextField();
                value.getStyleClass().add("txtRule");
                hBox1.getChildren().add(comboOp);
                hBox1.getChildren().add(value);
                break;
              }
            }
          }
        }
      }
    }
  }

  private void addFixValue(String item) {
    ArrayList<String> fields = null;
    Scene scene = thestage.getScene();
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    for (Node node : ap2.getChildren()) {
      if (node instanceof HBox) {
        HBox hBox1 = (HBox) node;
        for (Node nodeIn : hBox1.getChildren()) {
          if (nodeIn instanceof ComboBox) {
            ComboBox comboBox = (ComboBox) nodeIn;
            if (comboBox.getValue() != null && comboBox.getValue().equals(item)) {
              ComboBox comboOp = new ComboBox();
              for (String field : gui.getFixFields()) {
                comboOp.getItems().add(field);
              }
              while (hBox1.getChildren().size() > 1)
                hBox1.getChildren().remove(1);
              hBox1.getChildren().add(comboOp);

              if (item.equals("Add Tag")) {
                TextField value = new TextField();
                value.getStyleClass().add("txtFix");
                hBox1.getChildren().add(value);
              }
              break;
            }
          }
        }
      }
    }
  }

  @FXML
  protected void addRule(ActionEvent event) {
    int dif = 50;
    double xRule = addRule.getLayoutX() - 100;
    double yRule = addRule.getLayoutY();

    // Add combobox
    ComboBox comboBox = new ComboBox();
    for (Field field : gui.getFields()) {
      comboBox.getItems().add(field.getName());
    }
    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override public void changed(ObservableValue ov, String t, String item) {
        addNumericOperator(item);
      }
    });
    HBox hBox = new HBox (comboBox);
    hBox.setSpacing(5);
    hBox.setLayoutX(xRule);
    hBox.setLayoutY(yRule);

    Scene scene = thestage.getScene();
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    ap2.getChildren().add(hBox);

    addRule.setLayoutY(addRule.getLayoutY() + dif);

    // Move bottom elements if necessary
    if (addRule.getLayoutY() + addRule.getHeight() > line.getLayoutY()) {
      line.setLayoutY(line.getLayoutY() + dif);
      continueButton.setLayoutY(continueButton.getLayoutY() + dif);
    }
  }

  @FXML
  protected void addFix(ActionEvent event) {
    int dif = 50;
    double xRule = addFix.getLayoutX() - 100;
    double yRule = addFix.getLayoutY();

    // Add combobox
    ComboBox comboBox = new ComboBox();
    for (String fix : gui.getFixes()) {
      comboBox.getItems().add(fix);
    }
    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override public void changed(ObservableValue ov, String t, String item) {
        addFixValue(item);
      }
    });
    HBox hBox = new HBox (comboBox);
    hBox.setSpacing(5);
    hBox.setLayoutX(xRule);
    hBox.setLayoutY(yRule);

    Scene scene = thestage.getScene();
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    ap2.getChildren().add(hBox);

    addFix.setLayoutY(addFix.getLayoutY() + dif);

    // Move bottom elements if necessary
    if (addFix.getLayoutY() + addFix.getHeight() > line.getLayoutY()) {
      line.setLayoutY(line.getLayoutY() + dif);
      continueButton.setLayoutY(continueButton.getLayoutY() + dif);
    }
  }

  @FXML
  protected void openConfig(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Config");
    fileChooser.setInitialDirectory(new File("."));
    fileChooser.setInitialFileName("config.cfg");
    File file = fileChooser.showOpenDialog(thestage);
    try {
      if (file != null) {
        config = new Configuration();
        config.ReadFile(file.getAbsolutePath());
      }
    } catch (Exception ex) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("An error ocurred");
      alert.setContentText("There was an error while importing the configuration file");
      alert.showAndWait();
    }
  }

  @FXML
  protected void browseFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    File file = fileChooser.showOpenDialog(thestage);
    if (file != null)
      txtFile.setText(file.getPath());
  }

  private void ShowLoading() {
    HBox loading = new HBox();
    Label msg = new Label("Processing...");
    loading.setLayoutY(300);
    loading.setLayoutX(240);
    loading.getStyleClass().add("loading");
    loading.getChildren().add(msg);
    loading.setPadding(new Insets(50, 220, 50, 220));
    HBox bLoading = new HBox();
    bLoading.getStyleClass().add("loading2");
    bLoading.setPadding(new Insets(350, 550, 550, 250));
    bLoading.getChildren().add(loading);

    Scene scene = thestage.getScene();
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane0");
    ap2.getChildren().add(bLoading);
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

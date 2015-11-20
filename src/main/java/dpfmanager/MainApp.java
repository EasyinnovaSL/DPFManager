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
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Victor Muñoz
 * @version 1.0
 * @since 23/6/2115
 */

package dpfmanager;

import dpfmanager.shell.modules.autofixes.autofix;
import dpfmanager.shell.modules.classes.Configuration;
import dpfmanager.shell.modules.classes.Field;
import dpfmanager.shell.modules.classes.Fixes;
import dpfmanager.shell.modules.classes.NumberTextField;
import dpfmanager.shell.modules.classes.ProcessInput;
import dpfmanager.shell.modules.classes.ReportRow;
import dpfmanager.shell.modules.classes.Rules;
import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;
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
import javafx.collections.ObservableMap;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * The Class MainApp.
 */
public class MainApp extends Application {
  private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);
  private static Stage thestage;
  final int width = 970;
  final int height = 950;
  private static Configuration config;
  private static String dropped;
  private static Gui gui;
  int uniqueId = 0;
  private double defaultLineYlayout = 564.0;
  private final ToggleGroup group = new ToggleGroup();
  private final int reports_loaded = 50;
  private boolean all_reports_loaded;
  private static boolean firstRun = true;

  @FXML private TextField txtFile;
  @FXML private CheckBox radProf1, radProf2, radProf3, radProf4, radProf5;
  @FXML private Line line;
  @FXML private CheckBox chkFeedback, chkSubmit;
  @FXML private CheckBox chkHtml, chkXml, chkJson, chkPdf;
  @FXML private TextField txtName, txtSurname, txtEmail, txtJob, txtOrganization, txtCountry;
  @FXML private TextArea txtWhy;
  @FXML private Button addRule, continueButton, addFix;

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
    System.out.println("Default user folder: " + System.getProperty("user.home"));
    if (params == null || params.getRaw().size() == 0 || (params.getRaw().size() == 1 && params.getRaw().get(0).equals("-gui"))) {
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

  private void setDefaultBrowseDirectory(String path) {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "browse_dir";
    prefs.put(PREF_NAME, path);
  }

  private String getDefaultBrowseDirectory() {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "browse_dir";
    String defaultValue = ".";
    String propertyValue = prefs.get(PREF_NAME, defaultValue);
    if (new File(propertyValue).exists() && new File(propertyValue).isDirectory())
      return propertyValue;
    else
      return ".";
  }

  private boolean FirstTime() {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "first_time";
    String defaultValue = "1";
    String propertyValue = prefs.get(PREF_NAME, defaultValue);
    return propertyValue.equals("1");
  }

  private void LoadGui() throws Exception {
    gui = new Gui();
    gui.LoadConformanceChecker();

    if (FirstTime()) {
      ShowDisclaimer();
    } else {
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
    thestage.show();
  }

  private String getConfigDir() {
    //return ".";
    return System.getProperty("user.home") + "/DPF Manager";
  }

  @FXML
  private void ShowMain() throws Exception {
    String fxmlFile = "/fxml/design.fxml";
    LOG.debug("Loading FXML for main view from: {}", fxmlFile);

    FXMLLoader loader = new FXMLLoader();
    Parent rootNode1 = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
    double w = width;
    double h = height;
    if (firstRun) firstRun = false;
    else {
      w = thestage.getScene().getWidth();
      h = thestage.getScene().getHeight();
    }
    Scene scenemain = new Scene(rootNode1, w, h);
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
    thestage.sizeToScene();
    thestage.show();

    SplitPane splitPa1 = (SplitPane) scenemain.lookup("#splitPa1");
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
          for (File file : db.getFiles()) {
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
    File folder = new File(getConfigDir());
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isFile()) {
        if (fileEntry.getName().toLowerCase().endsWith(".dpf")) {
          RadioButton radio = new RadioButton();
          radio.setText(fileEntry.getName());
          radio.setToggleGroup(group);
          vBox.getChildren().add(radio);
        }
      }
    }

    Scene scene = thestage.getScene();
    AnchorPane pan = (AnchorPane) scene.lookup("#pane1");
    pan.getChildren().add(vBox);
    AnchorPane pan0 = (AnchorPane) scene.lookup("#pane0");

    ComboBox comboBox = new ComboBox();
    comboBox.getItems().add("File");
    comboBox.getItems().add("Folder");
    comboBox.setValue("File");
    comboBox.setOpacity(0.0);
    comboBox.setLayoutY(315.0);
    comboBox.setLayoutX(720.0);
    comboBox.setPrefWidth(10.0);
    comboBox.setPrefHeight(39.0);
    comboBox.setCursor(Cursor.HAND);
    comboBox.setId("choiceType");

    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String oldValue, String newValue) {
        Scene scene = thestage.getScene();
        AnchorPane pan0 = (AnchorPane) scene.lookup("#pane0");
        Button Select = (Button) pan0.lookup("#myButton");
        Select.fire();
      }
    });
    pan0.getChildren().add(comboBox);

    if (gui.getSelectedFile() != null) {
      TextField txtField = (TextField) scene.lookup("#txtBox1");
      txtField.setText(gui.getSelectedFile());
    }

    topMenuPositioning(scene);
  }

  protected void SetFile() {
    Scene scene = thestage.getScene();
    TextField txtField = (TextField) scene.lookup("#txtBox1");
    txtField.setText(dropped);
  }

  @FXML
  protected void proceed(ActionEvent event) throws Exception {
    try {
      Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);

      if (chkFeedback.isSelected()) {
        final String PREF_NAME = "feedback";
        String newValue = "1";
        prefs.put(PREF_NAME, newValue);
      } else {
        final String PREF_NAME = "feedback";
        String newValue = "0";
        prefs.put(PREF_NAME, newValue);
      }
      if (chkSubmit.isSelected()) {
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

          //add request header
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
              if (inputLine.equals("OK")) getok = true;
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
    AnchorPane ap3 = (AnchorPane) scene.lookup("#pane1");
    boolean oneChecked = false;
    for (Node node : ap3.getChildren()) {
      if (node instanceof VBox) {
        VBox vBox1 = (VBox) node;
        for (Node nodeIn : vBox1.getChildren()) {
          if (nodeIn instanceof RadioButton) {
            RadioButton radio = (RadioButton) nodeIn;
            if (radio.isSelected()) {
              config = new Configuration();
              String path = radio.getText();
              if (!new File(path).exists())
                path = getConfigDir() + "/" + radio.getText();
              config.ReadFile(path);
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
          if (new File(txtFile.getText()).isDirectory()) {
            File[] listOfFiles = new File(txtFile.getText()).listFiles();
            for (int j = 0; j < listOfFiles.length; j++) {
              if (listOfFiles[j].isFile()) {
                files.add(listOfFiles[j].getPath());
              }
            }
          } else {
            files.add(txtFile.getText());
          }
          boolean bl = config.getIsos().contains("Baseline");
          boolean ep = config.getIsos().contains("Tiff/EP");
          int it = -1;
          if (config.getIsos().contains("Tiff/IT")) it = 0;
          if (config.getIsos().contains("Tiff/IT-1")) it = 1;
          if (config.getIsos().contains("Tiff/IT-2")) it = 2;

          ProcessInput pi = new ProcessInput(extensions, bl, ep, it, config.getRules().getRules().size() > 0);
          ArrayList<String> formats = config.getFormats();

          String filename = pi.ProcessFiles(files, formats.contains("XML"), formats.contains("JSON"), formats.contains("HTML"), formats.contains("PDF"), null, true, config.getRules(), config.getFixes());

          if (formats.contains("HTML")) {
            ShowReport(filename, "html");
          } else if (formats.contains("XML")) {
            ShowReport(filename, "xml");
          } else if (formats.contains("JSON")) {
            ShowReport(filename, "json");
          } else if (formats.contains("PDF")) {
            new Thread(new Runnable() {
              @Override
              public void run() {
                try {
                  Desktop.getDesktop().open(new File(filename));
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }).start();

            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                try {
                  gotoMain(event);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
          }

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

  private void ShowReport(String filename, String format) {
    System.out.println("Showing report...");
    String styleBackground = "-fx-background-image: url('/images/topMenu.png'); " +
        "-fx-background-position: center center; " +
        "-fx-background-repeat: repeat-x;";
    String styleButton = "-fx-background-color: transparent;\n" +
        "\t-fx-border-width     : 0px   ;\n" +
        "\t-fx-border-radius: 0 0 0 0;\n" +
        "\t-fx-background-radius: 0 0 0";
    String styleButtonPressed = "-fx-background-color: rgba(255, 255, 255, 0.2);";

    Scene sceneReport = new Scene(new Group(), thestage.getScene().getWidth(), thestage.getScene().getHeight());

    VBox root = new VBox();
    SplitPane splitPa = new SplitPane();
    splitPa.setOrientation(Orientation.VERTICAL);

    Pane topImg = new Pane();
    topImg.setStyle(styleBackground);
    topImg.setMinWidth(thestage.getScene().getWidth());
    topImg.setMinHeight(50);
    topImg.setMaxHeight(50);

    // Button go to main
    Button checker = new Button();
    checker.setMinWidth(170);
    checker.setMinHeight(30);
    checker.setLayoutY(5.0);
    checker.setId("butChecker");

    checker.setCursor(Cursor.HAND);
    checker.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          //gotoReport(event);
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
    report.setId("butReport");

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
    about.setId("butAbout");

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
        double h = thestage.getScene().getHeight() - topImg.getHeight() - 50;
        splitPa.getItems().addAll(topImg);
        String file = filename;

        // If HTML show in webview
        if (format.equals("html")) {
          WebView browser = new WebView();
          //double w = width-topImg.getWidth();
          browser.setMinWidth(thestage.getScene().getWidth());
          browser.setMinHeight(h);
          //browser.setMaxWidth(width);
          browser.setMaxHeight(h);
          final WebEngine webEngine = browser.getEngine();
          webEngine.load("file:///" + file.replace("\\","/"));
          splitPa.getItems().addAll(browser);
        }
        // If PDF show in new window
        else if (format.equals("pdf")) {
          try {
            Desktop.getDesktop().open(new File(file));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        // Else show in TextArea
        else {
          TextArea ta = new TextArea();
          ta.setMinWidth(thestage.getScene().getWidth());
          ta.setMinHeight(h);
          ta.setEditable(false);

          String content = "";

          try {
            BufferedReader input = new BufferedReader(new FileReader(file));
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

          if (format.equals("json")) {
            content = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(content));
          }

          ta.setText(content);

          splitPa.getItems().addAll(ta);
        }

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

        thestage.sizeToScene();
        topMenuPositioning(sceneReport);
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
    Scene scene = new Scene(rootNode1, thestage.getScene().getWidth(), thestage.getScene().getHeight());
    scene.getStylesheets().add("/styles/style.css");

    thestage.setTitle("DPFManager");
    thestage.setScene(scene);
    thestage.sizeToScene();
    thestage.show();

    SplitPane splitPa1 = (SplitPane) scene.lookup("#splitPa1");
    splitPa1.lookupAll(".split-pane-divider").stream()
        .forEach(
            div -> div.setMouseTransparent(true));

    topMenuPositioning(scene);
  }

  void topMenuPositioning(Scene scene) {
    thestage.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
        Button report = (Button) scene.lookup("#butReport");
        Button checker = (Button) scene.lookup("#butChecker");
        Button about = (Button) scene.lookup("#butAbout");
        if (newSceneWidth.doubleValue() < 989) {
          if (report != null) report.setLayoutX(470.0);
          if (checker != null) checker.setLayoutX(290.0);
          if (about != null) about.setLayoutX(560.0);
        } else {
          double dif = (newSceneWidth.doubleValue() - width) / 2;
          if (report != null) report.setLayoutX(463.0 + dif);
          if (checker != null) checker.setLayoutX(283.0 + dif);
          if (about != null) about.setLayoutX(560.0 + dif);
        }
        VBox pan0 = (VBox) scene.lookup("#box0");
        if (pan0 != null) {
          if (newSceneWidth.doubleValue() < 989) {
            pan0.setPadding(new Insets(0, 0, 0, 0));
          } else {
            double dif = (newSceneWidth.doubleValue() - width) / 2;
            pan0.setPadding(new Insets(0, 0, 0, dif));
          }
        }
      }
    });
    thestage.setWidth(thestage.getWidth() - 1);
    thestage.setWidth(thestage.getWidth() + 1);
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
    if (chkPdf.isSelected()) config.getFormats().add("PDF");
    LoadSceneXml("/fxml/config4.fxml");

    ArrayList<String> classes = TiffConformanceChecker.getAutofixes();

    int ypos = 320;
    int xpos = 252;
    int dify = 50;
    Scene scene = thestage.getScene();
    AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
    boolean first = true;
    for (String className : classes) {
      autofix fix = (autofix) Class.forName("dpfmanager.shell.modules.autofixes." + className).newInstance();

      CheckBox check = new CheckBox();
      check.setText(fix.getDescription());
      check.setId(className);
      check.setLayoutY(ypos);
      check.setLayoutX(xpos);
      check.getStyleClass().add("checkreport");
      check.setTextFill(Paint.valueOf("white"));
      ap2.getChildren().add(check);

      ypos += dify;
      if (!first) {
        Button but = (Button) scene.lookup("#addFix");
        but.setLayoutY(but.getLayoutY() + dify);
        Line lin = (Line) scene.lookup("#line");
        lin.setLayoutY(lin.getLayoutY() + dify);
      } else {
        first = false;
      }
    }
  }

  @FXML
  protected void gotoConfig6(ActionEvent event) throws Exception {
    Fixes fixes = config.getFixes();
    /*if (chkAutoFixLE != null && chkAutoFixLE.isSelected())
      fixes.addFixFromTxt("ByteOrder,LittleEndian");
    if (chkAutoFixBE != null && chkAutoFixBE.isSelected())
      fixes.addFixFromTxt("ByteOrder,BigEndian");
    if (chkAutoFixPersonal != null && chkAutoFixPersonal.isSelected())
      fixes.addFixFromTxt("PrivateData,Clear");*/

    Scene scene = thestage.getScene();
    fixes.ReadFixes(scene);
    fixes.ReadAutofixes(scene);

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
    fileChooser.setInitialDirectory(new File(getConfigDir()));
    fileChooser.setInitialFileName("config.dpf");
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
      Scene scenereport = new Scene(rootNode2, thestage.getScene().getWidth(), thestage.getScene().getHeight());
      scenereport.getStylesheets().add("/styles/style.css");

      /*thestage.setMaxHeight(height);
      thestage.setMinHeight(height);
      thestage.setMaxWidth(width);
      thestage.setMinWidth(width);*/

      thestage.setScene(scenereport);
      thestage.sizeToScene();

      SplitPane splitPa1 = (SplitPane) scenereport.lookup("#splitPa1");
      splitPa1.lookupAll(".split-pane-divider").stream()
          .forEach(
              div -> div.setMouseTransparent(true));

      all_reports_loaded = false;
      ObservableList<ReportRow> data = ReadReports(0, reports_loaded);

      javafx.scene.control.TableView<ReportRow> tabReports = new javafx.scene.control.TableView<ReportRow>();
      tabReports.setId("tab_reports");

      tabReports.setEditable(false);
      TableColumn colDate = new TableColumn("Date");
      colDate.setMinWidth(90);
      colDate.setCellValueFactory(new PropertyValueFactory<ReportRow, String>("date"));

      TableColumn colN = new TableColumn("Files Processed");
      colN.setMinWidth(80);
      colN.setCellValueFactory(
          new PropertyValueFactory<ReportRow, String>("nfiles")
      );

      TableColumn colResult = new TableColumn("Result");
      colResult.setMinWidth(150);
      colResult.setCellValueFactory(
          new PropertyValueFactory<ReportRow, String>("result")
      );

      TableColumn colFixed = new TableColumn("Fixed");
      colFixed.setMinWidth(115);
      colFixed.setCellValueFactory(
          new PropertyValueFactory<ReportRow, String>("fixed")
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

      TableColumn<ReportRow, String> colScore = new TableColumn("Score");
      colScore.setMinWidth(80);
      colScore.setCellValueFactory(
              new PropertyValueFactory("score")
      );

      TableColumn colFormats = new TableColumn("Formats");
      colFormats.setMinWidth(95);
      colFormats.setCellValueFactory(
              new PropertyValueFactory<ReportRow, ObservableMap<String, String>>("formats")
      );

      tabReports.getColumns().addAll(colDate, colN, colResult, colFixed, colErrors, colWarnings, colPassed, colScore, colFormats);
      tabReports.setItems(data);

      tabReports.setLayoutX(82.0);
      tabReports.setLayoutY(270.0);
      tabReports.setPrefHeight(270.0);
      tabReports.setPrefWidth(835.0);
      tabReports.setCursor(Cursor.DEFAULT);

      changeColumnTextColor(colDate, Color.LIGHTGRAY);
      changeColumnTextColor(colN, Color.CYAN);
      changeColumnTextColor(colResult, Color.LIGHTGRAY);
      changeColumnTextColor(colFixed, Color.LIGHTGRAY);
      changeColumnTextColor(colErrors, Color.RED);
      changeColumnTextColor(colWarnings, Color.ORANGE);
      changeColumnTextColor(colPassed, Color.GREENYELLOW);
      changeColumnTextColor(colScore, Color.LIGHTGRAY);

      addChartScore(colScore);
      addFormatIcons(colFormats);

      Button button_load = (Button) scenereport.lookup("#button_load");
      button_load.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          data.addAll(ReadReports(data.size() - 1, reports_loaded));
          if (all_reports_loaded) {
            button_load.setVisible(false);
          }
          //System.out.println(data.size());
          addChartScore(colScore);
        }
      });

      if(all_reports_loaded) {
        button_load.setVisible(false);
      }

      AnchorPane ap2 = (AnchorPane) scenereport.lookup("#pane1");
      ap2.getChildren().add(tabReports);

      topMenuPositioning(scenereport);
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

  private void addChartScore(TableColumn colScore) {
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

  private void addFormatIcons(TableColumn colFormats) {
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
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                icon.setImage(new Image("images/format_" + i + ".png"));

                icon.setCursor(Cursor.HAND);

                icon.setOnMousePressed(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                    getScene().setCursor(Cursor.HAND);
                    if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                      System.out.println("Opening file " + item.get(i));
                      if (!i.equals("pdf")) {
                        ShowReport(item.get(i), i);
                      } else {
                        try {
                          Desktop.getDesktop().open(new File(item.get(i)));
                        } catch (IOException e) {
                          e.printStackTrace();
                        }
                      }
                    }
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

  private void addOperator(String item) {
    ArrayList<String> operators = null;
    ArrayList<String> values = null;
    for (Field field : gui.getFields()) {
      if (field.getName().equals(item)) {
        operators = field.getOperators();
        values = field.getValues();
      }
    }
    if (operators != null) {
      Scene scene = thestage.getScene();
      AnchorPane ap2 = (AnchorPane) scene.lookup("#pane1");
      for (Node node : ap2.getChildren()) {
        Boolean delete_combobox = true;
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
                Button bRemove = null;
                if (hBox1.getChildren().get(1) instanceof ComboBox) {
                  ComboBox bla = (ComboBox) hBox1.getChildren().get(1);
                  String val = (String) bla.getValue();
                  if (val!=null){
                    delete_combobox = false;
                  }
                }
                if (delete_combobox) {
                  while (hBox1.getChildren().size() > 1) {
                    for (int i = 1; i < hBox1.getChildren().size(); i++) {
                      if (hBox1.getChildren().get(i) instanceof Button) {
                        bRemove = (Button) hBox1.getChildren().get(i);
                      }
                      hBox1.getChildren().remove(i);
                    }
                  }

                  hBox1.getChildren().add(comboOp);
                  if (values == null) {
                    TextField value = new NumberTextField();
                    value.getStyleClass().add("txtRule");
                    hBox1.getChildren().add(value);
                  } else {
                    ComboBox comboVal = new ComboBox();
                    for (String value : values) {
                      comboVal.getItems().add(value);
                    }
                    hBox1.getChildren().add(comboVal);
                  }
                  hBox1.getChildren().add(bRemove);
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  private void addFixValue(String item) {
    Scene scene = thestage.getScene();
    Boolean delete_combobox;
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    for (Node node : ap2.getChildren()) {
      delete_combobox = true;
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
              Button bRemove = null;
              if (hBox1.getChildren().get(1) instanceof ComboBox) {
                ComboBox bla = (ComboBox) hBox1.getChildren().get(1);
                String val = (String) bla.getValue();
                if (val!=null){
                  delete_combobox = false;
                }
              }
              if (delete_combobox) {
                while (hBox1.getChildren().size() > 1) {
                  for (int i=1;i<hBox1.getChildren().size();i++)
                  {
                    if (hBox1.getChildren().get(i) instanceof Button)
                    {
                      bRemove = (Button)hBox1.getChildren().get(i);
                    }
                    hBox1.getChildren().remove(i);
                  }
                }

                hBox1.getChildren().add(comboOp);

                if (item.equals("Add Tag")) {
                  TextField value = new TextField();
                  value.getStyleClass().add("txtFix");
                  hBox1.getChildren().add(value);
                }

                hBox1.getChildren().add(bRemove);
                break;
              }
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

    // Remove button
    String styleButton = "-fx-background-color: transparent;\n" +
        "\t-fx-border-width     : 0px   ;\n" +
        "\t-fx-border-radius: 0 0 0 0;\n" +
        "\t-fx-background-radius: 0 0 0; -fx-text-fill: WHITE; -fx-font-weight:bold;";
    String styleButtonPressed = "-fx-border-width: 0px; -fx-background-color: rgba(255, 255, 255, 0.2);";
    Button remove = new Button();
    remove.setText("X");
    remove.styleProperty().bind(
        Bindings
            .when(remove.pressedProperty())
            .then(new SimpleStringProperty(styleButtonPressed))
            .otherwise(new SimpleStringProperty(styleButton)
            )
    );
    remove.setId("ID" + uniqueId);
    remove.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        deleteRule(remove.getId());
      }
    });
    comboBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String t, String item) {
        addOperator(item);
      }
    });
    HBox hBox = new HBox (comboBox, remove);
    hBox.setId("ID" + uniqueId++);
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

  void deleteRule(String id) {
    Scene scene = thestage.getScene();
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    ArrayList<Node> lRemove = new ArrayList<Node>();
    for (Node node : ap2.getChildren()) {
      if (node instanceof HBox) {
        HBox hBox1 = (HBox) node;
        if (hBox1.getId().equals(id)) {
          lRemove.add(node);
        } else {
          if (Integer.parseInt(hBox1.getId().substring(2)) > Integer.parseInt(id.substring(2))) {
            hBox1.setLayoutY(hBox1.getLayoutY() - 50);
          }
        }
      }
    }
    for (Node node : lRemove) {
      ap2.getChildren().remove(node);
    }
    if (addRule.getLayoutY()>339) {
      addRule.setLayoutY(addRule.getLayoutY() - 50);
      if (line.getLayoutY()>defaultLineYlayout){
        line.setLayoutY(line.getLayoutY() - 50);
        continueButton.setLayoutY(continueButton.getLayoutY() - 50);
      }
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
      @Override
      public void changed(ObservableValue ov, String t, String item) {
        addFixValue(item);
      }
    });

    // Remove button
    String styleButton = "-fx-background-color: transparent;\n" +
        "\t-fx-border-width     : 0px   ;\n" +
        "\t-fx-border-radius: 0 0 0 0;\n" +
        "\t-fx-background-radius: 0 0 0; -fx-text-fill: WHITE; -fx-font-weight:bold;";
    String styleButtonPressed = "-fx-border-width: 0px; -fx-background-color: rgba(255, 255, 255, 0.2);";
    Button remove = new Button();
    remove.setText("X");
    remove.styleProperty().bind(
        Bindings
            .when(remove.pressedProperty())
            .then(new SimpleStringProperty(styleButtonPressed))
            .otherwise(new SimpleStringProperty(styleButton)
            )
    );
    remove.setId("ID" + uniqueId);
    remove.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        deleteFix(remove.getId());
      }
    });

    HBox hBox = new HBox (comboBox, remove);
    hBox.setId("ID" + uniqueId++);
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

  void deleteFix(String id) {
    Scene scene = thestage.getScene();
    AnchorPane ap2 = (AnchorPane)scene.lookup("#pane1");
    ArrayList<Node> lRemove = new ArrayList<Node>();
    for (Node node : ap2.getChildren()) {
      if (node instanceof HBox) {
        HBox hBox1 = (HBox) node;
        if (hBox1.getId().equals(id)) {
          lRemove.add(node);
        } else {
          if (Integer.parseInt(hBox1.getId().substring(2)) > Integer.parseInt(id.substring(2))) {
            hBox1.setLayoutY(hBox1.getLayoutY() - 50);
          }
        }
      }
    }
    for (Node node : lRemove) {
      ap2.getChildren().remove(node);
    }
    if (addFix.getLayoutY()>339) {
      addFix.setLayoutY(addFix.getLayoutY() - 50);
      if (line.getLayoutY()>defaultLineYlayout){
        line.setLayoutY(line.getLayoutY() - 50);
        continueButton.setLayoutY(continueButton.getLayoutY() - 50);
      }
    }
  }

  @FXML
  protected void openConfig(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Config");
    fileChooser.setInitialDirectory(new File(getConfigDir()));
    fileChooser.setInitialFileName("config.dpf");
    File file = fileChooser.showOpenDialog(thestage);
    try {
      if (file != null) {
        Scene scene = thestage.getScene();
        AnchorPane pan = (AnchorPane)scene.lookup("#pane1");
        VBox vbox = (VBox) pan.getChildren().get(0);
        final ToggleGroup group;
        if (!vbox.getChildren().isEmpty()) {
          RadioButton radio_old = (RadioButton) vbox.getChildren().get(0);
          group = radio_old.getToggleGroup();
        }
        else{
          group = new ToggleGroup();
        }
        RadioButton radio = new RadioButton();
        radio.setText(file.getAbsolutePath());
        radio.setToggleGroup(group);
        vbox.getChildren().add(radio);
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
  protected void deleteConfig(ActionEvent event) throws Exception{
    Scene scene = thestage.getScene();
    AnchorPane ap3 = (AnchorPane)scene.lookup("#pane1");
    boolean oneChecked = false;
    for (Node node : ap3.getChildren()) {
      if (node instanceof VBox) {
        VBox vBox1 = (VBox) node;
        for (Node nodeIn : vBox1.getChildren()) {
          if (nodeIn instanceof RadioButton) {
            RadioButton radio = (RadioButton) nodeIn;
            if (radio.isSelected()) {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Delete configuration file");
              alert.setHeaderText("Are you sure to delete the configuration file '" + radio.getText() + "'?");
              alert.setContentText("The physical file in disk will be also removed");
              ButtonType buttonNo = new ButtonType("No");
              ButtonType buttonYes = new ButtonType("Yes");
              alert.getButtonTypes().setAll(buttonNo, buttonYes);
              Optional<ButtonType> result = alert.showAndWait();
              if (result.get() == buttonYes){
                File file = new File(radio.getText());
                vBox1.getChildren().remove(nodeIn);
                if (!file.delete()){
                  Alert alert2 = new Alert(Alert.AlertType.ERROR);
                  alert2.setTitle("Error");
                  alert2.setHeaderText("There was an error deleting the configuration file");
                  alert2.showAndWait();
                }
              }
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
    }
  }

  @FXML
  protected void infoFiles(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Help");
    alert.setHeaderText("The path to the files to check");
    alert.setContentText("This can be either a single file or a folder. Only the files with a valid TIF file extension will be processed.");
    alert.showAndWait();
  }

  @FXML
  protected void infoConfig(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Help");
    alert.setHeaderText("Configuration files define the options to check the files (ISO, report format and policy rules)");
    alert.setContentText("You can either create a new configuration file, import a new one from disk, or delete");
    alert.showAndWait();
  }

  @FXML
  protected void browseFile(ActionEvent event) {
    Scene scene = thestage.getScene();
    AnchorPane pan0 = (AnchorPane) scene.lookup("#pane0");
    ComboBox c = (ComboBox) pan0.lookup("#choiceType");
    if (c.getValue() == "File") {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open File");
      fileChooser.setInitialDirectory(new File(getDefaultBrowseDirectory()));
      File file = fileChooser.showOpenDialog(thestage);
      if (file != null) {
        txtFile.setText(file.getPath());
        if (file.exists() && file.getParent() != null && new File(file.getParent()).exists() && new File(file.getParent()).isDirectory()) {
          setDefaultBrowseDirectory(file.getParent());
        }
      }
    }
    else{
      DirectoryChooser folderChooser = new DirectoryChooser();
      folderChooser.setTitle("Open Folder");
      folderChooser.setInitialDirectory(new File(getDefaultBrowseDirectory()));
      File directory = folderChooser.showDialog(thestage);
      if (directory != null) {
        txtFile.setText(directory.getPath());
        setDefaultBrowseDirectory(directory.getPath());
      }
    }
    gui.setSelectedFile(txtFile.getText());
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

  private ObservableList<ReportRow> ReadReports(int start, int count) {
    List<ReportRow> list = new ArrayList<ReportRow>();
    ObservableList<ReportRow> data = FXCollections.observableArrayList(list);

    String baseDir = ReportGenerator.getReportsFolder();
    File reportsDir = new File(baseDir);
    if (reportsDir.exists()) {
      String[] directories = reportsDir.list(new FilenameFilter() {
        @Override
        public boolean accept(File current, String name) {
          return new File(current, name).isDirectory();
        }
      });

      Arrays.sort(directories, Collections.reverseOrder());

      int index = 0;
      for (int i = 0; i < directories.length; i++) {
        String reportDay = directories[i];
        File reportsDay = new File(baseDir + "/" + reportDay);
        String[] directories2 = reportsDay.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });

        // Convert to ints for ordering
        Integer[] int_directories = new Integer[directories2.length];
        for(int j = 0; j < directories2.length; j++) {
          int_directories[j] = Integer.parseInt(directories2[j]);
        }

        Arrays.sort(int_directories, Collections.reverseOrder());

        if (index + directories2.length >= start) {

          String[] available_formats = {"html", "xml", "json", "pdf"};

          for (int j = 0; j < int_directories.length; j++) {
            String reportDir = String.valueOf(int_directories[j]);

            if (index >= start && index < start + count) {
              ReportRow rr = null;
              File reportHtml = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.html");
              if (reportHtml.exists()) {
                rr = ReportRow.createRowFromHtml(reportDay, reportHtml);
              } else {
                File report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.xml");
                if (report.exists()) {
                  rr = ReportRow.createRowFromXml(reportDay, report);
                } else {
                  File reportJson = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary.json");
                  if (reportJson.exists()) {
                    rr = ReportRow.createRowFromJson(reportDay, reportJson);
                  } else {
                    File reportPdf = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report.pdf");
                    if (reportPdf.exists()) {
                      rr = ReportRow.createRowFromPdf(reportDay, reportPdf);
                    }
                  }
                }
              }

              for (String format : available_formats) {
                File report;
                if (format == "json" || format == "xml") {
                  report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/summary." + format);
                } else {
                  report = new File(baseDir + "/" + reportDay + "/" + reportDir + "/report." + format);
                }

                if (report.exists()) {
                  rr.addFormat(format, report.getPath());
                }
              }
              if (rr != null) {
                data.add(rr);
              }

              // Check if all done
              if(i == directories.length -1 && j == directories2.length -1) {
                all_reports_loaded = true;
              }
            }
            index++;
          }

        } else {
          index += directories2.length;
        }
      }
    }

    return data;
  }
}

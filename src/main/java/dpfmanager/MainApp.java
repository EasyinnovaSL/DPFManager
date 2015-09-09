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
 * @author Easy Innova
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager;

import dpfmanager.shell.modules.ProcessInput;
import dpfmanager.shell.modules.interfaces.CommandLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import sun.reflect.annotation.ExceptionProxy;

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
    if (params.getRaw().size() == 1 && params.getRaw().get(0).equals("-gui")) {
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

  private void LoadGui() throws Exception
  {
    String fxmlFile = "/fxml/design.fxml";
    LOG.debug("Loading FXML for main view from: {}", fxmlFile);
    FXMLLoader loader = new FXMLLoader();
    Parent rootNode1 = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

    LOG.debug("Showing JFX scene");

    Scene scenemain = new Scene(rootNode1, width, height);
    scenemain.getStylesheets().add("/styles/style.css");

    thestage.setTitle("DPFManager");
    thestage.setScene(scenemain);
    thestage.show();
  }

  @FXML
  protected void openFile(ActionEvent event) throws Exception {
    List<String> allowedExtensions = new ArrayList<String>();
    ArrayList<String> files = new ArrayList<String>();
    allowedExtensions.add(".tif");
    files.add(txtFile.getText());
    boolean bl = radBL.isSelected() || radAll.isSelected();
    boolean ep = radEP.isSelected() || radAll.isSelected();
    boolean it = radIT.isSelected() || radAll.isSelected();
    ProcessInput pi = new ProcessInput(allowedExtensions, bl, ep, it);
    String filename = pi.ProcessFiles(files, false, false, true, "", true);

    Scene scenereport = new Scene(new Group(), 1000, 1000);
    VBox root = new VBox();
    final WebView browser = new WebView();
    browser.setPrefWidth(1000);
    browser.setPrefHeight(1000);
    final WebEngine webEngine = browser.getEngine();
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(browser);
    webEngine.load("file:///" + System.getProperty("user.dir") + "/" + filename);
    root.getChildren().addAll(scrollPane);
    scenereport.setRoot(root);

    thestage.setScene(scenereport);
    thestage.sizeToScene();
  }

  @FXML
  protected void gotoMain(ActionEvent event) throws Exception {
    LoadGui();
  }

  @FXML
  protected void gotoReport(ActionEvent event) throws Exception {
    FXMLLoader loader2 = new FXMLLoader();
    String fxmlFile2 = "/fxml/design2.fxml";
    Parent rootNode2 = (Parent) loader2.load(getClass().getResourceAsStream(fxmlFile2));
    Scene scenereport = new Scene(rootNode2, width, height);
    scenereport.getStylesheets().add("/styles/style.css");

    thestage.setScene(scenereport);
    thestage.sizeToScene();
  }

  @FXML
  protected void browseFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    File file = fileChooser.showOpenDialog(thestage);
    txtFile.setText(file.getPath());
  }
}

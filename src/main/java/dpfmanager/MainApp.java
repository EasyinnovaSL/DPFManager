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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Class MainApp.
 */
public class MainApp extends Application {
  /**
   * The Constant LOG.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);

  private static Stage _stage;

  @FXML
  private TextField txtFile;

  @FXML
  private RadioButton radBL;

  @FXML
  private RadioButton radEP;

  @FXML
  private RadioButton radIT;

  @FXML
  private RadioButton radAll;

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws Exception the exception
   */
  public static void main(final String[] args) throws Exception {
    launch(args);
  }

  @FXML
  protected void openFile(ActionEvent event) {
    List<String> allowedExtensions = new ArrayList<String>();
    ArrayList<String> files = new ArrayList<String>();

    allowedExtensions.add(".tif");
    files.add(txtFile.getText());

    boolean bl = radBL.isSelected() || radAll.isSelected();
    boolean ep = radEP.isSelected() || radAll.isSelected();
    boolean it = radIT.isSelected() || radAll.isSelected();

    ProcessInput pi = new ProcessInput(allowedExtensions, bl, ep, it);
    pi.ProcessFiles(files, false, false, true, "", false);
  }

  @FXML
  protected void browseFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    File file = fileChooser.showOpenDialog(_stage);

    txtFile.setText(file.getPath());
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
      // GUI
      LOG.info("Starting Hello JavaFX and Maven demonstration application");

      String fxmlFile = "/fxml/design.fxml";
      LOG.debug("Loading FXML for main view from: {}", fxmlFile);
      FXMLLoader loader = new FXMLLoader();
      Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

      LOG.debug("Showing JFX scene");
      GraphicsDevice gd =
          GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

      final int width = 1439;
      final int height = gd.getDisplayMode().getHeight() - 75;

      Scene scene = new Scene(rootNode, width, height);
      scene.getStylesheets().add("/styles/style.css");

      stage.setTitle("DPFManager");
      stage.setScene(scene);
      _stage = stage;
      stage.show();
    } else {
      // Command Line
      CommandLine cl = new CommandLine(params);
      cl.launch();
      Platform.exit();
    }
  }
}

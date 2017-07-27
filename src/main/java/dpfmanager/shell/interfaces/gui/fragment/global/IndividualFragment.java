/**
 * <h1>PeriodicFragment.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.interfaces.gui.fragment.global;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.NavMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.report.util.ReportIndividualGui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import org.apache.commons.io.FileUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_INDIVIDUAL,
    viewLocation = "/fxml/fragments/individual.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class IndividualFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;


  /**
   * Full check elements
   */
  @FXML
  private Label fInput;
  @FXML
  private Label fPath;
  @FXML
  private Label fErrors;
  @FXML
  private Label fWarnings;
  @FXML
  private HBox fFormatsBox;
  @FXML
  private ImageView fOkImage;
  @FXML
  private ImageView fKoImage;
  @FXML
  private ImageView fQuestionImage;
  @FXML
  private GridPane gridFull;

  /**
   * Quick check elements
   */
  @FXML
  private Label qInput;
  @FXML
  private Label qPath;
  @FXML
  private HBox qFormatsBox;
  @FXML
  private ImageView qOkImage;
  @FXML
  private ImageView qKoImage;
  @FXML
  private GridPane gridQuick;


  /* Report Row info */
  private ReportIndividualGui info;

  public void init(ReportIndividualGui r) {
    info = r;
    info.load();
    if (info.isQuick()){
      loadReportRowQuick();
    } else if (!info.isOld()){
      loadReportRowFull();
    } else {
      loadReportRowOld();
    }
  }

  public void updateIcons(){
    info.readFormats();
    if (info.isQuick()){
      addFormatIcons(qFormatsBox);
    } else {
      addFormatIcons(fFormatsBox);
    }
  }

  private void loadReportRowQuick() {
    qInput.setText(info.getFilename());
    qPath.setText(info.getFilePath());

    // Result
    if (info.getErrors() > 0) {
      NodeUtil.showNode(qKoImage);
      NodeUtil.hideNode(qOkImage);
    } else {
      NodeUtil.hideNode(qKoImage);
      NodeUtil.showNode(qOkImage);
    }

    // Format Icons
    addFormatIcons(qFormatsBox);

    // Show
    NodeUtil.showNode(gridQuick);
    NodeUtil.hideNode(gridFull);
  }

  private void loadReportRowFull() {
    fInput.setText(info.getFilename());
    fPath.setText(info.getFilePath());

    // Result
    NodeUtil.hideNode(fQuestionImage);
    if (info.getErrors() > 0) {
      NodeUtil.showNode(fKoImage);
      NodeUtil.hideNode(fOkImage);
    } else {
      NodeUtil.hideNode(fKoImage);
      NodeUtil.showNode(fOkImage);
    }

    // Errors
    fErrors.setText(bundle.getString("errors").replace("%1", info.getErrors() + ""));
    if (info.getErrors() > 0) {
      fErrors.setTextFill(Color.RED);
    } else {
      fErrors.setTextFill(Color.YELLOWGREEN);
    }

    // Warnings
    fWarnings.setText(bundle.getString("warnings").replace("%1", "" + info.getWarnings() + ""));
    if (info.getWarnings() > 0) {
      fWarnings.setTextFill(Color.ORANGE);
    } else {
      fWarnings.setTextFill(Color.LIGHTGREY);
    }

    // Format Icons
    addFormatIcons(fFormatsBox);

    // Show
    NodeUtil.showNode(gridFull);
    NodeUtil.hideNode(gridQuick);
  }

  private void loadReportRowOld() {
    fInput.setText(info.getFilename());
    fPath.setText(info.getFilePath());

    // Result
    NodeUtil.hideNode(fKoImage);
    NodeUtil.hideNode(fOkImage);
    NodeUtil.showNode(fQuestionImage);

    // Errors
    fErrors.setText("");

    // Warnings
    fWarnings.setText("");

    // Format Icons
    addFormatIcons(fFormatsBox);

    // Show
    NodeUtil.showNode(gridFull);
    NodeUtil.hideNode(gridQuick);
  }

  private void addFormatIcons(HBox formatsBox) {
    formatsBox.getChildren().clear();
    List<String> sortedFormats = Arrays.asList("html","pdf","xml","mets", "json");
    Map<String, String> itemRead = info.getFormats();
    Integer version = info.getReportVersion();
    Map<String, String> item = new HashMap<>();
    if (version > 0) {
      // Transform reports
      for (String format : sortedFormats){
        if (!item.containsKey(format)) item.put(format, (itemRead.containsKey(format)) ? itemRead.get(format) : null);
      }
    } else {
      item = (itemRead != null) ? itemRead : new HashMap<>();
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
      } else if (version > 1){
        // Transformation need
        icon.setOpacity(0.4);
        icon.setOnMouseEntered(event -> icon.setOpacity(1.0));
        icon.setOnMouseExited(event -> icon.setOpacity(0.4));
        sMessage = new ShowMessage(i, info.getPath(), info.getConfig());
      }
      if (sMessage != null){
        final ShowMessage finalSMessage = sMessage;
        icon.setOnMouseClicked(event -> {
          if (event.getButton() == MouseButton.PRIMARY) {
            ArrayMessage am = new ArrayMessage();
            am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage(UiMessage.Type.SHOW));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_NAV, new NavMessage(i));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, finalSMessage);
            context.send(GuiConfig.PERSPECTIVE_SHOW, am);
          }
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem itemShow = new MenuItem(bundle.getString("showReport"));
        itemShow.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            ArrayMessage am = new ArrayMessage();
            am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage(UiMessage.Type.SHOW));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_NAV, new NavMessage(i));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, finalSMessage);
            context.send(GuiConfig.PERSPECTIVE_SHOW, am);
          }
        });
        MenuItem itemGenerate = new MenuItem(bundle.getString("generateReport"));
        itemGenerate.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            ArrayMessage am = new ArrayMessage();
            am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage(UiMessage.Type.SHOW));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_NAV, new NavMessage(i));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, finalSMessage);
            context.send(GuiConfig.PERSPECTIVE_SHOW, am);
          }
        });
        MenuItem itemDownload = new MenuItem(bundle.getString("downloadReport"));
        itemDownload.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            downloadReport(path);
          }
        });

        if (path == null) {
          contextMenu.getItems().addAll(itemGenerate);
        } else {
          contextMenu.getItems().addAll(itemShow, itemDownload);
        }

        icon.setOnContextMenuRequested(e -> contextMenu.show(icon, e.getScreenX(), e.getScreenY()));
        formatsBox.getChildren().add(icon);
      }
    }
  }

  public void downloadReport(String path){
    File src = new File(path);
    if (!src.exists() || !src.isFile()) return;

    String name = src.getName().substring(0, src.getName().indexOf("."));
    String extension = src.getName().substring(src.getName().indexOf("."));

    FileChooser fileChooser = new FileChooser();

    //Set extension filter
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extension.toLowerCase().substring(1, extension.length() -1) + " files (*"+extension+")", "*" + extension);
    fileChooser.getExtensionFilters().add(extFilter);
    fileChooser.setInitialFileName(name);

    //Show save file dialog
    File dest = fileChooser.showSaveDialog(GuiWorkbench.getMyStage());

    if(dest != null){
      try {
        FileUtils.copyFile(src, dest);
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorSavingReport")));
      }
    }
  }

  @FXML
  protected void makeFullReport(ActionEvent event) throws Exception {
    String inputFile = info.getFilePath();
    if (!new File(inputFile).exists()) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, bundle.getString("filesNotFound"), inputFile));
    } else {
      context.send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(inputFile, info.getConfig(), 100, false, false));
    }
  }

}
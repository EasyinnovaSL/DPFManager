/**
 * <h1>ReportsView.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.interfaces.gui.component.global;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.NavMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.global.comparators.IndividualComparator;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.interfaces.gui.fragment.global.IndividualFragment;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.report.util.ReportIndividualGui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import org.apache.commons.io.FileUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_GLOBAL,
    name = GuiConfig.COMPONENT_GLOBAL,
    viewLocation = "/fxml/global.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_GLOBAL)
public class GlobalView extends DpfView<GlobalModel, GlobalController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  private ReportGui info;
  @FXML
  private CheckBox htmlCheck;
  @FXML
  private CheckBox xmlCheck;
  @FXML
  private CheckBox metsCheck;
  @FXML
  private CheckBox pdfCheck;
  @FXML
  private CheckBox jsonCheck;
  @FXML
  private CheckBox checkError;
  @FXML
  private CheckBox checkCorrect;

  /**
   * Global report elements
   */
  @FXML
  private ProgressIndicator progressGlobal;
  @FXML
  private VBox vboxGlobal;
  @FXML
  private ImageView globalImage;
  @FXML
  private Label resultLabel;
  @FXML
  private VBox globalVBox;
  @FXML
  private Label globalDate;
  @FXML
  private Label globalScore;
  @FXML
  private Label globalDuration;
  @FXML
  private Label globalFiles;
  @FXML
  private Label globalErrors;
  @FXML
  private Label globalWarnings;
  @FXML
  private Label globalPassed;
  @FXML
  private HBox globalScoreBox;
  @FXML
  private HBox globalFormatsBox;
  @FXML
  private HBox globalActionsBox;

  /**
   * Individual reports elements
   */
  @FXML
  private GridPane gridHeadersFull;
  @FXML
  private GridPane gridHeadersQuick;
  @FXML
  private VBox individualsVBox;
  @FXML
  private Pagination pagination;
  @FXML
  private ProgressIndicator indicator;
  @FXML
  private StackPane gridStackPane;
  @FXML
  private Label labelOld;

  private Map<Integer, ManagedFragmentHandler<IndividualFragment>> individualHandlers;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  public Context getContext() {
    return context;
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(GuiGlobalMessage.class)) {
      GuiGlobalMessage gMessage = message.getTypedMessage(GuiGlobalMessage.class);
      if (gMessage.isRead()) {
        individualHandlers = new HashMap<>();
        currentMode = IndividualComparator.Mode.ERRORS;
        currentOrder = IndividualComparator.Order.DESC;
        gMessage.getReportGui().load();
        getController().readIndividualReports(gMessage.getReportGui());
      } else if (gMessage.isAddIndividual()) {
        gMessage.getReportIndividualGui().load();
        gMessage.getReportIndividualGui().loadFormats();
      } else if (gMessage.isSort()){
        getController().sortIndividuals();
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(GuiGlobalMessage.class)) {
      GuiGlobalMessage gMessage = message.getTypedMessage(GuiGlobalMessage.class);
      if (gMessage.isInit()) {
        showLoading();
        gMessage.setType(GuiGlobalMessage.Type.READ);
        context.send(gMessage);
      } else if (gMessage.isRead()) {
        initGlobalReport(gMessage.getReportGui());
        initPagination();
      } else if (gMessage.isAddIndividual()) {
        addIndividualReport(gMessage.getVboxId(), gMessage.getReportIndividualGui());
      } else if (gMessage.isSort()){
        if (pagination.getCurrentPageIndex() != 0) {
          pagination.setCurrentPageIndex(0);
        } else {
          reloadFirstPage();
        }
      }
    } else if (message != null && message.isTypeOf(AlertMessage.class)) {
      AlertMessage am = message.getTypedMessage(AlertMessage.class);
      if (am.hasResult() && am.getResult()) {
        File dir = new File(info.getInternalReportFolder());
        try {
          FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
          e.printStackTrace();
        }
        context.send(GuiConfig.PERSPECTIVE_REPORTS, new UiMessage());
      }
    } else if (message != null && message.isTypeOf(UiMessage.class)) {
      info.readFormats();
      addFormatIcons(info);
      updateIndividualsReports();
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new GlobalModel(context));
    setController(new GlobalController());
    getModel().setResourcebundle(bundle);
    getController().setResourcebundle(bundle);
    individualHandlers = new HashMap<>();
    pagination.setSkin(new PaginationBetterSkin(pagination));
    indicator.setProgress(-1);
    progressGlobal.setProgress(-1);
  }

  /**
   * Global report
   */
  private void initGlobalReport(ReportGui info) {
    this.info = info;
    NodeUtil.hideNode(gridHeadersQuick);
    NodeUtil.hideNode(gridHeadersFull);
    NodeUtil.hideNode(labelOld);
    info.readFormats();
    if (info.getErrors() == 0) {
      resultLabel.setText(bundle.getString("passedCheck"));
      resultLabel.setTextFill(Color.YELLOWGREEN);
      globalImage.setImage(new Image("images/icons/ok.png"));
    } else {
      resultLabel.setText(bundle.getString("errorCheck"));
      resultLabel.setTextFill(Color.RED);
      globalImage.setImage(new Image("images/icons/ko.png"));
    }
    globalDate.setText(info.getDate() + " " + info.getTime());
    globalScore.setText(info.getScore() + "%");
    globalFiles.setText(info.getNfiles() + "");
    globalErrors.setText(bundle.getString("errors").replace("%1", info.getErrors() + ""));
    globalWarnings.setText(bundle.getString("passedWithWarnings").replace("%1", "" + info.getWarnings() + ""));
    globalPassed.setText(bundle.getString("passed").replace("%1", "" + info.getPassed() + ""));
    if (info.getGlobalReport() != null) {
      globalDuration.setText(readableDuration(info.getGlobalReport()));
    }
    if (!isOldReport() && info.getGlobalReport().getConfig().isQuick()) {
      NodeUtil.showNode(gridHeadersQuick);
    } else {
      NodeUtil.showNode(gridHeadersFull);
    }
    addChartScore(info);
    addFormatIcons(info);
    addActionsIcons(info);
    showLoadingReports();
  }

  private String readableDuration(GlobalReport gr){
    return gr.getDurationHours() + ":" + gr.getDurationMinutes() + ":" + ((gr.getDurationMillis() > 500) ? gr.getDurationSeconds() + 1 : gr.getDurationSeconds());
  }

  private void addChartScore(ReportGui info) {
    Double score = info.getScore() * 1.0;

    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Correct", score),
            new PieChart.Data("Error", 100 - score));

    PieChart chart = new PieChart(pieChartData);
    chart.setId("pie_chart_global");
    chart.setMinSize(80, 80);
    chart.setMaxSize(80, 80);

    globalScoreBox.getChildren().clear();
    globalScoreBox.getChildren().add(chart);
  }

  private void addFormatIcons(ReportGui info) {
    Map<String, String> itemRead = info.getFormats();
    Integer version = info.getReportVersion();
    GlobalReport gr = info.getGlobalReport();
    globalFormatsBox.getChildren().clear();
    List<String> sortedFormats = Arrays.asList("html", "pdf", "xml", "json");
    Map<String, String> item = new HashMap<>();
    if (version > 0) {
      // Transform reports
      for (String format : sortedFormats) {
        if (!item.containsKey(format))
          item.put(format, (itemRead.containsKey(format)) ? itemRead.get(format) : null);
      }
    } else {
      item = itemRead;
    }
    for (String i : sortedFormats) {
      if (!item.containsKey(i)) continue;
      ImageView icon = new ImageView();
      icon.setId("but" + i);
      icon.setFitHeight(35);
      icon.setFitWidth(35);
      icon.setCursor(Cursor.HAND);
      icon.setImage(new Image("images/formats/" + i + ".png"));
      Tooltip.install(icon, new Tooltip(i.toUpperCase()));

      String path = item.get(i);
      ShowMessage sMessage = null;
      if (path != null && new File(path).exists()) {
        // Show directly
        sMessage = new ShowMessage(i, path);
      } else if (gr.getVersion() > 1) {
        // Transformation need
        icon.setOpacity(0.4);
        icon.setOnMouseEntered(event -> icon.setOpacity(1.0));
        icon.setOnMouseExited(event -> icon.setOpacity(0.4));
        Long formatUuid = Long.parseLong(info.getUuid() + Character.getNumericValue(i.charAt(0)));
        sMessage = new ShowMessage(formatUuid, i, info, true);
      }
      if (sMessage != null) {
        final ShowMessage finalSMessage = sMessage;
        icon.setOnMouseClicked(event -> {
          if (event.getButton() == MouseButton.PRIMARY) {
            ArrayMessage am = new ArrayMessage();
            am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage(UiMessage.Type.SHOW));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_NAV, new NavMessage(i));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(ShowMessage.Type.LOAD));
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
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(ShowMessage.Type.LOAD));
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
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(ShowMessage.Type.LOAD));
            am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, finalSMessage);
            context.send(GuiConfig.PERSPECTIVE_SHOW, am);
          }
        });
        MenuItem itemDownload = new MenuItem(bundle.getString("downloadReport"));
        itemDownload.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            getController().downloadReport(path);
          }
        });

        if (path == null) {
          contextMenu.getItems().addAll(itemGenerate);
        } else {
          contextMenu.getItems().addAll(itemShow, itemDownload);
        }

        icon.setOnContextMenuRequested(e -> contextMenu.show(icon, e.getScreenX(), e.getScreenY()));
        globalFormatsBox.getChildren().add(icon);
      }
    }
  }

  public void addActionsIcons(ReportGui info) {
    globalActionsBox.getChildren().clear();
    String path = info.getDeletePath();
    Integer size = 25;

    // Open folder button
    Button iconFolder = new Button();
    iconFolder.setMinHeight(size);
    iconFolder.setPrefHeight(size);
    iconFolder.setMaxHeight(size);
    iconFolder.setMinWidth(size);
    iconFolder.setPrefWidth(size);
    iconFolder.setMaxWidth(size);
    iconFolder.getStyleClass().addAll("folder-img", "icon-img");
    iconFolder.setCursor(Cursor.HAND);
    iconFolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // Open folder
        File file = new File(path);
        File dir = new File(file.getParent());
        if (Desktop.isDesktopSupported()) {
          new Thread(() -> {
            try {
              Desktop.getDesktop().open(dir);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }).start();
        }
      }
    });
    globalActionsBox.getChildren().add(iconFolder);

    // Trash button
    Button icon = new Button();
    icon.setMinHeight(size);
    icon.setPrefHeight(size);
    icon.setMaxHeight(size);
    icon.setMinWidth(size);
    icon.setPrefWidth(size);
    icon.setMaxWidth(size);
    icon.getStyleClass().addAll("delete-img", "icon-img");
    icon.setCursor(Cursor.HAND);
    icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // Alert confirmation
        AlertMessage am = new AlertMessage(AlertMessage.Type.CONFIRMATION, bundle.getString("deleteConfirmationGlobal"), bundle.getString("deleteInfoGlobal"));
        am.setTitle(bundle.getString("deleteTitle"));
        getContext().send(BasicConfig.MODULE_MESSAGE, am);
      }
    });
    globalActionsBox.getChildren().add(icon);
  }

  /**
   * Individual reports
   */

  boolean paginationInitiated = false;

  public void initPagination() {
    Integer oldPageCount = pagination.getPageCount();
    Integer oldPageIndex = pagination.getCurrentPageIndex();
    pagination.setPageCount(getController().getPagesCount());
    pagination.setCurrentPageIndex(0);
    if (paginationInitiated) {
      if (oldPageCount.equals(getController().getPagesCount()) && oldPageIndex.equals(0)) {
        reloadFirstPage();
      }
    } else {
      paginationInitiated = true;
      pagination.setPageFactory(new Callback<Integer, Node>() {
        @Override
        public Node call(Integer pageIndex) {
          return createPage(pageIndex);
        }
      });
    }
  }

  public void reloadFirstPage() {
    Node node = pagination.lookup("#vboxIndividuals0");
    if (node != null) {
      VBox vbox = (VBox) node;
      vbox.getChildren().clear();
      getController().loadAndPrintIndividuals("#vboxIndividuals0", 0);
    }
  }

  public VBox createPage(Integer pageIndex) {
    String id = "vboxIndividuals" + pageIndex;
    VBox box = new VBox();
    box.setId(id);
    box.setStyle("-fx-padding: 0 0 10 0;");
    getController().loadAndPrintIndividuals("#" + id, pageIndex);
    return box;
  }

  private void addIndividualReport(String vboxId, ReportIndividualGui rig) {
    hideLoadingReports();
    Node node = pagination.lookup(vboxId);
    if (node != null) {
      VBox vbox = (VBox) node;
      ManagedFragmentHandler<IndividualFragment> handler;
      if (individualHandlers.containsKey(rig.getId())) {
        handler = individualHandlers.get(rig.getId());
        handler.getController().updateIcons();
      } else {
        handler = context.getManagedFragmentHandler(IndividualFragment.class);
        handler.getController().init(rig);
        individualHandlers.put(rig.getId(), handler);
      }
      vbox.getChildren().add(handler.getFragmentNode());
      if (rig.isLast()) showBottom();
    }
  }

  private void updateIndividualsReports() {
    for (ManagedFragmentHandler<IndividualFragment> handler : individualHandlers.values()) {
      handler.getController().updateIcons();
    }
  }

  private IndividualComparator.Mode currentMode;
  private IndividualComparator.Order currentOrder;

  /**
   * Quick sort
   */
  @FXML
  private HBox hboxNameQ;
  @FXML
  private HBox hboxPathQ;
  @FXML
  private HBox hboxResultQ;

  @FXML
  protected void clickedColNameQ(MouseEvent event) throws Exception {
    clickedCol(hboxNameQ, IndividualComparator.Mode.NAME);
  }

  @FXML
  protected void clickedColPathQ(MouseEvent event) throws Exception {
    clickedCol(hboxPathQ, IndividualComparator.Mode.PATH);
  }

  @FXML
  protected void clickedColResultQ(MouseEvent event) throws Exception {
    clickedCol(hboxResultQ, IndividualComparator.Mode.RESULT);
  }

  /**
   * Full sort
   */
  @FXML
  private HBox hboxNameF;
  @FXML
  private HBox hboxPathF;
  @FXML
  private HBox hboxResultF;
  @FXML
  private HBox hboxErrorsF;
  @FXML
  private HBox hboxWarningsF;

  @FXML
  protected void clickedColNameF(MouseEvent event) throws Exception {
    clickedCol(hboxNameF, IndividualComparator.Mode.NAME);
  }
  @FXML
  protected void clickedColPathF(MouseEvent event) throws Exception {
    clickedCol(hboxPathF, IndividualComparator.Mode.PATH);
  }
  @FXML
  protected void clickedColResultF(MouseEvent event) throws Exception {
    clickedCol(hboxResultF, IndividualComparator.Mode.RESULT);
  }
  @FXML
  protected void clickedColErrorsF(MouseEvent event) throws Exception {
    clickedCol(hboxErrorsF, IndividualComparator.Mode.ERRORS);
  }
  @FXML
  protected void clickedColWarningsF(MouseEvent event) throws Exception {
    clickedCol(hboxWarningsF, IndividualComparator.Mode.WARNINGS);
  }

  private void clickedCol(HBox hbox, IndividualComparator.Mode mode){
    if (indicator.isVisible()) return;

    // Visual sort
    removeArrows();
    if (currentMode.equals(mode)) {
      swapOrder();
    } else {
      currentOrder = getDefaultOrder(mode);
    }
    currentMode = mode;
    addArrow(hbox);

    // Show loading
    showLoadingReports();
    context.send(GuiConfig.COMPONENT_GLOBAL, new GuiGlobalMessage(GuiGlobalMessage.Type.SORT));
  }

  public void showLoading() {
    showLoadingReports();
    NodeUtil.showNode(progressGlobal);
    NodeUtil.hideNode(vboxGlobal);
  }

  public void showLoadingReports() {
    NodeUtil.showNode(vboxGlobal);
    NodeUtil.hideNode(progressGlobal);
    NodeUtil.hideNode(pagination);
    NodeUtil.showNode(indicator);
    NodeUtil.hideNode(labelOld);
    hideBottom();
  }

  public void hideLoadingReports() {
    NodeUtil.showNode(pagination);
    NodeUtil.hideNode(indicator);
    if (isOldReport()) {
      NodeUtil.showNode(labelOld);
    } else {
      NodeUtil.hideNode(labelOld);
    }
  }

  private void addArrow(HBox hbox) {
    String type = (currentOrder.equals(IndividualComparator.Order.ASC)) ? "up" : "down";
    ImageView icon = new ImageView();
    icon.setFitHeight(10);
    icon.setFitWidth(10);
    icon.setImage(new Image("images/icons/caret-" + type + ".png"));
    hbox.getChildren().add(icon);
  }

  private void removeArrows() {
    hboxNameQ.getChildren().remove(1, hboxNameQ.getChildren().size());
    hboxResultQ.getChildren().remove(1, hboxResultQ.getChildren().size());
    hboxPathQ.getChildren().remove(1, hboxPathQ.getChildren().size());

    hboxNameF.getChildren().remove(1, hboxNameF.getChildren().size());
    hboxErrorsF.getChildren().remove(1, hboxErrorsF.getChildren().size());
    hboxWarningsF.getChildren().remove(1, hboxWarningsF.getChildren().size());
    hboxResultF.getChildren().remove(1, hboxResultF.getChildren().size());
    hboxPathF.getChildren().remove(1, hboxPathF.getChildren().size());
  }

  public IndividualComparator.Order getDefaultOrder(IndividualComparator.Mode mode){
    if (mode.equals(IndividualComparator.Mode.NAME)) return IndividualComparator.Order.ASC;
    return IndividualComparator.Order.DESC;
  }

  /**
   * Generate all reports
   */

  @FXML
  private VBox vboxGenerators;
  @FXML
  private VBox vboxTransforms;
  @FXML
  private Button buttonTransforms;
  @FXML
  private Button buttonGenerate;

  @FXML
  protected void showGenerate(ActionEvent event) throws Exception {
    showGenerate();
    htmlCheck.setSelected(false);
    xmlCheck.setSelected(false);
    metsCheck.setSelected(false);
    jsonCheck.setSelected(false);
    pdfCheck.setSelected(false);
  }

  @FXML
  protected void showTransform(ActionEvent event) throws Exception {
    showTransform();
    checkError.setSelected(true);
    checkCorrect.setSelected(false);
  }

  private void showGenerate() {
    NodeUtil.showNode(vboxGenerators);
    NodeUtil.hideNode(buttonGenerate);
  }

  private void showTransform() {
    NodeUtil.showNode(vboxTransforms);
    NodeUtil.hideNode(buttonTransforms);
  }

  @FXML
  protected void hideGenerate(ActionEvent event) throws Exception {
    hideGenerate();
  }

  private void hideGenerate() {
    NodeUtil.hideNode(vboxGenerators);
    NodeUtil.showNode(buttonGenerate);
  }

  @FXML
  protected void hideTransforms(ActionEvent event) throws Exception {
    hideTransforms();
  }

  private void hideTransforms() {
    NodeUtil.hideNode(vboxTransforms);
    NodeUtil.showNode(buttonTransforms);
  }

  @FXML
  protected void generateReportsClicked(ActionEvent event) throws Exception {
    String uuidStr = info.getUuid();
    for (String i : getSelectedFormats()) {
      uuidStr += getNumericValue(i.charAt(0));
    }
    ShowMessage sMessage = new ShowMessage(Long.parseLong(uuidStr), getSelectedFormats(), info, false);
    ArrayMessage am = new ArrayMessage();
    am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
    am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, sMessage);
    context.send(GuiConfig.PERSPECTIVE_SHOW, am);
  }

  @FXML
  protected void transformReports(ActionEvent event) throws Exception {
    if (getController().transformReport()) {
      hideTransforms();
    }
  }

  private void hideBottom(){
    NodeUtil.hideNode(vboxTransforms);
    NodeUtil.hideNode(buttonTransforms);
    NodeUtil.hideNode(vboxGenerators);
    NodeUtil.hideNode(buttonGenerate);
  }

  private void showBottom(){
    if (!isOldReport()){
      if (info.getGlobalReport().getConfig().isQuick()){
        NodeUtil.showNode(buttonTransforms);
      }
      NodeUtil.showNode(buttonGenerate);
    }
  }

  private String getNumericValue(Character ch) {
    if (ch.equals('h')) return "1";
    if (ch.equals('x')) return "2";
    if (ch.equals('m')) return "3";
    if (ch.equals('p')) return "4";
    if (ch.equals('j')) return "5";
    return "0";
  }

  public List<String> getSelectedFormats() {
    List<String> formats = new ArrayList<>();
    if (htmlCheck.isSelected()) formats.add("html");
    if (xmlCheck.isSelected()) formats.add("xml");
    if (metsCheck.isSelected()) formats.add("mets");
    if (pdfCheck.isSelected()) formats.add("pdf");
    if (jsonCheck.isSelected()) formats.add("json");
    return formats;
  }

  private void swapOrder(){
    if (currentOrder.equals(IndividualComparator.Order.ASC)) {
      currentOrder = IndividualComparator.Order.DESC;
    } else {
      currentOrder = IndividualComparator.Order.ASC;
    }
  }

  public IndividualComparator.Mode getCurrentMode() {
    return currentMode;
  }

  public IndividualComparator.Order getCurrentOrder() {
    return currentOrder;
  }

  public ReportGui getInfo() {
    return info;
  }

  public boolean isErrors(){
    return checkError.isSelected();
  }

  public boolean isCorrect(){
    return checkCorrect.isSelected();
  }

  public boolean isOldReport() {
    return info.getGlobalReport() == null;
  }

}

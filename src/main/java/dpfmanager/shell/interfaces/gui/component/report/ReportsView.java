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

package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.global.PaginationBetterSkin;
import dpfmanager.shell.interfaces.gui.component.global.comparators.IndividualComparator;
import dpfmanager.shell.interfaces.gui.component.report.comparators.ReportsComparator;
import dpfmanager.shell.interfaces.gui.fragment.ReportFragment;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.report.util.ReportGui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_REPORTS,
    name = GuiConfig.COMPONENT_REPORTS,
    viewLocation = "/fxml/summary.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_REPORTS)
public class ReportsView extends DpfView<ReportsModel, ReportsController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  // New view elements
  @FXML
  private VBox mainVBox;
  @FXML
  private AnchorPane paneStatistics;
  @FXML
  private Pagination pagination;
  @FXML
  private Button reloadButton2;

  // View elements
  @FXML
  private VBox reportsVbox;
  @FXML
  private VBox vboxReports;
  @FXML
  private Label labelEmpty;
  @FXML
  private ProgressIndicator indicator;
  @FXML
  private Label labelSize;
  @FXML
  private HBox hboxSize;
  @FXML
  private VBox hboxOptions;
  @FXML
  private Button clearOptionsButton;
  @FXML
  private RadioButton radAll;
  @FXML
  private RadioButton radOlder;
  @FXML
  private ToggleGroup toggleClear;
  @FXML
  private DatePicker datePicker;

  private Map<String, ManagedFragmentHandler<ReportFragment>> reportHandlers;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(ReportsMessage.class)) {
      ReportsMessage rMessage = message.getTypedMessage(ReportsMessage.class);
      if (rMessage.isRead()) {
        currentMode = ReportsComparator.Mode.DATE;
        currentOrder = ReportsComparator.Order.DESC;
        getController().readReports();
      } else if (rMessage.isSize()) {
        getModel().readReportsSize();
      } else if (rMessage.isAdd()) {
        rMessage.getReportGui().load();
      } else if (rMessage.isSort()){
        getController().sortReports();
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(ReportsMessage.class)) {
      ReportsMessage rMessage = message.getTypedMessage(ReportsMessage.class);
      if (rMessage.isReload()) {
        showLoading();
        context.send(new ReportsMessage(ReportsMessage.Type.READ));
      } else if (rMessage.isRead()) {
        initPagination();
      } else if (rMessage.isSize()) {
        printSize(getModel().getReportsSize());
      } else if (rMessage.isDelete()) {
        deleteReportGui(rMessage.getUuid());
      } else if (rMessage.isAdd()) {
        addReportGui(rMessage.getVboxId(), rMessage.getReportGui());
      } else if (rMessage.isSort()){
        if (pagination.getCurrentPageIndex() != 0) {
          pagination.setCurrentPageIndex(0);
        } else {
          reloadPage(0);
        }
      }
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new ReportsModel(context));
    setController(new ReportsController());
    getModel().setResourcebundle(bundle);
    hideClearOptions();
    reportHandlers = new HashMap<>();
    pagination.setSkin(new PaginationBetterSkin(pagination));
  }

  /**
   * Keep functions
   */
  private void loadReportsSize(){
    labelSize.setText(bundle.getString("folderSize").replace("%1", bundle.getString("loading")));
    getContext().send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.SIZE));
  }

  private void printSize(Long size){
    labelSize.setText(bundle.getString("folderSize").replace("%1", readableFileSize(size)));
  }

  private String readableFileSize(long size) {
    if (size <= 0) return "0";
    final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }

  public void showClearOptions(){
    NodeUtil.showNode(hboxOptions);
    clearOptionsButton.setText(bundle.getString("hideOptions"));
  }

  public void hideClearOptions(){
    NodeUtil.hideNode(hboxOptions);
    clearOptionsButton.setText(bundle.getString("clearOptions"));
  }

  private void showLoading() {
    indicator.setProgress(-1.0);
    NodeUtil.showNode(indicator);
    NodeUtil.hideNode(vboxReports);
    NodeUtil.hideNode(reloadButton2);
    NodeUtil.hideNode(pagination);
    NodeUtil.hideNode(labelEmpty);
    NodeUtil.hideNode(hboxSize);
  }

  public void hideLoading() {
    NodeUtil.hideNode(indicator);
    NodeUtil.showNode(vboxReports);
    NodeUtil.showNode(reloadButton2);

    if (getController().isEmpty()) {
      NodeUtil.showNode(labelEmpty);
      NodeUtil.hideNode(hboxSize);
      NodeUtil.hideNode(pagination);
    } else {
      NodeUtil.showNode(hboxSize);
      NodeUtil.hideNode(labelEmpty);
      NodeUtil.showNode(pagination);
    }
  }

  @FXML
  protected void loadMoreReports(ActionEvent event) throws Exception {
  }

  @FXML
  protected void reloadReports(ActionEvent event) throws Exception {
    context.send(new ReportsMessage(ReportsMessage.Type.RELOAD));
  }

  @FXML
  protected void clearOptions(ActionEvent event) throws Exception {
    if (hboxOptions.isVisible()){
      hideClearOptions();
    } else {
      showClearOptions();
      loadReportsSize();
    }
  }

  @FXML
  protected void clearReports(ActionEvent event) throws Exception {
    LocalDate date = LocalDate.now();
    date = date.plusYears(10L);
    RadioButton radio = (RadioButton) toggleClear.getSelectedToggle();
    if (radio.getId().equals("radOlder")){
      if (datePicker.getValue() != null) {
        date = datePicker.getValue();
      } else {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertSelectDate")));
        return;
      }
    }

    // All ok, delete
    if (getController().clearReports(date)){
      getController().clearData();
      showLoading();
      hideClearOptions();
      getContext().send(new ReportsMessage(ReportsMessage.Type.READ));
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, bundle.getString("successDeleteReports")));
      vboxReports.getChildren().clear();
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorDeleteReports")));
    }
  }

  public ToggleGroup getToggleClear() {
    return toggleClear;
  }

  public Context getContext() {
    return context;
  }

  /**
   * Pagination
   */

  boolean paginationInitiated = false;

  public void initPagination() {
    Integer oldPageCount = pagination.getPageCount();
    Integer oldPageIndex = pagination.getCurrentPageIndex();
    pagination.setPageCount(getController().getPagesCount());
    pagination.setCurrentPageIndex(0);
    if (paginationInitiated) {
      if (oldPageCount.equals(getController().getPagesCount()) && oldPageIndex.equals(0)) {
        reloadPage(0);
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

  public VBox createPage(Integer pageIndex) {
    String id = "vboxReports" + pageIndex;
    VBox box = new VBox();
    box.setId(id);
    box.setStyle("-fx-padding: 0 0 10 0;");
    getController().loadAndPrintReports("#" + id, pageIndex);
    return box;
  }

  public void addReportGui(String vboxId, ReportGui row) {
    hideLoading();
    Node node = pagination.lookup(vboxId);
    if (node != null) {
      VBox vbox = (VBox) node;
      ManagedFragmentHandler<ReportFragment> handler;
      if (reportHandlers.containsKey(row.getUuid())) {
        handler = reportHandlers.get(row.getUuid());
      } else {
        handler = context.getManagedFragmentHandler(ReportFragment.class);
        handler.getController().init(row);
        reportHandlers.put(row.getUuid(), handler);
      }
      vbox.getChildren().add(handler.getFragmentNode());
    }
  }

  private void deleteReportGui(String uuid) {
    boolean lastPage = pagination.getCurrentPageIndex() == pagination.getPageCount() - 1;
    int oldPages = getController().getPagesCount();
    if (reportHandlers.containsKey(uuid)){
      reportHandlers.remove(uuid);
    }
    getController().removeReport(uuid);
    int newPages = getController().getPagesCount();
    if (oldPages == newPages) {
      reloadPage(pagination.getCurrentPageIndex());
    } else {
      pagination.setPageCount(newPages);
      pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
    }
  }

  public void reloadPage(int index) {
    Node node = pagination.lookup("#vboxReports" + index);
    if (node != null) {
      VBox vbox = (VBox) node;
      vbox.getChildren().clear();
      getController().loadAndPrintReports("#vboxReports" + index, index);
    }
  }

  /**
   * Sort reports
   */

  private ReportsComparator.Mode currentMode;
  private ReportsComparator.Order currentOrder;

  @FXML private HBox hboxName;
  @FXML private HBox hboxPassed;
  @FXML private HBox hboxFiles;
  @FXML private HBox hboxErrors;
  @FXML private HBox hboxWarnings;
  @FXML private HBox hboxResult;
  @FXML private HBox hboxDate;
  @FXML private HBox hboxScore;

  @FXML
  protected void clickedColDate(MouseEvent event) throws Exception {
    clickedCol(hboxDate, ReportsComparator.Mode.DATE);
  }
  @FXML
  protected void clickedColName(MouseEvent event) throws Exception {
    clickedCol(hboxName, ReportsComparator.Mode.NAME);
  }
  @FXML
  protected void clickedColErrors(MouseEvent event) throws Exception {
    clickedCol(hboxErrors, ReportsComparator.Mode.ERRORS);
  }
  @FXML
  protected void clickedColWarnings(MouseEvent event) throws Exception {
    clickedCol(hboxWarnings, ReportsComparator.Mode.WARNINGS);
  }
  @FXML
  protected void clickedColResult(MouseEvent event) throws Exception {
    clickedCol(hboxResult, ReportsComparator.Mode.RESULT);
  }
  @FXML
  protected void clickedColPassed(MouseEvent event) throws Exception {
    clickedCol(hboxPassed, ReportsComparator.Mode.PASSED);
  }
  @FXML
  protected void clickedColScore(MouseEvent event) throws Exception {
    clickedCol(hboxScore, ReportsComparator.Mode.SCORE);
  }
  @FXML
  protected void clickedColFiles(MouseEvent event) throws Exception {
    clickedCol(hboxFiles, ReportsComparator.Mode.FILES);
  }

  private void clickedCol(HBox hbox, ReportsComparator.Mode mode){
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
    showLoading();
    context.send(GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.SORT));
  }

  private void addArrow(HBox hbox) {
    String type = (currentOrder.equals(ReportsComparator.Order.ASC)) ? "up" : "down";
    ImageView icon = new ImageView();
    icon.setFitHeight(10);
    icon.setFitWidth(10);
    icon.setImage(new Image("images/icons/caret-" + type + ".png"));
    hbox.getChildren().add(icon);
  }

  private void removeArrows() {
    hboxName.getChildren().remove(1, hboxName.getChildren().size());
    hboxErrors.getChildren().remove(1, hboxErrors.getChildren().size());
    hboxWarnings.getChildren().remove(1, hboxWarnings.getChildren().size());
    hboxResult.getChildren().remove(1, hboxResult.getChildren().size());
    hboxPassed.getChildren().remove(1, hboxPassed.getChildren().size());
    hboxScore.getChildren().remove(1, hboxScore.getChildren().size());
    hboxFiles.getChildren().remove(1, hboxFiles.getChildren().size());
    hboxDate.getChildren().remove(1, hboxDate.getChildren().size());
  }

  public ReportsComparator.Order getDefaultOrder(ReportsComparator.Mode mode){
    if (mode.equals(ReportsComparator.Mode.NAME)) return ReportsComparator.Order.ASC;
    return ReportsComparator.Order.DESC;
  }

  private void swapOrder(){
    if (currentOrder.equals(ReportsComparator.Order.ASC)){
      currentOrder = ReportsComparator.Order.DESC;
    } else {
      currentOrder = ReportsComparator.Order.ASC;
    }
  }

  public ReportsComparator.Mode getCurrentMode() {
    return currentMode;
  }

  public ReportsComparator.Order getCurrentOrder() {
    return currentOrder;
  }

}

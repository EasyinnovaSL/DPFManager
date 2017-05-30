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
import dpfmanager.shell.interfaces.gui.fragment.ReportFragment;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.statistics.messages.StatisticsMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.text.DecimalFormat;
import java.time.LocalDate;
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

  // View elements
  @FXML
  private VBox reportsVbox;
  @FXML
  private Button loadMore;
  @FXML
  private Button genStatistics;
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
  private HBox hboxOptions;
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

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(ReportsMessage.class)) {
      ReportsMessage rMessage = message.getTypedMessage(ReportsMessage.class);
      if (rMessage.isRead()) {
        getModel().loadReportsFromDir();
        getModel().printReports();
      } else if (rMessage.isSize()) {
        getModel().readReportsSize();
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(ReportsMessage.class)) {
      ReportsMessage rMessage = message.getTypedMessage(ReportsMessage.class);
      if (rMessage.isReload()) {
        showLoading();
        mainVBox.getChildren().clear();
        getModel().clearReportsLoaded();
        context.send(new ReportsMessage(ReportsMessage.Type.READ));
      } else if (rMessage.isRead()) {
        loadReportsSize();
      } else if (rMessage.isSize()) {
        printSize(getModel().getReportsSize());
      } else if (rMessage.isDelete()) {
        deleteReportGui(rMessage.getUuid());
      } else if (rMessage.isAdd()) {
        addReportGui(rMessage.getReportGui());
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
  }

  public void addReportGui(ReportGui row) {
    ManagedFragmentHandler<ReportFragment> handler = getModel().getReportGuiByUuid(row.getUuid());
    if (handler == null){
      if (row.isLoaded()){
        handler = context.getManagedFragmentHandler(ReportFragment.class);
        getModel().addReportFragment(handler);
        handler.getController().init(row);
      }
    }
    if (row.isLoaded()) {
      mainVBox.getChildren().add(handler.getFragmentNode());
      hideLoading();
    }
  }

  private void deleteReportGui(String uuid) {
    ManagedFragmentHandler<ReportFragment> toDelete = getModel().getReportGuiByUuid(uuid);
    if (toDelete != null) {
      mainVBox.getChildren().remove(toDelete.getFragmentNode());
      getModel().removeReportFragment(toDelete);
    }
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
    NodeUtil.hideNode(labelEmpty);
    NodeUtil.hideNode(loadMore);
    NodeUtil.hideNode(genStatistics);
    NodeUtil.hideNode(hboxSize);
  }

  public void hideLoading() {
    NodeUtil.hideNode(indicator);
    NodeUtil.showNode(vboxReports);

    if (getModel().isEmpty()) {
      NodeUtil.hideNode(loadMore);
      NodeUtil.showNode(labelEmpty);
      NodeUtil.hideNode(hboxSize);
      NodeUtil.hideNode(genStatistics);
    } else if (getModel().isAllReportsLoaded()) {
      NodeUtil.hideNode(labelEmpty);
      NodeUtil.hideNode(loadMore);
      NodeUtil.showNode(hboxSize);
      NodeUtil.showNode(genStatistics);
    } else {
      NodeUtil.showNode(loadMore);
      NodeUtil.showNode(hboxSize);
      NodeUtil.hideNode(labelEmpty);
      NodeUtil.showNode(genStatistics);
    }
  }

  @FXML
  protected void generateStadistics(ActionEvent event) throws Exception {
    getContext().send(BasicConfig.MODULE_STATISTICS, new StatisticsMessage(StatisticsMessage.Type.GENERATE));
  }

  @FXML
  protected void loadMoreReports(ActionEvent event) throws Exception {
    getModel().printMoreReports();
  }

  @FXML
  protected void clearOptions(ActionEvent event) throws Exception {
    if (hboxOptions.isVisible()){
      hideClearOptions();
    } else {
      showClearOptions();
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
      getModel().clearData();
      showLoading();
      hideClearOptions();
      getContext().send(new ReportsMessage(ReportsMessage.Type.READ));
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, bundle.getString("successDeleteReports")));
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorDeleteReports")));
    }
  }

  public ToggleGroup getToggleClear() {
    return toggleClear;
  }

  public void hideLoadMore() {
    NodeUtil.hideNode(loadMore);
  }

  public Context getContext() {
    return context;
  }

}

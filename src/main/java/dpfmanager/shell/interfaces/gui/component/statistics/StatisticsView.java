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

package dpfmanager.shell.interfaces.gui.component.statistics;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.TagsIdComparator;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.TagsMainComparator;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.TagsNameComparator;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.TagsThumbComparator;
import dpfmanager.shell.interfaces.gui.fragment.ReportFragment;
import dpfmanager.shell.interfaces.gui.fragment.statics.StatisticsFragment;
import dpfmanager.shell.interfaces.gui.fragment.statics.TagFragment;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.statistics.core.StatisticsObject;
import dpfmanager.shell.modules.statistics.messages.StatisticsMessage;
import dpfmanager.shell.modules.statistics.model.HistogramTag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_STATISTICS,
    name = GuiConfig.COMPONENT_STATISTICS,
    viewLocation = "/fxml/statistics.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_STATISTICS)
public class StatisticsView extends DpfView<StatisticsModel, StatisticsController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private VBox mainVBoxStatics;
  @FXML
  private Button genStatistics;
  @FXML
  private ProgressIndicator indicator;

  // General
  private Integer mainImagesCount;
  private Integer thumbnailsCount;

  // Tags
  @FXML
  private VBox vboxTags;
  private List<HistogramTag> tagsList;

  // Isos
  @FXML
  private VBox vboxIsos;

  // Policys
  @FXML
  private VBox vboxPolicys;

  /**
   * Clickable Tags Names
   */
  private final List<String> clickableTagNames = Arrays.asList("PhotometricInterpretation", "Orientation", "Compression", "BitsPerSample", "NewSubfileType", "SamplesPerPixel", "PlanarConfiguration", "ResolutionUnit", "Make", "Model", "Software", "Copyright");


  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(StatisticsMessage.class)) {
      StatisticsMessage sMessage = message.getTypedMessage(StatisticsMessage.class);
      if (sMessage.isRender()) {
        readStatistics(sMessage.getStatisticsObject());
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(StatisticsMessage.class)) {
      StatisticsMessage sMessage = message.getTypedMessage(StatisticsMessage.class);
      if (sMessage.isRender()) {
        hideLoading();
        renderStatistics();
      }
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new StatisticsModel(context));
    setController(new StatisticsController());
    getModel().setResourcebundle(bundle);

    // Show / Hide
    showLoading();
    getContext().send(BasicConfig.MODULE_STATISTICS, new StatisticsMessage(StatisticsMessage.Type.GENERATE));
  }

  @Override
  public Context getContext() {
    return context;
  }

  @FXML
  protected void generateStadistics(ActionEvent event) throws Exception {
    showLoading();
    getContext().send(BasicConfig.MODULE_STATISTICS, new StatisticsMessage(StatisticsMessage.Type.GENERATE));
  }

  private void readStatistics(StatisticsObject so){
    // Test
    so.printResults();

    // General
    mainImagesCount = so.getMainImagesCount();
    thumbnailsCount = so.getThumbnailsCount();

    // Tags Histogram
    tagsList = new ArrayList<>(so.getTags().values());
    tagsList.sort(new TagsMainComparator());
  }

  private void renderStatistics(){
    // Tags Histogram
    vboxTags.getChildren().clear();
    for (HistogramTag hTag : tagsList) {
      ManagedFragmentHandler<TagFragment> handler = getContext().getManagedFragmentHandler(TagFragment.class);
      handler.getController().init(hTag, mainImagesCount, thumbnailsCount);
      if (clickableTagNames.contains(hTag.getValue().getName())) {
        renderTagValues(hTag, handler);
      }
      vboxTags.getChildren().add(handler.getFragmentNode());
    }
  }

  private void renderTagValues(HistogramTag hTag, ManagedFragmentHandler<TagFragment> parent){
//    for (String value : hTag.getPossibleValues().keySet()){
//      Integer count =
//      ManagedFragmentHandler<TagFragment> child = getContext().getManagedFragmentHandler(TagFragment.class);
//      child.getController().initTagValue(hTag);
//      parent.getController().addChild(child);
//      vboxTags.getChildren().add(child.getFragmentNode());
//    }
  }

  public void show(){

  }

  public void hide(){

  }

  /**
   * Tags Headers click
   */
  @FXML
  protected void sortId(MouseEvent event) throws Exception {
    tagsList.sort(new TagsIdComparator());
    renderStatistics();
  }

  @FXML
  protected void sortName(MouseEvent event) throws Exception {
    tagsList.sort(new TagsNameComparator());
    renderStatistics();
  }
  @FXML
  protected void sortMain(MouseEvent event) throws Exception {
    tagsList.sort(new TagsMainComparator());
    renderStatistics();
  }
  @FXML
  protected void sortThumb(MouseEvent event) throws Exception {
    tagsList.sort(new TagsThumbComparator());
    renderStatistics();
  }

  /**
   * Loading
   */
  private void showLoading() {
    indicator.setProgress(-1.0);
    NodeUtil.showNode(indicator);
    NodeUtil.hideNode(mainVBoxStatics);
    NodeUtil.hideNode(genStatistics);
  }

  public void hideLoading() {
    NodeUtil.hideNode(indicator);
    NodeUtil.hideNode(genStatistics);
    NodeUtil.showNode(mainVBoxStatics);
  }
}

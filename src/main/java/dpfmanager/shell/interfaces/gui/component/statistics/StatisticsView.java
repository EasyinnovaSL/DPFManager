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
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.IsoComparator;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.PolicyComparator;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.TagsComparator;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.ValueTagsComparator;
import dpfmanager.shell.interfaces.gui.component.statistics.messages.ShowHideErrorsMessage;
import dpfmanager.shell.interfaces.gui.fragment.statics.ErrorFragment;
import dpfmanager.shell.interfaces.gui.fragment.statics.ErrorsListFragment;
import dpfmanager.shell.interfaces.gui.fragment.statics.IsoFragment;
import dpfmanager.shell.interfaces.gui.fragment.statics.RuleFragment;
import dpfmanager.shell.interfaces.gui.fragment.statics.TagFragment;
import dpfmanager.shell.modules.statistics.core.StatisticsObject;
import dpfmanager.shell.modules.statistics.messages.StatisticsMessage;
import dpfmanager.shell.modules.statistics.model.HistogramTag;
import dpfmanager.shell.modules.statistics.model.StatisticsError;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;
import dpfmanager.shell.modules.statistics.model.StatisticsIsoErrors;
import dpfmanager.shell.modules.statistics.model.StatisticsRule;
import dpfmanager.shell.modules.statistics.model.ValueTag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;
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

  public final Integer MAX_ROWS_TAGS = 10;
  public final Integer MAX_ROWS_ISO = 7;
  public final Integer MAX_ROWS_POLICY = 7;

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
  @FXML
  private ScrollPane scrollTags;
  private List<HistogramTag> tagsList;

  // Isos
  @FXML
  private VBox vboxIsos;
  @FXML
  private ScrollPane scrollIsos;
  private List<StatisticsIso> isosList;
  private List<ManagedFragmentHandler<IsoFragment>> isosListHandlers;

  // Iso errors
  @FXML
  private VBox vboxErrors;
  private List<ManagedFragmentHandler<ErrorsListFragment>> isoErrorsHandlers;

  // Policys
  @FXML
  private VBox vboxPolicys;
  @FXML
  private ScrollPane scrollPolicys;
  private List<StatisticsRule> policyList;

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
    } else if (message != null && message.isTypeOf(ShowHideErrorsMessage.class)) {
      ShowHideErrorsMessage sMessage = message.getTypedMessage(ShowHideErrorsMessage.class);
      // Show / Hide errors lists
      for (ManagedFragmentHandler<ErrorsListFragment> handler : isoErrorsHandlers) {
        handler.getController().setVisible(handler.getController().getId().equals(sMessage.getIsoId()) && sMessage.isShow());
      }
      // Select only current row
      for (ManagedFragmentHandler<IsoFragment> handler : isosListHandlers) {
        handler.getController().setSelected(handler.getController().getId().equals(sMessage.getIsoId()) && sMessage.isShow());
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
      } else if (sMessage.isGenerate()) {
        showLoading();
        getContext().send(BasicConfig.MODULE_STATISTICS, new StatisticsMessage(StatisticsMessage.Type.GENERATE));
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

  private void readStatistics(StatisticsObject so) {
    // Test
    so.printResults();

    // General
    mainImagesCount = so.getMainImagesCount();
    thumbnailsCount = so.getThumbnailsCount();

    // Tags Histogram
    tagsList = new ArrayList<>(so.getTags().values());
    tagsList.sort(new TagsComparator(TagsComparator.Mode.MAIN));

    // Isos
    isosList = new ArrayList<>(so.getIsos().values());
    isosList.sort(new IsoComparator(IsoComparator.Mode.NAME));

    // Policy
    policyList = new ArrayList<>(so.getPolicys().values());
    policyList.sort(new PolicyComparator(PolicyComparator.Mode.PERCENTAGE));
  }

  private void renderStatistics() {
    renderTags();
    renderIsos(true);
    renderPolicy();
    calculateMinHeight();
  }

  private void renderTags() {
    // Tags Histogram
    vboxTags.getChildren().clear();
    boolean first = true;
    for (HistogramTag hTag : tagsList) {
      ManagedFragmentHandler<TagFragment> handler = getContext().getManagedFragmentHandler(TagFragment.class);
      handler.getController().init(hTag, mainImagesCount, thumbnailsCount);
      vboxTags.getChildren().add(handler.getFragmentNode());
      if (first) {
        handler.getController().hideLine();
        first = false;
      }
      if (hTag.hasSpecificValues()) {
        renderTagValues(hTag, handler);
      }
    }
  }

  private void renderTagValues(HistogramTag hTag, ManagedFragmentHandler<TagFragment> parent) {
    List<ValueTag> valueTags = new ArrayList<>(hTag.getPossibleValues().values());
    valueTags.sort(new ValueTagsComparator());
    for (ValueTag valueTag : valueTags) {
      ManagedFragmentHandler<TagFragment> child = getContext().getManagedFragmentHandler(TagFragment.class);
      child.getController().initChild(hTag, valueTag);
      parent.getController().addChild(child);
      vboxTags.getChildren().add(child.getFragmentNode());
    }
  }

  private void renderIsos(boolean plusErrors) {
    vboxIsos.getChildren().clear();
    isosListHandlers = new ArrayList<>();
    if (plusErrors) {
      vboxErrors.getChildren().clear();
      isoErrorsHandlers = new ArrayList<>();
    }
    for (StatisticsIso sIso : isosList) {
      ManagedFragmentHandler<IsoFragment> handler = getContext().getManagedFragmentHandler(IsoFragment.class);
      handler.getController().init(sIso);
      if (plusErrors) renderIsosErrors(sIso);
      isosListHandlers.add(handler);
      vboxIsos.getChildren().add(handler.getFragmentNode());
    }
  }

  private void renderIsosErrors(StatisticsIso sIso) {
    StatisticsIsoErrors sIsoErrors = sIso.getIsoErrors();
    ManagedFragmentHandler<ErrorsListFragment> handler = getContext().getManagedFragmentHandler(ErrorsListFragment.class);
    List<Node> rows = new ArrayList<>();
    // Errors rows
    for (StatisticsError sError : sIsoErrors.getErrorsList()) {
      ManagedFragmentHandler<ErrorFragment> handlerChild = getContext().getManagedFragmentHandler(ErrorFragment.class);
      handlerChild.getController().init(sError, sIsoErrors.max);
      rows.add(handlerChild.getFragmentNode());
    }
    handler.getController().init(sIsoErrors, rows);
    isoErrorsHandlers.add(handler);
    vboxErrors.getChildren().add(handler.getFragmentNode());
  }

  private void renderPolicy() {
    vboxPolicys.getChildren().clear();
    for (StatisticsRule sRule : policyList) {
      ManagedFragmentHandler<RuleFragment> handler = getContext().getManagedFragmentHandler(RuleFragment.class);
      handler.getController().init(sRule);
      vboxPolicys.getChildren().add(handler.getFragmentNode());
    }
  }

  private void calculateMinHeight() {
    // Tags
    int currentRows = vboxTags.getChildren().size();
    int height = 1 + ((currentRows > MAX_ROWS_TAGS) ? MAX_ROWS_TAGS : currentRows) * 31;
    scrollTags.setMinHeight(height);
    scrollTags.setMaxHeight(height);

    // ISOs
    currentRows = vboxIsos.getChildren().size();
    height = 2 + ((currentRows > MAX_ROWS_ISO) ? MAX_ROWS_ISO : currentRows) * 31;
    scrollIsos.setMinHeight(height);
    scrollIsos.setMaxHeight(height);

    // Policy
    currentRows = vboxPolicys.getChildren().size();
    height = 2 + ((currentRows > MAX_ROWS_POLICY) ? MAX_ROWS_POLICY : currentRows) * 31;
    scrollPolicys.setMinHeight(height);
    scrollPolicys.setMaxHeight(height);
  }

  public void show() {

  }

  public void hide() {

  }

  /**
   * Tags Headers click
   */
  @FXML
  protected void sortId(MouseEvent event) throws Exception {
    tagsList.sort(new TagsComparator(TagsComparator.Mode.ID));
    renderTags();
  }

  @FXML
  protected void sortName(MouseEvent event) throws Exception {
    tagsList.sort(new TagsComparator(TagsComparator.Mode.NAME));
    renderTags();
  }

  @FXML
  protected void sortMain(MouseEvent event) throws Exception {
    tagsList.sort(new TagsComparator(TagsComparator.Mode.MAIN));
    renderTags();
  }

  @FXML
  protected void sortThumb(MouseEvent event) throws Exception {
    tagsList.sort(new TagsComparator(TagsComparator.Mode.THUMB));
    renderTags();
  }

  /**
   * ISO Headers Click
   */
  @FXML
  protected void sortIsoErrors(MouseEvent event) throws Exception {
    isosList.sort(new IsoComparator(IsoComparator.Mode.ERRORS));
    renderIsos(false);
  }

  @FXML
  protected void sortIsoWarnings(MouseEvent event) throws Exception {
    isosList.sort(new IsoComparator(IsoComparator.Mode.WARNINGS));
    renderIsos(false);
  }

  @FXML
  protected void sortIsoPassed(MouseEvent event) throws Exception {
    isosList.sort(new IsoComparator(IsoComparator.Mode.PASSED));
    renderIsos(false);
  }

  @FXML
  protected void sortIsoName(MouseEvent event) throws Exception {
    isosList.sort(new IsoComparator(IsoComparator.Mode.NAME));
    renderIsos(false);
  }

  @FXML
  protected void sortIsoCount(MouseEvent event) throws Exception {
    isosList.sort(new IsoComparator(IsoComparator.Mode.COUNT));
    renderIsos(false);
  }

  /**
   * Policy Headers Click
   */
  @FXML
  protected void sortPolicyName(MouseEvent event) throws Exception {
    policyList.sort(new PolicyComparator(PolicyComparator.Mode.NAME));
    renderPolicy();
  }

  @FXML
  protected void sortPolicyFiles(MouseEvent event) throws Exception {
    policyList.sort(new PolicyComparator(PolicyComparator.Mode.TOTAL));
    renderPolicy();
  }

  @FXML
  protected void sortPolicyFailed(MouseEvent event) throws Exception {
    policyList.sort(new PolicyComparator(PolicyComparator.Mode.FAILED));
    renderPolicy();
  }

  @FXML
  protected void sortPolicyPercent(MouseEvent event) throws Exception {
    policyList.sort(new PolicyComparator(PolicyComparator.Mode.PERCENTAGE));
    renderPolicy();
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

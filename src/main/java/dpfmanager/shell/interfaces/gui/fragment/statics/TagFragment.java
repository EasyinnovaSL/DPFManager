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

package dpfmanager.shell.interfaces.gui.fragment.statics;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.TagsThumbComparator;
import dpfmanager.shell.modules.statistics.core.StatisticsObject;
import dpfmanager.shell.modules.statistics.model.HistogramTag;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 18/04/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_TAG,
    viewLocation = "/fxml/statics-fragments/tag.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.PROTOTYPE)
public class TagFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private Label tagId;
  @FXML
  private Label tagName;
  @FXML
  private Label tagMain;
  @FXML
  private Label tagMainPercent;
  @FXML
  private Label tagThumb;
  @FXML
  private Label tagThumbPercent;

  @FXML
  private ProgressBar progressMain;
  @FXML
  private ProgressBar progressThumb;

  /** Tha childs handlers */
  private List<ManagedFragmentHandler<TagFragment>> children = new ArrayList<>();

  /** the internal Histogram Tag object */
  private HistogramTag hTag;

  public void init(HistogramTag h, Integer mainImages, Integer thumbnails) {
    hTag = h;
    Double mainPercentOne = (1.0 * hTag.getMainCount()) / (1.0 * mainImages);
    Double thumbPercentOne = (1.0 * hTag.getThumbCount()) / (1.0 * thumbnails);
    tagId.setText(String.valueOf(hTag.getValue().getId()));
    tagName.setText(hTag.getValue().getName());
    tagMain.setText(String.valueOf(hTag.getMainCount()));
    tagMainPercent.setText(String.valueOf(mainPercentOne * 100));
    tagThumb.setText(String.valueOf(hTag.getThumbCount()));
    tagThumbPercent.setText(String.valueOf(thumbPercentOne * 100));
    progressMain.setProgress(mainPercentOne);
    progressThumb.setProgress(thumbPercentOne);
  }

  public void initTagValue(){

  }

  public void addChild(ManagedFragmentHandler<TagFragment> child){
    children.add(child);
    if (!mainPane.getStyleClass().contains("hoverRow")) mainPane.getStyleClass().add("hoverRow");
  }

  public void show(){
    NodeUtil.showNode(mainPane);
  }

  public void hide(){
    NodeUtil.hideNode(mainPane);
  }

  @FXML
  protected void onGridPaneClicked(MouseEvent event) throws Exception {
    if (mainPane.getStyleClass().contains("active")){
      // Disable tag specificaton
      mainPane.getStyleClass().remove("active");
      for(ManagedFragmentHandler<TagFragment> child : children){
        child.getController().hide();
      }
    } else {
      // Active tag specification
      mainPane.getStyleClass().add("active");
      for(ManagedFragmentHandler<TagFragment> child : children){
        child.getController().show();
      }
    }
  }

}
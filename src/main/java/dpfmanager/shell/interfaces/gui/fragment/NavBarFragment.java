/**
 * <h1>BarFragment.java</h1> <p> This program is free software: you can redistribute it and/or
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

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.statistics.comparators.PolicyComparator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import org.controlsfx.control.BreadCrumbBar;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 03/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_BREAD,
    viewLocation = "/fxml/fragments/nav.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class NavBarFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private Label labelTable;
  @FXML
  private Label labelSeparator1;
  @FXML
  private Label labelReport;
  @FXML
  private Label labelSeparator2;
  @FXML
  private Label labelSingle;

  @FXML
  private Label labelPdf;

  private boolean initiated = false;
  private boolean reload = false;

  public void init() {
    if (initiated) return;
    setTable();
    initiated = true;
  }

  public void setTable(){
    NodeUtil.showNode(labelTable);
    NodeUtil.hideNode(labelSeparator1);
    NodeUtil.hideNode(labelReport);
    NodeUtil.hideNode(labelSeparator2);
    NodeUtil.hideNode(labelSingle);
    NodeUtil.hideNode(labelPdf);
    labelTable.getStyleClass().remove("myactive");
    labelReport.getStyleClass().remove("myactive");
    labelSingle.getStyleClass().remove("myactive");
    labelTable.getStyleClass().add("myactive");
  }

  public void setReport(){
    NodeUtil.showNode(labelTable);
    NodeUtil.showNode(labelSeparator1);
    NodeUtil.showNode(labelReport);
    NodeUtil.hideNode(labelSeparator2);
    NodeUtil.hideNode(labelSingle);
    NodeUtil.hideNode(labelPdf);
    labelTable.getStyleClass().remove("myactive");
    labelReport.getStyleClass().remove("myactive");
    labelSingle.getStyleClass().remove("myactive");
    labelReport.getStyleClass().add("myactive");
  }

  public void setSpecific(){
    NodeUtil.showNode(labelTable);
    NodeUtil.showNode(labelSeparator1);
    NodeUtil.showNode(labelReport);
    NodeUtil.showNode(labelSeparator2);
    NodeUtil.showNode(labelSingle);
    NodeUtil.hideNode(labelPdf);
    labelTable.getStyleClass().remove("myactive");
    labelReport.getStyleClass().remove("myactive");
    labelSingle.getStyleClass().remove("myactive");
    labelSingle.getStyleClass().add("myactive");
  }

  public void setPdfPage(int c, int m){
    if (c == 0 && m == 0){
      NodeUtil.hideNode(labelPdf);
    } else {
      labelPdf.setText(c + "/" + m);
      NodeUtil.showNode(labelPdf);
    }
  }

  public void setSpecificText(String format){
    labelSingle.setText(format);
  }

  @FXML
  protected void clickedTable(MouseEvent event) throws Exception {
    if (!labelTable.getStyleClass().contains("myactive")) {
      if (reload) {
        context.send(GuiConfig.PERSPECTIVE_REPORTS, new UiMessage(UiMessage.Type.RELOAD));
      } else {
        context.send(GuiConfig.PERSPECTIVE_REPORTS, new UiMessage(UiMessage.Type.SHOW));
      }
    }
  }

  @FXML
  protected void clickedReport(MouseEvent event) throws Exception {
    if (!labelReport.getStyleClass().contains("myactive")) {
      context.send(GuiConfig.PERSPECTIVE_GLOBAL, new UiMessage(UiMessage.Type.RELOAD));
    }
  }

  @FXML
  protected void clickedSingle(MouseEvent event) throws Exception {
  }

  public void setReload(boolean reload) {
    this.reload = reload;
  }
}

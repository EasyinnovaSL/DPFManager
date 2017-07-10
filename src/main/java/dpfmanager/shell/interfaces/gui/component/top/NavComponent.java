/**
 * <h1>TopComponent.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.top;

import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.NavMessage;
import dpfmanager.shell.interfaces.gui.fragment.NavBarFragment;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.View;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@View(id = GuiConfig.COMPONENT_NAV,
    name = GuiConfig.COMPONENT_NAV,
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_NAV)
public class NavComponent extends DpfSimpleView {

  private AnchorPane anchorPane;

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  private ManagedFragmentHandler<NavBarFragment> handler;

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(NavMessage.class)){
      NavMessage nm = message.getTypedMessage(NavMessage.class);
      if (nm.isTable()){
        handler.getController().setTable();
      } else if (nm.isReport()){
        handler.getController().setReport();
      } else if (nm.isSpecific()){
        handler.getController().setSpecific();
      } else if (nm.isText()){
        String format = nm.getText().toUpperCase();
        handler.getController().setSpecificText(format);
        handler.getController().setSpecific();
      } else if (nm.isPdf()){
        handler.getController().setPdfPage(nm.getCount(), nm.getMax());
      }
    }
    return anchorPane;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    handler = context.getManagedFragmentHandler(NavBarFragment.class);
    handler.getController().init();
    anchorPane = (AnchorPane) handler.getFragmentNode();
  }

  @Override
  public void sendMessage(String target, Object message) {
    context.send(target, message);
  }

}

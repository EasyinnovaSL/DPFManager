/**
 * <h1>InteroperabilityView.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.interoperability;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.component.periodical.PeriodicalController;
import dpfmanager.shell.interfaces.gui.component.periodical.PeriodicalModel;
import dpfmanager.shell.interfaces.gui.fragment.InteropFragment;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;
import dpfmanager.shell.modules.interoperability.core.ConformanceConfig;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityMessage;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityResponseMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.CloseMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicCheck;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_INTEROPERABILITY,
    name = GuiConfig.COMPONENT_INTEROPERABILITY,
    viewLocation = "/fxml/interoperability.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_INTEROPERABILITY)
public class InteroperabilityView extends DpfView<InteroperabilityModel, InteroperabilityController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private VBox mainVBox;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(CloseMessage.class)) {
      closeRequested();
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(InteroperabilityResponseMessage.class)){
      InteroperabilityResponseMessage irm = (InteroperabilityResponseMessage) message;
      if (irm.isObjects()) {
        for (ConformanceConfig cc : irm.getList()){
          addConformanceConfig(cc);
        }
      } else if (irm.isRemove()) {
        InteropFragment inf = getModel().getConformanceConfigByUuid(irm.getUuid()).getController();
        if (irm.isOk()) {
          deleteConformanceChecker(irm.getUuid());
        } else {
          inf.hideLoading();
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("errorRemovingCC").replace("%1", inf.getName())));
        }
      } else if (irm.isSave()) {
        InteropFragment inf = getModel().getConformanceConfigByUuid(irm.getUuid()).getController();
        if (irm.isOk()) {
          inf.savedSuccess();
        } else {
          inf.savedFailed();
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("errorSavingCC").replace("%1", inf.getName()), bundle.getString("infoConsole")));
        }
      } else if (irm.isEnable()) {
        InteropFragment inf = getModel().getConformanceConfigByUuid(irm.getUuid()).getController();
        if (!irm.isOk()) {
          inf.enableFailed();
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorEnableH").replace("%1", inf.getName()), bundle.getString("infoConsole")));
        }
      }
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Set model and controller
    setModel(new InteroperabilityModel());
    setController(new InteroperabilityController());
    getController().setResourcebundle(bundle);

    // Read from configurations
    context.send(BasicConfig.MODULE_INTEROPERABILITY, new InteroperabilityMessage(InteroperabilityMessage.Type.OBJECTS));
  }

  private void addConformanceConfig(ConformanceConfig cc){
    ManagedFragmentHandler<InteropFragment> handler = getContext().getManagedFragmentHandler(InteropFragment.class);
    handler.getController().init(cc);
    getModel().addConformanceFragment(handler);
    mainVBox.getChildren().add(handler.getFragmentNode());
  }

  private void deleteConformanceChecker(String name){
    ManagedFragmentHandler<InteropFragment> toDelete = getModel().getConformanceConfigByUuid(name);
    if (toDelete != null) {
      mainVBox.getChildren().remove(toDelete.getFragmentNode());
      getModel().removeConformanceFragment(toDelete);
    }
  }

  @FXML
  protected void newButtonClicked(ActionEvent event) throws Exception {
    ManagedFragmentHandler<InteropFragment> handler = getContext().getManagedFragmentHandler(InteropFragment.class);
    Long milis = System.currentTimeMillis();
    handler.getController().init(milis.toString());
    getModel().addConformanceFragment(handler);
    mainVBox.getChildren().add(handler.getFragmentNode());
  }

  private void closeRequested() {
    boolean found = false;
    for (ManagedFragmentHandler<InteropFragment> handler : getModel().getConformancesFragments()) {
      if (!handler.getController().isSaved()) {
        found = true;
        break;
      }
    }
    context.send(BasicConfig.MODULE_MESSAGE, new CloseMessage(CloseMessage.Type.CONFORMANCES, found));
  }

  @Override
  public Context getContext() {
    return context;
  }

}

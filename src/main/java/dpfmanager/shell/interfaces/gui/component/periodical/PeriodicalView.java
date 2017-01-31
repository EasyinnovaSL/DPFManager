/**
 * <h1>PeriodicalView.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;
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
 * Created by Adria Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_PERIODICAL,
    name = GuiConfig.COMPONENT_PERIODICAL,
    viewLocation = "/fxml/periodical.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_PERIODICAL)
public class PeriodicalView extends DpfView<PeriodicalModel, PeriodicalController> {

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
    if (message != null && message.isTypeOf(PeriodicMessage.class)) {
      PeriodicMessage pm = message.getTypedMessage(PeriodicMessage.class);
      if (pm.isList()) {
        addPeriodicalsChecks(pm.getPeriodicChecks());
      } else if (pm.isDelete()) {
        if (pm.getResult()) {
          deletePeriodicalCheck(pm.getUuid());
        } else {
          getModel().getPeriodicalCheckByUuid(pm.getUuid()).getController().hideLoading();
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorDeleteCron")));
        }
      } else if (pm.isSave()) {
        if (pm.getResult()) {
          getModel().getPeriodicalCheckByUuid(pm.getUuid()).getController().savedSuccess();
        } else {
          getModel().getPeriodicalCheckByUuid(pm.getUuid()).getController().hideLoading();
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorSavePeriodic")));
        }
      } else if (pm.isEdit()) {
        if (pm.getResult()) {
          getModel().getPeriodicalCheckByUuid(pm.getUuid()).getController().savedSuccess();
        } else {
          getModel().getPeriodicalCheckByUuid(pm.getUuid()).getController().hideLoading();
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorEditPeriodic")));
        }
      }
    }
    return null;
  }

  @Override
  public Context getContext() {
    return context;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Set model and controller
    setModel(new PeriodicalModel());
    setController(new PeriodicalController());
    getController().setResourcebundle(bundle);

    // Read from operating system
    context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.READ));
  }

  public void addPeriodicalsChecks(List<PeriodicCheck> list) {
    for (PeriodicCheck check : list) {
      ManagedFragmentHandler<PeriodicFragment> handler = context.getManagedFragmentHandler(PeriodicFragment.class);
      handler.getController().init(check);
      getModel().addPeriodicalFragment(handler);
      mainVBox.getChildren().add(handler.getFragmentNode());
    }
  }

  private void deletePeriodicalCheck(String uuid) {
    ManagedFragmentHandler<PeriodicFragment> toDelete = getModel().getPeriodicalCheckByUuid(uuid);
    if (toDelete != null) {
      mainVBox.getChildren().remove(toDelete.getFragmentNode());
      getModel().removePeriodicalCheck(toDelete);
    }
  }

  private void closeRequested() {
    boolean found = false;
    for (ManagedFragmentHandler<PeriodicFragment> handler : getModel().getPeriodicalsFragments()) {
      if (!handler.getController().isSaved()) {
        found = true;
        break;
      }
    }
    context.send(BasicConfig.MODULE_MESSAGE, new CloseMessage(CloseMessage.Type.PERIODICAL, found));
  }

  @FXML
  protected void newButtonClicked(ActionEvent event) throws Exception {
    ManagedFragmentHandler<PeriodicFragment> handler = getContext().getManagedFragmentHandler(PeriodicFragment.class);
    handler.getController().init();
    getModel().addPeriodicalFragment(handler);
    mainVBox.getChildren().add(handler.getFragmentNode());
  }

}

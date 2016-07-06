package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.component.messages.PeriodicalMessage;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;
import dpfmanager.shell.modules.messages.messages.CloseMessage;
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

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
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
    if (message != null && message.isTypeOf(PeriodicalMessage.class)) {
      PeriodicalMessage pm = message.getTypedMessage(PeriodicalMessage.class);
      if (pm.isDelete()) {
        deletePeriodicals();
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

    // Read from command line
    getController().readPeriodicalChecksWindows();
  }

  public void addPeriodicalFragmentToView(ManagedFragmentHandler<PeriodicFragment> handler) {
    mainVBox.getChildren().add(handler.getFragmentNode());
  }

  private void deletePeriodicals() {
    ManagedFragmentHandler<PeriodicFragment> toDelete = null;
    for (ManagedFragmentHandler<PeriodicFragment> handler : getModel().getPeriodicalsFragments()) {
      if (handler.getController().isDelete()) {
        toDelete = handler;
        break;
      }
    }
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
    handler.getController().init(getController());
    getModel().addPeriodicalsFragments(handler);
    addPeriodicalFragmentToView(handler);
  }

  public <T> ManagedFragmentHandler<T> getManagedFragmentHandler(Class<T> clazz){
    return context.getManagedFragmentHandler(clazz);
  }

}

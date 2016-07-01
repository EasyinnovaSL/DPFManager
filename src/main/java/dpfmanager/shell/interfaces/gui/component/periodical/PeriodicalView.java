package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.component.messages.PeriodicalMessage;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard1Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard2Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard3Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard4Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard5Fragment;
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

import java.util.ArrayList;
import java.util.List;
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

  private List<ManagedFragmentHandler<PeriodicFragment>> periodicalsFragments;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(PeriodicalMessage.class)){
      PeriodicalMessage pm = message.getTypedMessage(PeriodicalMessage.class);
      if (pm.isDelete()){
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

    // Init periodical checks list
    periodicalsFragments = new ArrayList<>();
//
//    ManagedFragmentHandler<PeriodicFragment> hanlder = getContext().getManagedFragmentHandler(PeriodicFragment.class);
//    hanlder.getController().init("D:/Bilevel.tif", "Baseline HTML");
//    mainVBox.getChildren().add(hanlder.getFragmentNode());
//
//    ManagedFragmentHandler<PeriodicFragment> hanlder2 = getContext().getManagedFragmentHandler(PeriodicFragment.class);
//    hanlder2.getController().init("C:/Bilevel.tif", "Baseline JSON");
//    mainVBox.getChildren().add(hanlder2.getFragmentNode());
  }

  private void deletePeriodicals(){
    ManagedFragmentHandler<PeriodicFragment> toDelete = null;
    for (ManagedFragmentHandler<PeriodicFragment> hanlder : periodicalsFragments){
      if (hanlder.getController().isDelete()){
        toDelete = hanlder;
        break;
      }
    }
    if (toDelete != null){
      mainVBox.getChildren().remove(toDelete.getFragmentNode());
      periodicalsFragments.remove(toDelete);
    }
  }

  @FXML
  protected void newButtonClicked(ActionEvent event) throws Exception {
    ManagedFragmentHandler<PeriodicFragment> hanlder = getContext().getManagedFragmentHandler(PeriodicFragment.class);
    hanlder.getController().init();
    periodicalsFragments.add(hanlder);
    mainVBox.getChildren().add(hanlder.getFragmentNode());
  }

}

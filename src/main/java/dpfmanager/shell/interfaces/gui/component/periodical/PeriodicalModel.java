package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;

import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class PeriodicalModel extends DpfModel<PeriodicalView, PeriodicalController> {

  private List<ManagedFragmentHandler<PeriodicFragment>> periodicalsFragments;

  public PeriodicalModel() {
    // Init periodical checks list
    periodicalsFragments = new ArrayList<>();
  }

  public void addPeriodicalFragment(ManagedFragmentHandler<PeriodicFragment> handler) {
    periodicalsFragments.add(handler);
  }

  public void removePeriodicalCheck(ManagedFragmentHandler<PeriodicFragment> handler) {
    periodicalsFragments.remove(handler);
  }

  public ManagedFragmentHandler<PeriodicFragment> getPeriodicalCheckByUuid(String uuid) {
    for (ManagedFragmentHandler<PeriodicFragment> handler : periodicalsFragments) {
      if (handler.getController().getUuid().equals(uuid)) {
        return handler;
      }
    }
    return null;
  }

  public List<ManagedFragmentHandler<PeriodicFragment>> getPeriodicalsFragments() {
    return periodicalsFragments;
  }

}
/**
 * <h1>PeriodicalModel.java</h1> <p> This program is free software: you can redistribute it
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

import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;

import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adria Llorens on 07/03/2016.
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
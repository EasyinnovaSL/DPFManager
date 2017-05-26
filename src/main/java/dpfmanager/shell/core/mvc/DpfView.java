/**
 * <h1>DpfView.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.core.mvc;

import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.interfaces.gui.fragment.statics.StatisticsFragment;
import javafx.event.Event;
import javafx.scene.Node;

import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;

/**
 * Created by Adria Llorens on 07/03/2016.
 */
public abstract class DpfView<M extends DpfModel, C extends DpfController> extends DpfSimpleView implements ViewInterface<M, C> {

  private M model = null;
  private C controller = null;

  public DpfView(){
  }

  @Override
  public M getModel() {
    return model;
  }

  @Override
  public C getController() {
    return controller;
  }

  @Override
  public void setModel(M m) {
    model = m;
    model.setView(this);
    model.setContext(getContext());
  }

  @Override
  public void setController(C c) {
    controller = c;
    controller.setView(this);
    controller.setContext(getContext());
  }

}

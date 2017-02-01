/**
 * <h1>DpfModel.java</h1> <p> This program is free software: you can redistribute it
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

import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 07/03/2016.
 */
public class DpfModel<V extends ViewInterface<?, C>, C extends ControllerInterface> implements ModelInterface<V, C> {

  private V view;

  private Context context;

  private ResourceBundle bundle;

  public DpfModel(){
  }

  public void setView(V v){
    view = v;
  }

  public void setContext(Context cont){
    context = cont;
  }

  @Override
  public V getView() {
    return view;
  }

  @Override
  public C getController() {
    return getView().getController();
  }

  @Override
  public Context getContext() {
    return context;
  }

  public void setResourcebundle(ResourceBundle bundle){
    this.bundle = bundle;
  }

  public ResourceBundle getBundle() {
    return bundle;
  }

}

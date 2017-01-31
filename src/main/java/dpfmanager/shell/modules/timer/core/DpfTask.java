/**
 * <h1>DpfTask.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.timer.core;

import dpfmanager.shell.core.context.DpfContext;

/**
 * Created by Adria Llorens on 25/04/2016.
 */
public abstract class DpfTask {

  protected DpfContext context;

  private int interval;
  private boolean stop = true;

  // Main run method
  public void run(){
    if (!stop) {
      perform();
      sleep(interval);
    }
  }

  // Task logic
  public abstract void perform();



  private boolean sleep(int milis) {
    try {
      Thread.sleep(milis);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Task methods
   */
  public void setInterval(int interval) {
    this.interval = interval;
  }

  public void play(){
    stop = false;
    run();
  }

  public void stop(){
    stop = true;
  }

  /**
   * Context methods
   */
  public DpfContext getContext(){
    return context;
  }

  public void setContext(DpfContext c){
    if (context == null) {
      context = c;
      handleContext(context);
    }
  }

  protected abstract void handleContext(DpfContext context);
}

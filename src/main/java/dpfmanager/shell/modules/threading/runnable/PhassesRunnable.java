/**
 * <h1>PhassesRunnable.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.threading.runnable;

import dpfmanager.shell.core.context.DpfContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adria Llorens on 14/04/2016.
 */
public class PhassesRunnable extends DpfRunnable {

  private List<DpfRunnable> firstList;
  private List<DpfRunnable> secondList;

  public PhassesRunnable(List<DpfRunnable> l1, List<DpfRunnable> l2){
    // No context yet
    firstList = l1;
    secondList = l2;
  }

  @Override
  public void handleContext(DpfContext c) {
  }

  @Override
  public void runTask() {
    // Launch all first runnables
    List<Thread> threads = new ArrayList<>();
    int i = 1;
    for (DpfRunnable runnable : firstList){
      runnable.setName("First "+i);
      threads.add(run(runnable));
      i++;
    }

    // Wait for al of them
    boolean error = false;
    for (Thread waiting : threads){
      try {
        waiting.join();
      } catch(Exception e){
        error = true;
        break;
      }
    }

    // Now run second list
    if (!error){
      int j = 1;
      for (DpfRunnable runnable : secondList){
        runnable.setName("Second "+j);
        run(runnable);
        j++;
      }
    }
  }

  public Thread run(DpfRunnable runnable){
    runnable.setContext(context);
    Thread thread = new Thread(runnable);
    thread.start();
    return thread;
  }
}

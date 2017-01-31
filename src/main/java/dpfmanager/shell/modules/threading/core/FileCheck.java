/**
 * <h1>FileCheck.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.threading.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Adria Llorens on 14/04/2016.
 */
public class FileCheck {

  /** Check information */
  private long uuid;
  private int total;
  private int errors;
  private Configuration config;
  private String internal;
  private String input;
  private List<SmallIndividualReport> individuals;

  /** Runnable tasks of this check */
  private List<DpfRunnable> runnables;

  /** Initial task */
  private DpfRunnable initialTask;

  public FileCheck(long u){
    uuid = u;
    individuals = new ArrayList<>();
    runnables = new ArrayList<>();
    errors = 0;
  }

  public void init(int n, Configuration c, String i, String ri){
    total = n;
    config = c;
    internal = i;
    input = ri;
  }

  public void addIndividual(IndividualReport ir){
    SmallIndividualReport small = new SmallIndividualReport(ir);
    individuals.add(small);
  }

  public List<SmallIndividualReport> getIndividuals() {
    return individuals;
  }

  public Configuration getConfig() {
    return config;
  }

  public void setConfig(Configuration config) {
    this.config = config;
  }

  public String getInternal() {
    return internal;
  }

  public boolean allFinished(){
    return total == getFinished();
  }

  public void addError(){
    errors++;
  }

  public long getUuid() {
    return uuid;
  }

  public int getFinished() {
    return individuals.size() + errors;
  }

  public String getInput() {
    String ret = input;
    if (ret.length() > 50) {
      ret = ret.substring(0, 47) + "...";
    }
    return ret;
  }

  public void setInitialTask(DpfRunnable initialTask) {
    this.initialTask = initialTask;
  }

  public DpfRunnable getInitialTask() {
    return initialTask;
  }

  public int getTotal() {
    return total;
  }
}

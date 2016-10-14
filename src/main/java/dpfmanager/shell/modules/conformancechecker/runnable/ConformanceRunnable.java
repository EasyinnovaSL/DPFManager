/**
 * <h1>ConformanceRunnable.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.conformancechecker.runnable;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.core.ProcessInput;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.util.List;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public class ConformanceRunnable extends DpfRunnable {

  private String filename;
  private ProcessInput pi;
  private String internalReportFolder;
  private Configuration config;
  private int id;
  private long uuid;

  public ConformanceRunnable(List<ConformanceChecker> list){
    // No context yet
    pi = new ProcessInput(list);
  }

  @Override
  public void handleContext(DpfContext context) {
    pi.setContext(context);
  }

  public void setParameters(String name, int i, String internal, Configuration conf, long u){
    filename = name;
    id = i;
    internalReportFolder = internal;
    config = conf;
    uuid = u;
  }

  @Override
  public void runTask() {
    // if config is null, get the default one
    if (config == null){
      config = pi.getDefaultConfigurationFromFile(filename);
    }
    // If no default one, create one
    if (config == null){
      config = new Configuration();
      config.addFormat("XML");
    }
    // Process the input and get a list of individual reports
    IndividualReport ir = pi.processFile(filename, internalReportFolder, config, id);
    if (ir != null && !ir.isError()) {
      ir.setIdReport(id);
      ir.setInternalReportFolder(internalReportFolder);
      // Tell report module to create it
      ir.setUuid(uuid);
      context.send(BasicConfig.MODULE_REPORT, new IndividualReportMessage(ir, config));
    } else{
      // Tell multi threading that one report fail (no wait for it)
      if (ir != null){
        ir.setIdReport(id);
        ir.setInternalReportFolder(internalReportFolder);
        ir.setUuid(uuid);
      }
      context.send(BasicConfig.MODULE_THREADING, new IndividualStatusMessage(ir, config, uuid));
    }
  }

}

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
 * @author Adria Llorens
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
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.runnable.IndividualReportsRunnable;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.util.List;

/**
 * Created by Adria Llorens on 13/04/2016.
 */
public class ConformanceRunnable extends DpfRunnable {

  private String filename;
  private ProcessInput pi;
  private String internalReportFolder;
  private Configuration config;
  private int id;
  private long uuid;
  private ReportGenerator generator;

  public ConformanceRunnable(List<ConformanceChecker> list, ReportGenerator gen) {
    // No context yet
    pi = new ProcessInput(list);
    generator = gen;
  }

  @Override
  public void handleContext(DpfContext context) {
    pi.setContext(context);
  }

  public void setParameters(String name, int i, String internal, Configuration conf, long u) {
    filename = name;
    id = i;
    internalReportFolder = internal;
    config = conf;
    uuid = u;
  }

  @Override
  public void runTask() {
    // if config is null, get the default one
    if (config != null) {
      boolean isQuick = config.isQuick();
      if (config.isDefault()) {
        config = pi.getDefaultConfigurationFromFile(filename);
        if (config != null){
          config.setQuick(isQuick);
        }
      }
    }
    if (config == null) {
      config = pi.getDefaultConfigurationFromFile(filename);
    }
    if (config == null) {
      config = pi.getDefaultConfigurationFromFile("a.tif");
    }
    // If no default one, create one
    if (config == null) {
      config = new Configuration();
      config.addFormat("XML");
    }

    // Process the input and get a list of individual reports
    IndividualReport ir = pi.processFile(filename, internalReportFolder, config, id);
    if (ir != null && !ir.isError()) {
      ir.setIdReport(id);
      ir.setInternalReportFolder(internalReportFolder);
      ir.setUuid(uuid);

      // Serialize it
      String filenameNorm = filename.replaceAll("\\\\", "/");
      String serFileName = ir.getReportId() + "-" +filenameNorm.substring(filenameNorm.lastIndexOf("/") + 1) + ".ser";
      ir.filter();
      ir.write(internalReportFolder + "/serialized", serFileName);
      ir.defilter();
      ir = (IndividualReport) IndividualReport.read(internalReportFolder + "/serialized/" + serFileName);

      // Create report
      IndividualReportsRunnable run = new IndividualReportsRunnable(generator);
      run.setContext(context);
      run.setParameters(ir, config);
      run.runTask();
    } else {
      // Tell multi threading that one report fail (no wait for it)
      if (ir != null) {
        ir.setIdReport(id);
        ir.setInternalReportFolder(internalReportFolder);
        ir.setUuid(uuid);
      }
      context.send(BasicConfig.MODULE_THREADING, new IndividualStatusMessage(ir, config, uuid));
    }
  }

}

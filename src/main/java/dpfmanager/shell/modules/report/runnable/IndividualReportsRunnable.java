/**
 * <h1>IndividualReportsRunnable.java</h1> <p> This program is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as published by the Free
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

package dpfmanager.shell.modules.report.runnable;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import org.apache.logging.log4j.Level;

import java.io.File;

/**
 * Created by Adria Llorens on 13/04/2016.
 */
public class IndividualReportsRunnable extends DpfRunnable {

  private IndividualReport ir;
  private ReportGenerator generator;
  private Configuration config;

  public IndividualReportsRunnable(ReportGenerator g) {
    // No context yet
    generator = g;
  }

  @Override
  public void handleContext(DpfContext context) {
  }

  public void setParameters(IndividualReport i, Configuration c) {
    ir = i;
    config = c;
  }

  @Override
  public void runTask() {
    // Generate report
    String outputfile = generator.getReportName(ir.getInternalReportFodler(), ir.getReportFileName(), ir.getIdReport());
    generator.generateIndividualReport(outputfile, ir, config);

    // Notify individual report finished
//    context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("individualReport").replace("%1", getOutputFolderFile(config, outputfile))));
    context.send(BasicConfig.MODULE_THREADING, new IndividualStatusMessage(ir, config));
  }

}

/**
 * <h1>ReportService.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.report.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.report.runnable.GlobalReportsRunnable;
import dpfmanager.shell.modules.report.runnable.IndividualReportsRunnable;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_REPORT)
@Scope("singleton")
public class ReportService extends DpfService {

  private ReportGenerator generator;

  private Configuration config;
  private String internalReportFolder;

  @PostConstruct
  public void init() {
    // No context yet
    generator = new ReportGenerator();
  }

  @Override
  protected void handleContext(DpfContext context) {
    generator.setContext(context);
  }

  // Main function
  public void tractIndividualMessage(IndividualReportMessage message) {
    createIndividualReports(message.getIndividual(), message.getConfig());
  }

  // Main function
  public void tractGlobalMessage(GlobalReportMessage message) {
    createGlobalReports(message.getUuid(), message.getIndividuals(), message.getConfig());
    config = message.getConfig();
  }

  private void createIndividualReports(IndividualReport ir, Configuration config) {
    // Create individual runnable
    IndividualReportsRunnable run = new IndividualReportsRunnable(generator);
    run.setParameters(ir, config);
    context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(ir.getUuid(), run));
  }

  private void createGlobalReports(Long uuid, List<SmallIndividualReport> individuals, Configuration config) {
    // Create global runnable
    GlobalReportsRunnable run = new GlobalReportsRunnable(generator);
    run.setParameters(individuals, config);
    context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(uuid, run));
  }

  public Configuration getConfig() {
    return config;
  }
}

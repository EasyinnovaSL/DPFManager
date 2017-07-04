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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.report.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.report.messages.GenerateIndividualMessage;
import dpfmanager.shell.modules.report.messages.GenerateMessage;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.report.runnable.GlobalReportsRunnable;
import dpfmanager.shell.modules.report.runnable.IndividualReportsRunnable;
import dpfmanager.shell.modules.report.runnable.MakeReportRunnable;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * Created by Adria Llorens on 07/04/2016.
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
    createGlobalReports(message.getUuid(), message.getIndividuals(), message.getConfig(), message.getStart(), message.getCheckedIsos());
    config = message.getConfig();
  }

  private void createIndividualReports(IndividualReport ir, Configuration config) {
    // Create individual runnable
    IndividualReportsRunnable run = new IndividualReportsRunnable(generator);
    run.setParameters(ir, config);
    context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(ir.getUuid(), run));
  }

  private void createGlobalReports(Long uuid, List<SmallIndividualReport> individuals, Configuration config, Date start, List<String> checkedIsos) {
    // Create global runnable
    GlobalReportsRunnable run = new GlobalReportsRunnable(generator);
    run.setParameters(individuals, config, start, checkedIsos);
    context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(uuid, run));
  }

  public void tractGenerateMessage(GenerateMessage gm) {
    Long uuid = gm.getUuid();
    String internalReportFolder = null;
//    Integer globalValue = gm.getFormat().toLowerCase().equals("mets") ? 0 : 1;
    Integer globalValue = 1;
    Integer max = (gm.isOnlyGlobal()) ? globalValue : gm.getGlobalReport().getIndividualReports().size() + globalValue;

    // Transform individual reports runnables
    List<MakeReportRunnable> individualsMakers = new ArrayList<>();
    for (SmallIndividualReport sir : gm.getGlobalReport().getIndividualReports()) {
      if (internalReportFolder == null) internalReportFolder = sir.getInternalReportFodler();
      sir.predictImagePath();

      MakeReportRunnable mrr = new MakeReportRunnable(generator);
      mrr.setIndividualParameters(sir, gm.getGlobalReport(), gm.getFormat());
      individualsMakers.add(mrr);
    }

    // Transforms global report and show runnable
    MakeReportRunnable mrr = new MakeReportRunnable(generator);
    mrr.setGlobalParameters(internalReportFolder, gm.getGlobalReport(), globalValue, gm.getFormat());

    // Init progress bar
    context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(uuid, max, globalValue, mrr));


    if (!gm.isOnlyGlobal()){
      // Start individual transforms
      for (MakeReportRunnable iMrr : individualsMakers){
        context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(uuid, iMrr));
      }
    } else {
      // Start global transforms
      context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(uuid, 0));
    }
  }

  public void tractGenerateIndividualMessage(GenerateIndividualMessage gm) {
    IndividualReport ir = (IndividualReport) IndividualReport.read(gm.getPath());
    MakeReportRunnable mrr = new MakeReportRunnable(generator);
    mrr.setIndividualParameters(ir, gm.getPath(), gm.getFormat(), gm.getConfig());
    context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(System.currentTimeMillis(), mrr));
  }

  public Configuration getConfig() {
    return config;
  }
}

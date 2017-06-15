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
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.io.File;

/**
 * Created by Adria Llorens on 13/04/2016.
 */
public class MakeReportRunnable extends DpfRunnable {

  private SmallIndividualReport sir;
  private String format;
  private ReportGenerator generator;
  private GlobalReport global;
  private String internalReportFolder;
  private Integer globalValue;

  private boolean individual = false;

  public MakeReportRunnable(ReportGenerator g) {
    // No context yet
    generator = g;
  }

  @Override
  public void handleContext(DpfContext context) {
  }

  public void setIndividualParameters(SmallIndividualReport i, GlobalReport g, String f) {
    sir = i;
    global = g;
    format = f;
    individual = true;
  }

  public void setGlobalParameters(String i, GlobalReport g, Integer gv, String f) {
    internalReportFolder = i;
    global = g;
    format = f;
    globalValue = gv;
    individual = false;
  }

  @Override
  public void runTask() {
    if (individual){
      generateIndividualReport();
    } else {
      generateGlobalReport();
    }
  }

  private void generateIndividualReport(){
    String filename = sir.getReportPath().substring(sir.getReportPath().lastIndexOf("/") + 1);
    String reportSerialized = sir.getInternalReportFodler() + "serialized/" + filename + ".ser";
    File reportSerializedFile = new File(reportSerialized);
    if (reportSerializedFile.exists()){
      IndividualReport ir = (IndividualReport) IndividualReport.read(reportSerialized);
      String outputfile = generator.getReportName(ir.getInternalReportFodler(), ir.getReportFileName(), ir.getIdReport());
      generator.transformIndividualReport(outputfile, format, ir, global.getConfig());
      context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(1));
    }
  }

  private void generateGlobalReport(){
    String outputPath = generator.transformGlobalReport(internalReportFolder, format, global);
    context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(globalValue));
    if (globalValue == 0) {
      outputPath = internalReportFolder;
    }
    if (outputPath != null){
      context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(format, outputPath));
    }
  }

}

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
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.io.File;
import java.util.List;

/**
 * Created by Adria Llorens on 13/04/2016.
 */
public class MakeReportRunnable extends DpfRunnable {

  private SmallIndividualReport sir;
  private IndividualReport ir;
  private String format;
  private String path;
  private ReportGenerator generator;
  private GlobalReport global;
  private ReportGui info;
  private String internalReportFolder;
  private Integer globalValue;
  private Configuration config;

  private boolean individual = false;
  private boolean onlyIndividual = false;
  private boolean onlyGlobal = true;
  private boolean showAtEnd;

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

  public void setIndividualParameters(IndividualReport i, String p, String f, Configuration c) {
    ir = i;
    path = p;
    format = f;
    config = c;
    onlyIndividual = true;
  }

  public void setGlobalParameters(String i, ReportGui in, Integer gv, String f, boolean o) {
    internalReportFolder = i;
    info = in;
    global = info.getGlobalReport();
    format = f;
    globalValue = gv;
    onlyGlobal = o;
    individual = false;
    showAtEnd = false;
  }

  public void setShowAtEnd(boolean showAtEnd) {
    this.showAtEnd = showAtEnd;
  }

  @Override
  public void runTask() {
    if (onlyIndividual){
      generateOnlyIndividualReport();
    } else if (individual){
      generateIndividualReport();
    } else {
      generateGlobalReport();
    }
  }

  private void generateOnlyIndividualReport(){
    File reportSerializedFile = new File(path);
    if (reportSerializedFile.exists()){
      IndividualReport ir = (IndividualReport) IndividualReport.read(reportSerializedFile.getPath());
      String outputfile = generator.getReportName(ir.getInternalReportFodler(), ir.getReportFileName(), ir.getIdReport());
      generator.transformIndividualReport(outputfile, format, ir, config);
      String finalOutput = outputfile;
      if (format.toLowerCase().equals("html")){
        String first = outputfile.substring(0, outputfile.lastIndexOf("/"));
        String second = outputfile.substring(outputfile.lastIndexOf("/")) + "." + format;
        finalOutput = first + "/html" + second;
      } else if (format.toLowerCase().equals("mets")) {
        finalOutput = outputfile + ".mets.xml";
      } else {
        finalOutput = outputfile + "." + format;
      }
      context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(format, finalOutput));
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
      context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(getUuid(), 1));
    }
  }

  private void generateGlobalReport(){
    String outputPath = generator.transformGlobalReport(internalReportFolder, format, global);
    context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(getUuid(), globalValue));
    if (format.toLowerCase().equals("mets")) outputPath = internalReportFolder;
    if (outputPath != null){
      if (onlyGlobal){
        // Show report
        context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(getUuid(), format, outputPath));
      } else if (showAtEnd) {
        // Show javafx global report
        context.send(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(getUuid(), info));
      }
    }
  }

}

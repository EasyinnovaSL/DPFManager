/**
 * <h1>GlobalReportsRunnable.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.report.runnable;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Adria Llorens on 13/04/2016.
 */
public class GlobalReportsRunnable extends DpfRunnable {

  private List<SmallIndividualReport> individuals;
  private ReportGenerator generator;
  private Configuration config;
  private Date start;
  private List<String> checkedIsos;
  private Map<String, String> zipsPaths;

  public GlobalReportsRunnable(ReportGenerator g) {
    // No context yet
    generator = g;
  }

  @Override
  public void handleContext(DpfContext context) {
  }

  public void setParameters(List<SmallIndividualReport> i, Configuration c, Date s, List<String> ci, Map<String, String> z) {
    individuals = i;
    config = c;
    start = s;
    checkedIsos = ci;
    zipsPaths = z;
  }

  @Override
  public void runTask() {
    String internalReportFolder = null;
    Long uuid = null;

    // Generate GlobalReport
    GlobalReport global = new GlobalReport();
    global.init(config, checkedIsos, zipsPaths);
    for (SmallIndividualReport individual : individuals) {
      global.addIndividual(individual);
      if (individual != null) {
        if (internalReportFolder == null) {
          internalReportFolder = individual.getInternalReportFodler();
        }
        if (uuid == null) {
          uuid = individual.getUuid();
        }
      }
    }
    global.generate(start);

    if (internalReportFolder != null) {
      // Create it
      try {
        generator.makeSummaryReport(internalReportFolder, global, config);
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("globalReport").replace("%1", config.getOutput() != null ? config.getOutput() : internalReportFolder)));
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("durationReport").replace("%1", global.prettyPrintDuration())));
      } catch (OutOfMemoryError e) {
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorOccurred"), bundle.getString("outOfMemory")));
      }

      // Send report over FTP
      try {
        if (DPFManagerProperties.getFeedback()) {
          sendFtpCamel(internalReportFolder);
        }
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("exception"), e));
        e.printStackTrace();
      }

      // Inform that report has finished
      context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.FINISH, uuid));
    }
  }

  /**
   * Sends the report to the preforma FTP.
   *
   * @throws NoSuchAlgorithmException An error occurred
   */
  private void sendFtpCamel(String internalReportFolder) throws NoSuchAlgorithmException, IOException {
    File serFile = new File(internalReportFolder + "/summary.ser");
    if (!serFile.exists()) return;
    String summarySer = FileUtils.readFileToString(serFile, "utf-8");
    String ftp = "84.88.145.109";
    String user = "preformaapp";
    String password = "2.eX#lh>";
    CamelContext contextcc = new DefaultCamelContext();

    try {
      contextcc.addRoutes(new RouteBuilder() {
        public void configure() {
          from("direct:sendFtp").to("sftp://" + user + "@" + ftp + "/?password=" + password);
        }
      });
      ProducerTemplate template = contextcc.createProducerTemplate();
      contextcc.start();
      template.sendBody("direct:sendFtp", summarySer);
      contextcc.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("feedbackSent")));
  }

}

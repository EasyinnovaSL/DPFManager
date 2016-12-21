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
 * @author Adrià Llorens
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
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public class GlobalReportsRunnable extends DpfRunnable {

  private List<IndividualReport> individuals;
  private ReportGenerator generator;
  private Configuration config;

  public GlobalReportsRunnable(ReportGenerator g){
    // No context yet
    generator = g;
  }

  @Override
  public void handleContext(DpfContext context) {
  }

  public void setParameters(List<IndividualReport> i, Configuration c){
    individuals = i;
    config = c;
  }

  @Override
  public void runTask() {
    String internalReportFolder = null;
    Long uuid = null;

    // Generate GlobalReport
    GlobalReport global = new GlobalReport();
    for (IndividualReport individual : individuals) {
      global.addIndividual(individual);
      if (individual != null){
        if (internalReportFolder == null){
          internalReportFolder = individual.getInternalReportFodler();
        }
        if (uuid == null){
          uuid = individual.getUuid();
        }
      }
    }
    global.generate();

    if (internalReportFolder != null) {
      // Create it
      String summaryXmlFile = null;
      try {
        summaryXmlFile = generator.makeSummaryReport(internalReportFolder, global, config);
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("globalReport").replace("%1", internalReportFolder)));
      } catch (OutOfMemoryError e) {
        context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorOccurred"), bundle.getString("outOfMemory")));
      }

      // Send report over FTP
      try {
        if (DPFManagerProperties.getFeedback() && summaryXmlFile != null) {
          sendFtpCamel(summaryXmlFile);
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
   * @param summaryXmlFile the summary xml
   * @throws NoSuchAlgorithmException An error occurred
   */
  private void sendFtpCamel(String summaryXmlFile)
      throws NoSuchAlgorithmException, IOException {
    String summaryXml = FileUtils.readFileToString(new File(summaryXmlFile), "utf-8");
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("sendingFeedback")));
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
      template.sendBody("direct:sendFtp", summaryXml);
      contextcc.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("feedbackSent")));
  }

}

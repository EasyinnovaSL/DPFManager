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
import org.apache.logging.log4j.Level;

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

    // Create it
    String summaryXml = null;
    try {
      summaryXml = generator.makeSummaryReport(internalReportFolder, global, config);
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("globalReport").replace("%1", internalReportFolder)));
    } catch (OutOfMemoryError e) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorOccurred"), bundle.getString("outOfMemory")));
    }

    // Send report over FTP
    try {
      if (DPFManagerProperties.getFeedback() && summaryXml != null) {
        sendFtpCamel(summaryXml);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("exception"), e));
      e.printStackTrace();
    }

    // Inform that report has finished
    context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.FINISH, uuid));
  }

  /**
   * Sends the report to the preforma FTP.
   *
   * @param summaryXml the summary xml
   * @throws NoSuchAlgorithmException An error occurred
   */
  private void sendFtpCamel(String summaryXml)
      throws NoSuchAlgorithmException {
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

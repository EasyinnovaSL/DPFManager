package dpfmanager.shell.modules.report.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
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
  private int idReport;

  @PostConstruct
  public void init() {
    // No context yet
    generator = new ReportGenerator();
    idReport = 1;
  }

  @Override
  protected void handleContext(DpfContext context){
    generator.setContext(context);
  }

  // Main function
  public void tractIndividualMessage(IndividualReportMessage message){
    createIndividualReports(message.getIndividuals(), message.getConfig());
  }

  // Main function
  public void tractGlobalMessage(GlobalReportMessage message){
    createGlobalReports(message.getGlobal(), message.getConfig());
    config =  message.getConfig();
  }

  private void createIndividualReports(List<IndividualReport> individuals, Configuration config){
    for (IndividualReport ir : individuals) {
      // Generate report
      String outputfile = generator.getReportName(internalReportFolder, ir.getReportFileName(), idReport);
      generator.generateIndividualReport(outputfile, ir, config, internalReportFolder);
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Created report '" + outputfile + "'"));
      idReport++;
    }
  }

  private void createGlobalReports(GlobalReport global, Configuration config){
    String summaryXml = null;
    try {
      summaryXml = generator.makeSummaryReport(internalReportFolder, global, config);
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Created global report into '" + internalReportFolder + "'"));
    } catch (OutOfMemoryError e) {
      context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "An error occured", "Out of memory"));
    }

    // Send report over FTP
    try {
      if(DPFManagerProperties.getFeedback() && summaryXml != null) {
        sendFtpCamel(summaryXml);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception ocurred", e));
      e.printStackTrace();
    }
  }

  public void initNewReportFolder(String folder){
    internalReportFolder = folder;
  }

  public String getInternalReportFolder() {
    return internalReportFolder;
  }

  /**
   * Sends the report to the preforma FTP.
   *
   * @param summaryXml the summary xml
   * @throws NoSuchAlgorithmException An error occurred
   */
  private void sendFtpCamel(String summaryXml)
      throws NoSuchAlgorithmException {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Sending feedback"));
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
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Feedback sent"));
  }

  public Configuration getConfig() {
    return config;
  }
}

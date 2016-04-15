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
    createGlobalReports(message.getIndividuals(), message.getConfig());
    config = message.getConfig();
  }

  private void createIndividualReports(IndividualReport ir, Configuration config) {
    // Create individual runnable
    IndividualReportsRunnable run = new IndividualReportsRunnable(generator);
    run.setParameters(ir, config);
    context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(run));
  }

  private void createGlobalReports(List<IndividualReport> individuals, Configuration config) {
    // Create global runnable
    GlobalReportsRunnable run = new GlobalReportsRunnable(generator);
    run.setParameters(individuals, config);
    context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(run));
  }

  public Configuration getConfig() {
    return config;
  }
}

package dpfmanager.shell.modules.report.runnable;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.core.ProcessInput;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public class IndividualReportsRunnable extends DpfRunnable {

  private ProcessInput pi;
  private IndividualReport ir;
  private ReportGenerator generator;
  private Configuration config;

  public IndividualReportsRunnable(ReportGenerator g) {
    // No context yet
    pi = new ProcessInput();
    generator = g;
  }

  @Override
  public void handleContext(DpfContext context) {
    pi.setContext(context);
  }

  public void setParameters(IndividualReport i, Configuration c) {
    ir = i;
    config = c;
  }

  @Override
  public void runTask() {
    // Generate report
    String outputfile = generator.getReportName(ir.getInternalReportFodler(), ir.getReportFileName(), ir.getIdReport());
    generator.generateIndividualReport(outputfile, ir, config, ir.getInternalReportFodler());
    // Notify individual report finished
    context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Created individual report '" + outputfile + "'"));
    context.send(BasicConfig.MODULE_THREADING, new IndividualStatusMessage(ir));
  }

}

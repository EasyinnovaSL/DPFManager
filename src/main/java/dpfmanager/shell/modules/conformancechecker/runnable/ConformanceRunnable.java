package dpfmanager.shell.modules.conformancechecker.runnable;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.core.ProcessInput;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.util.List;

/**
 * Created by Adrià Llorens on 13/04/2016.
 */
public class ConformanceRunnable extends DpfRunnable {

  private String filename;
  private ProcessInput pi;
  private String internalReportFolder;
  private Configuration config;
  private int id;
  private long uuid;

  public ConformanceRunnable(){
    // No context yet
    pi = new ProcessInput();
  }

  @Override
  public void handleContext(DpfContext context) {
    pi.setContext(context);
  }

  public void setParameters(String name, int i, String internal, Configuration conf, long u){
    filename = name;
    id = i;
    internalReportFolder = internal;
    config = conf;
    uuid = u;
  }

  @Override
  public void runTask() {
    printOut("Processing file " + filename+" (thread)");

    // Process the input and get a list of individual reports
    IndividualReport ir = pi.processFile(filename, internalReportFolder, config, id);
    ir.setUuid(uuid);

    // Tell report module to create it
    context.send(BasicConfig.MODULE_REPORT, new IndividualReportMessage(ir, config));
  }

}

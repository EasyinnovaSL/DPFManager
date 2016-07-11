package dpfmanager.shell.modules.conformancechecker.runnable;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.core.ProcessInput;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
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
    // Process the input and get a list of individual reports
    IndividualReport ir;
    ir = pi.processFile(filename, internalReportFolder, config, id);
    if (ir != null && !ir.isError()) {
      ir.setIdReport(id);
      ir.setInternalReportFolder(internalReportFolder);
      // Tell report module to create it
      ir.setUuid(uuid);
      context.send(BasicConfig.MODULE_REPORT, new IndividualReportMessage(ir, config));
    } else{
      // Tell multi threading that one report fail (no wait for it)
      if (ir != null){
        ir.setIdReport(id);
        ir.setInternalReportFolder(internalReportFolder);
        ir.setUuid(uuid);
      }
      context.send(BasicConfig.MODULE_THREADING, new IndividualStatusMessage(ir, uuid));
    }
  }

}

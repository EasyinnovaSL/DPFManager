package dpfmanager.shell.modules.report.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class GlobalReportMessage extends DpfMessage {

  private GlobalReport global;
  private Configuration config;

  public GlobalReportMessage(GlobalReport g, Configuration c){
    global = g;
    config = c;
  }

  public GlobalReport getGlobal() {
    return global;
  }

  public Configuration getConfig() {
    return config;
  }

}

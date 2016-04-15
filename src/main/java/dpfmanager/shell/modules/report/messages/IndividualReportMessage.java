package dpfmanager.shell.modules.report.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;

import java.util.List;

/**
 * Created by Adrià Llorens on 08/04/2016.
 */
public class IndividualReportMessage extends DpfMessage {

  private IndividualReport individual;
  private Configuration config;

  public IndividualReportMessage(IndividualReport i, Configuration c){
    individual = i;
    config = c;
  }

  public IndividualReport getIndividual() {
    return individual;
  }

  public Configuration getConfig() {
    return config;
  }

}

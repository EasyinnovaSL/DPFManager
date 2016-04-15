package dpfmanager.shell.modules.threading.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;

/**
 * Created by Adrià Llorens on 08/04/2016.
 */
public class IndividualStatusMessage extends DpfMessage {

  private IndividualReport individual;

  public IndividualStatusMessage(IndividualReport ir) {
    individual = ir;
  }

  public IndividualReport getIndividual() {
    return individual;
  }
}

package dpfmanager.shell.modules.threading.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;

/**
 * Created by Adrià Llorens on 08/04/2016.
 */
public class IndividualStatusMessage extends DpfMessage {

  private IndividualReport individual;
  private Long uuid;

  public IndividualStatusMessage(IndividualReport ir) {
    individual = ir;
  }

  public IndividualStatusMessage(IndividualReport ir, Long u) {
    individual = ir;
    uuid = u;
  }

  public IndividualReport getIndividual() {
    return individual;
  }

  public Long getUuid() {
    if (uuid != null){
      return uuid;
    } else if (individual != null){
      return individual.getUuid();
    }
    return uuid;
  }
}

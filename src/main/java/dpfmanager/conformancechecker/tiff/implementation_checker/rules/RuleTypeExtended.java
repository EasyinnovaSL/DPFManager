package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;

/**
 * Created by easy on 14/10/2016.
 */
public class RuleTypeExtended extends RuleType {
  String iso;

  public void setIso(String iso) {
    this.iso = iso;
  }

  public String getIso() {
    return iso;
  }

  public RuleTypeExtended(RuleType rul) {
    super();
    this.setAssert(rul.getAssert());
    this.setContext(rul.getContext());
    this.setDescription(rul.getDescription());
    this.setId(rul.getId());
    this.setLevel(rul.getLevel());
    this.setTitle(rul.getTitle());
  }
}

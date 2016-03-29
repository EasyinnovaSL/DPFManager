package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffNode;

/**
 * Created by easy on 16/03/2016.
 */
public class RuleResult {
  String message;
  boolean ok;
  TiffNode node;
  RuleObject rule;

  public RuleResult(boolean ok, TiffNode node, RuleObject rule, String message) {
    this.message = message;
    this.node = node;
    this.ok = ok;
    this.rule = rule;
  }

  public String getMessage() {
    return message;
  }

  public boolean ok() {
    return ok;
  }

  @Override
  public String toString() {
    return ok ? "OK" : "KO" + ": " + message;
  }

  public void setNode(TiffNode node) {
    this.node = node;
  }

  public TiffNode getNode() {
    return node;
  }
}

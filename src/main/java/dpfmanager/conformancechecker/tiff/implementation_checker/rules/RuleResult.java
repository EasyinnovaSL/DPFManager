package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffNode;

/**
 * Created by easy on 16/03/2016.
 */
public class RuleResult {
  String message;
  String location = null;
  boolean ok;
  TiffNode node;
  RuleObject rule;
  boolean warning = false;

  public RuleResult() {
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setWarning(boolean warning) {
    this.warning = warning;
  }

  public boolean getWarning() {
    return warning;
  }

  public void setLocation(String location) {
    this.location = location;
  }

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

  public String getContext() {
    return node.getContext();
  }

  public String getLocation() {
    if (location != null) return location;
    else return getContext();
  }

  public String getDescription() {
    return message;
  }
}

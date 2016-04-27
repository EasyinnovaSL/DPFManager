package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 11/03/2016.
 */
public class RuleObject {
  String context;
  String reference;
  int critical;
  AssertObject assertion;
  List<DiagnosticObject> diagnostics = null;

  public void setContext(String context) {
    this.context = context;
  }

  @XmlAttribute
  public String getContext() {
    return context;
  }

  @XmlElement(name = "diagnostic")
  public void setDiagnostics(List<DiagnosticObject> diagnostics) {
    this.diagnostics = diagnostics;
  }

  public List<DiagnosticObject> getDiagnostics() {
    return diagnostics;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getReference() {
    return reference;
  }

  @XmlAttribute
  public void setCritical(int critical) {
    this.critical = critical;
  }

  public int getCritical() {
    return critical;
  }

  @XmlElement(name = "assert")
  public void setAssertionField(AssertObject assertion) {
    this.assertion = assertion;
  }

  public AssertObject getAssertionField() {
    return assertion;
  }

  @Override
  public String toString() {
    String s = "";
    s += assertion.toString();
    return s;
  }
}

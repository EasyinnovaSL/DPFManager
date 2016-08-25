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
  String level;
  String id;
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
  public void setLevel(String level) {
    this.level = level;
  }

  public String getLevel() {
    return level;
  }

  @XmlAttribute
  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
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

  public boolean isCritical() {
    return level != null && level.equals("critical");
  }

  public boolean isWarning() {
    return level != null && level.equals("warning");
  }
}

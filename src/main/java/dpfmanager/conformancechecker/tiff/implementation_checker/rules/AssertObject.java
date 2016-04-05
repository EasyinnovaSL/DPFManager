package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by easy on 11/03/2016.
 */
public class AssertObject {
  String test;
  String diagnostics;

  @XmlValue
  protected String value;

  public void setTest(String test) {
    this.test = test;
  }

  @XmlAttribute(name="test")
  public String getTest() {
    return test;
  }

  public void setDiagnostics(String diagnostics) {
    this.diagnostics = diagnostics;
  }

  @XmlAttribute(name="diagnostics")
  public String getDiagnostics() {
    return diagnostics;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    String s = "";
    s += value;
    return s;
  }
}

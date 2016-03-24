package dpfmanager.conformancechecker.tiff.ImplementationChecker.rules;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by easy on 23/03/2016.
 */
public class IncludeObject {
  String subsection;

  @XmlValue
  String value;

  public void setSubsection(String subsection) {
    this.subsection = subsection;
  }

  @XmlAttribute
  public String getSubsection() {
    return subsection;
  }

  public String getValue() {
    return value;
  }
}

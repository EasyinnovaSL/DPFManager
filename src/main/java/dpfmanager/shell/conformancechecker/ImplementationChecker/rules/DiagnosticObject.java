package dpfmanager.shell.conformancechecker.ImplementationChecker.rules;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by easy on 11/03/2016.
 */
public class DiagnosticObject {
  String id;
  String lang;

  @XmlValue
  String value;

  public void setId(String id) {
    this.id = id;
  }

  @XmlAttribute
  public String getId() {
    return id;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  @XmlAttribute
  public String getLang() {
    return lang;
  }
}

package dpfmanager.conformancechecker.tiff.ImplementationChecker.rules;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 11/03/2016.
 */
public class RulesObject {
  String description;
  List<RuleObject> rules = null;

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlAttribute
  public String getDescription() {
    return description;
  }

  @XmlElement(name = "rule")
  public void setRules(List<RuleObject> rules) {
    this.rules = rules;
  }

  public List<RuleObject> getRules() {
    return rules;
  }

  @Override
  public String toString() {
    return description;
  }
}

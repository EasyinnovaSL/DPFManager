package dpfmanager.shell.conformancechecker.ImplementationChecker.rules;

import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RulesObject;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by easy on 11/03/2016.
 */
@XmlRootElement
public class ImplementationCheckerObject {
  String title;
  String description;
  List<RulesObject> rules = null;

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @XmlElement(name = "rules")
  public void setRules(List<RulesObject> rules) {
    this.rules = rules;
  }

  public List<RulesObject> getRules() {
    return rules;
  }
}

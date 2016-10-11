package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

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
  String version;
  String author;
  String iso;
  List<IncludeObject> includes = null;
  List<RulesObject> rules = null;

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setIso(String iso) {
    this.iso = iso;
  }

  public String getIso() {
    return iso;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getAuthor() {
    return author;
  }

  @XmlElement(name = "include")
  public void setIncludes(List<IncludeObject> includes) {
    this.includes = includes;
  }

  public List<IncludeObject> getIncludes() {
    return includes;
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

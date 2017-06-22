package dpfmanager.shell.interfaces.gui.fragment.wizard;

/**
 * Created by Adria Llorens on 24/10/2016.
 */
public class RuleTreeItem {

  private String id;
  private String name;
  private String description;
  private String reference;

  public RuleTreeItem(){
  }

  public RuleTreeItem(String n){
    id = "";
    name = n;
    description = "";
    reference = null;
  }

  public RuleTreeItem(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
    reference = null;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }
}

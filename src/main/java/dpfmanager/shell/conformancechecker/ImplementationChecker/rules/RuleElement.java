package dpfmanager.shell.conformancechecker.ImplementationChecker.rules;

import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffNode;

import java.util.List;

/**
 * Created by easy on 14/03/2016.
 */
public class RuleElement {
  String fieldName;
  Filter filter;
  TiffNode node;

  public RuleElement(String field, TiffNode node) {
    this.node = node;
    filter = null;
    fieldName = field;
    if (field.contains("[")) {
      String filter = field.substring(field.indexOf("[") + 1);
      filter = filter.substring(0, filter.indexOf("]")).trim();
      this.filter = new Filter(filter);
      fieldName = field.substring(0, field.indexOf("[")).trim();
    }
  }

  public String getName() {
    String val = fieldName;
    if (fieldName.contains("%")) {
      val = val.substring(0, val.indexOf("%"));
    }
    return val.trim();
  }

  public String operate(String value) {
    String val = value;
    if (this.fieldName.contains("%")) {
      String op = this.fieldName.substring(this.fieldName.indexOf("%") + 1);
      val = (Integer.parseInt(value) % Integer.parseInt(op)) + "";
    }
    return val;
  }

  public List<TiffNode> getChildren() {
    return node.getChildren(getName(), filter);
  }

  public String getValue() {
    if (!node.hasChild(getName(), filter)) return null;
    String op1value = node.getChild(getName(), filter).getValue();
    String value = operate(op1value);
    return value;
  }
}

package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffNode;

import java.util.List;

/**
 * Created by easy on 14/03/2016.
 */
public class RuleElement {
  String fieldName;
  Filter filter;
  TiffNode node;
  TiffNode model;

  public RuleElement(String field, TiffNode nodeBase, TiffNode model) {
    this.node = nodeBase;
    this.model = model;
    filter = null;
    fieldName = field;
    if (!fieldName.startsWith("$")) {
      while (fieldName.contains(".") || fieldName.contains("[")) {
        int indexDot = fieldName.indexOf(".");
        int indexCla = fieldName.indexOf("[");
        if (indexDot > -1 && (indexCla == -1 || indexDot < indexCla)) {
          String parent = fieldName.substring(0, indexDot);
          if (parent.length() > 0) {
            node = node.getChild(parent);
          }
          fieldName = fieldName.substring(indexDot + 1);
        } else {
          String filter = fieldName.substring(indexCla + 1);
          String remaining = filter.substring(filter.indexOf("]") + 1).trim();
          filter = filter.substring(0, filter.indexOf("]")).trim();
          this.filter = new Filter(filter);
          String currentfield = fieldName.substring(0, indexCla).trim();
          if (remaining.length() == 0) {
            fieldName = currentfield;
          } else {
            node = node.getChild(currentfield, this.filter);
            this.filter = null;
            fieldName = remaining;
          }
        }
      }
    }
  }

  String getName() {
    String val = fieldName;
    if (fieldName.contains("%")) {
      val = val.substring(0, val.indexOf("%"));
    }
    if (fieldName.contains("*")) {
      val = val.substring(0, val.indexOf("*"));
    }
    return val.trim();
  }

  String operate(String value) {
    String val = value;
    if (this.fieldName.contains("%")) {
      String op = this.fieldName.substring(this.fieldName.indexOf("%") + 1);
      val = (Integer.parseInt(value) % Integer.parseInt(op)) + "";
    }
    if (this.fieldName.contains("*")) {
      String op = this.fieldName.substring(this.fieldName.indexOf("*") + 1);
      val = (Integer.parseInt(value) * Integer.parseInt(op)) + "";
    }
    return val;
  }

  public List<TiffNode> getChildren() {
    return node.getChildren(getName(), filter);
  }

  public String getValue() {
    if (fieldName.startsWith("$") && fieldName.endsWith("$")) {
      String val = fieldName.replace("$", "");
      TiffNode node = null;
      String[] parts = val.split("\\.");
      for (String nodeName : parts) {
        if (node == null) {
          if (model.getContext().equals(nodeName))
            node = model;
        } else {
          node = node.getChild(nodeName);
        }
        if (node == null) return null;
      }
      val = node.getValue();
      return val;
    }
    if (fieldName.startsWith("'") && fieldName.endsWith("'")) {
      String val = fieldName.substring(1);
      val = val.substring(0, val.length() - 1);
      return val;
    }
    try {
      int ival = Integer.parseInt(fieldName);
      return ival + "";
    } catch (Exception ex) {

    }
    if (node == null) return null;
    if (!node.hasChild(getName(), filter)) return null;
    String op1value = node.getChild(getName(), filter).getValue();
    String value = operate(op1value);
    if (value.contains("/")) {
      try {
        int inum = Integer.parseInt(value.substring(0, value.indexOf("/")));
        int iden = Integer.parseInt(value.substring(value.indexOf("/") + 1));
        return (inum / (float)iden) + "";
      } catch (Exception ex) {

      }
    }
    if (value.startsWith("[") && value.endsWith("]")) {
      String val = value.substring(1);
      val = val.substring(0, val.length() - 1);
      val = val.split(",")[0];
      value = val;
    }
    return value;
  }
}

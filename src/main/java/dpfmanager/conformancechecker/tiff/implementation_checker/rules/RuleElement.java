package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffNode;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffTag;

import java.util.List;

/**
 * Created by easy on 14/03/2016.
 */
public class RuleElement {
  String fieldName;
  String index;
  Filter filter;
  TiffNode node;
  TiffNode model;
  public boolean valid;
  String multiplier;
  String elevator;

  public RuleElement(String field, TiffNode nodeBase, TiffNode model) {
    valid = true;
    this.node = nodeBase;
    this.model = model;
    filter = null;
    fieldName = field;
    if (fieldName.contains("*")) {
      multiplier = fieldName.substring(0, fieldName.indexOf("*"));
      fieldName = fieldName.substring(fieldName.indexOf("*")+1);
    }
    if (fieldName.contains("^")) {
      elevator = fieldName.substring(0, fieldName.indexOf("^"));
      fieldName = fieldName.substring(fieldName.indexOf("^")+1);
    }
    if (!fieldName.startsWith("$")) {
      while (fieldName.contains(".") || fieldName.contains("[") || fieldName.contains("(")) {
        int indexDot = fieldName.indexOf(".");
        int indexCla = fieldName.indexOf("[");
        int indexPar = fieldName.indexOf("(");
        if (indexDot > -1 && (indexCla == -1 || indexDot < indexCla) && (indexPar == -1 || indexDot < indexPar)) {
          String parent = fieldName.substring(0, indexDot);
          if (parent.length() > 0) {
            if (node != null)
              node = node.getChild(parent);
            else
            {
              valid = false;
              break;
            }
          }
          fieldName = fieldName.substring(indexDot + 1);
        } else if (indexCla > -1 && (indexPar == -1 || indexCla < indexPar)) {
          String filter = fieldName.substring(indexCla + 1);
          String remaining = filter.substring(filter.indexOf("]") + 1).trim();
          filter = filter.substring(0, filter.indexOf("]")).trim();
          this.filter = new Filter(filter);
          String currentfield = fieldName.substring(0, indexCla).trim();
          if (remaining.length() == 0) {
            fieldName = currentfield;
          } else {
            if (node != null)
              node = node.getChild(currentfield, this.filter);
            else
            {
              valid = false;
              break;
            }
            this.filter = null;
            fieldName = remaining;
          }
        } else {
          String filter = fieldName.substring(indexPar + 1);
          String remaining = filter.substring(filter.indexOf(")") + 1).trim();
          index = filter.substring(0, filter.indexOf(")")).trim();
          fieldName = remaining;
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
    if (node == null) return null;
    return node.getChildren(getName(), filter);
  }

  String operateMultiplier(String val) {
    String value = val;
    if (elevator != null) {
      value = (Math.pow(Integer.parseInt(elevator), Integer.parseInt(val))) + "";
    }
    if (multiplier != null) {
      value = (Integer.parseInt(value) * Integer.parseInt(multiplier)) + "";
    }
    return value;
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
      return operateMultiplier(val);
    } else if (fieldName.startsWith("'") && fieldName.endsWith("'")) {
      String val = fieldName.substring(1);
      val = val.substring(0, val.length() - 1);
      return operateMultiplier(val);
    }

    try {
      int ival = Integer.parseInt(fieldName);
      return operateMultiplier(ival + "");
    } catch (Exception ex) {

    }
    if(fieldName.equals("true") || fieldName.equals("false"))
      return fieldName;

    if (node == null) return null;
    if (index != null) {
      TiffTag ttag = ((TiffTag)node);
      String tagvalue = ttag.getValue().replace("[", "").replace("]", "");
      return tagvalue.split(",")[Integer.parseInt(index)];
    }
    if (!node.hasChild(getName(), filter)) return null;
    String op1value = node.getChild(getName(), filter).getValue();
    String value = operate(op1value);
    if (value != null) {
      if (value.contains("/")) {
        try {
          int inum = Integer.parseInt(value.substring(0, value.indexOf("/")));
          int iden = Integer.parseInt(value.substring(value.indexOf("/") + 1));
          return (inum / (float) iden) + "";
        } catch (Exception ex) {

        }
      }
      if (value.startsWith("[") && value.endsWith("]")) {
        String val = value.substring(1);
        val = val.substring(0, val.length() - 1);
        val = val.split(",")[0];
        value = val;
      }
    } else {
      value = "";
    }
    return operateMultiplier(value);
  }
}

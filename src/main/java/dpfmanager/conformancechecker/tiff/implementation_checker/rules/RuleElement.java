/**
 * <h1>RuleElement.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

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
          if (filter.indexOf("]") == -1)
            filter.toString();
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
      value = (Math.pow(Double.parseDouble(elevator), Double.parseDouble(val))) + "";
    }
    if (multiplier != null) {
      value = (Double.parseDouble(value) * Integer.parseInt(multiplier)) + "";
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
          if (inum == 0 && iden == 0) return "0";
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

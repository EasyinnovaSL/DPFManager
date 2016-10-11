/**
 * <h1>Rule.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.conformancechecker.tiff.policy_checker;

/**
 * Created by easy on 06/10/2015.
 */
public class Rule {
  private String tag;
  private String operator;
  private String value;
  private boolean warning;

  /**
   * Instantiates a new Rule.
   */
  public Rule() {
    warning = false;
  }

  /**
   * Gets tag.
   *
   * @return the tag
   */
  public String getTag() {
    return tag;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    try {
      Integer.parseInt(value);
      return "int";
    } catch (Exception e2) {
      try {
        Double.parseDouble(value);
        return "double";
      } catch (Exception e3) {
        return "string";
      }
    }
  }

  /**
   * Gets operator.
   *
   * @return the operator
   */
  public String getOperator() {
    return operator;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }

  public boolean getWarning() { return warning; }

  /**
   * Instantiates a new Rule.
   *
   * @param tag      the tag
   * @param operator the operator
   * @param value    the value
   */
  public Rule (String tag, String operator, String value, boolean warning) {
    this.tag = tag;
    this.operator = operator;
    this.value = value;
    this.warning = warning;
  }

  /**
   * Txt string.
   *
   * @return the string
   */
  public String Txt() {
    String txt = "";
    if (tag != null) txt += tag;
    txt += ",";
    if (operator != null) txt += operator;
    txt += ",";
    if (value != null) txt += value;
    txt += ",";
    txt += warning ? "1" : "0";
    return txt;
  }

  /**
   * Read txt.
   *
   * @param txt the txt
   */
  public void ReadTxt(String txt) {
    if (txt.contains(",")) {
      String[] fields = txt.split(",");
      tag = fields[0];
      operator = fields[1];
      value = fields[2];
      if (fields.length > 3) {
        warning = fields[3].equals("1");
      }
    }
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setWarning(boolean warning) {
    this.warning = warning;
  }
}

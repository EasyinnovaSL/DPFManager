/**
 * <h1>Fix.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Víctor Muñoz Sola
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.metadata_fixer;

/**
 * Created by easy on 09/10/2015.
 */
public class Fix {
  private String tag;
  private String operator;
  private String value;

  /**
   * Instantiates a new Fix.
   */
  public Fix() {

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

  /**
   * Instantiates a new Fix.
   *
   * @param tag      the tag
   * @param operator the operator
   * @param value    the value
   */
  public Fix (String tag, String operator, String value) {
    this.tag = tag;
    this.operator = operator;
    this.value = value;
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
    if (operator != null) {
      txt += operator;
      txt += ",";
    }
    if (value != null) txt += value;
    return txt;
  }

  /**
   * Read txt.
   *
   * @param txt the txt
   */
  public void ReadTxt(String txt) {
    if (txt.contains(",")) {
      tag = txt.substring(0, txt.indexOf(","));
      String v2 = txt.substring(txt.indexOf(",") + 1);
      if (v2.contains(",")) {
        operator = v2.substring(0, v2.indexOf(","));
        value = v2.substring(v2.indexOf(",") + 1);
      } else {
        value = v2;
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
}

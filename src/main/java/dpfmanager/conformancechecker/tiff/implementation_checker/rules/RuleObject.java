/**
 * <h1>RuleObject.java</h1> <p> This program is free software: you can redistribute it
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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 11/03/2016.
 */
public class RuleObject {
  String context;
  String reference;
  String level;
  String id;
  String description;
  String iso;
  AssertObject assertion;
  List<DiagnosticObject> diagnostics = null;

  public void setContext(String context) {
    this.context = context;
  }

  @XmlAttribute
  public String getContext() {
    return context;
  }

  @XmlElement(name = "diagnostic")
  public void setDiagnostics(List<DiagnosticObject> diagnostics) {
    this.diagnostics = diagnostics;
  }

  public List<DiagnosticObject> getDiagnostics() {
    return diagnostics;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getReference() {
    return reference;
  }

  public void setIso(String iso) {
    this.iso = iso;
  }

  public String getIso() {
    return iso;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @XmlAttribute
  public void setLevel(String level) {
    this.level = level;
  }

  public String getLevel() {
    return level;
  }

  @XmlAttribute
  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @XmlElement(name = "assert")
  public void setAssertionField(AssertObject assertion) {
    this.assertion = assertion;
  }

  public AssertObject getAssertionField() {
    return assertion;
  }

  @Override
  public String toString() {
    String s = "";
    s += assertion.toString();
    return s;
  }

  public boolean isCritical() {
    return level != null && level.equals("critical");
  }

  public boolean isWarning() {
    return level != null && level.equals("warning");
  }
}

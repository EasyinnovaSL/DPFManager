/**
 * <h1>RuleResult.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
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
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;

/**
 * Created by easy on 16/03/2016.
 */
public class RuleResult {
  String message;
  String location = null;
  boolean ok;
  TiffNode node;
  RuleType rule;
  boolean warning = false;
  boolean info = false;
  String ruleDescription;
  boolean relaxed = false;

  public RuleResult() {
  }

  public String getRuleDescription() {
    return ruleDescription;
  }

  public void setRuleDescription(String ruleDescription) {
    this.ruleDescription = ruleDescription;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setWarning(boolean warning) {
    this.warning = warning;
  }

  public boolean getInfo() {
    return info;
  }

  public void setInfo(boolean info) {
    this.info = info;
  }

  public boolean getWarning() {
    return warning;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public RuleResult(boolean ok, TiffNode node, RuleType rule) {
    this.message = rule.getAssert().getValue();
    this.node = node;
    this.ok = ok;
    this.rule = rule;
  }

  public RuleResult(String message) {
    this.message = message;
    this.node = null;
    this.ok = false;
    this.rule = null;
  }

  public String getMessage() {
    return message;
  }

  public boolean ok() {
    return ok;
  }

  @Override
  public String toString() {
    return ok ? "OK" : "KO" + ": " + message;
  }

  public void setNode(TiffNode node) {
    this.node = node;
  }

  public TiffNode getNode() {
    return node;
  }

  public String getContext() {
    if (node == null) return "";
    return node.getContext();
  }

  public String getReference() {
    if (rule != null) {
      if (rule.getReferenceText() != null) {
        if (rule.getReferenceText().length() > 0) {
          return rule.getReferenceText();
        }
      }
    }
    return null;
  }

  public String getLocation() {
    if (location != null) {
      return location;
    } else if (node != null) {
      return node.toString();
    }
    return getContext();
  }

  public String getDescription() {
    return message;
  }

  public RuleType getRule() {
    return rule;
  }

  public void setRelaxed(boolean relaxed) {
    this.relaxed = relaxed;
  }

  public boolean isRelaxed() {
    return relaxed;
  }
}

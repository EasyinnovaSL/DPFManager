/**
 * <h1>Schematron.java</h1> <p> This program is free software: you can redistribute it
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

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.AssertType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RulesType;

import com.easyinnova.tiff.model.TiffDocument;
import java.util.ArrayList;

/**
 * Created by easy on 21/09/2015.
 */
public class Schematron {
  /**
   * Instantiates a new Schematron.
   */
  public Schematron()
  {
  }

  public Validator testXMLnoSchematron(TiffDocument tiffModel, Rules rules) throws Exception {
    String xmlTiffModel = TiffConformanceChecker.getValidationXmlString(tiffModel);

    ImplementationCheckerObjectType rulesObj = new ImplementationCheckerObjectType();
    RulesType rulesType = new RulesType();
    int index = 1;
    for (Rule rule : rules.getRules()) {
      RuleType ruleObj = new RuleType();
      if (rule.getWarning()) ruleObj.setLevel("warning");
      else ruleObj.setLevel("error");
      ruleObj.setId("pol-" + index++);
      ruleObj.setContext("ifd[class=image]");
      String tag = rule.getTag();
      boolean stringValue = false;
      if (tag.equals("ByteOrder")) {
        tag = "byteOrder";
        ruleObj.setContext("tiffValidationObject");
        stringValue = true;
      }
      if (tag.equals("DPI")) {
        tag = "dpi";
        stringValue = true;
      }
      if (tag.equals("EqualXYResolution")) {
        tag = "equalXYResolution";
        stringValue = true;
      }
      if (tag.equals("NumberImages")) {
        ruleObj.setContext("tiffValidationObject");
        tag = "numberImages";
      }
      AssertType assertObj = new AssertType();
      String operator = rule.getOperator();
      if (operator.equals("=")) operator = "==";
      if (rule.getValue().contains(",")) stringValue = true;
      String sTest = "";
      ArrayList<String> values = new ArrayList<>();
      for (String value : rule.getValue().split(";")) {
        String value2 = value;
        if (tag.equals("Compression")) value2 = TiffConformanceChecker.compressionCode(value) + "";
        if (tag.equals("Photometric")) {
          value2 = TiffConformanceChecker.photometricCode(value) + "";
          if (value2.equals("1")) values.add("0");
        }
        if (tag.equals("Planar")) {
          value2 = TiffConformanceChecker.planarCode(value) + "";
        }
        values.add(value2);
      }
      for (String value : values) {
        if (tag.equals("Photometric")) tag = "PhotometricInterpretation";
        if (tag.equals("Planar")) tag = "PlanarConfiguration";
        if (sTest.length() > 0) sTest += " || ";
        if (tag.equals("byteOrder")) sTest += "{" + tag + " " + operator + " ";
        else if (tag.equals("numberImages")) sTest += "{" + tag + " " + operator + " ";
        else if (tag.equals("dpi")) sTest += "{" + tag + " " + operator + " ";
        else if (tag.equals("equalXYResolution")) sTest += "{" + tag + " " + operator + " ";
        else sTest += "{tags.tag[name=" + tag + "] " + operator + " ";
        if (stringValue) sTest += "'";
        sTest += value;
        if (stringValue) sTest += "'";
        sTest += "}";
      }
      assertObj.setTest(sTest);
      RuleType.Description desc = new RuleType.Description();
      String op = rule.getOperator();
      if (op.equals("<")) op = "&lt;";
      else if (op.equals(">")) op = "&gt;";
      desc.setValue(rule.getTag() + " " + op + " " + rule.getValue());
      ruleObj.setDescription(desc);
      if (!rule.getWarning()) assertObj.setValue("Invalid " + rule.getTag());
      else assertObj.setValue("Warning on " + rule.getTag());
      ruleObj.setAssert(assertObj);
      rulesType.getRule().add(ruleObj);
    }
    rulesObj.getRules().add(rulesType);

    TiffValidationObject model = Validator.createValidationObjectFromXml(xmlTiffModel);

    Validator validation = new Validator();
    validation.validate(model, rulesObj, false);

    return validation;
  }
}

/**
 * <h1>Validator.java</h1> <p> This program is free software: you can redistribute it and/or modify
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

package dpfmanager.conformancechecker.tiff.implementation_checker;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffNode;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.Clausule;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.Clausules;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleElement;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.AssertType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.IncludeType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RulesType;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by easy on 11/03/2016.
 */
public class Validator {
  /* The Dpf logger */
  private DpfLogger Logger;

  private TiffValidationObject model;
  private ValidationResult result;

  static HashMap<String, ImplementationCheckerObjectType> preLoadedValidatorsSingleton = new HashMap<>();

  public List<RuleResult> getErrors() {
    return result.getErrors();
  }

  public List<RuleResult> getWarningsAndInfos() {
    return result.getWarnings(true);
  }

  public List<RuleResult> getWarnings() {
    return result.getWarnings(false);
  }

  public Validator() {
    Logger = null;
  }

  public Validator(DpfLogger log) {
    Logger = log;
  }

  /**
   * Main validate function
   */
  public ValidationResult validate(String content, String rulesFile, boolean fastBreak) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    try {
      TiffValidationObject obj = createValidationObjectFromXml(content);
      ImplementationCheckerObjectType rules = ImplementationCheckerLoader.getRules(rulesFile);
      validate(obj, rules, fastBreak);
    } catch (Exception ex) {
      RuleResult rr = new RuleResult("Fatal error in TIFF file");
      result = new ValidationResult();
      result.add(rr);
    }
    return result;
  }

  public static TiffValidationObject createValidationObjectFromXml(String content) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    StringReader reader = new StringReader(content);
    TiffValidationObject obj = (TiffValidationObject) jaxbUnmarshaller.unmarshal(reader);
    return obj;
  }

  public void validate(TiffValidationObject model, ImplementationCheckerObjectType rules, boolean fastBreak) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    result = new ValidationResult();
    this.model = model;

    boolean bbreak = false;
    List<RuleType> ordRules = new ArrayList<RuleType>();
    for (RulesType ruleSet : rules.getRules()) {
      for (RuleType rule : ruleSet.getRule()) {
        if (!rule.getExperimental()) {
          if (rule.isCritical())
            ordRules.add(rule);
        }
      }
    }
    for (RulesType ruleSet : rules.getRules()) {
      for (RuleType rule : ruleSet.getRule()) {
        if (!rule.getExperimental()) {
          if (!rule.isCritical())
            ordRules.add(rule);
        }
      }
    }

    for (RuleType rule : ordRules) {
      if (rule.getId().equals("IFD-0012"))
        rule.toString();

      String context = rule.getContext();
      List<TiffNode> objects = model.getObjectsFromContext(context, true);
      for (TiffNode node : objects) {
        boolean ok = checkRule(rule, node);
        if (!ok && (rule.isWarning() || rule.isInfo())) ok = true;
        if (!ok)
          ok = ok;
        if (!ok && (rule.isCritical() || fastBreak)) {
          bbreak = true;
          break;
        }
      }
      if (bbreak) {
        break;
      }
    }
  }


  boolean checkRule(RuleType rule, TiffNode node) {
    boolean ok = true;

    try {
      AssertType test = rule.getAssert();
      String expression = test.getTest();

      // Get clausules
      Clausules clausules = new Clausules();
      if (!clausules.parse(expression))
        ConformanceChecker.Logger.println("Error on rule " + rule.toString());

      // Analyze clausules
      for (int ic = 0; ic < clausules.getClausules().size(); ic++) {
        Clausule clausule = clausules.getClausules().get(ic);
        if (clausule.value.startsWith("count(")) {
          // Child count
          String countField = clausule.value.substring(clausule.value.indexOf("(") + 1);
          countField = countField.substring(0, countField.indexOf(")"));
          String checkop = clausule.value.substring(clausule.value.indexOf(")") + 1).trim();

          RuleElement field = new RuleElement(countField, node, model);
          if (!field.valid) {
            //ok = false;
          } else {
            List<TiffNode> childs = field.getChildren();
            int n = childs.size();
            if (checkop.startsWith("==")) {
              String field2 = checkop.substring("==".length()).trim();
              ok = n == Integer.parseInt(field2);
            } else if (checkop.startsWith("!=")) {
              String field2 = checkop.substring("!=".length()).trim();
              ok = n != Integer.parseInt(field2);
            } else if (checkop.startsWith(">")) {
              String field2 = checkop.substring(">".length()).trim();
              ok = n > Integer.parseInt(field2);
            } else if (checkop.startsWith("<")) {
              String field2 = checkop.substring("<".length()).trim();
              ok = n < Integer.parseInt(field2);
            }
          }
        } else if (clausule.value.startsWith("date(")) {
          // Check datetime format
          String dateTimeField = clausule.value.substring(clausule.value.indexOf("(") + 1);
          dateTimeField = dateTimeField.substring(0, dateTimeField.indexOf(")"));

          RuleElement field = new RuleElement(dateTimeField, node, model);
          if (!field.valid) {
            //ok = false;
          } else {
            List<TiffNode> childs = field.getChildren();
            for (TiffNode nod : childs) {
              ok = false;
              String value = nod.getValue();
              if (value.length() == 19) {
                if (value.split(" ").length == 2 && value.split(":").length == 5) {
                  String sdate = value.split(" ")[0];
                  String stime = value.split(" ")[1];
                  try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
                    sdf.setLenient(false);
                    java.util.Date date = sdf.parse(sdate);
                    sdf = new SimpleDateFormat("HH:mm:ss");
                    sdf.setLenient(false);
                    java.util.Date time = sdf.parse(stime);
                    ok = true;
                  } catch (Exception ex) {

                  }
                }
              }
            }
          }
        } else if (clausule.value.contains("==") || clausule.value.contains(">") || clausule.value.contains("<") || clausule.value.contains("!=")) {
          // Check field values
          String operation = clausule.value.contains("==") ? "==" : (clausule.value.contains(">") ? ">" : clausule.value.contains("!=") ? "!=" : "<");
          RuleElement op1 = new RuleElement(clausule.value.substring(0, clausule.value.indexOf(operation)), node, model);
          if (!op1.valid) {
            //ok = false;
          } else {
            String value = op1.getValue();
            if (value == null) return ok;
            RuleElement op2 = new RuleElement(clausule.value.substring(clausule.value.indexOf(operation) + operation.length()).trim(), node, model);
            if (!op2.valid) {
              ok = false;
            } else {
              String value2 = op2.getValue();
              if (value2 == null)
                op2.getValue();
              if (value2 == null)
                value2 = "";
              if (value.contains("/"))
                value = Double.parseDouble(value.split("/")[0]) / Double.parseDouble(value.split("/")[1]) + "";
              if (value2.contains("/"))
                value2 = Double.parseDouble(value2.split("/")[0]) / Double.parseDouble(value2.split("/")[1]) + "";
              if (operation.equals("==")) ok = value.equals(value2);
              else if (operation.equals("!="))
                ok = !value.equals(value2);
              else if (operation.equals(">")) {
                try {
                  ok = Double.parseDouble(value) > Double.parseDouble(value2);
                } catch (Exception ex) {
                  ok = false;
                }
              } else {
                try {
                  ok = Double.parseDouble(value) < Double.parseDouble(value2);
                } catch (Exception ex) {
                  ok = false;
                }

                if (!ok)
                  ok = false;
              }
            }
          }
        } else {
          if (clausule.value.startsWith("!")) {
            // Check field does not exist
            RuleElement elem = new RuleElement(clausule.value.substring(1), node, model);
            if (!elem.valid) {
              ok = true;
            } else {
              ok = elem.getChildren().size() == 0;
            }
          } else {
            // Check field exists
            RuleElement elem = new RuleElement(clausule.value, node, model);
            if (!elem.valid) {
              ok = false;
            } else {
              List<TiffNode> childs = elem.getChildren();
              if (childs == null) ok = false;
              else ok = childs.size() > 0;
            }
          }
        }

        // Lazy evaluation
        Clausules.Operator nextClausuleOperator = null;
        if (ic + 1 < clausules.getClausules().size())
          nextClausuleOperator = clausules.getClausules().get(ic + 1).operator;
        if (nextClausuleOperator != null) {
          if (nextClausuleOperator == Clausules.Operator.OR && ok)
            break;
          if (nextClausuleOperator == Clausules.Operator.AND && !ok)
            break;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      ok = false;
    }

    if (ok && rule.isWarning()) {
      RuleResult rr = new RuleResult(false, node, rule);
      rr.setWarning(true);
      result.add(rr);
    } else if (ok && rule.isInfo()) {
      RuleResult rr = new RuleResult(false, node, rule);
      rr.setInfo(true);
      result.add(rr);
    } else if (ok) {
      RuleResult rr = new RuleResult(true, node, rule);
      result.add(rr);
    } else if (!rule.isWarning() && !rule.isInfo()) {
      RuleResult rr = new RuleResult(false, node, rule);
      result.add(rr);
    }

    return ok;
  }

  public ValidationResult getResult() {
    return result;
  }

  private void printOut(String line) {
    if (Logger != null) {
      Logger.println(line);
    }
  }
}

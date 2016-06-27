package dpfmanager.conformancechecker.tiff.implementation_checker;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffNode;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.AssertObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.Clausules;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.ImplementationCheckerObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.IncludeObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleElement;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RulesObject;
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

  static HashMap<String, ImplementationCheckerObject> preLoadedValidatorsSingleton = new HashMap<>();

  public List<RuleResult> getErrors() {
    return result.getErrors();
  }

  public List<RuleResult> getWarnings() {
    return result.getWarnings();
  }

  public Validator() {
    Logger = null;
  }

  public Validator(DpfLogger log) {
    Logger = log;
  }

  /**
   * Read filefrom resources string.
   *
   * @param pathStr the path str
   * @return the string
   */
  public InputStream getFileFromResources(String pathStr) {
    InputStream fis = null;
    Path path = Paths.get(pathStr);
    try {
      if (Files.exists(path)) {
        // Look in current dir
        fis = new FileInputStream(pathStr);
      } else {
        // Look in JAR
        Class cls = ReportGenerator.class;
        ClassLoader cLoader = cls.getClassLoader();
        fis = cLoader.getResourceAsStream(pathStr);
      }
    } catch (FileNotFoundException e) {
      printOut("File " + pathStr + " not found in dir.");
    }

    return fis;
  }

  synchronized ImplementationCheckerObject getRules(String rulesFile) throws JAXBException {
    ImplementationCheckerObject rules = null;

    if (!preLoadedValidatorsSingleton.containsKey(rulesFile)) {
      JAXBContext jaxbContext = JAXBContext.newInstance(ImplementationCheckerObject.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      rules = (ImplementationCheckerObject) jaxbUnmarshaller.unmarshal(getFileFromResources(rulesFile));

      if (rules.getIncludes() != null) {
        for (IncludeObject inc : rules.getIncludes()) {
          JAXBContext jaxbContextInc = JAXBContext.newInstance(ImplementationCheckerObject.class);
          Unmarshaller jaxbUnmarshallerInc = jaxbContextInc.createUnmarshaller();
          ImplementationCheckerObject rulesIncluded = (ImplementationCheckerObject) jaxbUnmarshallerInc.unmarshal(getFileFromResources("implementationcheckers/" + inc.getValue()));

          if (inc.getSubsection() == null || inc.getSubsection().length() == 0) {
            rules.getRules().addAll(0, rulesIncluded.getRules());
          } else {
            for (RulesObject ro : rulesIncluded.getRules()) {
              if (ro.getDescription().equals(inc.getSubsection())) {
                rules.getRules().add(ro);
              }
            }
          }
        }
      }

      preLoadedValidatorsSingleton.put(rulesFile, rules);
    } else {
      rules = preLoadedValidatorsSingleton.get(rulesFile);
    }
    return rules;
  }

  void validate(TiffValidationObject model, String rulesFile) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    result = new ValidationResult();

    ImplementationCheckerObject rules = getRules(rulesFile);

    boolean bbreak = false;
    for (RulesObject ruleSet : rules.getRules()) {
      for (RuleObject rule : ruleSet.getRules()) {
        String context = rule.getContext();
        List<TiffNode> objects = model.getObjectsFromContext(context, true);
        for (TiffNode node : objects) {
          boolean ok = checkRule(rule, node);
          if (!ok && rule.isCritical()) {
            bbreak = true;
            break;
          }
        }
        if (bbreak) {
          break;
        }
      }
      if (bbreak) {
        break;
      }
    }
  }

  void validate(String content, String rulesFile) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    StringReader reader = new StringReader(content);
    model = (TiffValidationObject) jaxbUnmarshaller.unmarshal(reader);

    validate(model, rulesFile);
  }

  public void validateBaseline(TiffValidationObject model) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    this.model = model;
    validate(model, "implementationcheckers/BaselineProfileChecker.xml");
  }

  public void validateTiffEP(TiffValidationObject model) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    this.model = model;
    validate(model, "implementationcheckers/TiffEPProfileChecker.xml");
  }

  public void validateTiffIT(TiffValidationObject model) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    this.model = model;
    validate(model, "implementationcheckers/TiffITProfileChecker.xml");
  }

  public void validateTiffITP1(TiffValidationObject model) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    this.model = model;
    validate(model, "implementationcheckers/TiffITP1ProfileChecker.xml");
  }

  public void validateTiffITP2(TiffValidationObject model) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    this.model = model;
    validate(model, "implementationcheckers/TiffITP2ProfileChecker.xml");
  }

  public void validateBaseline(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "implementationcheckers/BaselineProfileChecker.xml");
  }

  public void validateTiffEP(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "implementationcheckers/TiffEPProfileChecker.xml");
  }

  public void validateTiffIT(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "implementationcheckers/TiffITProfileChecker.xml");
  }

  public void validateTiffITP1(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "implementationcheckers/TiffITP1ProfileChecker.xml");
  }

  public void validateTiffITP2(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "implementationcheckers/TiffITP2ProfileChecker.xml");
  }

  boolean checkRule(RuleObject rule, TiffNode node) {
    boolean ok = true;

    try {
      AssertObject test = rule.getAssertionField();
      String expression = test.getTest();

      // Get clausules
      Clausules clausules = new Clausules();
      if (!clausules.parse(expression))
        ConformanceChecker.Logger.println("Error on rule " + rule.toString());

      // Analyze clausules
      for (String clausule : clausules.getClausules()) {
        if (clausule.startsWith("count(")) {
          // Child count
          String countField = clausule.substring(clausule.indexOf("(") + 1);
          countField = countField.substring(0, countField.indexOf(")"));
          String checkop = clausule.substring(clausule.indexOf(")") + 1).trim();

          RuleElement field = new RuleElement(countField, node, model);
          if (!field.valid) ok = false;
          else {
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
        } else if (clausule.contains("==") || clausule.contains(">") || clausule.contains("<") || clausule.contains("!=")) {
          // Check field values
          String operation = clausule.contains("==") ? "==" : (clausule.contains(">") ? ">" : clausule.contains("!=") ? "!=" : "<");
          RuleElement op1 = new RuleElement(clausule.substring(0, clausule.indexOf(operation)), node, model);
          if (!op1.valid) ok = false;
          else {
            String value = op1.getValue();
            if (value == null) return ok;
            RuleElement op2 = new RuleElement(clausule.substring(clausule.indexOf(operation) + operation.length()).trim(), node, model);
            if (!op2.valid) ok = false;
            else {
              String value2 = op2.getValue();
              if (value.contains("/"))
                value = Double.parseDouble(value.split("/")[0]) / Double.parseDouble(value.split("/")[1]) + "";
              if (value2.contains("/"))
                value2 = Double.parseDouble(value2.split("/")[0]) / Double.parseDouble(value2.split("/")[1]) + "";
              if (operation.equals("==")) ok = value.equals(value2);
              else if (operation.equals("!="))
                ok = !value.equals(value2);
              else if (operation.equals(">"))
                ok = Double.parseDouble(value) > Double.parseDouble(value2);
              else {
                ok = Double.parseDouble(value) < Double.parseDouble(value2);

                if (!ok)
                  ok = false;
              }
            }
          }
        } else {
          if (clausule.startsWith("!")) {
            // Check field does not exist
            RuleElement elem = new RuleElement(clausule.substring(1), node, model);
            if (!elem.valid) ok = false;
            else {
              ok = elem.getChildren().size() == 0;
            }
          } else {
            // Check field exists
            RuleElement elem = new RuleElement(clausule, node, model);
            if (!elem.valid) ok = false;
            else {
              List<TiffNode> childs = elem.getChildren();
              if (childs == null) ok = false;
              else ok = childs.size() > 0;
            }
          }
        }

        // Lazy evaluation
        if (clausules.getOperator() != Clausules.Operator.NULL && clausules.getOperator() == Clausules.Operator.OR && ok)
          break;
        if (clausules.getOperator() == Clausules.Operator.NULL || clausules.getOperator() == Clausules.Operator.AND && !ok)
          break;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      ok = false;
    }

    if (ok) {
      RuleResult rr = new RuleResult(true, node, rule, rule.toString() + " on node " + node.toString());
      result.add(rr);
    } else if (rule.isWarning()) {
      RuleResult rr = new RuleResult(false, node, rule, rule.toString() + " on node " + node.toString());
      rr.setWarning(true);
      result.add(rr);
    } else {
      RuleResult rr = new RuleResult(false, node, rule, rule.toString() + " on node " + node.toString());
      result.add(rr);
    }

    return ok;
  }

  private void printOut(String line) {
    if (Logger != null) {
      Logger.println(line);
    }
  }
}

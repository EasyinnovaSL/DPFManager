package dpfmanager.conformancechecker.tiff.implementation_checker;

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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.*;

import org.xml.sax.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by easy on 11/03/2016.
 */
public class Validator {
  TiffValidationObject model;
  ValidationResult result;
  private DpfLogger logger;

  public Validator(DpfLogger dpfLogger){
    logger = dpfLogger;
  }

  public List<RuleResult> getErrors() {
    return result.getErrors();
  }

  public List<RuleResult> getWarnings() {
    return result.getWarnings();
  }

  /**
   * Read filefrom resources string.
   *
   * @param pathStr the path str
   * @return the string
   */
  public static InputStream getFileFromResources(String pathStr) {
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
      System.err.println("File " + pathStr + " not found in dir.");
    }

    return fis;
  }

  void validate(String path, String rulesFile) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    result = new ValidationResult();

    JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    model = (TiffValidationObject) jaxbUnmarshaller.unmarshal(new File(path));

    jaxbContext = JAXBContext.newInstance(ImplementationCheckerObject.class);
    jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    ImplementationCheckerObject rules = (ImplementationCheckerObject) jaxbUnmarshaller.unmarshal(getFileFromResources(rulesFile));

    if (rules.getIncludes() != null) {
      for (IncludeObject inc : rules.getIncludes()) {
        jaxbContext = JAXBContext.newInstance(ImplementationCheckerObject.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ImplementationCheckerObject rulesIncluded = (ImplementationCheckerObject) jaxbUnmarshaller.unmarshal(getFileFromResources(inc.getValue()));
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

    boolean bbreak = false;
    for (RulesObject ruleSet : rules.getRules()) {
      for (RuleObject rule : ruleSet.getRules()) {
        String context = rule.getContext();
        List<TiffNode> objects = model.getObjectsFromContext(context, true);
        for (TiffNode node : objects) {
          boolean ok = checkRule(rule, node);
          if (!ok && rule.getCritical() == 1) {
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

  public void validateBaseline(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "BaselineProfileChecker.xml");
  }

  public void validateTiffEP(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "TiffEPProfileChecker.xml");
  }

  public void validateTiffIT(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "TiffITProfileChecker.xml");
  }

  public void validateTiffITP1(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "TiffITP1ProfileChecker.xml");
  }

  public void validateTiffITP2(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    validate(path, "TiffITP2ProfileChecker.xml");
  }

  boolean checkRule(RuleObject rule, TiffNode node) {
    boolean ok = true;

    try {
      AssertObject test = rule.getAssertionField();
      String expression = test.getTest();

      // Get clausules
      Clausules clausules = new Clausules();
      if (!clausules.parse(expression)) logger.println("Error on rule " + rule.toString());

      // Analyze clausules
      for (String clausule : clausules.getClausules()) {
        if (clausule.startsWith("count(")) {
          // Child count
          String countField = clausule.substring(clausule.indexOf("(") + 1);
          countField = countField.substring(0, countField.indexOf(")"));
          String checkop = clausule.substring(clausule.indexOf(")") + 1).trim();

          RuleElement field = new RuleElement(countField, node, model);
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
        } else if (clausule.contains("==") || clausule.contains(">") || clausule.contains("<") || clausule.contains("!=")) {
          // Check field values
          String operation = clausule.contains("==") ? "==" : (clausule.contains(">") ? ">" : clausule.contains("!=") ? "!=" : "<");
          RuleElement op1 = new RuleElement(clausule.substring(0, clausule.indexOf(operation)), node, model);
          String value = op1.getValue();
          if (value == null) return ok;
          RuleElement op2 = new RuleElement(clausule.substring(clausule.indexOf(operation) + operation.length()).trim(), node, model);
          String value2 = op2.getValue();
          if (operation.equals("==")) ok = value.equals(value2);
          else if (operation.equals("!=")) ok = Float.parseFloat(value) != Float.parseFloat(value2);
          else if (operation.equals(">")) ok = Float.parseFloat(value) > Float.parseFloat(value2);
          else ok = Float.parseFloat(value) < Float.parseFloat(value2);
          if (!ok)
            ok = false;
        } else {
          if (clausule.startsWith("!")) {
            // Check field does not exist
            RuleElement elem = new RuleElement(clausule.substring(1), node, model);
            ok = elem.getChildren().size() == 0;
          } else {
            // Check field exists
            RuleElement elem = new RuleElement(clausule, node, model);
            ok = elem.getChildren().size() > 0;
          }
        }

        // Lazy evaluation
        if (clausules.getOperator() != null && clausules.getOperator().equals("||") && ok)
          break;
        if (clausules.getOperator() == null || clausules.getOperator().equals("&&") && !ok)
          break;
      }
    } catch (Exception ex) {
      ok = false;
    }

    if (ok) {
      result.add(new RuleResult(true, node, rule, rule.toString() + " on node " + node.toString()));
    } else {
      result.add(new RuleResult(false, node, rule, rule.toString() + " on node " + node.toString()));
    }

    return ok;
  }
}

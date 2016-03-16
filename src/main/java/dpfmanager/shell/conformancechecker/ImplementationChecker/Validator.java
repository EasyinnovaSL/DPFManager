package dpfmanager.shell.conformancechecker.ImplementationChecker;

import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffNode;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffValidationObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.AssertObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.Clausules;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.ImplementationCheckerObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RuleElement;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RuleObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RuleResult;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RulesObject;
import dpfmanager.shell.reporting.ReportGenerator;

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
  List<RuleResult> result;

  public List<RuleResult> getResult() {
    return result;
  }

  public List<RuleResult> getErrors() {
    List<RuleResult> errors = new ArrayList<>();
    for (RuleResult res : result) {
      if (!res.ok()) {
        errors.add(res);
      }
    }
    return errors;
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

  public void validate(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    result = new ArrayList<>();

    JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    model = (TiffValidationObject) jaxbUnmarshaller.unmarshal(new File(path));

    jaxbContext = JAXBContext.newInstance(ImplementationCheckerObject.class);
    jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    ImplementationCheckerObject rules = (ImplementationCheckerObject) jaxbUnmarshaller.unmarshal(getFileFromResources("BaselineProfileChecker.xml"));

    for (RulesObject ruleSet : rules.getRules()) {
      for (RuleObject rule : ruleSet.getRules()) {
        String context = rule.getContext();
        List<TiffNode> objects = model.getObjectsFromContext(context, true);
        for (TiffNode node : objects) {
          checkRule(rule, node);
        }
      }
    }
  }

  void checkRule(RuleObject rule, TiffNode node) {
    boolean ok = false;

    try {
      AssertObject test = rule.getAssertionField();
      String expression = test.getTest();

      // Get clausules
      Clausules clausules = new Clausules();
      if (!clausules.parse(expression)) System.out.println("Error on rule " + rule.toString());

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
          } else if (checkop.startsWith(">")) {
            String field2 = checkop.substring(">".length()).trim();
            ok = n > Integer.parseInt(field2);
          } else if (checkop.startsWith("<")) {
            String field2 = checkop.substring("<".length()).trim();
            ok = n < Integer.parseInt(field2);
          }
        } else if (clausule.contains("==") || clausule.contains(">") || clausule.contains("<")) {
          // Check field values
          String operation = clausule.contains("==") ? "==" : (clausule.contains(">") ? ">" : "<");
          RuleElement op1 = new RuleElement(clausule.substring(0, clausule.indexOf(operation)), node, model);
          String value = op1.getValue();
          if (value == null) return;
          RuleElement op2 = new RuleElement(clausule.substring(clausule.indexOf(operation) + operation.length()).trim(), node, model);
          String value2 = op2.getValue();
          if (operation.equals("==")) ok = value.equals(value2);
          else if (operation.equals(">")) ok = Float.parseFloat(value) > Float.parseFloat(value2);
          else ok = Float.parseFloat(value) < Float.parseFloat(value2);
        } else {
          // Check field exists
          RuleElement elem = new RuleElement(clausule, node, model);
          ok = elem.getChildren().size() > 0;
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
      result.add(new RuleResult(true, "Rule OK " + rule.toString() + " on node " + node.toString()));
    } else {
      result.add(new RuleResult(false, "Rule KO " + rule.toString() + " on node " + node.toString()));
    }
  }
}

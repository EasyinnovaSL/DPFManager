package dpfmanager.shell.conformancechecker.ImplementationChecker;

import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffNode;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffValidationObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.AssertObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.Clausules;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.ImplementationCheckerObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RuleElement;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RuleObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RulesObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.*;
import org.xml.sax.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by easy on 11/03/2016.
 */
public class Validator {
  TiffValidationObject model;

  public void validate(String path) throws JAXBException, ParserConfigurationException, IOException, SAXException {
    JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    model = (TiffValidationObject) jaxbUnmarshaller.unmarshal(new File(path));

    jaxbContext = JAXBContext.newInstance(ImplementationCheckerObject.class);
    jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    ImplementationCheckerObject rules = (ImplementationCheckerObject) jaxbUnmarshaller.unmarshal(new File("BaselineProfileChecker.xml"));

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

          RuleElement field = new RuleElement(countField, node);
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
          RuleElement op1 = new RuleElement(clausule.substring(0, clausule.indexOf(operation)), node);
          String value = op1.getValue();
          if (value == null) return;
          String op2 = getValue(clausule.substring(clausule.indexOf(operation) + operation.length()).trim());
          if (operation.equals("==")) ok = value.equals(op2);
          else if (operation.equals(">")) ok = Integer.parseInt(value) > Integer.parseInt(op2);
          else ok = Integer.parseInt(value) < Integer.parseInt(op2);
        } else {
          // Check field exists
          RuleElement elem = new RuleElement(clausule, node);
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
      System.out.println("Rule OK " + rule.toString() + " on node " + node.toString());
    } else {
      System.out.println("Rule KO " + rule.toString() + " on node " + node.toString());
    }
  }

  String getValue(String field) {
    String val = field;
    if (val.contains("$")) {
      val = val.replace("$", "");
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
    }
    return val;
  }
}

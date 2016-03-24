package dpfmanager.conformancechecker.tiff.ImplementationChecker.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by easy on 14/03/2016.
 */
public class Clausules {
  List<String> clausules;
  String operator;

  public Clausules() {

  }

  public Clausules(List<String> clausules, String operator)
  {
    this.clausules = clausules;
    this.operator = operator;
  }

  public String getOperator() {
    return operator;
  }

  public List<String> getClausules() {
    return clausules;
  }

  public boolean parse(String expression) {
    clausules = new ArrayList<String>();
    operator = null;
    String expr = expression;
    while (expr.startsWith("{")) {
      clausules.add(expr.substring(1, expr.indexOf("}")));
      expr = expr.substring(expr.indexOf("}") + 1).trim();
      if (expr.length() == 0) break;
      String op = expr.substring(0, 2);
      if (operator == null) operator = op;
      else if (!op.equals(operator)) {
        return false;
      }
      expr = expr.substring(2).trim();
    }
    return true;
  }
}

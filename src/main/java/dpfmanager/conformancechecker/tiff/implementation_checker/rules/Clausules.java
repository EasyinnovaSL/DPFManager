package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by easy on 14/03/2016.
 */
public class Clausules {
  public enum Operator { AND, OR, NULL };
  List<String> clausules;
  Operator operator = Operator.NULL;

  public Clausules() {

  }

  public Clausules(List<String> clausules, String operator)
  {
    this.clausules = clausules;
    if (operator.equals("||")) this.operator = Operator.OR;
    else if (operator.equals("&&")) this.operator = Operator.AND;
  }

  public Operator getOperator() {
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
      if (operator == null) {
        if (op.equals("&&")) operator = Operator.AND;
        else if (op.equals("||")) operator = Operator.OR;
      }
      else {
        Operator sop = Operator.NULL;
        if (op.equals("&&")) sop = Operator.AND;
        else if (op.equals("||")) sop = Operator.OR;
        if (sop != operator) {
          return false;
        }
      }
      expr = expr.substring(2).trim();
    }
    return true;
  }
}

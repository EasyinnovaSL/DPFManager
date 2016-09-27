/**
 * <h1>Clausules.java</h1> <p> This program is free software: you can redistribute it
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

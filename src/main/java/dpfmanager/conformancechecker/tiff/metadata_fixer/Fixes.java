/**
 * <h1>Fixes.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.conformancechecker.tiff.metadata_fixer;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by easy on 09/10/2015.
 */
public class Fixes {
  private ArrayList<Fix> fixes;

  /**
   * Instantiates a new Fixes.
   */
  public Fixes() {
    fixes = new ArrayList<Fix>();
  }

  /**
   * Gets fixes.
   *
   * @return the fixes
   */
  public ArrayList<Fix> getFixes() {
    return fixes;
  }

  public void set(ArrayList<Fix> fixes) {
    this.fixes = fixes;
  }

  public void ReadAutofixes(VBox autoFixesBox) {
    for (Node node : autoFixesBox.getChildren()) {
      CheckBox check = (CheckBox) node;
      if (check.isSelected()) {
        addFixFromTxt(check.getId() + ",Yes");
      }
    }
  }

  /**
   * Write.
   *
   * @param pw the pw
   */
  public void Write(PrintWriter pw) {
    for (Fix fix : fixes) {
      pw.println("FIX\t" + fix.Txt());
    }
  }

  /**
   * Add fix from txt.
   *
   * @param txt the txt
   */
  public void addFixFromTxt(String txt) {
    Fix fix = new Fix();
    fix.ReadTxt(txt);
    fixes.add(fix);
  }

  /**
   * Add fix from parameters.
   *
   * @param tag the tag
   * @param operator the operator
   * @param value the value
   */
  public void addFix(String tag, String operator, String value) {
    Fix fix = new Fix();
    fix.setTag(tag);
    fix.setOperator(operator);
    fix.setValue(value);
    fixes.add(fix);
  }
}

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
}

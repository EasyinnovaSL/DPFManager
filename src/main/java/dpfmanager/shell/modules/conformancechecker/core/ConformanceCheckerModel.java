/**
 * <h1>ConformanceCheckerModel.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.interfaces.gui.component.dessign.DessignController;
import dpfmanager.shell.interfaces.gui.component.dessign.DessignView;

import com.easyinnova.policy_checker.model.Field;

import java.util.ArrayList;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class ConformanceCheckerModel extends DpfModel<DessignView, DessignController> {
  private String txtFile;

  // User Interface
  private ArrayList<Field> fields;

  // Check files configuration
  private Configuration config;
  private TiffConformanceChecker conformance;

  public ConformanceCheckerModel() {
    // init vars
    txtFile = "";
    LoadConformanceChecker();
  }

  public void LoadConformanceChecker() {
    conformance = new TiffConformanceChecker(null, null);
    fields = conformance.getConformanceCheckerFields();
  }

  /**
   * Gets fields.
   *
   * @return the fields
   */
  public ArrayList<Field> getFields() {
    return fields;
  }

  public void setConfig(Configuration c) {
    config = c;
  }

  public Configuration getConfig() {
    return config;
  }
}

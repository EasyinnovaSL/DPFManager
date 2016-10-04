/**
 * <h1>ConfigModel.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.config;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.modules.messages.messages.AlertMessage;

import java.util.ArrayList;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ConfigModel extends DpfModel<ConfigView, ConfigController> {
  private Configuration config;
  private boolean editingConfig;
  private int step;
  private String path;

  //User Interface
  private int uniqueId;

  private TiffConformanceChecker conformance;

  public ConfigModel() {
    // init vars
    conformance = new TiffConformanceChecker();
    config = new Configuration();
    editingConfig = false;
    step = 0;
    uniqueId = 0;
  }

  public void initNewConfig(){
    // Trigger when "New" button clicked
    step = 0;
    editingConfig = false;
    config = new Configuration();
    config.initDefault();
    path = null;
  }

  public void initEditConfig(String path){
    // Trigger when "Edit" button clicked
    step = 0;
    try {
      this.path = path;
      editingConfig = true;
      config = new Configuration();
      config.ReadFile(path);
    } catch (Exception e) {
      this.path = null;
      editingConfig = false;
      config = new Configuration();
      config.initDefault();
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("readConfigFail"), getBundle().getString("startingNew")));
    }
  }

  public void saveConfig(String filename) throws Exception {
    config.SaveFile(filename);
  }

  public boolean isEditing() {
    return editingConfig;
  }

  public Configuration getConfiguration() {
    return config;
  }

  public void setConfigStep(int x) {
    step = x;
  }

  public int getConfigStep() {
    return step;
  }

  public int getNextId(){
    int x = uniqueId;
    uniqueId++;
    return x;
  }

  public ArrayList<Field> getFields() {
    return conformance.getConformanceCheckerFields();
  }

  public String getFieldToolTip(String field) {
    for (Field f : conformance.getConformanceCheckerFields()) {
      if (f.getName().equals(field)) {
        return f.getDescription();
      }
    }
    return field;
  }

  public ArrayList<String> getFixes() {
    ArrayList<String> fixes = new ArrayList<>();
    fixes.add("removeTag");
    fixes.add("addTag");
    return fixes;
  }

  public ArrayList<String> getFixFields() {
    ArrayList<String> fields = new ArrayList<>();
    fields.add("ImageDescription");
    fields.add("Copyright");
    fields.add("Artist");
    fields.add("DateTime");
    fields.add("Software");
    fields.add("Make");
    fields.add("Model");
    return fields;
  }

  public ArrayList<String> getOperators(String name){
    for (Field field : getFields()) {
      if (field.getName().equals(name)) {
        return field.getOperators();
      }
    }
    return new ArrayList<>();
  }

  public ArrayList<String> getValues(String name){
    for (Field field : getFields()) {
      if (field.getName().equals(name)) {
        return field.getValues();
      }
    }
    return new ArrayList<>();
  }

  public String getPath() {
    return path;
  }
}

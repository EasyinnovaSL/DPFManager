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

  public ArrayList<String> getFixes() {
    ArrayList<String> fixes = new ArrayList<>();
    fixes.add(getBundle().getString("removeTag"));
    fixes.add(getBundle().getString("addTag"));
    return fixes;
  }

  public ArrayList<String> getFixFields() {
    ArrayList<String> fields = new ArrayList<>();
    fields.add("ImageDescription");
    fields.add("Copyright");
    fields.add("Artist");
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

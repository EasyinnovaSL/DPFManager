package dpfmanager.shell.jacp.interfaces.gui.component.config;

import dpfmanager.conformancechecker.tiff.Configuration;
import dpfmanager.conformancechecker.tiff.Field;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.interfaces.gui.ui.design.DessignModel;
import dpfmanager.shell.interfaces.gui.ui.main.MainModel;
import dpfmanager.shell.jacp.core.mvc.DpfModel;

import java.util.ArrayList;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ConfigModel extends DpfModel<ConfigView, ConfigController> {
  private Configuration config;
  private boolean editingConfig;
  private int step;

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
  }

  public void initEditConfig(String path){
    // Trigger when "Edit" button clicked
    step = 0;
    try {
      editingConfig = true;
      config = new Configuration();
      config.ReadFile(path);
    } catch (Exception e) {
      editingConfig = false;
      config = new Configuration();
      config.initDefault();
      // TODO make alert
      System.out.println("Cannot read config file. Starting new config instead.");
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
    fixes.add("Remove Tag");
    fixes.add("Add Tag");
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
}

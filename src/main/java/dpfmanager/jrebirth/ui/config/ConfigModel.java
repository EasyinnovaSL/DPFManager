package dpfmanager.jrebirth.ui.config;

import dpfmanager.jrebirth.ui.dessign.DessignModel;
import dpfmanager.jrebirth.ui.main.MainModel;
import dpfmanager.shell.conformancechecker.Configuration;
import dpfmanager.shell.conformancechecker.Field;

import org.jrebirth.af.core.ui.DefaultModel;

import java.util.ArrayList;

/**
 * Created by Adrià Llorens on 08/02/2016.
 */
public class ConfigModel extends DefaultModel<ConfigModel, ConfigView> {

  private Configuration config;
  private boolean editingConfig;
  private int step;

  //User Interface
  private int uniqueId;

  @Override
  protected void initModel() {
    // init vars
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
    getView().getController().clearAllSteps();
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
      getModel(MainModel.class).getErrorCommand().setMessage("Cannot read config file. Starting new config instead.");
      getModel(MainModel.class).getErrorCommand().run();
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
    return getModel(DessignModel.class).getFields();
  }

  public ArrayList<String> getFixes() {
    return getModel(DessignModel.class).getFixes();
  }

  public ArrayList<String> getFixFields() {
    return getModel(DessignModel.class).getFixFields();
  }

  public ArrayList<String> getOperators(String name){
    return getModel(DessignModel.class).getOperators(name);
  }

  public ArrayList<String> getValues(String name){
    return getModel(DessignModel.class).getValues(name);
  }
}

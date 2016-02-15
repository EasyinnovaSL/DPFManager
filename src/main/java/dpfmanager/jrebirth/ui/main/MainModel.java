package dpfmanager.jrebirth.ui.main;

import dpfmanager.jrebirth.DPFManagerProperties;
import dpfmanager.jrebirth.command.ErrorCommand;
import dpfmanager.jrebirth.ui.stack.StackModel;

import org.jrebirth.af.core.ui.DefaultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public final class MainModel extends DefaultModel<MainModel, MainView> {

  /** The class logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(MainModel.class);

  private ErrorCommand errorCommand;
  static Map<String, String> testValues;

  @Override
  protected void initModel() {
    super.initModel();
    errorCommand = getCommand(ErrorCommand.class);
    testValues = new HashMap<>();
    getView().getRootNode().setCenter(getModel(StackModel.class, MainPage.class).getRootNode());
  }

  @Override
  protected void showView() { }

  public static void setTestParam(String key, String value) {
    testValues.put(key, value);
  }

  public static String getTestParams(String key) {
    return testValues.get(key);
  }

  public void showReports(){
    if (!getView().getShowReports().isSelected()){
      getView().getShowReports().fire();
    }
  }

  public void showLoading(){
    getView().showLoading();
  }

  public void hideLoading(){
    getView().hideLoading();
  }

  public void showFirstTime(){
    getView().getShowFirstTime().fire();
  }

  public void showConfig(){
    getView().getShowConfig().fire();
  }

  public void showDessign(){
    getView().getShowDessign().fire();
  }

  public void enableFlowPane(){
    getView().enableFlowPane();
  }

  public String getConfigDir() {
    return DPFManagerProperties.getConfigDir();
  }

  public boolean getFirstTime(){
    return DPFManagerProperties.getFirstTime();
  }

  public void setConfigDir(String path){
    DPFManagerProperties.setDefaultDir(path);
  }

  public ErrorCommand getErrorCommand(){
    return errorCommand;
  }

}

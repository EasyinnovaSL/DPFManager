package dpfmanager.shell.interfaces.Gui.ui.main;

import dpfmanager.shell.interfaces.DPFManagerProperties;
import dpfmanager.shell.interfaces.Gui.command.ErrorCommand;
import dpfmanager.shell.interfaces.Gui.ui.bottom.BottomController;
import dpfmanager.shell.interfaces.Gui.ui.bottom.BottomModel;
import dpfmanager.shell.interfaces.Gui.ui.stack.StackModel;
import javafx.fxml.FXML;
import javafx.scene.Node;

import org.jrebirth.af.core.ui.DefaultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
  static boolean noDisc = false;
  private double dividerPositionV;

  @Override
  protected void initModel() {
    super.initModel();
    errorCommand = getCommand(ErrorCommand.class);
    testValues = new HashMap<>();
  }

  @Override
  protected void showView() { }

  public void createDefaultConfigurationFiles() {
    File file = new File(getConfigDir() + "/Baseline HTML.dpf");
    if (!file.exists()) {
      try {
        String txt = "ISO\tBaseline\nFORMAT\tHTML\n";
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(txt);
        writer.close();
      } catch (Exception ex) {

      }
    }
  }

  public static void setTestParam(String key, String value) {
    testValues.put(key, value);
  }

  public static String getTestParams(String key) {
    return testValues.get(key);
  }

  public double getDividerPositionV() {
    return dividerPositionV;
  }

  public void setDividerPositionV(double dividerPositionV) {
    this.dividerPositionV = dividerPositionV;
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

  public static void setNoDisc(boolean bol){
    noDisc = bol;
  }

  public boolean getFirstTime(){
    return !noDisc && DPFManagerProperties.getFirstTime();
  }

  public void setConfigDir(String path){
    DPFManagerProperties.setDefaultDir(path);
  }

  public ErrorCommand getErrorCommand(){
    return errorCommand;
  }

}

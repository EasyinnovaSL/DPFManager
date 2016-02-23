package dpfmanager.shell.gui.ui.config;

import dpfmanager.shell.interfaces.GuiApp;
import dpfmanager.shell.gui.ui.config.sub.SubConfig1Controller;
import dpfmanager.shell.gui.ui.config.sub.SubConfig2Controller;
import dpfmanager.shell.gui.ui.config.sub.SubConfig3Controller;
import dpfmanager.shell.gui.ui.config.sub.SubConfig4Controller;
import dpfmanager.shell.gui.ui.config.sub.SubConfig6Controller;
import dpfmanager.shell.gui.ui.main.MainModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

import java.io.File;

/**
 * Created by Adrià Llorens on 08/02/2016.
 */
public class ConfigController extends DefaultController<ConfigModel, ConfigView> {

  static private ConfigModel model;
  static private SubConfig1Controller controller1;
  static private SubConfig2Controller controller2;
  static private SubConfig3Controller controller3;
  static private SubConfig4Controller controller4;
  static private SubConfig6Controller controller6;


  public ConfigController(final ConfigView view) throws CoreException {
    super(view);
    model = getModel();
  }

  public void clearAllSteps() {
    controller1.clear();
    controller2.clear();
    controller3.clear();
    controller4.clear();
    controller6.clear();
  }

  public static ConfigModel getMyModel() {
    return model;
  }

  public static void setSubController1(SubConfig1Controller con) {
    controller1 = con;
  }

  public static void setSubController2(SubConfig2Controller con) {
    controller2 = con;
  }

  public static void setSubController3(SubConfig3Controller con) {
    controller3 = con;
  }

  public static void setSubController4(SubConfig4Controller con) {
    controller4 = con;
  }

  public static void setSubController6(SubConfig6Controller con){
    controller6 = con;
  }

  @Override
  protected void initEventHandlers() throws CoreException {
    // step buttons action
    for (int i = 1; i < 7; i++) {
      final int step = i;
      getView().getStepButton(step).setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          gotoConfig(step);
        }
      });
    }

    //Continue button action
    getView().getContinueButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        int lastBlue = 1, i = 1;
        boolean found = false;
        while (!found && i < 7) {
          if (getView().getStepButton(i).getStyleClass().contains("blue-but")) {
            lastBlue = i;
          } else {
            found = true;
          }
          i++;
        }

        gotoConfig(lastBlue + 1);
      }
    });
  }

  @Override
  protected void initEventAdapters() throws CoreException {
  }

  // Main goto step wizard
  public void gotoConfig(int x) {
    saveSettings(getModel().getConfigStep());
    getModel().setConfigStep(x);
    loadSettings(x);
    if (x < 7) {
      getView().showSubConfig(x);
      getView().setStepsBlue(x);
      getView().setConfigArrowTranslate(x);
      getView().changeStepTitle(x);
      getView().setContinueButtonTest(x);
    } else {
      saveConfig();
    }
  }

  private void saveConfig() {
    File file;
    String value = MainModel.getTestParams("saveConfig");
    if (value != null) {
      //Save in specified output
      file = new File(value);
    } else {
      //Open save dialog
      FileChooser fileChooser = new FileChooser();
      fileChooser.setInitialDirectory(new File(getModel().getModel(MainModel.class).getConfigDir()));
      fileChooser.setInitialFileName("config.dpf");
      fileChooser.setTitle("Save Config");
      file = fileChooser.showSaveDialog(GuiApp.getMyStage());
    }

    if (file != null) {
      try {
        getModel().saveConfig(file.getAbsolutePath());
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }

    getModel().getModel(MainModel.class).showDessign();
  }

  public void saveSettings(int step) {
    switch (step) {
      case 1:
        controller1.saveIsos(getModel().getConfiguration());
        break;
      case 2:
        controller2.saveRules(getModel().getConfiguration());
        break;
      case 3:
        controller3.saveFormats(getModel().getConfiguration());
        controller3.saveOutput(getModel().getConfiguration());
        break;
      case 4:
        controller4.saveFixes(getModel().getConfiguration());
        break;
    }
  }

  public void loadSettings(int step){
    switch (step) {
      case 1:
        controller1.loadIsos(getModel().getConfiguration());
        break;
      case 2:
        controller2.loadRules(getModel().getConfiguration());
        break;
      case 3:
        controller3.loadFormats(getModel().getConfiguration());
        controller3.loadOutput(getModel().getConfiguration());
        break;
      case 4:
        controller4.loadFixes(getModel().getConfiguration());
        break;
      case 6:
        controller6.loadSummary(getModel().getConfiguration());
        break;
    }
  }

}
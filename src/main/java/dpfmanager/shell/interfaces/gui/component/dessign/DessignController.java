package dpfmanager.shell.interfaces.gui.component.dessign;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class DessignController extends DpfController<DessignModel, DessignView> {

  public void mainCheckFiles() {
    if (getView().getInputText().getText().equals("Select a file")) {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, "Please select a file"));
      return;
    }

    RadioButton radio = getView().getSelectedConfig();
    if (radio == null) {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, "Please select a configuration file"));
      return;
    }

    // Send to conformance checker module
    String input = getView().getInputText().getText();
    String path = getFileByPath(radio.getText()).getAbsolutePath();
    getContext().send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(input, path));
  }

  public void selectInputAction() {
    String txtFile = null;
    ComboBox c = getView().getComboChoice();
    String configDir = DPFManagerProperties.getDefaultDir();
    if (c.getValue() == "File") {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open File");
      fileChooser.setInitialDirectory(new File(configDir));
      List<File> files = fileChooser.showOpenMultipleDialog(GuiWorkbench.getMyStage());
      if (files != null) {
        String sfiles = "";
        for (File file : files) {
          if (sfiles.length() > 0) sfiles += ";";
          sfiles += file.getPath();
        }
        txtFile = sfiles;
        if (new File(sfiles).exists() && new File(sfiles).getParent() != null && new File(new File(sfiles).getParent()).exists() && new File(new File(sfiles).getParent()).isDirectory()) {
          String path = new File(sfiles).getParent();
          DPFManagerProperties.setDefaultDir(path);
        }
      }
    } else {
      DirectoryChooser folderChooser = new DirectoryChooser();
      folderChooser.setTitle("Open Folder");
      folderChooser.setInitialDirectory(new File(configDir));
      File directory = folderChooser.showDialog(GuiWorkbench.getMyStage());
      if (directory != null) {
        txtFile = directory.getPath();
        DPFManagerProperties.setDefaultDir(directory.getPath());
      }
    }
    if (txtFile != null) {
      getView().getInputText().setText(txtFile);
    }
  }

  public void performEditConfigAction() {
    if (getView().getSelectedConfig() != null) {
      String text = getView().getSelectedConfig().getText();
      String path = getFileByPath(text).getAbsolutePath();
      ArrayMessage am = new ArrayMessage();
      am.add(GuiConfig.PERSPECTIVE_CONFIG, new UiMessage());
      am.add(GuiConfig.PERSPECTIVE_CONFIG + "." + GuiConfig.COMPONENT_CONFIG, new ConfigMessage(ConfigMessage.Type.EDIT, path));
      getContext().send(GuiConfig.PERSPECTIVE_CONFIG, am);
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, "Please select a configuration file"));
    }
  }

  public void performImportConfigAction() {
    File file;
    String value = GuiWorkbench.getTestParams("import");
    if (value != null) {
      //Test mode
      file = new File(value);
    } else {
      //Ask for file
      String configDir = DPFManagerProperties.getDefaultDir();
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Config");
      fileChooser.setInitialDirectory(new File(configDir));
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DPF config files (*.dpf)", "*.dpf");
      fileChooser.getExtensionFilters().add(extFilter);
      file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
    }

    if (file != null) {
      DPFManagerProperties.setDefaultDir(file.getParent());
      getView().addConfigFile(file.getAbsolutePath());
    }
  }

  public void performDeleteConfigAction(String text) {
    File file = getFileByPath(text);
    if (file.delete()) {
      getView().deleteSelectedConfig();
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "There was an error deleting the configuration file"));
    }
  }

  public void testAction() {
    try {
      String nullable = null;
      if (true) {
        nullable.substring(2);
      }
    }
    catch(Exception ex){
      getContext().send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception ocurred!", ex));
    }
//    getContext().send(BasicConfig.MODULE_MESSAGE, new LogMessage(this.getClass(), Level.INFO, "Info message"));
  }

  private File getFileByPath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      String configDir = DPFManagerProperties.getConfigDir();
      file = new File(configDir + "/" + path);
    }
    return file;
  }
}

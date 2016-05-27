package dpfmanager.shell.interfaces.gui.component.dessign;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.messages.WidgetMessage;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class DessignController extends DpfController<DessignModel, DessignView> {

  public void mainCheckFiles() {
    if (getView().getInputText().getText().equals("Select a file") || getView().getInputText().getText().equals("Select a folder")) {
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
    int recursive = getView().getRecursive();
    ArrayMessage am = new ArrayMessage();
    am.add(GuiConfig.COMPONENT_BAR, new WidgetMessage(WidgetMessage.Action.SHOW, WidgetMessage.Target.TASKS));
    am.add(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(ConformanceMessage.Type.GUI, input, path, recursive));
    getContext().send(GuiConfig.COMPONENT_BAR, am);
  }

  public void selectInputAction() {
    String txtFile = null;
    ComboBox c = getView().getComboChoice();
    String configDir = DPFManagerProperties.getDefaultDirFile();
    //getContext().send(BasicConfig.MODULE_MESSAGE, new LogMessage(this.getClass(), Level.DEBUG, "Config dir: " + configDir));
    if (c.getValue() == "File") {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open File");
      fileChooser.setInitialDirectory(new File(configDir));
      List<File> files = fileChooser.showOpenMultipleDialog(GuiWorkbench.getMyStage());
      if (files != null) {
        String sfiles = "";
        File last = null;
        for (File file : files) {
          if (sfiles.length() > 0) sfiles += ";";
          sfiles += file.getPath();
          last = file;
        }
        txtFile = sfiles;
        if (last.exists() && last.getParent() != null && last.getParentFile().exists() && last.getParentFile().isDirectory()) {
          String path = last.getParent();
          DPFManagerProperties.setDefaultDirFile(path);
        }
      }
    } else {
      DirectoryChooser folderChooser = new DirectoryChooser();
      folderChooser.setTitle("Open Folder");
      folderChooser.setInitialDirectory(new File(configDir));
      File directory = folderChooser.showDialog(GuiWorkbench.getMyStage());
      if (directory != null) {
        txtFile = directory.getPath();
        DPFManagerProperties.setDefaultDirFile(directory.getPath());
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
      String configDir = DPFManagerProperties.getDefaultDirConfig();
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Config");
      fileChooser.setInitialDirectory(new File(configDir));
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DPF config files (*.dpf)", "*.dpf");
      fileChooser.getExtensionFilters().add(extFilter);
      file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
    }

    if (file != null) {
      DPFManagerProperties.setDefaultDirConfig(file.getParent());
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Copy configuration");
      alert.setHeaderText("Do you want to copy the config file into config folder?");
      alert.setContentText("Caution: This may override an existing config file.");
      ButtonType buttonTypeYes = new ButtonType("Yes");
      ButtonType buttonTypeNo = new ButtonType("No");
      alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == buttonTypeYes) {
        // Copy the file
        boolean needAdd = true;
        boolean error = false;
        File configFile = new File(DPFManagerProperties.getConfigDir() + "/" + file.getName());
        if (configFile.exists()) {
          configFile.delete();
          needAdd = false;
        }
        try {
          FileUtils.copyFile(file, configFile);
        } catch (IOException e) {
          error = true;
        }
        if (error) {
          // Add source file
          getView().addConfigFile(file.getAbsolutePath());
          getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.WARNING, "Error copying config file."));
        } else if (needAdd) {
          getView().addConfigFile(configFile.getName());
        }
      } else {
        getView().addConfigFile(file.getAbsolutePath());
      }
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
  }

  public void testAction2() {
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

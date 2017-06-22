/**
 * <h1>DessignController.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.dessign;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adria Llorens on 07/03/2016.
 */
public class DessignController extends DpfController<DessignModel, DessignView> {

  public void mainFullCheck(){
    mainCheckFiles(false);
  }

  public void mainQuickCheck(){
    mainCheckFiles(true);
  }

  public void mainCheckFiles(boolean isQuick) {
    if (!getView().isAvailable()) {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, getBundle().getString("alertConformances"), new UiMessage(UiMessage.Type.SHOW), GuiConfig.PERSPECTIVE_INTEROPERABILITY));
      return;
    }

    String input = "";
    List<String> treeSelected = getView().getTreeSelectedItems();
    if (treeSelected != null) {
      // Tree view
      if (treeSelected.isEmpty()) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("alertFile")));
        return;
      } else {
        for (String sel : treeSelected) {
          input += sel + ";";
        }
        input = input.substring(0, input.length() - 1);
      }
    } else {
      // Input text
      if (getView().getInputText().getText().isEmpty() || getView().getInputText().getText().equals(getBundle().getString("inputText")) || getView().getInputText().getText().equals(getBundle().getString("selectFolder"))) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("alertFile")));
        return;
      } else {
        input = getView().getInputText().getText();
      }
    }

    RadioButton radio = getView().getSelectedConfig();
    if (radio == null) {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("alertConfigFile")));
      return;
    }

    // Send to conformance checker module
    String path = "";
    if (!radio.getText().equals(getBundle().getString("default"))) {
      path = getFileByPath(radio.getText()).getAbsolutePath();
    }
    int recursive = getView().getRecursive();
    getContext().send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(input, path, recursive, true, isQuick));
  }

  public void selectInputAction() {
    String txtFile = null;
    ComboBox c = getView().getComboChoice();
    String configDir = DPFManagerProperties.getDefaultDirFile();
    if (c.getValue().equals(getBundle().getString("comboFile"))) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(getBundle().getString("openFile"));
      fileChooser.setInitialDirectory(new File(configDir));
      fileChooser.getExtensionFilters().addAll(generateExtensionsFilters());
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
    } else if (c.getValue().equals(getBundle().getString("comboFolder"))) {
      DirectoryChooser folderChooser = new DirectoryChooser();
      folderChooser.setTitle(getBundle().getString("openFolder"));
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

  private List<FileChooser.ExtensionFilter> generateExtensionsFilters() {
    List<FileChooser.ExtensionFilter> filtersCC = new ArrayList<>();
    List<FileChooser.ExtensionFilter> filters = new ArrayList<>();
    List<String> allExtensions = new ArrayList<>();

    // Conformances extensions
    for (ConformanceChecker cc : getView().getInterService().getConformanceCheckers(false)) {
      allExtensions.addAll(parseExtensions(cc.getConfig().getExtensions()));
      filtersCC.add(new FileChooser.ExtensionFilter(cc.getConfig().getName(), parseExtensions(cc.getConfig().getExtensions())));
    }
    // Common extensions
    allExtensions.addAll(parseExtensions(DPFManagerProperties.getCommonExtensions()));

    filters.add(new FileChooser.ExtensionFilter(getBundle().getString("acceptedFiles"), allExtensions));
    filters.addAll(filtersCC);
    filters.add(new FileChooser.ExtensionFilter(getBundle().getString("compressedFiles"), parseExtensions(DPFManagerProperties.getCommonExtensions())));

    return filters;
  }

  public List<String> getAcceptedExetsniosn() {
    List<String> extensions = new ArrayList<>();
    for (ConformanceChecker cc : getView().getInterService().getConformanceCheckers(false)) {
      extensions.addAll(cc.getConfig().getExtensions());
    }
    extensions.addAll(DPFManagerProperties.getCommonExtensions());
    return extensions;
  }

  private List<String> parseExtensions(List<String> extensions) {
    List<String> newExtensions = new ArrayList<>();
    for (String ext : extensions) {
      newExtensions.add("*." + ext.toLowerCase());
      newExtensions.add("*." + ext.toUpperCase());
    }
    return newExtensions;
  }

  public void performEditConfigAction() {
    if (getView().getSelectedConfig() != null) {
      String text = getView().getSelectedConfig().getText();
      String path = null;
      if (!text.equals(getBundle().getString("default"))){
        path = getFileByPath(text).getAbsolutePath();
      }
      ArrayMessage am = new ArrayMessage();
      am.add(GuiConfig.PERSPECTIVE_CONFIG, new UiMessage());
      am.add(GuiConfig.PERSPECTIVE_CONFIG + "." + GuiConfig.COMPONENT_CONFIG, new ConfigMessage(ConfigMessage.Type.EDIT, path));
      getContext().send(GuiConfig.PERSPECTIVE_CONFIG, am);
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("alertConfigFile")));
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
      fileChooser.setTitle(getBundle().getString("openConfig"));
      fileChooser.setInitialDirectory(new File(configDir));
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(getBundle().getString("dpfConfigFiles"), "*.dpf");
      fileChooser.getExtensionFilters().add(extFilter);
      file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
    }

    addConfigFile(file, true);
  }

  public void addConfigFile(File file, boolean ask) {
    if (file != null) {
      // Check valid config
      if (!readConfig(file.getPath())) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, DPFManagerProperties.getBundle().getString("errorReadingConfFile")));
        file = null;
      }
    }

    if (file != null && ask) {
      DPFManagerProperties.setDefaultDirConfig(file.getParent());
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(getBundle().getString("copyTitle"));
      alert.setHeaderText(getBundle().getString("copyHeader"));
      alert.setContentText(getBundle().getString("copyContent"));
      ButtonType buttonTypeYes = new ButtonType(getBundle().getString("yes"));
      ButtonType buttonTypeNo = new ButtonType(getBundle().getString("no"));
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
          String description = readDescription(file);
          getView().addConfigFile(file.getAbsolutePath(), description, false);
          getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.WARNING, getBundle().getString("errorCopyConfig")));
        } else if (needAdd) {
          String description = readDescription(configFile);
          getView().addConfigFile(configFile.getName(), description, false);
        }
      } else {
        String description = readDescription(file);
        getView().addConfigFile(file.getAbsolutePath(), description, false);
      }
    } else if (file != null && !ask) {
      getView().addConfigFile(file.getAbsolutePath(), readDescription(file), false);
    }
  }

  private boolean readConfig(String path) {
    try {
      Configuration config = new Configuration();
      config.ReadFile(path);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private String readDescription(String path) {
    try {
      Configuration config = new Configuration();
      config.ReadFile(path);
      return config.getDescription();
    } catch (Exception e) {
      return "";
    }
  }

  public void performDeleteConfigAction(String text) {
    File file = getFileByPath(text);
    if (file.delete()) {
      getView().deleteSelectedConfig();
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, getBundle().getString("deleteError")));
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

  public String readDescription(File file) {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      NodeList nList = doc.getDocumentElement().getChildNodes();
      for (int i = 0; i < nList.getLength(); i++) {
        org.w3c.dom.Node node = nList.item(i);
        if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
          Element elem = (Element) node;
          if (elem.getTagName().equals("description")) {
            return elem.getTextContent();
          }
        }
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }
}

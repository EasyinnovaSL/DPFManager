package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes.clearPrivateData;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.WidgetMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_BAR,
    viewLocation = "/fxml/bottom-bar.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class BarFragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private Button consoleBut;
  @FXML
  private StackPane consoleSeparator;
  @FXML
  private Button taskBut;

  @FXML
  private ComboBox comboBox;

  private boolean firsttime = true;
  private boolean consoleVisible = false;
  private boolean tasksVisible = false;
  private Map<String,String> languages;

  // Main functions

  public void init() {
    if (firsttime) {
      setDefault();
      initLanguages();
      firsttime = false;
    }
  }

  private void initLanguages() {
    languages = new HashMap<>();
    List<String> strLocales = loadLanguages();
    for (String strLocale : strLocales){
      Locale loc = new Locale(strLocale);
      languages.put(loc.getDisplayLanguage().toLowerCase(), strLocale);
      comboBox.getItems().add(upperFirstLetter(loc.getDisplayLanguage()));
    }
    Collections.sort(comboBox.getItems());
    comboBox.setValue(upperFirstLetter(Locale.getDefault().getDisplayName()));
  }

  private String upperFirstLetter(String word){
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  private List<String> loadLanguages(){
    List<String> array = new ArrayList<>();
    try {
      // Load from jar
      String path = "DPF Manager-jfx.jar";
      if (new File(path).exists()) {
        ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
          if (!entry.isDirectory() && entry.getName().endsWith(".properties") && entry.getName().contains("bundles") && !entry.getName().contains("language.properties")) {
            array.add(entry.getName().substring(entry.getName().lastIndexOf("/") + 1).replace("language_", "").replace(".properties", ""));
          }
        }
      }
    } catch (Exception ex) {
    }

    if (array.isEmpty()) {
      // Load through reflections
      Reflections reflections = new Reflections("", new ResourcesScanner());
      Set<String> resources = reflections.getResources(Pattern.compile(".*\\.properties"));
      for (String resource : resources) {
        if (resource.contains("bundles/language_") && !resource.endsWith("language.properties")){
          array.add(resource.replace("bundles/language_","").replace(".properties",""));
        }
      }
    }

    // Add default, english
    array.add("en");

    return array;
  }

  public void setDefault() {
    NodeUtil.showNode(consoleBut);
    NodeUtil.showNode(consoleSeparator);
    NodeUtil.showNode(taskBut);
  }

  /**
   * Console pane
   */
  @FXML
  protected void showHideConsole(ActionEvent event) throws Exception {
    if (consoleBut.getStyleClass().contains("active")) {
      // Hide Console
      consoleVisible = false;
      consoleBut.getStyleClass().remove("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.HIDE, WidgetMessage.Target.CONSOLE));
    } else {
      // Show console
      consoleVisible = true;
      consoleBut.getStyleClass().add("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.SHOW, WidgetMessage.Target.CONSOLE));
    }
  }

  public void showHideTasks() {
    try {
      showHideTasks(null);
    } catch (Exception e) {
    }
  }

  @FXML
  protected void showHideTasks(ActionEvent event) throws Exception {
    if (taskBut.getStyleClass().contains("active")) {
      // Hide tasks
      tasksVisible = false;
      taskBut.getStyleClass().remove("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.HIDE, WidgetMessage.Target.TASKS));
    } else {
      // Show tasks
      tasksVisible = true;
      taskBut.getStyleClass().add("active");
      context.send(GuiConfig.COMPONENT_PANE, new WidgetMessage(WidgetMessage.Action.SHOW, WidgetMessage.Target.TASKS));
    }
  }

  /**
   * Language
   */
  @FXML
  protected void onChangeLanguage(ActionEvent event) throws Exception {
    String display = (String) comboBox.getValue();
    DPFManagerProperties.setLanguage(languages.get(display.toLowerCase()));
    context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, bundle.getString("changeLanguage")));
  }

  /**
   * Extra functions
   */

  public boolean isVisible() {
    return consoleVisible || tasksVisible;
  }

  public boolean isTasksvisible() {
    return tasksVisible;
  }

}

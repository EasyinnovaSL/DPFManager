/**
 * <h1>DessignView.java</h1> <p> This program is free software: you can redistribute it and/or
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
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.ClassFinder;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.DessignMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.fragment.ConformanceBoxFragment;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.interoperability.core.InteroperabilityService;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import org.controlsfx.control.CheckTreeView;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_DESIGN,
    name = GuiConfig.COMPONENT_DESIGN,
    viewLocation = "/fxml/dessign.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_DESIGN)
public class DessignView extends DpfView<DessignModel, DessignController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private HBox treeViewHBox;
  @FXML
  private ComboBox comboChoice;
  @FXML
  private VBox loadingVbox;
  @FXML
  private ScrollPane configScroll;
  @FXML
  private TextField inputText;
  @FXML
  private CheckBox recursiveCheck;
  @FXML
  private Button reloadButton;

  @FXML
  private Label availableLabel;
  @FXML
  private FlowPane flowPane;
  @FXML
  private VBox vboxAvailable;

  @Autowired
  private InteroperabilityService interService;

  private VBox vBoxConfig;
  private ToggleGroup group;
  private RadioButton selectedButton;
  private CheckTreeView<String> checkTreeView;
  private boolean first = true;
  private boolean available;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(AlertMessage.class)) {
      AlertMessage am = message.getTypedMessage(AlertMessage.class);
      RadioButton radio = getSelectedConfig();
      if (radio != null && am.hasResult() && am.getResult()) {
        getController().performDeleteConfigAction(radio.getText());
      }
    } else if (message != null && message.isTypeOf(UiMessage.class)) {
      UiMessage uiMessage = message.getTypedMessage(UiMessage.class);
      if (uiMessage.isShow()) {
        if (!first) {
          readAvailableConformances();
          addConfigFiles();
        } else {
          first = false;
          showLoadingConfig();
        }
      }
    } else if (message != null && message.isTypeOf(DessignMessage.class)) {
      if (first) {
        first = false;
      } else {
        readAvailableConformances();
        addConfigFiles();
      }
    }
    return null;
  }

  @Override
  public Context getContext() {
    return context;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Pre load classes
    try {
      for (String className : ClassFinder.getClassNamesFromPackage("com.easyinnova.tiff")) {
        Class.forName("com.easyinnova.tiff" + "." + className);
      }
      for (String className : ClassFinder.getClassNamesFromPackage("com.easyinnova.implementation_checker")) {
        Class.forName("com.easyinnova.implementation_checker" + "." + className);
      }
      for (String className : ClassFinder.getClassNamesFromPackage("com.easyinnova.policy_checker")) {
        Class.forName("com.easyinnova.policy_checker" + "." + className);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Set model and controller
    setModel(new DessignModel());
    setController(new DessignController());
    getController().setResourcebundle(bundle);

    // Add input types
    if (comboChoice.getItems().size() < 2) {
      comboChoice.setCursor(Cursor.HAND);
      comboChoice.setPrefWidth(10.0);
      comboChoice.setMaxWidth(10.0);
      comboChoice.setMinWidth(10.0);
      comboChoice.getItems().add(bundle.getString("comboFile"));
      comboChoice.getItems().add(bundle.getString("comboFolder"));
      comboChoice.getItems().add(bundle.getString("comboTreeview"));
      comboChoice.setValue(bundle.getString("comboFile"));
    }
    NodeUtil.hideNode(recursiveCheck);

    // Add TreeView
    addTreeView();
    NodeUtil.hideNode(treeViewHBox);
    NodeUtil.hideNode(reloadButton);

    // Loading available conformances
    available = false;
    NodeUtil.hideNode(vboxAvailable);

    comboChoice.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        comboChoice.requestFocus();
      }
    });
//    Label label = new Label(bundle.getString("loadingAvailableCC"));
//    label.getStyleClass().add("lightgrey");
//    flowPane.getChildren().add(label);
  }

  private void addTreeView() {
    // Root node (my computer)
    CheckBoxTreeItem<String> rootNode = new CheckBoxTreeItem<>(getHostName(), new ImageView(new Image("images/computer.png")));
    checkTreeView = new CheckTreeView<>(rootNode);
    rootNode.addEventHandler(TreeItem.<Object>branchExpandedEvent(), new ExpandEventHandler(checkTreeView));
    rootNode.addEventHandler(TreeItem.<Object>branchCollapsedEvent(), new CollapseEventHandler());

    // Root items
    Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
    for (Path name : rootDirectories) {
      if (Files.isDirectory(name)) {
        FilePathTreeItem treeNode = new FilePathTreeItem(name);
        rootNode.getChildren().add(treeNode);
      }
    }
    rootNode.setExpanded(true);

    // Add data and add to gui
    treeViewHBox.getChildren().clear();
    treeViewHBox.getChildren().add(checkTreeView);
    HBox.setHgrow(checkTreeView, Priority.ALWAYS);
  }

  private String getHostName() {
    String hostName = bundle.getString("myComputer");
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException x) {
    }
    return hostName;
  }

  private void addConfigFiles() {
    String previous = null;
    if (selectedButton != null) {
      previous = selectedButton.getText();
    }
    selectedButton = null;
    group = new ToggleGroup();
    vBoxConfig = new VBox();
    vBoxConfig.setId("vBoxConfig");
    vBoxConfig.setSpacing(3);
    vBoxConfig.setPadding(new Insets(5));

    // Default one
    String description = interService.getDescriptionFromDefault();
    if (description != null) {
      addConfigFile(bundle.getString("default"), description, bundle.getString("default").equalsIgnoreCase(previous));
    }

    // User configs
    File folder = new File(DPFManagerProperties.getConfigDir());
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isFile()) {
        if (fileEntry.getName().toLowerCase().endsWith(".dpf")) {
          description = getController().readDescription(fileEntry);
          addConfigFile(fileEntry.getName(), description, fileEntry.getName().equalsIgnoreCase(previous));
        }
      }
    }

    // Drag and drop
    configScroll.setOnDragDropped(event -> {
      // Files dropped
      Dragboard db = event.getDragboard();
      boolean success = false;
      if (db.hasFiles()) {
        success = true;
        for (File file : db.getFiles()) {
          getController().addConfigFile(file, false);
        }
      }
      event.setDropCompleted(success);
      event.consume();
    });
    configScroll.setOnDragOver(event -> {
      if (acceptedFiles(event.getDragboard(), Arrays.asList("dpf"), false)) {
        event.acceptTransferModes(TransferMode.MOVE);
      }
      event.consume();
    });
    configScroll.setOnDragEntered(event -> {
      if (acceptedFiles(event.getDragboard(), Arrays.asList("dpf"), false)) {
        configScroll.getStyleClass().add("on-drag");
      }
      event.consume();
    });
    configScroll.setOnDragExited(event -> {
      configScroll.getStyleClass().remove("on-drag");
      event.consume();
    });

    configScroll.setContent(vBoxConfig);
  }

  public void addConfigFile(String text, String description, boolean selected) {
    RadioButton radio = new RadioButton();
    radio.setId("radioConfig" + vBoxConfig.getChildren().size());
    radio.setText(text);
    radio.setToggleGroup(group);
    radio.setSelected(selected);
    if (description != null && !description.isEmpty()) {
      radio.setTooltip(new Tooltip(description));
    }
    radio.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        selectedButton = radio;
      }
    });
    vBoxConfig.getChildren().add(radio);
    if (selected) {
      selectedButton = radio;
    }
  }

  public void deleteSelectedConfig() {
    RadioButton rad = getSelectedConfig();
    group.getToggles().remove(rad);
    vBoxConfig.getChildren().remove(rad);
  }

  private void readAvailableConformances() {
    int intCount = 0, extCount = 0;
    flowPane.getChildren().clear();
    for (ConformanceChecker cc : interService.getConformanceCheckers()) {
      ManagedFragmentHandler<ConformanceBoxFragment> handler = getContext().getManagedFragmentHandler(ConformanceBoxFragment.class);
      handler.getController().load(cc);
      flowPane.getChildren().add(handler.getFragmentNode());
      if (cc.getConfig().isBuiltIn()) {
        intCount++;
      } else {
        extCount++;
      }
    }
    available = (intCount + extCount) > 0;
    if (!available) {
      Label label = new Label(bundle.getString("noAvailableCC"));
      label.getStyleClass().addAll("label-exclamation");
      label.setFont(new Font(14));
      flowPane.getChildren().add(label);
      NodeUtil.showNode(vboxAvailable);
    } else if (intCount > 0 && extCount == 0) {
      NodeUtil.hideNode(vboxAvailable);
    } else {
      NodeUtil.showNode(vboxAvailable);
    }
  }

  /**
   * FXML Events Handlers
   */

  @FXML
  protected void selectFileClicked(ActionEvent event) throws Exception {
    getController().selectInputAction();
  }

  @FXML
  protected void checkFilesClicked(ActionEvent event) throws Exception {
    getController().mainFullCheck();
  }

  @FXML
  protected void quickCheckFilesClicked(ActionEvent event) throws Exception {
    getController().mainQuickCheck();
  }

  @FXML
  protected void onChangeInputType(ActionEvent event) throws Exception {
    if (comboChoice.getValue() == bundle.getString("comboFile")) {
      inputText.setText(bundle.getString("selectFile"));
      NodeUtil.showNode(inputText);
      NodeUtil.hideNode(recursiveCheck);
      NodeUtil.hideNode(treeViewHBox);
      NodeUtil.hideNode(reloadButton);
    } else if (comboChoice.getValue() == bundle.getString("comboFolder")) {
      inputText.setText(bundle.getString("selectFolder"));
      NodeUtil.showNode(inputText);
      NodeUtil.showNode(recursiveCheck);
      NodeUtil.hideNode(treeViewHBox);
      NodeUtil.hideNode(reloadButton);
    } else if (comboChoice.getValue() == bundle.getString("comboTreeview")) {
      NodeUtil.hideNode(inputText);
      NodeUtil.hideNode(recursiveCheck);
      NodeUtil.showNode(treeViewHBox);
      NodeUtil.showNode(reloadButton);
    }
    if (!GuiWorkbench.isTestMode()) {
      getController().selectInputAction();
    }
  }

  @FXML
  protected void reloadTreeView(ActionEvent event) throws Exception {
    addTreeView();
  }

  @FXML
  protected void showFileInfo(ActionEvent event) throws Exception {
    String header = bundle.getString("filesPopHeader");
    String content = bundle.getString("filesPopContent");
    getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, header, content));
  }

  @FXML
  protected void showConfigInfo(ActionEvent event) throws Exception {
    String header = bundle.getString("configPopHeader");
    String content = bundle.getString("configPopContent");
    getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, header, content));
  }

  @FXML
  protected void newButtonClicked(ActionEvent event) throws Exception {
    ArrayMessage am = new ArrayMessage();
    am.add(GuiConfig.PERSPECTIVE_CONFIG, new UiMessage());
    am.add(GuiConfig.PERSPECTIVE_CONFIG + "." + GuiConfig.COMPONENT_CONFIG, new ConfigMessage(ConfigMessage.Type.NEW));
    getContext().send(GuiConfig.PERSPECTIVE_CONFIG, am);
  }

  @FXML
  protected void importButtonClicked(ActionEvent event) throws Exception {
    getController().performImportConfigAction();
  }

  @FXML
  protected void editButtonClicked(ActionEvent event) throws Exception {
    getController().performEditConfigAction();
  }

  @FXML
  protected void deleteButtonClicked(ActionEvent event) throws Exception {
    RadioButton radio = getSelectedConfig();
    if (radio != null) {
      if (!radio.getText().equals(bundle.getString("default"))) {
        AlertMessage am = new AlertMessage(AlertMessage.Type.CONFIRMATION, bundle.getString("deleteConfirmation").replace("%1", radio.getText()), bundle.getString("deleteInfo"));
        am.setTitle(bundle.getString("deleteTitle"));
        getContext().send(BasicConfig.MODULE_MESSAGE, am);
      } else {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertDefaultConfigDelete")));
      }
    } else {
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("alertConfigFile")));
    }
  }

  /**
   * Drag and drop input
   */
  @FXML
  protected void onDragDroppedInput(DragEvent event) throws Exception {
    // Files dropped
    Dragboard db = event.getDragboard();
    boolean success = false;
    if (db.hasFiles()) {
      success = true;
      String filePath = "";
      for (File file : db.getFiles()) {
        if (!filePath.isEmpty()) {
          filePath += ";";
        }
        filePath += file.getAbsolutePath();
      }
      String currentText = inputText.getText();
      if (!currentText.isEmpty() && !currentText.equals(bundle.getString("inputText"))) {
        inputText.setText(currentText + ";" + filePath);
      } else {
        inputText.setText(filePath);
      }
    }
    event.setDropCompleted(success);
    event.consume();
  }

  @FXML
  protected void onDragOverInput(DragEvent event) throws Exception {
    // Filter accepted files
    if (acceptedFiles(event.getDragboard(), getController().getAcceptedExetsniosn(), true)) {
      event.acceptTransferModes(TransferMode.MOVE);
    }
    event.consume();
  }

  @FXML
  protected void onDragEnteredInput(DragEvent event) throws Exception {
    if (acceptedFiles(event.getDragboard(), getController().getAcceptedExetsniosn(), true)) {
      inputText.getStyleClass().add("on-drag");
    }
    event.consume();
  }

  @FXML
  protected void onDragExitedInput(DragEvent event) throws Exception {
    inputText.getStyleClass().remove("on-drag");
    event.consume();
  }

  public boolean acceptedFiles(Dragboard db, List<String> accepted, boolean folder) {
    if (db.hasFiles()) {
      for (File file : db.getFiles()) {
        String ext = getExtension(file.getName());
        if (file.isDirectory() && !folder) {
          return false;
        }
        if (file.isFile() && !accepted.contains(ext)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private String getExtension(String name) {
    int i = name.lastIndexOf('.');
    if (i > 0) {
      return name.substring(i + 1).toLowerCase();
    }
    return "";
  }

  public List<String> getTreeSelectedItems() {
    if (treeViewHBox.isVisible()) {
      int index = 0;
      List<String> selected = new ArrayList<>();
      TreeItem<String> item = checkTreeView.getTreeItem(index);
      while (item != null) {
        if (item instanceof FilePathTreeItem) {
          FilePathTreeItem fpItem = (FilePathTreeItem) item;
          if (!fpItem.isDirectory() && fpItem.isFullSelected()) {
            if (!selected.contains(fpItem.getParentPath())) {
              selected.add(fpItem.getFullPath());
            }
          } else if (fpItem.isFullSelected()) {
            selected.add(fpItem.getFullPath());
          }
        }
        item = checkTreeView.getTreeItem(++index);
      }
      return selected;
    } else {
      return null;
    }
  }

  private void showLoadingConfig() {
    ProgressIndicator pi = new ProgressIndicator();
    pi.setPrefWidth(75);
    pi.setPrefHeight(75);
    pi.setProgress(-1);
    vBoxConfig = new VBox();
    vBoxConfig.setAlignment(Pos.TOP_CENTER);
    vBoxConfig.setPrefWidth(configScroll.getWidth() - 5);
    vBoxConfig.getChildren().add(pi);
    VBox.setMargin(pi, new Insets(10, 0, 0, 0));
    configScroll.setContent(vBoxConfig);
  }

  public RadioButton getSelectedConfig() {
    RadioButton radio = (RadioButton) group.getSelectedToggle();
    if (radio == null) {
      return selectedButton;
    }
    return radio;
  }

  public ComboBox getComboChoice() {
    return comboChoice;
  }

  public TextField getInputText() {
    return inputText;
  }

  public int getRecursive() {
    if (recursiveCheck.isSelected()) {
      return 100;
    }
    return 1;
  }

  public boolean isAvailable() {
    return available;
  }

  public InteroperabilityService getInterService() {
    return interService;
  }
}

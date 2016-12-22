/**
 * <h1>ConfigView.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.config;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard1Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard2Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard4Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard5Fragment;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard3Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard6Fragment;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.logging.log4j.Level;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_CONFIG,
    name = GuiConfig.COMPONENT_CONFIG,
    viewLocation = "/fxml/config.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_CONFIG)
public class ConfigView extends DpfView<ConfigModel, ConfigController> {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private GridPane gridSave;
  @FXML
  private HBox hboxName;
  @FXML
  private HBox hboxDescription;
  @FXML
  private Button continueButton;
  @FXML
  private Button backButton;
  @FXML
  private TextField saveNameInput;
  @FXML
  private TextField saveDescInput;
  @FXML
  private ImageView configArrow;
  @FXML
  private ImageView stepImage;
  @FXML
  private Label stepTitle;
  @FXML
  private VBox wizard;
  @FXML
  private Label labelW1;

  @FXML
  private Button step1, step2, step3, step4, step5;

  private List<Node> includedNodes;
  private List<Button> stepsButtons;
  private boolean isEdit;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(ConfigMessage.class)) {
      ConfigMessage cMessage = message.getTypedMessage(ConfigMessage.class);
      if (cMessage.isNew()) {
        isEdit = false;
        getModel().initNewConfig();
      } else if (cMessage.isEdit()) {
        isEdit = true;
        getModel().initEditConfig(cMessage.getPath());
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(ConfigMessage.class)) {
      getController().clearAllSteps();
      gotoConfig(1);
      if (!isEdit) {
        saveDescInput.clear();
        saveNameInput.clear();
      } else {
        if (loadName(getModel().getPath())) {
          File configFile = new File(getModel().getPath());
          saveNameInput.setText(configFile.getName().replace(".dpf", ""));
        } else {
          saveNameInput.clear();
        }
        saveDescInput.setText(getModel().getConfiguration().getDescription());
      }
    }
    return null;
  }

  private boolean loadName(String path){
    if (path != null){
      File tmp1 = new File(path);
      File tmp2 = new File(DPFManagerProperties.getConfigDir());
      for (File file : tmp2.listFiles()){
        if (file.getAbsolutePath().equals(tmp1.getAbsolutePath())){
          return true;
        }
      }
    }
    return false;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Set model and controller
    setModel(new ConfigModel());
    setController(new ConfigController());
    getController().setResourcebundle(bundle);
    getModel().setResourcebundle(bundle);

    stepsButtons = Arrays.asList(step1, step2, step3, step4, step5);

    initAllFragments();
  }

  public void clear(){
    saveNameInput.clear();
  }

  private void initAllFragments() {
    getContext().getManagedFragmentHandler(Wizard1Fragment.class).getController().init();
    getContext().getManagedFragmentHandler(Wizard2Fragment.class).getController().setController(getController());
    getContext().getManagedFragmentHandler(Wizard6Fragment.class).getController().setController(getController());
    getContext().getManagedFragmentHandler(Wizard2Fragment.class).getController().setModel(getModel());
    getContext().getManagedFragmentHandler(Wizard4Fragment.class).getController().setModel(getModel());

    getController().setFragment1(getContext().getManagedFragmentHandler(Wizard1Fragment.class));
    getController().setFragment2(getContext().getManagedFragmentHandler(Wizard2Fragment.class));
    getController().setFragment3(getContext().getManagedFragmentHandler(Wizard3Fragment.class));
    getController().setFragment4(getContext().getManagedFragmentHandler(Wizard4Fragment.class));
    getController().setFragment5(getContext().getManagedFragmentHandler(Wizard5Fragment.class));
    getController().setFragment6(getContext().getManagedFragmentHandler(Wizard6Fragment.class));

    wizard.getChildren().clear();
    wizard.getChildren().add(getContext().getManagedFragmentHandler(Wizard1Fragment.class).getFragmentNode());
    wizard.getChildren().add(getContext().getManagedFragmentHandler(Wizard2Fragment.class).getFragmentNode());
    wizard.getChildren().add(getContext().getManagedFragmentHandler(Wizard3Fragment.class).getFragmentNode());
    wizard.getChildren().add(getContext().getManagedFragmentHandler(Wizard4Fragment.class).getFragmentNode());
    wizard.getChildren().add(getContext().getManagedFragmentHandler(Wizard5Fragment.class).getFragmentNode());
    wizard.getChildren().add(getContext().getManagedFragmentHandler(Wizard6Fragment.class).getFragmentNode());

    includedNodes =  wizard.getChildren();
  }

  @FXML
  protected void doContinue(){
    int lastBlue = 1, i = 1;
    boolean found = false;
    while (!found && i < 6) {
      if (getStepButton(i).getStyleClass().contains("blue-but")) {
        lastBlue = i;
      } else {
        found = true;
      }
      i++;
    }
    gotoConfig(lastBlue + 1);
  }

  @FXML
  protected void doBack(){
    getContext().send(GuiConfig.PERSPECTIVE_DESSIGN, new UiMessage(UiMessage.Type.SHOW));
  }

  @FXML
  protected void gotoConfig1(){
    gotoConfig(1);
  }

  @FXML
  protected void gotoConfig2(){
    gotoConfig(2);
  }

  @FXML
  protected void gotoConfig3(){
    gotoConfig(3);
  }

  @FXML
  protected void gotoConfig4(){
    gotoConfig(4);
  }

  @FXML
  protected void gotoConfig5(){
    gotoConfig(5);
  }

  /** Main goto step wizard */
  public void gotoConfig(int x) {
    if (getModel().getConfigStep() != x) {
      getController().saveSettings(getModel().getConfigStep());
      getModel().setConfigStep(x);
      getController().loadSettings(x);
    }
    if (x < 6) {
      showSubConfig(x);
      setStepsBlue(x);
      setConfigArrowTranslate(x);
      changeStepTitle(x);
      setContinueButton(x);
    } else {
      getController().saveConfig();
    }
    if (x == 1){
      NodeUtil.showNode(labelW1);
    } else {
      NodeUtil.hideNode(labelW1);
    }
    NodeUtil.showNode(backButton);
    NodeUtil.showNode(continueButton);
  }

  public void gotoEdit() {
    showSubConfig(6);
    setStepsBlue(2);
    setConfigArrowTranslate(2);
    changeStepTitle(2);
    NodeUtil.hideNode(labelW1);
    NodeUtil.hideNode(backButton);
    NodeUtil.hideNode(continueButton);
  }

  public void showSubConfig(int x) {
    if (x < 1 || x > 6) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Requested show sub config out of bounds!"));
      return;
    }

    for (Node node : includedNodes) {
      NodeUtil.hideNode(node);
    }

    Node current = includedNodes.get(x - 1);
    NodeUtil.showNode(current);
  }

  public void setStepsBlue(int x) {
    if (x < 1 || x > 5) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("stepOutOfBounds")));
      return;
    }

    for (int i = 1; i < 6; i++) {
      stepsButtons.get(i - 1).getStyleClass().remove("blue-but");
      stepsButtons.get(i - 1).getStyleClass().remove("grey-but");
      if (i <= x) {
        stepsButtons.get(i - 1).getStyleClass().add("blue-but");
      } else {
        stepsButtons.get(i - 1).getStyleClass().add("grey-but");
      }
    }
  }

  public void setConfigArrowTranslate(int x) {
    if (x == 1) {
      configArrow.setTranslateX(-35);
    } else {
      configArrow.setTranslateX(20 + (x - 2) * 55);
    }
  }

  public void changeStepTitle(int x){
    switch (x){
      case 1:
        stepTitle.setText(bundle.getString("configIC"));
        break;
      case 2:
        stepTitle.setText(bundle.getString("configPC"));
        break;
      case 3:
        stepTitle.setText(bundle.getString("configReport"));
        break;
      case 4:
        stepTitle.setText(bundle.getString("configMF"));
        break;
      case 5:
        stepTitle.setText(bundle.getString("configCS"));
        break;
    }

    Image img = new Image("file:src/main/resources/images/tab-buttons/tab-"+x+"-blue.png");
    stepImage.setImage(img);
  }

  public void setContinueButton(int x){
    if (x == 5) {
      NodeUtil.showNode(gridSave);
      continueButton.setText(bundle.getString("configSave"));
    } else {
      NodeUtil.hideNode(gridSave);
      continueButton.setText(bundle.getString("configContinue"));
    }
  }

  /* Elements Getters */

  public Button getStepButton(int x) {
    if (x < 1 || x > 5) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("stepOutOfBounds")));
      return null;
    }
    return stepsButtons.get(x - 1);
  }

  public String getSaveFilename(){
    return saveNameInput.getText();
  }

  public String getDescription(){
    return saveDescInput.getText();
  }

  @Override
  public Context getContext() {
    return context;
  }
}

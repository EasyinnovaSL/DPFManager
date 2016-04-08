package dpfmanager.shell.interfaces.gui.component.config;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard1Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard2Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard4Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard5Fragment;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard6Fragment;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.fragment.wizard.Wizard3Fragment;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import org.apache.logging.log4j.Level;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

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
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_CONFIG)
public class ConfigView extends DpfView<ConfigModel, ConfigController> {

  @Resource
  private Context context;

  @FXML
  private Button continueButton;
  @FXML
  private ImageView configArrow;
  @FXML
  private ImageView stepImage;
  @FXML
  private Label stepTitle;
  @FXML
  private VBox wizard;

  @FXML
  private Button step1, step2, step3, step4, step5, step6;

  private List<Node> includedNodes;
  private List<Button> stepsButtons;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message instanceof ConfigMessage) {
      ConfigMessage cMessage = (ConfigMessage) message;
      if (cMessage.isNew()) {
        getModel().initNewConfig();
      } else if (cMessage.isEdit()) {
        getModel().initEditConfig(cMessage.getPath());
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message instanceof ConfigMessage) {
      getController().clearAllSteps();
      gotoConfig(1);
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Set model and controller
    setModel(new ConfigModel());
    setController(new ConfigController());

    stepsButtons = Arrays.asList(step1, step2, step3, step4, step5 ,step6);

    initAllFragments();
  }

  private void initAllFragments() {
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
    while (!found && i < 7) {
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

  @FXML
  protected void gotoConfig6(){
    gotoConfig(6);
  }

  /** Main goto step wizard */
  public void gotoConfig(int x) {
    getController().saveSettings(getModel().getConfigStep());
    getModel().setConfigStep(x);
    getController().loadSettings(x);
    if (x < 7) {
      showSubConfig(x);
      setStepsBlue(x);
      setConfigArrowTranslate(x);
      changeStepTitle(x);
      setContinueButtonTest(x);
    } else {
      getController().saveConfig();
    }
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
    if (x < 1 || x > 6) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Requested step button out of bounds!"));
      return;
    }

    for (int i = 1; i < 7; i++) {
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
        stepTitle.setText("IMPLEMENTATION CHECKER");
        break;
      case 2:
        stepTitle.setText("POLICY CHECKER");
        break;
      case 3:
        stepTitle.setText("REPORT");
        break;
      case 4:
        stepTitle.setText("METADATA FIXER");
        break;
      case 5:
        stepTitle.setText("PERIODICAL CHECKS");
        break;
      case 6:
        stepTitle.setText("CONFIGURATION SUMMARY");
        break;
    }

    Image img = new Image("file:src/main/resources/images/tab-buttons/tab-"+x+"-blue.png");
    stepImage.setImage(img);
  }

  public void setContinueButtonTest(int x){
    if (x == 6) {
      continueButton.setText("Save configuration");
    } else {
      continueButton.setText("Continue");
    }
  }

  /* Elements Getters */

  public Button getStepButton(int x) {
    if (x < 1 || x > 6) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Requested step button out of bounds!"));
      return null;
    }
    return stepsButtons.get(x - 1);
  }

  @Override
  public Context getContext() {
    return context;
  }
}

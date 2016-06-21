package dpfmanager.shell.interfaces.gui.component.about;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.interfaces.gui.component.config.ConfigController;
import dpfmanager.shell.interfaces.gui.component.config.ConfigModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_ABOUT,
    name = GuiConfig.COMPONENT_ABOUT,
    viewLocation = "/fxml/about.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_ABOUT)
public class AboutView extends DpfSimpleView {

  @Resource
  private Context context;

  @FXML
  private Label lblVersion;
  @FXML
  private CheckBox chkFeedback;

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    lblVersion.setText(DPFManagerProperties.getVersion());
    chkFeedback.setSelected(DPFManagerProperties.getFeedback());
  }

  @FXML
  protected void changeFeedback(){
    DPFManagerProperties.setFeedback(chkFeedback.isSelected());
  }

  @Override
  public void sendMessage(String target, Object message) {
    context.send(target, message);
  }

}

package dpfmanager.shell.interfaces.gui.component.show;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 17/03/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_SHOW,
    name = GuiConfig.COMPONENT_SHOW,
    viewLocation = "/fxml/show.fxml",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_SHOW)
public class ShowView extends DpfView<ShowModel, ShowController> {

  @Resource
  private Context context;

  @FXML
  private TextArea textArea;
  @FXML
  private WebView webView;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(ShowMessage.class)) {
      ShowMessage sMessage = message.getTypedMessage(ShowMessage.class);
      System.out.println("Show the message");
      getController().showSingleReport(sMessage.getType(), sMessage.getPath());
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new ShowModel());
    setController(new ShowController());

    hideAll();
  }

  @Override
  public Context getContext() {
    return context;
  }

  /**
   * Show Hide
   */

  public void showTextArea(String content) {
    textArea.setText(content);
    NodeUtil.showNode(textArea);
    hideWebView();
    System.out.println("Show textarea");
  }

  public void hideTextArea() {
    NodeUtil.hideNode(textArea);
    textArea.clear();
  }

  public void showWebView(String path) {
    webView.getEngine().load("file:///" + path.replace("\\", "/"));
    NodeUtil.showNode(webView);
    hideTextArea();
  }

  public void hideWebView() {
    NodeUtil.hideNode(webView);
    webView.getEngine().load("");
  }

  public void hideAll() {
    hideTextArea();
    hideWebView();
  }

}

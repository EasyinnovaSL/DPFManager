package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.JacpExceptionMessage;
import javafx.scene.Node;

import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.handler.AErrorDialogHandler;

/**
 * Created by Adrià Llorens on 06/04/2016.
 */
public class CustomErrorHandler extends AErrorDialogHandler {

  private static Context context = null;

  public static void setContext(Context c){
    context = c;
  }

  @Override
  public Node createExceptionDialog(Throwable th) {
    if (context != null) {
      context.send(BasicConfig.MODULE_MESSAGE, new JacpExceptionMessage(th));
    }
    return null;
  }
}
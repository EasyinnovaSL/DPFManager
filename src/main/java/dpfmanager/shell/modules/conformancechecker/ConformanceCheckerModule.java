package dpfmanager.shell.modules.conformancechecker;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.core.ConformanceCheckerService;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_CONFORMANCE,
    name = BasicConfig.MODULE_CONFORMANCE,
    active = true)
public class ConformanceCheckerModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private ConformanceCheckerService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(ConformanceMessage.class)){
      ConformanceMessage cm = dpfMessage.getTypedMessage(ConformanceMessage.class);
      if (cm.isGui()) {
        String path = cm.getPath();
        String input = cm.getInput();
        if (!service.readConfig(path)) {
          getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, "Error reading configuration file"));
        } else {
          // Show loading
//          getContext().send(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.SHOW));
//          getContext().send(GuiConfig.PERSPECTIVE_DESSIGN, new LoadingMessage(LoadingMessage.Type.SHOW));

          service.setParameters(null, cm.getRecursive());
          service.startCheck(input);
        }
      }
    }
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    GuiContext guiContext = new GuiContext(context);
    ConformanceChecker.setLogger(new DpfLogger(guiContext, false));
    service.setContext(guiContext);
  }

  @Override
  public Context getContext(){
    return context;
  }

}

package dpfmanager.shell.interfaces.console;

import dpfmanager.shell.application.launcher.noui.CommandLauncher;
import dpfmanager.shell.core.config.ConsoleConfig;
import dpfmanager.shell.interfaces.console.commandline.CommandLineApp;
import javafx.application.Application.Parameters;
import javafx.application.Platform;
import javafx.event.Event;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.perspective.FXPerspective;
import org.jacpfx.rcp.registry.ComponentRegistry;
import org.jacpfx.rcp.registry.PerspectiveRegistry;
import org.jacpfx.rcp.util.ShutdownThreadsHandler;
import org.jacpfx.rcp.util.TearDownHandler;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 02/03/2016.
 */
@Perspective(id = ConsoleConfig.PRESPECTIVE,
    name = ConsoleConfig.PRESPECTIVE,
    components = {  }
)
public class ConsolePrespective implements FXPerspective {

  @Resource
  public Context context;

  private Parameters params;

  @Override
  public void handlePerspective(Message<Event, Object> message, PerspectiveLayout perspectiveLayout) {
    // Received messages
    if (message.getMessageBody().equals("DPF INIT")){
      initLogic();
    }
  }


  @PostConstruct
  public void onStartPerspective(PerspectiveLayout perspectiveLayout, FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Initial params
    params = CommandLauncher.parameters;
  }

  public void initLogic(){
    // Begin the logic
    CommandLineApp cl = new CommandLineApp(params);
    cl.launch();

    // End, close
    close();
  }

  public void close(){
    ShutdownThreadsHandler.shutdowAll();
    TearDownHandler.handleGlobalTearDown();
    ComponentRegistry.clearOnShutdown();
    PerspectiveRegistry.clearOnShutdown();
    Platform.exit();
  }

}

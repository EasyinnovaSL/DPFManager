package dpfmanager.shell.jacp.application;

import dpfmanager.shell.jacp.application.launcher.ui.GuiLauncher;
import dpfmanager.shell.jacp.core.workbench.GuiWorkbench;

import org.jacpfx.rcp.workbench.FXWorkbench;

/**
 * Created by Adrià Llorens on 01/03/2016.
 */
public abstract class GuiApplication extends GuiLauncher {

  @Override
  protected Class<? extends FXWorkbench> getWorkbenchClass() {
    return GuiWorkbench.class;
  }

  @Override
  protected String[] getBasePackages() {
    return new String[]{
        "dpfmanager.shell.jacp.modules",                    // Dpf Modules
        "dpfmanager.shell.jacp.interfaces.gui.component",   // UI Components
        "dpfmanager.shell.jacp.interfaces.gui.prespective"  // Prespectives
    };
  }

  @Override
  public String getXmlConfig() {
    return "DpfSpring.xml";
  }
}

package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.shell.core.config.GuiConfig;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_5,
    viewLocation = "/fxml-jr/config/subconfig5.fxml",
    scope = Scope.SINGLETON)
public class Wizard5Fragment {

  @Resource
  private Context context;

  public Wizard5Fragment() {

  }

  public void clear() {
  }

}

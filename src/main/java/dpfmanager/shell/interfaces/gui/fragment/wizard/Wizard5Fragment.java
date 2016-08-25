package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.GuiConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_5,
    viewLocation = "/fxml/config/subconfig5.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class Wizard5Fragment {

  @Resource
  private Context context;

  @FXML
  private Label labIsos, labRules, labReports, labFixes;

  public Wizard5Fragment() {

  }

  public void clear(){
    labIsos.setText("");
    labRules.setText("");
    labReports.setText("");
    labFixes.setText("");
  }

  public void loadSummary(Configuration config) {
    labIsos.setText(config.getTxtIsos());
    labReports.setText(config.getTxtFormats());
    labRules.setText(config.getTxtRules());
    labFixes.setText(config.getTxtFixes());
  }

}

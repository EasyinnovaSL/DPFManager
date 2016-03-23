package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.tiff.Configuration;
import dpfmanager.shell.core.config.GuiConfig;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_1,
    viewLocation = "/fxml/config/subconfig1.fxml",
    scope = Scope.SINGLETON)
public class Wizard1Fragment {

  @Resource
  private Context context;

  @FXML
  private CheckBox radProf1, radProf2, radProf3, radProf4, radProf5;

  public Wizard1Fragment() {
  }

  public void clear() {
    radProf1.setSelected(false);
    radProf2.setSelected(false);
    radProf3.setSelected(false);
    radProf4.setSelected(false);
    radProf5.setSelected(false);
  }

  public void saveIsos(Configuration config) {
    config.getIsos().clear();
    if (radProf1.isSelected()) config.addISO("Baseline");
    if (radProf2.isSelected()) config.addISO("Tiff/EP");
    if (radProf3.isSelected()) config.addISO("Tiff/IT");
    if (radProf4.isSelected()) config.addISO("Tiff/IT-1");
    if (radProf5.isSelected()) config.addISO("Tiff/IT-2");
  }

  public void loadIsos(Configuration config) {
    radProf1.setSelected(config.getIsos().contains("Baseline"));
    radProf2.setSelected(config.getIsos().contains("Tiff/EP"));
    radProf3.setSelected(config.getIsos().contains("Tiff/IT"));
    radProf4.setSelected(config.getIsos().contains("Tiff/IT-1"));
    radProf5.setSelected(config.getIsos().contains("Tiff/IT-2"));
  }
}

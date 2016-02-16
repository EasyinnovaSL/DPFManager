package dpfmanager.shell.interfaces.Gui.ui.config.sub;

import dpfmanager.shell.interfaces.Gui.ui.config.ConfigController;
import dpfmanager.shell.interfaces.Gui.ui.config.ConfigModel;
import dpfmanager.shell.interfaces.Gui.ui.config.ConfigView;
import dpfmanager.shell.conformancechecker.Configuration;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.jrebirth.af.core.ui.fxml.AbstractFXMLController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 11/02/2016.
 */
public class SubConfig6Controller extends AbstractFXMLController<ConfigModel, ConfigView> {

  @FXML
  private Label labIsos;

  @FXML
  private Label labRules;

  @FXML
  private Label labReports;

  @FXML
  private Label labFixes;

  @Override
  public void initialize(final URL url, final ResourceBundle resource) {
    ConfigController.setSubController6(this);
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


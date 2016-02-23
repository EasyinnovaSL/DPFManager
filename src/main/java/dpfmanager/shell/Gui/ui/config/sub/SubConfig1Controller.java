package dpfmanager.shell.gui.ui.config.sub;

import dpfmanager.shell.gui.ui.config.ConfigController;
import dpfmanager.shell.gui.ui.config.ConfigModel;
import dpfmanager.shell.gui.ui.config.ConfigView;
import dpfmanager.conformancechecker.tiff.Configuration;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import org.jrebirth.af.core.ui.fxml.AbstractFXMLController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 11/02/2016.
 */
public class SubConfig1Controller extends AbstractFXMLController<ConfigModel, ConfigView> {

  @FXML
  private CheckBox radProf1;

  @FXML
  private CheckBox radProf2;

  @FXML
  private CheckBox radProf3;

  @FXML
  private CheckBox radProf4;

  @FXML
  private CheckBox radProf5;

  @Override
  public void initialize(final URL url, final ResourceBundle resource) {
    ConfigController.setSubController1(this);
  }

  public void clear(){
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


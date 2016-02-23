package dpfmanager.shell.core.command;

import javafx.scene.control.Alert;

import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.WaveBean;
import org.jrebirth.af.core.command.single.ui.DefaultUIBeanCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Adrià Llorens on 03/02/2016.
 */
public final class ErrorCommand extends DefaultUIBeanCommand<WaveBean> {

  /** The class logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCommand.class);

  private String message = "";

  @Override
  public void initCommand() {
    // You must put your initialization code here (if any)
  }

  @Override
  protected void perform(final Wave wave) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("An error occured");
    alert.setContentText(message);
    alert.initOwner(getLocalFacade().getGlobalFacade().getApplication().getStage());
    alert.showAndWait();
  }

  public void setMessage(String message){
    this.message = message;
  }

}

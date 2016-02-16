package dpfmanager.shell.interfaces.Gui.command;

import javafx.stage.DirectoryChooser;

import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.WaveBean;
import org.jrebirth.af.core.command.single.ui.DefaultUIBeanCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Adrià Llorens on 10/02/2016.
 */
public class OpenDialogCommand extends DefaultUIBeanCommand<WaveBean> {

  /** The class logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(OpenDialogCommand.class);

  public enum Type {FILE, FOLDER};

  private Type type;
  private String output;

  @Override
  public void initCommand() {
    // You must put your initialization code here (if any)
  }

  @Override
  protected void perform(final Wave wave) {
    output = "";
    if (type == Type.FILE){

    }
    else if (type == Type.FOLDER){
      DirectoryChooser folderChooser = new DirectoryChooser();
      folderChooser.setTitle("Select Output Folder");
      //folderChooser.setInitialDirectory(new File(getDefaultBrowseDirectory()));
      File directory = folderChooser.showDialog(getLocalFacade().getGlobalFacade().getApplication().getStage());
      if (directory != null) {
        output = directory.getPath();
        //setDefaultBrowseDirectory(directory.getPath());
      }
      //gui.setSelectedFile(txtFile.getText());
    }
  }

  public void setType(Type type){
    this.type = type;
  }

  public String getOutput(){
    return output;
  }

}

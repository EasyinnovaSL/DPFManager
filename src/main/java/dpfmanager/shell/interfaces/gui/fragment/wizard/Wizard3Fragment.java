package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.tiff.Configuration;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.io.File;

/**
 * Created by Adrià Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_3,
    viewLocation = "/fxml-jr/config/subconfig3.fxml",
    scope = Scope.SINGLETON)
public class Wizard3Fragment {

  @Resource
  private Context context;

  @FXML
  private CheckBox chkDefaultOutput;

  @FXML
  private HBox hboxOutput;

  @FXML
  private TextField txtOutput;

  @FXML
  private Button butOutput;

  @FXML
  private CheckBox chkHtml, chkXml, chkJson, chkPdf;

  public Wizard3Fragment() {
  }

  public void clear() {
    txtOutput.clear();
    chkDefaultOutput.setSelected(true);
    chkHtml.setSelected(false);
    chkXml.setSelected(false);
    chkJson.setSelected(false);
    chkPdf.setSelected(false);
  }

  public void saveFormats(Configuration config) {
    config.getFormats().clear();
    if (chkHtml.isSelected()) config.addFormat("HTML");
    if (chkXml.isSelected()) config.addFormat("XML");
    if (chkJson.isSelected()) config.addFormat("JSON");
    if (chkPdf.isSelected()) config.addFormat("PDF");
  }

  public void saveOutput(Configuration config) {
    if (!chkDefaultOutput.isSelected()) {
      config.setOutput(txtOutput.getText());
    } else {
      config.setOutput(null);
    }
  }

  public void loadFormats(Configuration config) {
    chkHtml.setSelected(config.getFormats().contains("HTML"));
    chkXml.setSelected(config.getFormats().contains("XML"));
    chkJson.setSelected(config.getFormats().contains("JSON"));
    chkPdf.setSelected(config.getFormats().contains("PDF"));
  }

  public void loadOutput(Configuration config) {
    if (config.getOutput() != null) {
      chkDefaultOutput.setSelected(false);
      txtOutput.setText(config.getOutput());
      hboxOutput.setVisible(true);
      hboxOutput.setManaged(true);
    } else {
      chkDefaultOutput.setSelected(true);
    }
  }

  @FXML
  protected void changeDefault(ActionEvent event) throws Exception {
    if (chkDefaultOutput.isSelected()) {
      hboxOutput.setVisible(false);
      hboxOutput.setManaged(false);
      txtOutput.clear();
    } else {
      hboxOutput.setVisible(true);
      hboxOutput.setManaged(true);
    }
  }

  @FXML
  public void browseOutput(ActionEvent event) {
    DirectoryChooser folderChooser = new DirectoryChooser();
    folderChooser.setTitle("Select Output Folder");
    //folderChooser.setInitialDirectory(new File(getDefaultBrowseDirectory()));
    File directory = folderChooser.showDialog(GuiWorkbench.getMyStage());
    if (directory != null) {
      txtOutput.setText(directory.getPath());
      //setDefaultBrowseDirectory(directory.getPath());
    }
    //gui.setSelectedFile(txtFile.getText());
  }
}

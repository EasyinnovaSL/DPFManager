/**
 * <h1>Wizard3Fragment.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.fragment.wizard;

import dpfmanager.conformancechecker.configuration.Configuration;
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
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 08/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_WIZARD_3,
    viewLocation = "/fxml/config/subconfig3.fxml",
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class Wizard3Fragment {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

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
    folderChooser.setTitle(bundle.getString("w3SelectOutput"));
    //folderChooser.setInitialDirectory(new File(getDefaultBrowseDirectory()));
    File directory = folderChooser.showDialog(GuiWorkbench.getMyStage());
    if (directory != null) {
      txtOutput.setText(directory.getPath());
      //setDefaultBrowseDirectory(directory.getPath());
    }
    //gui.setSelectedFile(txtFile.getText());
  }
}

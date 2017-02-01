/**
 * <h1>FirstView.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.first;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_FIRST,
    name = GuiConfig.COMPONENT_FIRST,
    viewLocation = "/fxml/first.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_FIRST)
public class FirstView extends DpfSimpleView {

  @Resource
  private Context context;
  @Resource
  private ResourceBundle bundle;

  @FXML
  private TextField txtEmail, txtName, txtSurname, txtJob, txtOrganization, txtCountry;
  @FXML
  private TextArea txtWhy;
  @FXML
  private CheckBox chkSubmit, chkFeedback;

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    if (!GuiWorkbench.getFirstTime()){
      context.send(GuiConfig.PERSPECTIVE_DESSIGN, new UiMessage());
    }
  }

  @FXML
  protected void proceed() {
    try {
      DPFManagerProperties.setFeedback(chkFeedback.isSelected());
      if (chkSubmit.isSelected()) {
        boolean ok = true;
        if (txtName.getText().length() == 0) ok = false;
        if (txtSurname.getText().length() == 0) ok = false;
        if (txtEmail.getText().length() == 0) ok = false;
        if (txtJob.getText().length() == 0) ok = false;
        if (txtOrganization.getText().length() == 0) ok = false;
        if (txtCountry.getText().length() == 0) ok = false;
        if (txtWhy.getText().length() == 0) ok = false;
        if (!ok) {
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("missingData"), bundle.getString("fillData")));
          return;
        }
        if (txtEmail.getText().indexOf("@") < 0 || txtEmail.getText().indexOf("@") > txtEmail.getText().lastIndexOf(".")) {
          ok = false;
          context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, bundle.getString("incorrectEmail"), bundle.getString("rewriteEmail")));
        }
        if (ok) {
          String url = "http://dpfmanager.org/form.php";
          URL obj = new URL(url);
          java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

          //add request header
          con.setRequestMethod("POST");
          con.setRequestProperty("User-Agent", "Mozilla/5.0");
          con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

          String urlParameters = "email=" + txtEmail.getText();
          urlParameters += "&name=" + txtName.getText();
          urlParameters += "&surname=" + txtSurname.getText();
          urlParameters += "&jobrole=" + txtJob.getText();
          urlParameters += "&organization=" + txtOrganization.getText();
          urlParameters += "&country=" + txtCountry.getText();
          urlParameters += "&comments=" + txtWhy.getText();
          urlParameters += "&formtype=DPFManagerApp";

          // Send post request
          con.setDoOutput(true);
          DataOutputStream wr = new DataOutputStream(con.getOutputStream());
          wr.writeBytes(urlParameters);
          wr.flush();
          wr.close();

          boolean getok = false;
          int responseCode = con.getResponseCode();
          if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
              if (inputLine.equals("OK")) getok = true;
            }
            in.close();
          }
          if (getok) {
            DPFManagerProperties.setFirstTime(false);
          } else {
            context.send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, bundle.getString("errorOccurred"), bundle.getString("tryAgain")));
            DPFManagerProperties.setFirstTime(true);
          }
        }
      } else {
        DPFManagerProperties.setFirstTime(false);
      }

      // Show main
      context.send(GuiConfig.PERSPECTIVE_DESSIGN, new UiMessage());
    } catch (Exception ex) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("exception"), ex));
    }
  }

  @Override
  public void sendMessage(String target, Object message) {
    context.send(target, message);
  }

}

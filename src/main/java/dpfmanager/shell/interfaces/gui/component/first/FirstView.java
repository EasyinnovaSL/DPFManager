package dpfmanager.shell.interfaces.gui.component.first;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
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
 * Created by Adrià Llorens on 25/02/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_FIRST,
    name = GuiConfig.COMPONENT_FIRST,
    viewLocation = "/fxml-jr/first.fxml",
    active = true,
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_FIRST)
public class FirstView implements FXComponent {

  @Resource
  private Context context;

  @FXML
  private TextField txtEmail, txtName, txtSurname, txtJob, txtOrganization, txtCountry;
  @FXML
  private TextArea txtWhy;
  @FXML
  private CheckBox chkSubmit, chkFeedback;

  @Override
  public Node handle(final Message<Event, Object> message) {
    return null;
  }

  @Override
  public Node postHandle(Node node, Message<Event, Object> message) {
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    if (!GuiWorkbench.getFirstTime()){
      context.send(GuiConfig.PRESPECTIVE_DESSIGN, new UiMessage(UiMessage.Type.SHOW));
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
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Missing data");
          alert.setContentText("Please fill in all the fields");
          alert.showAndWait();
          return;
        }
        if (txtEmail.getText().indexOf("@") < 0 || txtEmail.getText().indexOf("@") > txtEmail.getText().lastIndexOf(".")) {
          ok = false;
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Incorrect email");
          alert.setContentText("Please write your email correctly");
          alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error ocurred");
            alert.setContentText("Sorry, we could not proceed your request. Try again the next time you run DPFmanager");
            alert.showAndWait();

            DPFManagerProperties.setFirstTime(true);
          }
        }
      } else {
        DPFManagerProperties.setFirstTime(false);
      }

      // Show main
      context.send(GuiConfig.PRESPECTIVE_DESSIGN, new UiMessage(UiMessage.Type.SHOW));
    } catch (Exception ex) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("An error occured");
      alert.setContentText(ex.toString());
      alert.showAndWait();
    }
  }

}

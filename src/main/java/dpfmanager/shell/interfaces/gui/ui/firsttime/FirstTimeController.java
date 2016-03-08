package dpfmanager.shell.interfaces.gui.ui.firsttime;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.gui.ui.main.MainModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
public class FirstTimeController extends DefaultController<FirstTimeModel, FirstTimeView> {

  public FirstTimeController(final FirstTimeView view) throws CoreException {
    super(view);
  }

  @Override
  protected void initEventHandlers() throws CoreException {
    // Proceed Button
    getView().getProceedButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          DPFManagerProperties.setFeedback(getView().getChkFeedback().isSelected());
          if (getView().getChkSubmit().isSelected()) {
            boolean ok = true;
            if (getView().getTxtName().getText().length() == 0) ok = false;
            if (getView().getTxtSurname().getText().length() == 0) ok = false;
            if (getView().getTxtEmail().getText().length() == 0) ok = false;
            if (getView().getTxtJob().getText().length() == 0) ok = false;
            if (getView().getTxtOrganization().getText().length() == 0) ok = false;
            if (getView().getTxtCountry().getText().length() == 0) ok = false;
            if (getView().getTxtWhy().getText().length() == 0) ok = false;
            if (!ok) {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Error");
              alert.setHeaderText("Missing data");
              alert.setContentText("Please fill in all the fields");
              alert.showAndWait();
              return;
            }
            if (getView().getTxtEmail().getText().indexOf("@") < 0 || getView().getTxtEmail().getText().indexOf("@") > getView().getTxtEmail().getText().lastIndexOf(".")) {
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

              String urlParameters = "email=" + getView().getTxtEmail().getText();
              urlParameters += "&name=" + getView().getTxtName().getText();
              urlParameters += "&surname=" + getView().getTxtSurname().getText();
              urlParameters += "&jobrole=" + getView().getTxtJob().getText();
              urlParameters += "&organization=" + getView().getTxtOrganization().getText();
              urlParameters += "&country=" + getView().getTxtCountry().getText();
              urlParameters += "&comments=" + getView().getTxtWhy().getText();
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
          getModel().getModel(MainModel.class).enableFlowPane();
          getModel().getModel(MainModel.class).showDessign();
        } catch (Exception ex) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("An error occured");
          alert.setContentText(ex.toString());
          alert.showAndWait();
        }
      }
    });
  }

}
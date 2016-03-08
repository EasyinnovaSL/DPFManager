package dpfmanager.shell.interfaces.gui.ui.about;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.gui.ui.main.MainModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import org.jrebirth.af.api.resource.fxml.FXMLItem;
import org.jrebirth.af.core.resource.Resources;
import org.jrebirth.af.core.resource.fxml.FXML;
import org.jrebirth.af.core.ui.DefaultView;

/**
 * Created by Adrià Llorens on 02/02/2016.
 */
public class AboutView extends DefaultView<AboutModel, ScrollPane, AboutController> {

  private static FXMLItem ROOT_EMBEDDED_FXML = Resources.create(new FXML("fxml-jr", "about"));

  private ScrollPane scrollPane;

  public AboutView(final AboutModel model) {
    super(model);
  }

  @Override
  public void start() {
    try {
      GridPane grid = new GridPane();
      grid.getStyleClass().add("background-main");
      grid.setAlignment(Pos.TOP_CENTER);
      grid.getChildren().add(getFXMLNode());

      scrollPane = getRootNode();
      scrollPane.setFitToHeight(true);
      scrollPane.setFitToWidth(true);
      scrollPane.setMaxWidth(970);
      scrollPane.getStyleClass().add("background-main");
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setContent(grid);
    } catch (Exception ex) {
      ex.printStackTrace();
      getModel().getModel(MainModel.class).getErrorCommand().setMessage(ex.toString());
      getModel().getModel(MainModel.class).getErrorCommand().run();
    }
  }

  private Node getFXMLNode() {
    Node rootNode = ROOT_EMBEDDED_FXML.get().getNode();
    Label label = (Label) rootNode.lookup("#lblVersion");
    label.setText("version " + DPFManagerProperties.getVersion());
    return rootNode;
  }

}
package dpfmanager.jrebirth.ui.main;

import dpfmanager.jrebirth.ui.about.AboutModel;
import dpfmanager.jrebirth.ui.dessign.DessignModel;
import dpfmanager.jrebirth.ui.report.ReportsModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.component.ui.stack.StackModel;
import org.jrebirth.af.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public final class MainView extends DefaultView<MainModel, BorderPane, MainController> {

  /**
   * The class logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);

  private ToggleButton showDessign;
  private ToggleButton showReports;
  private ToggleButton showAbout;
  private ToggleButton showConfig;
  private ToggleButton showFirstTime;

  private Rectangle rectConformance;
  private Rectangle rectReports;
  private Rectangle rectAbout;

  private FlowPane flowPane;
  private StackPane stackPane;


  /**
   * Default Constructor.
   *
   * @param model the controls view model
   * @throws CoreException if build fails
   */
  public MainView(final MainModel model) throws CoreException {
    super(model);
    stackPane = (StackPane) model.getModel(StackModel.class, MainPage.class).getRootNode();
    stackPane.getStyleClass().add("background-main");
    stackPane.setId("mainPane");
    getRootNode().setCenter(stackPane);
    getRootNode().getStyleClass().add("background-main");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void initView() {
    showDessign = new ToggleButton("Conformance Checker");
    showDessign.getStyleClass().add("top-button");
    showDessign.setId("butDessign");

    showReports = new ToggleButton("Reports");
    showReports.getStyleClass().add("top-button");
    showReports.getStyleClass().add("top-button-center");
    showReports.setId("butReports");

    showAbout = new ToggleButton("About");
    showAbout.getStyleClass().add("top-button");
    showAbout.setId("butAbout");

    // Hidden button
    showConfig = new ToggleButton("");
    showConfig.setId("butConfig");
    showConfig.setVisible(false);
    showConfig.setManaged(false);
    showFirstTime = new ToggleButton("");
    showFirstTime.setId("butFirst");
    showFirstTime.setVisible(false);
    showFirstTime.setManaged(false);

    final ToggleGroup group = new PersistentButtonToggleGroup();
    group.getToggles().addAll(showDessign, showReports, showAbout, showConfig, showFirstTime);

    flowPane = new FlowPane();
    flowPane.getChildren().addAll(showDessign, createSeparator(), showReports, createSeparator(), showAbout, createTriangle(1), createTriangle(2), createTriangle(3), showConfig, showFirstTime);
    flowPane.getStyleClass().add("top-pane");
    flowPane.getStyleClass().add("background-main");
    flowPane.setAlignment(Pos.TOP_CENTER);

    getRootNode().setTop(flowPane);
  }

  private StackPane createSeparator() {
    StackPane pane = new StackPane();
    pane.setMaxWidth(2);
    pane.setMinWidth(2);
    pane.setMaxHeight(13);
    pane.setMinHeight(13);
    pane.getStyleClass().add("top-separator");
    return pane;
  }

  private Rectangle createTriangle(int id) {
    Rectangle rect = new Rectangle();
    rect.setWidth(20);
    rect.setHeight(20);
    rect.getTransforms().add(new Rotate(45, 0, 0));
    rect.setTranslateY(18);
    rect.setFill(Color.BLACK);
    switch (id) {
      case 1:
        rectConformance = rect;
        rect.setTranslateX(-215);
        rect.setVisible(true);
        break;
      case 2:
        rectReports = rect;
        rect.setTranslateX(-120);
        rect.setVisible(false);
        break;
      case 3:
        rectAbout = rect;
        rect.setTranslateX(-70);
        rect.setVisible(false);
        break;
      default:
    }
    return rect;
  }

  @Override
  public void start() {
    if (getModel().getFirstTime()) {
      flowPane.setVisible(false);
      flowPane.setManaged(false);
      showFirstTime.fire();
    } else {
      showDessign.fire();
    }
  }

  @Override
  public void reload() {
    super.reload();
    // Custom code to process when the view is displayed the 1+n time
  }

  @Override
  public void hide() {
    super.hide();
    // Custom code to process when the view is hidden
  }

  public void enableFlowPane(){
    flowPane.setVisible(true);
    flowPane.setManaged(true);
  }

  public void showLoading() {
//    stackPane.getStyleClass().add("background-loading");
//    stackPane.getStyleClass().remove("background-main");
    flowPane.setDisable(true);
  }

  public void hideLoading() {
//    stackPane.getStyleClass().remove("background-loading");
//    stackPane.getStyleClass().add("background-main");
    flowPane.setDisable(false);
  }

  public ToggleButton getShowDessign() {
    return this.showDessign;
  }

  public ToggleButton getShowReports() {
    return showReports;
  }

  public ToggleButton getShowAbout() {
    return showAbout;
  }

  public ToggleButton getShowConfig() {
    return showConfig;
  }

  public ToggleButton getShowFirstTime() {
    return showFirstTime;
  }

  public StackPane getStackPane() {
    return stackPane;
  }

  private class PersistentButtonToggleGroup extends ToggleGroup {

    public PersistentButtonToggleGroup() {
      super();
      getToggles().addListener(new ListChangeListener<Toggle>() {
        @Override
        public void onChanged(final Change<? extends Toggle> c) {
          while (c.next()) {
            for (final Toggle addedToggle : c.getAddedSubList()) {
              ((ToggleButton) addedToggle).addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                  showHideTriangles(((ToggleButton) addedToggle).getId());
                  if (addedToggle.equals(getSelectedToggle())) {
                    mouseEvent.consume();
                    performReload(((ToggleButton) addedToggle).getId());
                  }
                }

                private void showHideTriangles(String id) {
                  switch (id) {
                    case "butDessign":
                      rectConformance.setVisible(true);
                      rectReports.setVisible(false);
                      rectAbout.setVisible(false);
                      break;
                    case "butReports":
                      rectConformance.setVisible(false);
                      rectReports.setVisible(true);
                      rectAbout.setVisible(false);
                      break;
                    case "butAbout":
                      rectConformance.setVisible(false);
                      rectReports.setVisible(false);
                      rectAbout.setVisible(true);
                      break;
                  }
                }

                private void performReload(String id) {
                  switch (id) {
                    case "butDessign":
                      getModel().getModel(DessignModel.class).getView().reload();
                      break;
                    case "butReports":
                      getModel().getModel(ReportsModel.class).getView().reload();
                      break;
                    case "butAbout":
                      getModel().getModel(AboutModel.class).getView().reload();
                      break;
                  }
                }
              });
            }
          }
        }
      });
    }
  }

}

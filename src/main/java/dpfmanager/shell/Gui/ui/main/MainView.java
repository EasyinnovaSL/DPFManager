package dpfmanager.shell.gui.ui.main;

import dpfmanager.shell.gui.ui.about.AboutModel;
import dpfmanager.shell.gui.ui.bottom.BottomModel;
import dpfmanager.shell.gui.ui.dessign.DessignModel;
import dpfmanager.shell.gui.ui.report.ReportsModel;
import dpfmanager.shell.gui.ui.stack.StackModel;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultView;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public final class MainView extends DefaultView<MainModel, BorderPane, MainController> {

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
  private AnchorPane mainPane;
  private SplitPane mainSplit;
  private Button showBottom;

  private Node divider;

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
    stackPane.setMinHeight(150);
    getRootNode().getStyleClass().add("background-main");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void initView() {
    /* Bottom Pane */

    // Main Split
    mainSplit = new SplitPane();
    mainSplit.setId("mainSplit");
    mainSplit.setOrientation(Orientation.VERTICAL);
    mainSplit.setDividerPositions(0.8);
    mainSplit.getStyleClass().add("background-main");

    mainPane = new AnchorPane();
    mainPane.setId("mainPane");
    mainPane.getStyleClass().add("background-main");
    mainPane.getChildren().add(mainSplit);
    AnchorPane.setTopAnchor(mainSplit, 0.0);
    AnchorPane.setBottomAnchor(mainSplit, 0.0);
    AnchorPane.setLeftAnchor(mainSplit, 0.0);
    AnchorPane.setRightAnchor(mainSplit, 0.0);

    mainSplit.getItems().add(stackPane);
    mainSplit.getItems().add(getModel().getModel(BottomModel.class).getRootNode());
    getRootNode().setCenter(mainPane);

    // Show bottom button
    showBottom = new Button();
    showBottom.setText("Show");
    showBottom.getStyleClass().addAll("bot-button");
    showBottom.setVisible(false);
    showBottom.setManaged(false);
    showBottom.setPadding(new Insets(4, 5, 4, 5));

    mainPane.getChildren().add(showBottom);
    AnchorPane.setBottomAnchor(showBottom, 0.0);
    AnchorPane.setRightAnchor(showBottom, 0.0);

    // Hide Bottom pane
    getModel().getModel(BottomModel.class).getController().hideBottomPane();

    /* Top Buttons */

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

    // Hidden Buttons
    showConfig = new ToggleButton("");
    showConfig.setId("butConfig");
    showConfig.setVisible(false);
    showConfig.setManaged(false);

    showFirstTime = new ToggleButton("");
    showFirstTime.setId("butFirst");
    showFirstTime.setVisible(false);
    showFirstTime.setManaged(false);

    ToggleGroup group = new PersistentButtonToggleGroup();
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
      getModel().createDefaultConfigurationFiles();
      flowPane.setVisible(false);
      flowPane.setManaged(false);
      showFirstTime.fire();
    } else {
      showDessign.fire();
    }
  }

  private Node getDivider(){
    if (divider == null){
      divider = mainPane.lookup("#mainSplit > .split-pane-divider");
    }
    return divider;
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

  public void hideBottomPane(){
    // Hide divider
    double pos = mainSplit.getDividerPositions()[0];
    getModel().setDividerPositionV(pos);
    if (getDivider() != null){
      if (getDivider().getStyleClass().contains("show-divider")) {
        getDivider().getStyleClass().remove("show-divider");
      }
      getDivider().setVisible(false);
      getDivider().setManaged(false);
    }

    // Show button
    showBottom.setVisible(true);
    showBottom.setManaged(true);
  }

  public void showBottomPane(){
    // Show divider
    double pos = getModel().getDividerPositionV();
    mainSplit.setDividerPositions(pos);
    if (getDivider() != null){
      if (!getDivider().getStyleClass().contains("show-divider")) {
        getDivider().getStyleClass().add("show-divider");
      }
      getDivider().setVisible(true);
      getDivider().setManaged(true);
    }

    // Hide button
    showBottom.setVisible(false);
    showBottom.setManaged(false);
  }

  /* Getters */

  public Button getShowBottom() {
    return showBottom;
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

package dpfmanager.shell.interfaces.gui.fragment;

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.UiMessage;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.util.LayoutUtil;

import java.awt.peer.ButtonPeer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
@Fragment(id = GuiConfig.FRAGMENT_TOP,
    resourceBundleLocation = "bundles.language",
    scope = Scope.SINGLETON)
public class TopFragment extends FlowPane {

  @Resource
  private Context context;
  private ResourceBundle bundle;

  private ToggleButton showDessign;
  private ToggleButton showReports;
  private ToggleButton showPeriodical;
  private ToggleButton showAbout;
  private ToggleGroup group;
  private List<ToggleButton> topButtons;

  private Rectangle rectConformance;
  private Rectangle rectReports;
  private Rectangle rectAbout;

  private String ButDessign = "butDessign";
  private String ButReports = "butReports";
  private String ButPeriodical = "butPeriodical";
  private String ButAbout = "butAbout";

  private String currentId;

  public TopFragment() {
    topButtons = new ArrayList<>();
    currentId = "";

    // Show buttons
    showDessign = new ToggleButton("Conformance Checker");
    showDessign.getStyleClass().addAll("top-button", "active");
    showDessign.setId(ButDessign);
    topButtons.add(showDessign);

    showReports = new ToggleButton("reports");
    showReports.getStyleClass().add("top-button");
    showReports.getStyleClass().add("top-button-center");
    showReports.setId(ButReports);
    topButtons.add(showReports);

    showPeriodical = new ToggleButton("periodical");
    showPeriodical.getStyleClass().add("top-button");
    showPeriodical.getStyleClass().add("top-button-center");
    showPeriodical.setId(ButPeriodical);
    topButtons.add(showPeriodical);

    showAbout = new ToggleButton("about");
    showAbout.getStyleClass().add("top-button");
    showAbout.setId(ButAbout);
    topButtons.add(showAbout);

    group = new PersistentButtonToggleGroup();
    group.getToggles().addAll(showDessign, showReports, showPeriodical, showAbout);

    this.getChildren().addAll(showDessign, createSeparator(), showReports, createSeparator(), showPeriodical, createSeparator(), showAbout);
    this.getStyleClass().add("top-pane");
    this.getStyleClass().add("background-main");
    this.setAlignment(Pos.TOP_CENTER);
  }

  public void setBundle(ResourceBundle bundle) {
    showDessign.setText(bundle.getString("conformanceChecker"));
    showReports.setText(bundle.getString("reports"));
    showPeriodical.setText(bundle.getString("periodical"));
    showAbout.setText(bundle.getString("about"));
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

  // Decide witch toggle will be blue by perspective id
  public void setCurrentToggle(String id) {
    currentId = id;
    String finalId = "";
    if (id.equals(GuiConfig.PERSPECTIVE_DESSIGN) || id.equals(GuiConfig.PERSPECTIVE_CONFIG)) {
      finalId = ButDessign;
    } else if (id.equals(GuiConfig.PERSPECTIVE_REPORTS) || id.equals(GuiConfig.PERSPECTIVE_SHOW)) {
      finalId = ButReports;
    } else if (id.equals(GuiConfig.PERSPECTIVE_ABOUT)) {
      finalId = ButAbout;
    } else if (id.equals(GuiConfig.PERSPECTIVE_PERIODICAL)) {
      finalId = ButPeriodical;
    }
    makeBlue(getToggleById(finalId));
  }

  public String getCurrentId() {
    return currentId;
  }

  private ToggleButton getToggleById(String id) {
    for (ToggleButton but : topButtons) {
      if (but.getId().equals(id)) {
        return but;
      }
    }
    return null;
  }

  private class PersistentButtonToggleGroup extends ToggleGroup {

    public PersistentButtonToggleGroup() {
      super();
      getToggles().addListener(new ListChangeListener<Toggle>() {
        @Override
        public void onChanged(final Change<? extends Toggle> c) {
          while (c.next()) {
            for (final Toggle addedToggle : c.getAddedSubList()) {
              ToggleButton button = ((ToggleButton) addedToggle);
              button.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                  ToggleButton source = getActual();
                  ToggleButton target = button;
                  if (source.equals(target) && samePerspective(target.getId())) {
                    doReload(target.getId());
                  } else {
//                    showHideTriangles(target.getId());
                    doShow(target.getId());
                  }
                  mouseEvent.consume();
                }

                private boolean samePerspective(String id){
                  if (id.equals(ButReports)){
                    return currentId.equals(GuiConfig.PERSPECTIVE_REPORTS);
                  } else if (id.equals(ButDessign)){
                    return currentId.equals(GuiConfig.PERSPECTIVE_DESSIGN);
                  }
                  return false;
                }

                private ToggleButton getActual() {
                  for (ToggleButton but : topButtons) {
                    if (but.getStyleClass().contains("active")) {
                      return but;
                    }
                  }
                  return null;
                }

                private void doShow(String id) {
                  switch (id) {
                    case "butDessign":
                      context.send(GuiConfig.PERSPECTIVE_DESSIGN, new UiMessage(UiMessage.Type.SHOW));
                      break;
                    case "butReports":
                      context.send(GuiConfig.PERSPECTIVE_REPORTS, new UiMessage(UiMessage.Type.SHOW));
                      break;
                    case "butAbout":
                      context.send(GuiConfig.PERSPECTIVE_ABOUT, new UiMessage(UiMessage.Type.SHOW));
                      break;
                    case "butPeriodical":
                      context.send(GuiConfig.PERSPECTIVE_PERIODICAL, new UiMessage(UiMessage.Type.SHOW));
                      break;
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

                private void doReload(String id) {
                  switch (id) {
                    case "butDessign":
                      context.send(GuiConfig.PERSPECTIVE_DESSIGN, new UiMessage(UiMessage.Type.RELOAD));
                      break;
                    case "butReports":
                      context.send(GuiConfig.PERSPECTIVE_REPORTS, new UiMessage(UiMessage.Type.RELOAD));
                      break;
                    case "butAbout":
                      context.send(GuiConfig.PERSPECTIVE_ABOUT, new UiMessage(UiMessage.Type.RELOAD));
                      break;
                    case "butPeriodical":
                      context.send(GuiConfig.PERSPECTIVE_PERIODICAL, new UiMessage(UiMessage.Type.RELOAD));
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

  public void makeBlue(ToggleButton selected) {
    // others
    for (ToggleButton butotn : topButtons) {
      if (butotn.getStyleClass().contains("active")) {
        butotn.getStyleClass().remove("active");
      }
    }

    // selected
    if (!selected.getStyleClass().contains("active")) {
      selected.getStyleClass().add("active");
    }
  }

}

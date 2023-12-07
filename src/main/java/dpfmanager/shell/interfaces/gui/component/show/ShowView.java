/**
 * <h1>ShowView.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.show;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.NavMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.interfaces.gui.fragment.TopFragment;
import dpfmanager.shell.modules.report.messages.GenerateIndividualMessage;
import dpfmanager.shell.modules.report.messages.GenerateMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 17/03/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_SHOW,
    name = GuiConfig.COMPONENT_SHOW,
    viewLocation = "/fxml/show.fxml",
    active = true,
    resourceBundleLocation = "bundles.language",
    initialTargetLayoutId = GuiConfig.TARGET_CONTAINER_SHOW)
public class ShowView extends DpfView<ShowModel, ShowController> {

  @Resource
  private Context context;

  @FXML
  private TextArea textArea;
  @FXML
  private WebView webView;
  @FXML
  private ComboBox comboIndividuals;
  private String folderPath, extension;

  @FXML
  private ScrollPane scrollPdfPages;
  @FXML
  private VBox pdfPagesVBox;
  @FXML
  private VBox showVBox;

  @FXML
  private Label labelLoading;
  @FXML
  private ProgressIndicator indicator;
  @FXML
  private ProgressBar progressLoading;

  private Map<Long, ShowReport> showReports;
  private Long currentReport;
  private int currentPdfPage;
  private Integer currentPdfPageMax;
  private String currentPdfPath;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
    if (message != null && message.isTypeOf(ShowMessage.class)) {
      ShowMessage sMessage = message.getTypedMessage(ShowMessage.class);
      if (sMessage.isShow()) {
        if (sMessage.getInfo() != null && sMessage.getUuid().equals(currentReport)) {
        } else if (sMessage.getUuid() == null || sMessage.getUuid().equals(currentReport)) {
          getController().showSingleReport(sMessage.getType(), sMessage.getPath(), true);
        }
      } else if (sMessage.isGenerate()) {
        if (showReports.containsKey(sMessage.getUuid())) {
          ShowReport sr = showReports.get(sMessage.getUuid());
          if (sr.finished) {
            getController().showSingleReport(sMessage.getType(), sMessage.getInternal(), false);
          }
        }
      }
    }
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(ShowMessage.class)) {
      ShowMessage sMessage = message.getTypedMessage(ShowMessage.class);
      if (sMessage.isLoad()) {
        hideAll();
        showLoading();
      } else if (sMessage.isShow()) {
        if (sMessage.getInfo() != null && sMessage.getUuid().equals(currentReport)) {
          String currentId = context.getManagedFragmentHandler(TopFragment.class).getController().getCurrentId();
          if (currentId.equals(GuiConfig.PERSPECTIVE_SHOW)) {
            ArrayMessage am = new ArrayMessage();
            am.add(GuiConfig.PERSPECTIVE_GLOBAL, new UiMessage(UiMessage.Type.SHOW));
            am.add(GuiConfig.PERSPECTIVE_GLOBAL + "." + GuiConfig.COMPONENT_GLOBAL, new GuiGlobalMessage(GuiGlobalMessage.Type.INIT, sMessage.getInfo()));
            context.send(GuiConfig.PERSPECTIVE_GLOBAL, am);
          }
        } else if (sMessage.getUuid() == null || sMessage.getUuid().equals(currentReport)) {
          getController().showSingleReportFX(sMessage.getType(), sMessage.getPath(), true);
        }
      } else if (sMessage.isGenerate()) {
        if (showReports.containsKey(sMessage.getUuid())) {
          ShowReport sr = showReports.get(sMessage.getUuid());
          if (sr.finished && sMessage.getTypes().size() > 0) {
            // Transformation DONE, return to global report
            context.send(GuiConfig.PERSPECTIVE_GLOBAL, new UiMessage(UiMessage.Type.SHOW));
          } else if (sr.finished) {
            // Transformation DONE, show
            hideAll();
            getController().showSingleReportFX(sMessage.getType(), sMessage.getInternal(), false);
          } else {
            // Already initiated
            hideAll();
            if (sr.onlyGlobal) {
              showLoading();
            } else {
              showLoadingMultiple();
            }
            updateLoading(sr.count, sr.max);
            currentReport = sMessage.getUuid();
          }
        } else {
          // Init new transformation
          ShowReport sr = new ShowReport(sMessage.getUuid());
          sr.onlyGlobal = sMessage.isOnlyGlobal();
          currentReport = sMessage.getUuid();
          showReports.put(sMessage.getUuid(), sr);
          hideAll();
          if (sMessage.isOnlyGlobal()) {
            showLoading();
          } else {
            showLoadingMultiple();
          }
          if (sMessage.getTypes() != null) {
            context.send(BasicConfig.MODULE_REPORT, new GenerateMessage(sMessage.getTypes(), sMessage.getInfo(), sMessage.getUuid(), sMessage.isOnlyGlobal()));
          } else {
            context.send(BasicConfig.MODULE_REPORT, new GenerateMessage(sMessage.getType(), sMessage.getInfo(), sMessage.getUuid(), sMessage.isOnlyGlobal()));
          }
        }
      } else if (sMessage.isIndividual()) {
        hideAll();
        showLoading();
        context.send(BasicConfig.MODULE_REPORT, new GenerateIndividualMessage(sMessage.getType(), sMessage.getPath(), sMessage.getConfig()));
      } else if (sMessage.isInit()) {
        if (showReports.containsKey(sMessage.getUuid())) {
          ShowReport sr = showReports.get(sMessage.getUuid());
          sr.max = sMessage.getNumber();
          sr.globalValue = sMessage.getValue();
          sr.mrr = sMessage.getMakeReportRunnable();
          updateLoading(sr.count, sr.max);
        }
      } else if (sMessage.isUpdate()) {
        if (showReports.containsKey(sMessage.getUuid())) {
          ShowReport sr = showReports.get(sMessage.getUuid());
          sr.count += sMessage.getNumber();
          if (sMessage.getUuid().equals(currentReport)) {
            updateLoading(sr.count, sr.max);
          }
          if (sr.count == sr.max) {
            sr.finished = true;
          }
          if (sr.globalValue + sr.count == sr.max && sr.mrr != null) {
            context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(sr.uuid, sr.mrr, "individual"));
          }
        }
      }
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new ShowModel());
    setController(new ShowController());
    showReports = new HashMap<>();
    indicator.setProgress(-1);
    hideAll();

    comboIndividuals.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        comboIndividuals.requestFocus();
      }
    });

    // Center content
    scrollPdfPages.setHvalue(0.5);
    scrollPdfPages.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        scrollPdfPages.setHvalue(0.5);
      }
    });
    scrollPdfPages.hvalueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        scrollPdfPages.setHvalue(0.5);
      }
    });
    scrollPdfPages.vvalueProperty().addListener(
        (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
          if (newValue.doubleValue() == 1.0) {
            if (currentPdfPageMax != null && currentPdfPage < currentPdfPageMax) {
              currentPdfPage++;
            }
            nextPdfFilePage();
          }
          updatePageIndicator();
        });

  }

  public void updatePageIndicator() {
    if (currentPdfPageMax == null) {
      context.send(GuiConfig.COMPONENT_NAV, new NavMessage(0, 0));
    } else {
      Double selectedPage = scrollPdfPages.getVvalue() * (currentPdfPage + 1);
      Integer count = selectedPage.intValue() + 1;
      if (count >= currentPdfPageMax) count = currentPdfPageMax;
      context.send(GuiConfig.COMPONENT_NAV, new NavMessage(count, currentPdfPageMax));
    }
  }

  @Override
  public Context getContext() {
    return context;
  }

  @FXML
  protected void changeIndividual(ActionEvent event) throws Exception {
    if (extension != null && extension.equals("pdf") && comboIndividuals.getSelectionModel().getSelectedItem() != null) {
      String name = (String) comboIndividuals.getSelectionModel().getSelectedItem();
      String filename = folderPath + "/" + name + "." + extension;
      resetScrollPdf(filename);
      nextPdfFilePage();
    } else {
      setTextAreaContent();
    }
  }

  public void addComboChilds(ObservableList<String> names) {
    comboIndividuals.setItems(names);
  }

  public void selectComboChild(String name) {
    comboIndividuals.setValue(name);
  }

  public void clearComboBox() {
    if (comboIndividuals.getItems() != null){
      comboIndividuals.getItems().clear();
    }
  }

  public void setCurrentReportParams(String path, String ext) {
    folderPath = path;
    extension = ext;
  }

  public void setTextAreaContent() {
    String name = (String) comboIndividuals.getSelectionModel().getSelectedItem();
    File individual = new File(folderPath + "/" + name + "." + extension);
    if (individual.exists()) {
      textArea.setText(getController().getContent(individual.getPath()));
    }
  }

  /**
   * Show Hide
   */

  public void showLoadingMultiple() {
    NodeUtil.showNode(labelLoading);
    NodeUtil.showNode(progressLoading);
    NodeUtil.hideNode(indicator);
    progressLoading.setProgress(-1);
  }

  public void showLoading() {
    NodeUtil.showNode(indicator);
    NodeUtil.hideNode(labelLoading);
    NodeUtil.hideNode(progressLoading);
  }

  public void hideLoading() {
    NodeUtil.hideNode(labelLoading);
    NodeUtil.hideNode(progressLoading);
    NodeUtil.hideNode(indicator);
  }

  public void updateLoading(int count, int max) {
    double progress = (count * 1.0) / (max * 1.0);
    if (count == 0) {
      progressLoading.getStyleClass().remove("green-bar");
      if (!progressLoading.getStyleClass().contains("blue-bar")) {
        progressLoading.getStyleClass().add("blue-bar");
      }
    }
    if (progress == 1.0) {
      progressLoading.getStyleClass().remove("blue-bar");
      progressLoading.getStyleClass().add("green-bar");
    }
    if (count < 0) {
      progress = -1;
    }
    progressLoading.setProgress(progress);
  }

  public void showTextArea() {
    setTextAreaContent();
    NodeUtil.showNode(textArea);
    NodeUtil.showNode(showVBox);
  }

  public void hideTextArea() {
    NodeUtil.hideNode(textArea);
    NodeUtil.hideNode(showVBox);
    textArea.clear();
  }

  public void showComboBox() {
    NodeUtil.showNode(comboIndividuals);
  }

  public void hideComboBox() {
    NodeUtil.hideNode(comboIndividuals);
  }

  public void showWebView(String path) {
    webView.getEngine().load("file:///" + path.replace("\\", "/"));
    NodeUtil.showNode(webView);
    NodeUtil.showNode(showVBox);
  }

  public void hideWebView() {
    NodeUtil.hideNode(webView);
    NodeUtil.hideNode(showVBox);
  }

  public void showPdfView(String path) {
    resetScrollPdf(path);
    nextPdfFilePage();
    NodeUtil.showNode(scrollPdfPages);
  }

  public void resetScrollPdf(String path){
    currentPdfPage = 0;
    currentPdfPageMax = null;
    currentPdfPath = path;
    scrollPdfPages.setVvalue(0.0);
    pdfPagesVBox.getChildren().clear();
  }

  public void hidePdfView() {
    pdfPagesVBox.getChildren().clear();
    NodeUtil.hideNode(scrollPdfPages);
  }

  public void hideAll() {
    hideTextArea();
    hideWebView();
    hideComboBox();
    hidePdfView();
    hideLoading();
  }

  /**
   * PDF Viewer
   */

  private void nextPdfFilePage() {
    if (currentPdfPageMax != null && currentPdfPage > currentPdfPageMax) return;
    try {
      pdfPagesVBox.getChildren().add(getIndicatorPdf());
      PDDocument document = Loader.loadPDF(new File(currentPdfPath));
      PDFRenderer renderer = new PDFRenderer(document);
      PDPageTree pages = document.getPages();
      currentPdfPageMax = pages.getCount();
      if (currentPdfPage < currentPdfPageMax) {
        BufferedImage pageImage = renderer.renderImageWithDPI(currentPdfPage, 300, ImageType.RGB);
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(pageImage, null));
        pdfPagesVBox.getChildren().remove(pdfPagesVBox.getChildren().size() - 1);
        pdfPagesVBox.getChildren().add(imageView);
        VBox.setMargin(imageView, new Insets(0, 0, 30, 0));
        updatePageIndicator();
      } else {
        pdfPagesVBox.getChildren().remove(pdfPagesVBox.getChildren().size() - 1);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private ProgressIndicator getIndicatorPdf() {
    ProgressIndicator indicatorPdf = new ProgressIndicator();
    indicatorPdf.setProgress(-1);
    indicatorPdf.setMinSize(150, 150);
    indicatorPdf.setPrefSize(150, 150);
    indicatorPdf.setMaxSize(150, 150);
    VBox.setMargin(indicatorPdf, new Insets(20));
    return indicatorPdf;
  }
}

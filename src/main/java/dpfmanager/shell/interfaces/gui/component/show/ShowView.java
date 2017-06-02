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

import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.mvc.DpfView;
import dpfmanager.shell.core.util.NodeUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;
import org.jpedal.PdfDecoderFX;
import org.jpedal.examples.baseviewer.BaseViewerFX;
import org.jpedal.examples.viewer.OpenViewerFX;
import org.jpedal.examples.viewer.gui.javafx.FXViewerTransitions;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfPageData;

import java.io.File;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 17/03/2016.
 */
@DeclarativeView(id = GuiConfig.COMPONENT_SHOW,
    name = GuiConfig.COMPONENT_SHOW,
    viewLocation = "/fxml/show.fxml",
    active = true,
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
  private VBox panePdfViewer;
  @FXML
  private VBox showVBox;

  @Override
  public void sendMessage(String target, Object dpfMessage) {
    context.send(target, dpfMessage);
  }

  @Override
  public void handleMessageOnWorker(DpfMessage message) {
  }

  @Override
  public Node handleMessageOnFX(DpfMessage message) {
    if (message != null && message.isTypeOf(ShowMessage.class)) {
      ShowMessage sMessage = message.getTypedMessage(ShowMessage.class);
      getController().showSingleReport(sMessage.getType(), sMessage.getPath());
    }
    return null;
  }

  @PostConstruct
  public void onPostConstructComponent(FXComponentLayout layout, ResourceBundle resourceBundle) {
    // Init MVC
    setModel(new ShowModel());
    setController(new ShowController());

    hideAll();

    comboIndividuals.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        comboIndividuals.requestFocus();
      }
    });

  }

  @Override
  public Context getContext() {
    return context;
  }

  @FXML
  protected void changeIndividual(ActionEvent event) throws Exception {
    setTextAreaContent();
  }

  public void addComboChild(String name, boolean selected) {
    comboIndividuals.getItems().add(name);
    if (selected) {
      comboIndividuals.getSelectionModel().select(name);
      setTextAreaContent();
    }
  }

  public void clearComboBox() {
    comboIndividuals.getItems().clear();
  }

  public void setCurrentReportParams(String path, String ext){
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

  public void showTextArea() {
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
    webView.getEngine().load("");
  }

  public void showPdfView(String path) {
    opePdfFile(path);
    NodeUtil.showNode(panePdfViewer);
  }

  public void hidePdfView() {
    NodeUtil.hideNode(panePdfViewer);
  }

  public void hideAll() {
    hideTextArea();
    hideWebView();
    hideComboBox();
    hidePdfView();
  }

  /**
   * PDF Viewer
   */

  private void opePdfFile(String absolutePath) {
    // Load PDF Viewer
    System.out.println("OPEN FILE");
    BaseViewerFX bvfx = new BaseViewerFX();
    Scene scene = bvfx.setupViewer(500,1200);

    // Add to layout
    panePdfViewer.getChildren().clear();
    panePdfViewer.getChildren().add(scene.getRoot());

    // Load pdf
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try{
          bvfx.loadPDF(absolutePath);
        } catch(Exception e){
        }
//        Node node = scene.lookup("#open");
//        NodeUtil.hideNode(node);
        System.out.println("LOADED!");
      }
    });

//    bvfx.loadPDF(absolutePath);
//
//    try {
//      PdfDecoderFX pdf = new PdfDecoderFX();
//      pdf.openPdfFile(absolutePath);
//
//      // Goes to first page
//      int currentPage = 1;
//      try {
//        final PdfPageData pageData = pdf.getPdfPageData();
//        final int rotation = pageData.getRotation(currentPage); //rotation angle of current page
//
//            /*
//             * Only call this when the page is displayed vertically, otherwise
//             * it will mess up the document cropping on side-ways documents.
//            */
//        if (rotation == 0 || rotation == 180) {
//          pdf.setPageParameters(scale, currentPage);
//        }
//
//        pdf.decodePage(currentPage);
//        //wait to ensure decoded
//        pdf.waitForDecodingToFinish();
//      } catch (final Exception e) {
//        e.printStackTrace();
//      }
//
//      fitToX(BaseViewerFX.FitToPage.AUTO);
//      updateNavButtons();
//      setBorder();
//      adjustPagePosition(center.getViewportBounds());

//    } catch (final PdfException ex) {
//      ex.printStackTrace();
//    }
  }

}

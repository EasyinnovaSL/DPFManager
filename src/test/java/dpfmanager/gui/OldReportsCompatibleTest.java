package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 11/01/2016.
 */
public class OldReportsCompatibleTest extends ApplicationTest {

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Override
  public void customPreTest() throws Exception {
    // Copy old report to folder
    String last = ReportGenerator.getLastReportPath();
    String reportId = "0";
    String reportDay = last;
    if (last.endsWith("/")) {
      last = last.substring(0, last.length() - 1);
      reportId = last.substring(last.lastIndexOf("/") + 1);
      reportDay = last.substring(0, last.lastIndexOf("/"));
    }
    List<String> olds = Arrays.asList("html", "json", "xml", "pdf");
    for (int i = 0; i< olds.size(); i++) {
      Integer nextReportId = Integer.parseInt(reportId) + 1 + i;
      File newReportFolder = new File(reportDay + "/" + nextReportId);
      File oldReportFolder = new File("src/test/resources/OldReports/" + olds.get(i));
      FileUtils.copyDirectory(oldReportFolder, newReportFolder);
    }
  }

  @Override
  public void customPostTest() throws Exception {
  }

  @Test
  public void testOldReportCompatible() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running old report compatible test...");

    // Check reports table
    clickOnAndReloadTop("#butReports", "#pane-reports");
    waitUntilExists("#lastReportRow");
    VBox mainVBox = (VBox) scene.lookup("#vboxReports0");
    AnchorPane row = (AnchorPane) mainVBox.getChildren().get(0);
    checkValidRow((GridPane) row.getChildren().get(0), "PDF");  //Pdf
    row = (AnchorPane) mainVBox.getChildren().get(1);
    checkValidRow((GridPane) row.getChildren().get(0), "XML");  //Xml
    row = (AnchorPane) mainVBox.getChildren().get(2);
    checkValidRow((GridPane) row.getChildren().get(0), "JSON"); //Json
    row = (AnchorPane) mainVBox.getChildren().get(3);
    checkValidRow((GridPane) row.getChildren().get(0), "HTML"); //Html

    // Check global view formats
//    List<String> foundTypes = new ArrayList<>();
//    List<String> expectedTypes = Arrays.asList("xml","mets");
//    HBox formatsBox = (HBox) grid.getChildren().get(8);
//    for (Node n : formatsBox.getChildren()){
//      ImageView iv = (ImageView) n;
//      foundTypes.add(iv.getId().replace("but", ""));
//    }
//    Assert.assertEquals("Available buttons", expectedTypes, foundTypes);
  }

  private void checkValidRow(GridPane grid, String type) {
    Assert.assertEquals("Report row N files (" + type + ")", "2", ((Label) grid.getChildren().get(1)).getText());
    Assert.assertEquals("Report row N passed (" + type + ")", "1 passed", ((Label) grid.getChildren().get(8)).getText());
    Assert.assertEquals("Report row N errors (" + type + ")", "1 errors", ((Label) grid.getChildren().get(6)).getText());
    Assert.assertEquals("Report row N warnings (" + type + ")", "1 warnings", ((Label) grid.getChildren().get(7)).getText());
  }

}


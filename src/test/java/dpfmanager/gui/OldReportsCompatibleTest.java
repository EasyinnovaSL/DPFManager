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
  String reportDay;
  Integer nextReportId;

  @Override
  public void init() throws Exception {
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Override
  public void customPreTest() throws Exception {
    String last = ReportGenerator.getLastReportPath();
    String reportId = "0";
    reportDay = last;
    if (last.endsWith("/")) {
      last = last.substring(0, last.length() - 1);
      reportId = last.substring(last.lastIndexOf("/") + 1);
      reportDay = last.substring(0, last.lastIndexOf("/"));
    }
    nextReportId = Integer.parseInt(reportId);
  }

  @Override
  public void customPostTest() throws Exception {
  }

  @Test
  public void testOldReportCompatible() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running old report compatible test...");

    // Formats to test
    List<String> olds = Arrays.asList("html", "json", "xml", "pdf");
    for (String old : olds) {
      // Copy old report
      copyReport(old);

      // Check reports table
      clickOnAndReloadTop("#butReports", "#pane-reports");
      waitUntilExists("#lastReportRow");
      VBox mainVBox = (VBox) scene.lookup("#vboxReports0");
      AnchorPane row = (AnchorPane) mainVBox.getChildren().get(0);
      checkValidRow((GridPane) row.getChildren().get(0), old);

      // Check global view formats
      clickOnAndReload("#vboxReports0 GridPane");
      waitUntilExists("#pane-global");
      waitUntilExists("#but" + old);
      HBox globalFormatsBox = (HBox) scene.lookup("#globalFormatsBox");
      Assert.assertEquals("Global formats old", 1, globalFormatsBox.getChildren().size());
      FxAssert.verifyThat("#but" + old, NodeMatchers.isNotNull());

      // Go to main check files
      clickOnAndReloadTop("#butDessign", "#pane-design");
      waitUntilExists("#pane-design");
    }
  }

  private void copyReport(String old) throws Exception{
    nextReportId++;
    File newReportFolder = new File(reportDay + "/" + nextReportId);
    File oldReportFolder = new File("src/test/resources/OldReports/" + old);
    FileUtils.copyDirectory(oldReportFolder, newReportFolder);
  }

  private void checkValidRow(GridPane grid, String type) {
    Assert.assertEquals("Report row N files (" + type + ")", "2", ((Label) grid.getChildren().get(1)).getText());
    Assert.assertEquals("Report row N passed (" + type + ")", "1 passed", ((Label) grid.getChildren().get(8)).getText());
    Assert.assertEquals("Report row N errors (" + type + ")", "1 errors", ((Label) grid.getChildren().get(6)).getText());
    Assert.assertEquals("Report row N warnings (" + type + ")", "1 warnings", ((Label) grid.getChildren().get(7)).getText());
  }

}


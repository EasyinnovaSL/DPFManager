package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobotException;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 11/01/2016.
 */
public class StatisticsTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/Statistics.dpf";
  private String statistics1 = "src/test/resources/Statistics1.zip";
  private String statistics2 = "src/test/resources/Statistics2.zip";
  private String statistics3 = "src/test/resources/Statistics3.zip";

  @Override
  public void init() throws Exception {
    System.out.println("LAUNCH");
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Override
  public void customPreTest() throws Exception {
    System.out.println("BEFORE");
    backupReportsFolder();
  }

  @Override
  public void customPostTest() throws Exception {
    System.out.println("AFTER");
    restoreReportsFolder();
  }

  @Test
  public void testStatisticsPane() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running statistics test...");

    // Import config
    GuiWorkbench.setTestParam("import", inputConfigPath);
    waitUntilExists("#importButton");
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(inputConfigPath);
    // First Run the 3 check
    List<String> list = Arrays.asList(statistics1, statistics2, statistics3);
    for (String inputFile : list) {
      writeText("#inputText", inputFile);
      clickOnAndReload("#checkFilesButton");
    }

    // Wait for the checks
    waitForCheckFiles(list.size());
    clickOnAndReloadBot("#taskBut");

    // Go to statistics and wait for them
    clickOnAndReloadTop("#butStatistics", "#pane-statistics");
    waitUntilExists("#genStatisticsButton");
    Button genStatisticsButton = (Button) scene.lookup("#genStatisticsButton");
    boolean disable = genStatisticsButton.isDisable();
    System.out.println("Disabled: " + disable);
    while (disable){
      sleep(250);
      disable = genStatisticsButton.isDisable();
    }

    // Check left Summary
    Assert.assertEquals("3", ((Label) scene.lookup("#labelNReports")).getText());
    Assert.assertEquals("7", ((Label) scene.lookup("#labelNTiffs")).getText());
    Assert.assertEquals("2.3", ((Label) scene.lookup("#labelATiffs")).getText());
    Assert.assertEquals("9.8 MB", ((Label) scene.lookup("#labelASize")).getText());
    // Check right Summary
    Assert.assertEquals("8", ((Label) scene.lookup("#labelNMain")).getText());
    Assert.assertEquals("1.1", ((Label) scene.lookup("#labelAMain")).getText());
    Assert.assertEquals("2", ((Label) scene.lookup("#labelNThumb")).getText());
    Assert.assertEquals("28.6%", ((Label) scene.lookup("#labelPThumb")).getText());

    /**
     * Check tags
     */
    makeScroll(10, true); // Scroll to tags vbox
    VBox vboxTags = (VBox) scene.lookup("#vboxTags");
    Assert.assertTrue(!vboxTags.getChildren().isEmpty());
    // Check first tag
    Node firstTag = vboxTags.getChildren().get(0);
    Assert.assertEquals("256", ((Label) firstTag.lookup("#tagId")).getText());
    Assert.assertEquals("ImageWidth", ((Label) firstTag.lookup("#tagName")).getText());
    Assert.assertEquals("8", ((Label) firstTag.lookup("#tagMain")).getText());
    Assert.assertEquals("2", ((Label) firstTag.lookup("#tagThumb")).getText());
    // Check Clickable tag
    Node planarTag = vboxTags.getChildren().get(25);
    Node subPlanarTag = vboxTags.getChildren().get(26);
    Assert.assertEquals("284", ((Label) planarTag.lookup("#tagId")).getText());
    Assert.assertEquals("PlanarConfiguration", ((Label) planarTag.lookup("#tagName")).getText());
    Assert.assertEquals("6", ((Label) planarTag.lookup("#tagMain")).getText());
    Assert.assertEquals("2", ((Label) planarTag.lookup("#tagThumb")).getText());
    FxAssert.verifyThat(subPlanarTag, NodeMatchers.isInvisible());
    clickOnSpecificScrollPane(".PlanarConfiguration #tagName", "#scrollTags");
    FxAssert.verifyThat(subPlanarTag, NodeMatchers.isVisible());

    /**
     * Check ISOs
     */
    VBox vboxIsos = (VBox) scene.lookup("#vboxIsos");
    Assert.assertTrue(!vboxIsos.getChildren().isEmpty());
    Node firstIso = vboxIsos.getChildren().get(0);
    Assert.assertEquals("3", ((Label) firstIso.lookup("#isoErr")).getText());
    Assert.assertEquals("4", ((Label) firstIso.lookup("#isoOk")).getText());
    Node secondIso = vboxIsos.getChildren().get(1);
    Assert.assertEquals("7", ((Label) secondIso.lookup("#isoErr")).getText());
    Assert.assertEquals("0", ((Label) secondIso.lookup("#isoOk")).getText());

    /**
     * Check Specific Errors
     */
    VBox vboxErrors = (VBox) scene.lookup("#vboxErrors");
    VBox vboxBaseline = (VBox) vboxErrors.getChildren().get(0);
    clickOnAndReload("#vboxPolicys"); // Move to policies for make visible ISOs
    FxAssert.verifyThat(vboxBaseline, NodeMatchers.isInvisible());
    clickOnSpecificScrollPane("#vboxIsos #isoName", "#scrollIsos");
    FxAssert.verifyThat(vboxBaseline, NodeMatchers.isVisible());

    /**
     * Check Policies
     */
    VBox vboxPolicys = (VBox) scene.lookup("#vboxPolicys");
    Assert.assertTrue(!vboxPolicys.getChildren().isEmpty());
    Node firstPolicy = vboxPolicys.getChildren().get(0);
    Assert.assertEquals("10", ((Label) firstPolicy.lookup("#ruleFiles")).getText());
    Assert.assertEquals("6", ((Label) firstPolicy.lookup("#ruleKO")).getText());
    Node secondPolicy = vboxPolicys.getChildren().get(1);
    Assert.assertEquals("10", ((Label) secondPolicy.lookup("#ruleFiles")).getText());
    Assert.assertEquals("1", ((Label) secondPolicy.lookup("#ruleKO")).getText());
  }

  public void backupReportsFolder() throws IOException {
    File reportsCurrent = new File(DPFManagerProperties.getReportsDir());
    File reportsBackup = new File(reportsCurrent.getParentFile().getAbsolutePath() + "/reports-backup");
    FileUtils.moveDirectory(reportsCurrent, reportsBackup);
  }

  public void restoreReportsFolder() throws IOException {
    File reportsCurrent = new File(DPFManagerProperties.getReportsDir());
    File reportsBackup = new File(reportsCurrent.getParentFile().getAbsolutePath() + "/reports-backup");
    FileUtils.deleteDirectory(reportsCurrent);
    FileUtils.moveDirectory(reportsBackup, reportsCurrent);
  }

}


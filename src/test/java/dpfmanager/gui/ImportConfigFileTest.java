package dpfmanager.gui;

import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;

/**
 * Created by Adrià Llorens on 07/01/2016.
 */
public class ImportConfigFileTest extends ApplicationTest {

  private String inputPathEdit = "src/test/resources/ConfigFiles/config.dpf";
  private String inputPathCopy = "src/test/resources/ConfigFiles/Copy.dpf";

  private File targetFile;
  private File targetFileBak;

  Stage stage = null;

  @Override
  public void init() throws Exception {
    customPreTest();
    stage = launch(GuiApp.class, "-gui", "-test");
    scene = stage.getScene();
  }

  @Override
  public void customPreTest() {
    // Backup Copy.dpf
    String targetPath = DPFManagerProperties.getConfigDir() + "/Copy.dpf";
    targetFile = new File(targetPath);
    targetFileBak = getBackupFile(targetPath);
    if (targetFile.exists()){
      targetFile.renameTo(targetFileBak);
      targetFile.delete();
    }
  }

  @Override
  public void customPostTest() {
    // Delete imported config
    if (targetFile.exists()){
      targetFile.delete();
    }
    // Restore backup
    if (targetFileBak.exists()) {
      targetFileBak.renameTo(targetFile);
    }
  }

  @Test
  public void testImportConfigFile() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running import and edit config file test...");

    /**
     *  Test edit imported config
     */

    //Import config but DON'T copy
    GuiWorkbench.setTestParam("import", inputPathEdit);
    clickOnScroll("#importButton");
    clickOnScroll("No");
    clickOnImportedConfig(inputPathEdit);

    //Edit
    clickOnAndReload("#editButton", "#pane-config");

    //Test step buttons
    clickOnAndReload("#step3");
    clickOnAndReload("#step5");
    clickOnAndReload("#step4");
    clickOnAndReload("#step2");
    clickOnAndReload("#step1");

    //Go to summary and compare
    clickOnAndReload("#step5");
    FxAssert.verifyThat("#labIsos", NodeMatchers.hasText(ImplementationCheckerLoader.getIsoName("TIFF_EP") + ", " + ImplementationCheckerLoader.getIsoName("TiffITP1ProfileChecker")));
    FxAssert.verifyThat("#labRules", NodeMatchers.hasText("ImageLength < 1000"));
    FxAssert.verifyThat("#labReports", NodeMatchers.hasText("HTML, PDF"));
    FxAssert.verifyThat("#labFixes", NodeMatchers.hasText("Add Tag Artist 'EasyTest'"));

    clickOnAndReloadTop("#butDessign","#pane-design");

    /**
     * Test copy imported file
     */
    System.out.println("Running import and copy");

    //Import config AND copy
    GuiWorkbench.setTestParam("import", inputPathCopy);
    clickOnScroll("#importButton");
    clickOnScroll("Yes");

    // Check config file copied
    Assert.assertEquals(true, targetFile.exists());

    // Check config file in GUI
    VBox vbox = (VBox) scene.lookup("#vBoxConfig");
    int found = 0;
    for (Node node : vbox.getChildren()) {
      RadioButton rb = (RadioButton) node;
      if (rb.getText().equals(targetFile.getName())){
        found++;
      }
    }
    Assert.assertEquals(1,found);

  }

}


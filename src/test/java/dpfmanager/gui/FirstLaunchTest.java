package dpfmanager.gui;

import dpfmanager.shell.application.app.GuiApp;
import dpfmanager.shell.modules.report.ReportGenerator;
import javafx.stage.Stage;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public class FirstLaunchTest extends ApplicationTest {

  Stage stage = null;

  @Override
  public void init() throws Exception{
    stage = launch(GuiApp.class);
    scene = stage.getScene();
  }

  @BeforeClass
  public final static void beforeClass() {
    // Backup the config file
    File reportFolder = new File(ReportGenerator.getReportsFolder());
    File configFileOld = new File(reportFolder.getParent()+"/dpfmanager.properties");
    File configFileNew = new File(reportFolder.getParent()+"/dpfmanager.properties-bak");
    if (configFileOld.exists()){
      configFileOld.renameTo(configFileNew);
    }
  }

  @AfterClass
  public final static void afterClass() {
    // Restore config file
    File reportFolder = new File(ReportGenerator.getReportsFolder());
    File configFileOld = new File(reportFolder.getParent()+"/dpfmanager.properties");
    File configFileNew = new File(reportFolder.getParent()+"/dpfmanager.properties-bak");
    if (configFileOld.exists()){
      configFileOld.delete();
    }
    if (configFileNew.exists()){
      configFileNew.renameTo(configFileOld);
    }
  }

  @Test
  public void testFirstScreen() throws Exception {
    //Now init app
    WaitForAsyncUtils.waitForFxEvents();
    FxAssert.verifyThat("#pane-first", NodeMatchers.isNotNull());

    //Check config file now exists
    File reportFolder = new File(ReportGenerator.getReportsFolder());
    File configFile = new File(reportFolder.getParent()+"/dpfmanager.properties");
    Assert.assertTrue("Config file (dpfmanager.properties) does't exist", configFile.exists());
  }

}

package dpfmanager.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationFixture;

/**
 * Created by Adrià Llorens on 30/12/2015.
 */
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

  static Stage stage;
  public static Stage launch(Class<? extends Application> appClass,
                             String... appArgs) throws Exception {
    if(stage==null)
    {stage = FxToolkit.registerPrimaryStage();
      FxToolkit.setupApplication(appClass, appArgs);}
    return stage;
  }
  @Before
  public final void internalBefore()
      throws Exception {
    FxToolkit.registerPrimaryStage();
    FxToolkit.setupApplication(this);
  }
  @After
  public final void internalAfter()
      throws Exception {
    FxToolkit.cleanupApplication(this);
  }
  @Override
  public void init()
      throws Exception {}

  @Override
  public abstract void start(Stage stage)
      throws Exception;

  @Override
  public void stop()
      throws Exception {}

}

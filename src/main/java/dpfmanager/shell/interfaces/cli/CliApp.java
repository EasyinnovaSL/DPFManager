package dpfmanager.shell.interfaces.cli;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.api.resource.ResourceItem;
import org.jrebirth.af.api.ui.Model;
import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.core.application.AbstractApplication;
import org.jrebirth.af.core.application.DefaultApplication;
import org.jrebirth.af.core.concurrent.JRebirthThread;

import java.util.List;

/**
 * Created by Adrià Llorens on 24/02/2016.
 */
public class CliApp extends DefaultApplication<Pane> {

  public static void main(final String... args) {
//    CommandLineApp cl = new CommandLineApp(Arrays.asList(args));
//    cl.launch();
    Application.launch(args);
//    try {
//      AnnotatedComponentFactory factory = new AnnotatedComponentFactory();
//      Component comp = factory.buildComponent(EmptyModel.class);
//      ModulesManager manager = comp.getService(ModulesManager.class);
//      String omg = "";
//    }
//    catch (Exception e){
//      e.printStackTrace();
//    }
  }


  @Override
  public Class<? extends Model> getFirstModelClass() {
    return EmptyModel.class;
  }

  @Override
  protected void customizeStage(Stage stage) {
//    stage.initStyle(StageStyle.TRANSPARENT);
//    stage.setX(2000.0);
//    stage.setY(2000.0);
  }
}

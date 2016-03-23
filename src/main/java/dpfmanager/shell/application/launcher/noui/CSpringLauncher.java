package dpfmanager.shell.application.launcher.noui;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;

import org.jacpfx.api.handler.ErrorDialogHandler;
import org.jacpfx.rcp.components.workbench.DefaultWorkbenchDecorator;
import org.jacpfx.rcp.components.workbench.WorkbenchDecorator;
import org.jacpfx.rcp.handler.DefaultErrorDialogHandler;
import org.jacpfx.rcp.handler.ExceptionHandler;
import org.jacpfx.rcp.registry.ClassRegistry;
import org.jacpfx.rcp.util.ClassFinder;
import org.jacpfx.rcp.workbench.FXWorkbench;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Adrià Llorens on 03/03/2016.
 */
public abstract class CSpringLauncher extends Application {
  protected NoUiWorkbench workbench;


  public NoUiWorkbench getWorkbench() {
    return this.workbench;
  }

  protected abstract Class<? extends FXWorkbench> getWorkbenchClass();

  protected void scanPackegesAndInitRegestry() {
    final String[] packages = getBasePackages();
    if (packages == null)
      throw new InvalidParameterException("no  packes declared, declare all packages containing perspective and component");
    final Optional<String> emptyDeclaration = Stream.of(packages).filter(pack -> pack.isEmpty()).findFirst();
    if (emptyDeclaration.isPresent())
      throw new InvalidParameterException("no  empty declaration is allowed");
    final ClassFinder finder = new ClassFinder();
    ClassRegistry.clearAllClasses();
    Stream.of(packages).forEach(p -> {
      try {
        ClassRegistry.addClasses(Arrays.asList(finder.getAll(p)));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    });
  }

  protected void initExceptionHandler() {
    ExceptionHandler.initExceptionHandler(getErrorHandler());
    Thread.currentThread().setUncaughtExceptionHandler(ExceptionHandler.getInstance());

  }

  /**
   * Return all packages which contains component and perspective that should be scanned. This is
   * needed to find component/prespectives by id.
   *
   * @return an array of package names
   */
  protected abstract String[] getBasePackages();

  /**
   * Will be executed after Spring/JavaFX initialisation.
   *
   * @param stage the javafx Stage
   */
  protected abstract void postInit(Stage stage);

  /**
   * Returns an ErrorDialog handler to display exceptions and errors in workspace. Overwrite this
   * method if you need a customized handler.
   *
   * @return an ErrorHandler instance
   */
  protected ErrorDialogHandler<Node> getErrorHandler() {
    return new DefaultErrorDialogHandler();
  }

  /**
   * Return an instance of your WorkbenchDecorator, which defines the basic layout structure with
   * toolbars and main content
   *
   * @return returns an instance of a {@link org.jacpfx.rcp.components.workbench.WorkbenchDecorator}
   */
  protected WorkbenchDecorator getWorkbenchDecorator() {
    return new DefaultWorkbenchDecorator();
  }
}

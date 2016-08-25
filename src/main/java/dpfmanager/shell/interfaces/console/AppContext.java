package dpfmanager.shell.interfaces.console;

import dpfmanager.shell.core.DPFManagerProperties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class AppContext {

  private static ApplicationContext ctx;

  public static void loadContext(String path) {
    ctx = new ClassPathXmlApplicationContext(path);
  }

  public static ApplicationContext getApplicationContext() {
    return ctx;
  }

  public static void close() {
    ((ConfigurableApplicationContext)ctx).close();
    DPFManagerProperties.setFinished(true);
  }

}

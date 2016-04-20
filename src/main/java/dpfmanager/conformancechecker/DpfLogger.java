package dpfmanager.conformancechecker;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 06/04/2016.
 */
public class DpfLogger {

  private DpfContext context;
  private boolean print;

  public DpfLogger() {
    context = null;
  }

  public DpfLogger(DpfContext c, boolean p) {
    context = c;
    print = p;
  }

  public void println(String line) {
    if (context != null && print) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, line));
    }
  }
}

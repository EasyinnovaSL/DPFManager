package dpfmanager.conformancechecker;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 06/04/2016.
 */
public class DpfLogger {

  private Context context;

  public DpfLogger(){
    context = null;
  }

  public DpfLogger(Context c){
    context = c;
  }

  public void println(String line){
    if (context != null) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, line));
    }
  }
}

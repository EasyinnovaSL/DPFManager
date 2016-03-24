package dpfmanager.shell.modules.conformancechecker.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class ConformanceMessage extends DpfMessage {

  private String input;
  private String path;

  public ConformanceMessage(String i, String p){
    input = i;
    path = p;
  }

  public String getPath(){
    return path;
  }

  public String getInput(){
    return input;
  }
}

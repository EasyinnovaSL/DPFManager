package dpfmanager.shell.modules.report.messages;

import dpfmanager.shell.core.messages.DpfMessage;

import java.util.List;

/**
 * Created by Adrià Llorens on 08/04/2016.
 */
public class StatusMessage extends DpfMessage {

  public enum Type {
    INIT,
    FINISH
  }

  private Type type;
  private String folder;
  private List<String> formats;
  private boolean silence;

  public StatusMessage(Type t, String internal){
    type = t;
    folder = internal;
  }

  public StatusMessage(Type t, List<String> f, String fileFolder, boolean s){
    type = t;
    folder = fileFolder;
    formats = f;
    silence = s;
  }

  public boolean isInit(){
    return type.equals(Type.INIT);
  }

  public boolean isFinish(){
    return type.equals(Type.FINISH);
  }

  public String getFolder(){
    return folder;
  }

  public List<String> getFormats() {
    return formats;
  }

  public boolean isSilence() {
    return silence;
  }
}

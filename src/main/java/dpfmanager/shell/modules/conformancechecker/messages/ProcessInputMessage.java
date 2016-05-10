package dpfmanager.shell.modules.conformancechecker.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;

import java.io.File;
import java.util.List;

/**
 * Created by Adrià Llorens on 09/05/2016.
 */
public class ProcessInputMessage extends DpfMessage {

  private Type type;
  private Long uuid;
  private List<String> files;
  private Configuration config;
  private String internalReportFolder;
  private String inputStr;
  private Integer toWait;

  public enum Type {
    WAIT, FILE
  }

  public ProcessInputMessage(Type type, Long uuid, List<String> files, Configuration config, String internalReportFolder, String inputStr, Integer toWait){
    // Wait
    this.type = type;
    this.uuid = uuid;
    this.files = files;
    this.config = config;
    this.internalReportFolder = internalReportFolder;
    this.inputStr = inputStr;
    this.toWait = toWait;
  }

  public ProcessInputMessage(Type type, Long uuid, List<String> files){
    // File
    this.type = type;
    this.uuid = uuid;
    this.files = files;
  }

  public boolean isWait(){
    return type.equals(Type.WAIT);
  }

  public boolean isFile(){
    return type.equals(Type.FILE);
  }

  public Long getUuid() {
    return uuid;
  }

  public List<String> getFiles() {
    return files;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getInternalReportFolder() {
    return internalReportFolder;
  }

  public String getInputStr() {
    return inputStr;
  }

  public Integer getToWait() {
    return toWait;
  }
}

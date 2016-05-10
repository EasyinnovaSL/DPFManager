package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.configuration.Configuration;

import java.util.List;

/**
 * Created by Adrià Llorens on 10/05/2016.
 */
public class ProcessInputParameters {

  private String internalReportFolder;
  private String inputStr;
  private Configuration config;
  private int toWait;
  private List<String> files;

  public ProcessInputParameters(String internalReportFolder, String inputStr, Configuration config, int toWait, List<String> files) {
    this.internalReportFolder = internalReportFolder;
    this.inputStr = inputStr;
    this.config = config;
    this.toWait = toWait;
    this.files = files;
  }

  public String getInternalReportFolder() {
    return internalReportFolder;
  }

  public String getInputStr() {
    return inputStr;
  }

  public Configuration getConfig() {
    return config;
  }

  public List<String> getFiles() {
    return files;
  }

  public int getToWait() {
    return toWait;
  }

  public void add(List<String> files2){
    files.addAll(files2);
    toWait--;
  }
}

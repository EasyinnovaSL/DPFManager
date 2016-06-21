package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.conformancechecker.runnable.ConformanceRunnable;
import dpfmanager.shell.modules.conformancechecker.runnable.ProcessInputRunnable;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_CONFORMANCE)
@Scope("singleton")
public class ConformanceCheckerService extends DpfService {

  private ConformanceCheckerModel model;

  private int recursive;
  private boolean silence;
  private Long uuid;

  /** The list of checks waiting for process input*/
  private Map<Long,ProcessInputParameters> filesToCheck;

  @PostConstruct
  private void init() {
    model = new ConformanceCheckerModel();
    filesToCheck = new HashMap<>();
    setDefaultParameters();
  }

  @Override
  protected void handleContext(DpfContext context) {
  }

  public boolean readConfig(String path) {
    return getModel().readConfig(path);
  }

  public void setConfig(Configuration config) {
    getModel().setConfig(config);
  }

  private void setDefaultParameters() {
    recursive = 1;
    silence = false;
  }

  public void setParameters(Configuration config, Integer r, Long u) {
    if (config != null) {
      getModel().setConfig(config);
    }
    if (r != null) {
      recursive = r;
    }
    if (u != null){
      uuid = u;
    } else {
      uuid = System.currentTimeMillis();
    }
  }

  /** First check files method (from command line) */
  public void initMultiProcessInputRun(List<String> files) {
    String finalFiles = "";
    for (String filename : files) {
      finalFiles += filename + ";";
    }
    finalFiles = finalFiles.substring(0, finalFiles.length() - 1);
    initProcessInputRun(finalFiles);
  }

  /** First check files method (from gui) */
  public void initProcessInputRun(String filename) {
    initProcessInputRun(filename, filename, null);
  }

  public void initProcessInputRun(String filename, String inputStr, String internalReportFolder) {
    ProcessInputRunnable pr = new ProcessInputRunnable(filename, inputStr, internalReportFolder, recursive, getModel().getConfig());
    context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.NEW, uuid, pr, inputStr));
  }

  synchronized public void tractProcessInputMessage(ProcessInputMessage pim){
    if (pim.isWait()){
      ProcessInputParameters pip = new ProcessInputParameters(pim.getInternalReportFolder(), pim.getInputStr(), pim.getConfig(), pim.getToWait(), pim.getFiles());
      filesToCheck.put(pim.getUuid(), pip);
    } else if (pim.isFile()){
      if (filesToCheck.containsKey(pim.getUuid())){
        filesToCheck.get(pim.getUuid()).add(pim.getFiles());
      }
    }

    // Check if waiting is finish
    Integer left = filesToCheck.get(pim.getUuid()).getToWait();
    if (left == 0 && filesToCheck.get(pim.getUuid()).getFiles().size() > 0){
      // Start check
      startCheck(pim.getUuid(),filesToCheck.get(pim.getUuid()));
    } else {
      // Finish app
      AppContext.close();
    }
  }

  public void startCheck(Long uuid, ProcessInputParameters pip) {
    // Init
    context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.INIT, uuid, pip.getFiles().size(), pip.getConfig(), pip.getInternalReportFolder(), pip.getInputStr()));

    // Now process files
    ProcessFiles(uuid, pip.getFiles(), pip.getConfig(), pip.getInternalReportFolder());
  }

  /**
   * Process a list of files and create the runnables.
   *
   * @param config the config
   * @return the path to the internal report folder
   */
  private String ProcessFiles(Long uuid, List<String> files, Configuration config, String internalReportFolder) {
    // Process each input of the list
    int idReport = 1;
    for (final String filename : files) {
      ConformanceRunnable run = new ConformanceRunnable();
      run.setParameters(filename, idReport, internalReportFolder, config, uuid);
      context.send(BasicConfig.MODULE_THREADING, new RunnableMessage(uuid, run));
      idReport++;
    }

    if (!new File(internalReportFolder).exists()) {
      internalReportFolder = null;
    }

    return internalReportFolder;
  }

  private ConformanceCheckerModel getModel() {
    return model;
  }

}

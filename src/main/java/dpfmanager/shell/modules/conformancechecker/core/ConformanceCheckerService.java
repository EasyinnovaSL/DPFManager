/**
 * <h1>ConformanceCheckerService.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.modules.conformancechecker.messages.ProcessInputMessage;
import dpfmanager.shell.modules.conformancechecker.runnable.ConformanceRunnable;
import dpfmanager.shell.modules.conformancechecker.runnable.ProcessInputRunnable;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.interoperability.core.InteroperabilityService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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

  @Resource(name = "parameters")
  private Map<String, String> parameters;

  @Autowired
  private InteroperabilityService interService;

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
    } else if (left == 0){
      String internal = filesToCheck.get(pim.getUuid()).getInternalReportFolder();
      filesToCheck.remove(pim.getUuid());
      cancelCheck(pim.getUuid(), internal);
      if (parameters.get("mode") != null && parameters.get("mode").equals("CMD")) {
        // Finish app
        AppContext.close();
      }
    }
  }

  public void startCheck(Long uuid, ProcessInputParameters pip) {
    // Init
    context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.INIT, uuid, pip.getFiles().size(), pip.getConfig(), pip.getInternalReportFolder(), pip.getInputStr()));

    // Now process files
    ProcessFiles(uuid, pip.getFiles(), pip.getConfig(), pip.getInternalReportFolder());
  }

  public void cancelCheck(Long uuid, String internal) {
    // Cancel in DB
    getContext().send(BasicConfig.MODULE_DATABASE, new JobsMessage(JobsMessage.Type.CANCEL, uuid));
    // Cancel in Multithreading
    getContext().send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.CANCEL, uuid, internal));
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
      List<ConformanceChecker> conformances = interService.getConformanceCheckers();
      ConformanceRunnable run = new ConformanceRunnable(conformances);
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

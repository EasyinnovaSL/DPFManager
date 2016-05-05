package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.runnable.ConformanceRunnable;
import dpfmanager.shell.modules.conformancechecker.runnable.ProcessRunnable;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;

import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

  @PostConstruct
  private void init() {
    model = new ConformanceCheckerModel();
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

  public void setParameters(Configuration config, Integer r) {
    if (config != null) {
      getModel().setConfig(config);
    }
    if (r != null) {
      recursive = r;
    }
  }

  public void startMultiCheck(List<String> files) {
    String finalFiles = "";
    for (String filename : files) {
      finalFiles += filename + ";";
    }
    finalFiles = finalFiles.substring(0, finalFiles.length() - 1);
    startCheck(finalFiles);
  }

  public void startCheck(String filename) {
    startCheck(filename, filename, null);
  }

  public void startCheck(String filename, String inputStr, String internalReportFolder) {
    ProcessRunnable pr = new ProcessRunnable(filename, inputStr, internalReportFolder, recursive, getModel().getConfig());
    context.send(BasicConfig.MODULE_THREADING, new GlobalStatusMessage(GlobalStatusMessage.Type.NEW, pr, inputStr));
  }

  private ConformanceCheckerModel getModel() {
    return model;
  }

}

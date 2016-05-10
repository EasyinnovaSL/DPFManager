package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.external.ExternalConformanceChecker;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Muñoz on 04/09/2015.
 */
public class ProcessInput {

  private boolean outOfmemory = false;
  private DpfContext context;
  private List<String> tempFiles;

  public ProcessInput() {
    tempFiles = new ArrayList<>();
  }

  /**
   * Sets the logger.
   *
   * @param context the Dpf context
   */
  public void setContext(DpfContext context) {
    this.context = context;
  }

  /**
   * Get the list of conformance checkers available to use.
   */
  private List<ConformanceChecker> getConformanceCheckers() {
    TiffConformanceChecker tiffcc = new TiffConformanceChecker();
    ArrayList<ConformanceChecker> l = new ArrayList<>();
    l.add(tiffcc);

    String path = "package/resources/plugins/video/MediaConch.exe";
    if (!new File(path).exists()) path = "plugins/video/MediaConch.exe";
    if (!new File(path).exists()) path = "../plugins/video/MediaConch.exe";
    if (new File(path).exists()) {
      ArrayList<String> params = new ArrayList<>();
      params.add("-mc");
      params.add("-fx");
      ArrayList<String> standards = new ArrayList<>();
      standards.add("MOV");
      ArrayList<String> extensions = new ArrayList<>();
      extensions.add("MOV");
      ExternalConformanceChecker ext = new ExternalConformanceChecker(path, params, standards, extensions);
      l.add(ext);
    }

    path = "package/resources/plugins/pdf/verapdf.bat";
    if (!new File(path).exists()) path = "plugins/pdf/verapdf.bat";
    if (!new File(path).exists()) path = "../plugins/pdf/verapdf.bat";
    if (new File(path).exists()) {
      ArrayList<String> params = new ArrayList<>();
      params.add("--format");
      params.add("xml");
      ArrayList<String> standards = new ArrayList<>();
      standards.add("PDF");
      ArrayList<String> extensions = new ArrayList<>();
      extensions.add("PDF");
      ExternalConformanceChecker ext = new ExternalConformanceChecker(path, params, standards, extensions);
      l.add(ext);
    }

    return l;
  }

  /**
   * Get the appropiate conformance checker to run the given file.
   */
  private ConformanceChecker getConformanceCheckerForFile(String filename) {
    ConformanceChecker result = null;
    for (ConformanceChecker cc : getConformanceCheckers()) {
      if (cc.acceptsFile(filename)) {
        result = cc;
        break;
      }
    }
    if (result != null) {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Conformance checker for file " + filename + ": " + result.toString()));
    } else {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Conformance checker for file " + filename + " not found"));
    }
    return result;
  }

  /**
   * Process an input.
   *
   * @param filename             the source path
   * @param internalReportFolder the internal report folder
   * @param config               the report configuration
   * @return generated list of individual reports
   */
  public IndividualReport processFile(String filename, String internalReportFolder, Configuration config) {
    IndividualReport ir = null;
    // File
    ConformanceChecker cc = getConformanceCheckerForFile(filename);
    if (cc != null) {
      try {
        ir = cc.processFile(filename, filename, internalReportFolder, config);
        if (ir != null) {
          outOfmemory = true;
        }
      } catch (ReadTagsIOException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error in File " + filename));
      } catch (ReadIccConfigIOException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error in File " + filename));
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "File " + filename + " is not an accepted format"));
    }
    return ir;
  }

}

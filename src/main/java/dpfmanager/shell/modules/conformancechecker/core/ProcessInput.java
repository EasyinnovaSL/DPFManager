/**
 * <h1>ProcessInput.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Victor Muñoz
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.external.ExternalConformanceChecker;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Victor Muñoz on 04/09/2015.
 */
public class ProcessInput {

  private DpfContext context;
  private ResourceBundle bundle;
  private List<ConformanceChecker> conformanceCheckers;

  public ProcessInput(List<ConformanceChecker> list) {
    conformanceCheckers = list;
    bundle = DPFManagerProperties.getBundle();
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
    return conformanceCheckers;
  }

  /**
   * Get the appropiate conformance checker to run the given file.
   */
  private List<ConformanceChecker> getConformanceCheckersForFile(String filename) {
    List<ConformanceChecker> result = new ArrayList<>();
    for (ConformanceChecker cc : getConformanceCheckers()) {
      if (cc.acceptsFile(filename)) {
        result.add(cc);
      }
    }
    if (!result.isEmpty()) {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("ccForFile").replace("%1",filename).replace("%2", result.toString())));
    } else {
      context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("ccNotFound").replace("%1",filename)));
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
  public IndividualReport processFile(String filename, String internalReportFolder, Configuration config, int id) {
    IndividualReport ir = null;
    if (isUrl(filename)) {
      // URL
      try {
        List<ConformanceChecker> ccList = getConformanceCheckersForFile(filename);
        if (!ccList.isEmpty()){
          Integer index = ThreadLocalRandom.current().nextInt(0, ccList.size());
          ConformanceChecker cc = ccList.get(index);
          // Download the file and store it in a temporary file
          InputStream input = new java.net.URL(filename).openStream();
          String filename2 = createTempFile(internalReportFolder, new File(filename).getName(), input);
          filename = java.net.URLDecoder.decode(filename, "UTF-8");
          ir = cc.processFile(filename2, filename, internalReportFolder, config, id);
          // Delete the temporary file
          File file = new File(filename2);
          file.delete();
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("unacceptedFormatUrl").replace("%1", filename)));
        }
      } catch (ReadTagsIOException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorInFile").replace("%1", filename)));
      } catch (ReadIccConfigIOException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorInFile").replace("%1", filename)));
      } catch (MalformedURLException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorInFile").replace("%1", filename)));
      } catch (UnsupportedEncodingException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorInFile").replace("%1", filename)));
      } catch (IOException e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorInFile").replace("%1", filename)));
      }
    } else {
      // File
      List<ConformanceChecker> ccList = getConformanceCheckersForFile(filename);
      if (!ccList.isEmpty()){
        Integer index = ThreadLocalRandom.current().nextInt(0, ccList.size());
        ConformanceChecker cc = ccList.get(index);
        try {
          ir = cc.processFile(filename, filename, internalReportFolder, config, id);
        } catch (Exception ex) {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorInFile").replace("%1", filename)));
        }
      } else {
        ir = new IndividualReport(true);
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.WARN, bundle.getString("unacceptedFormat").replace("%1", filename)));
      }
    }
    if (ir == null) {
      ir = new IndividualReport(true);
    }
    return ir;
  }

  /**
   * Gets the default configuration object from file type
   *
   * @param filename the filename
   * @return the configuration for this file
   */
  public Configuration getDefaultConfigurationFromFile(String filename){
    List<ConformanceChecker> ccList = getConformanceCheckersForFile(filename);
    if (!ccList.isEmpty()) {
      Integer index = ThreadLocalRandom.current().nextInt(0, ccList.size());
      ConformanceChecker cc = ccList.get(index);
      return cc.getDefaultConfiguration();
    }
    return null;
  }

  /**
   * Creates a temporary file to store the input stream.
   *
   * @param folder the folder to store the created temporary file
   * @param name   the name of the temporary file
   * @param stream the input stream
   * @return the path to the created temporary file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private String createTempFile(String folder, String name, InputStream stream) throws IOException {
    // Create the path to the temporary file
    String filename2 = "x" + name;
    if (filename2.contains("/")) {
      filename2 = filename2.substring(filename2.lastIndexOf("/") + 1);
    }
    while (new File(filename2).isFile()) {
      filename2 = "x" + filename2;
    }
    filename2 = folder + filename2;

    // Write the file
    File targetFile = new File(filename2);
    OutputStream outStream = new FileOutputStream(targetFile);
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = stream.read(buffer)) != -1) {
      outStream.write(buffer, 0, bytesRead);
    }
    outStream.close();
    return filename2;
  }

  /**
   * Checks if the filename is an URL.
   *
   * @param filename the filename
   * @return true, if it is a url
   */
  private boolean isUrl(String filename) {
    boolean ok = true;
    try {
      new java.net.URL(filename);
    } catch (Exception ex) {
      ok = false;
    }
    return ok;
  }

}

/**
 * <h1>ClientService.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.client.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.client.messages.RequestMessage;
import dpfmanager.shell.modules.client.messages.ResponseMessage;
import dpfmanager.shell.modules.client.upload.HttpClient;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_CLIENT)
@Scope("singleton")
public class ClientService extends DpfService {

  @Resource(name = "parameters")
  private Map<String, String> parameters;

  private ResourceBundle bundle;

  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
  }

  @Override
  protected void handleContext(DpfContext context) {
    // init context
  }

  public void makeRequest(RequestMessage rm) {
    if (rm.isAsk()) {
      askForJob(rm.getString());
    } else if (rm.isCheck()) {
      newCheckRequest(rm.getFiles(), rm.getTmpFiles(), rm.getConfig());
    }
  }

  /**
   * Main functions
   */
  private void askForJob(String id) {
    HttpClient client = new HttpClient(context, parameters.get("-url"));
    if (client.isError()) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("urlError").replace("%1",parameters.get("-url"))));
      return;
    }
    client.setId(id);
    send(client);
  }

  private void newCheckRequest(List<String> files, List<String> tmpFiles, Configuration config) {
    HttpClient client = new HttpClient(context, parameters.get("-url"));
    if (client.isError()) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("urlError").replace("%1",parameters.get("-url"))));
      return;
    }

    // Add the files to check
    for (String path : files) {
      File file = new File(path);
      if (file.exists() && file.isFile()) {
        client.addFile(file);
      }
    }
    for (String path : tmpFiles) {
      File file = new File(path);
      if (file.exists() && file.isFile()) {
        client.addTmpFile(file);
      }
    }

    // Add the configuration file
    try {
      Path tmpPath = Files.createTempFile("config", "dpf");
      File dest = tmpPath.toFile();
      if (dest == null) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorTmpFile")));
        return;
      }
      config.SaveFile(dest.getAbsolutePath());
      client.addConfig(dest);
    } catch (Exception e) {
      e.printStackTrace();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorTmpFile")));
      return;
    }

    send(client);
  }

  /**
   * Request responses
   */
  public void parseResponse(ResponseMessage rm) {
    Gson gson = new Gson();
    Map<String, String> map = gson.fromJson(rm.getMessage(), Map.class);
    if (map.get("type").equalsIgnoreCase("ASK")) {
      parseAskResponse(map);
    } else if (map.get("type").equalsIgnoreCase("CHECK")) {
      parseCheckResponse(map);
    }
  }

  public void parseAskResponse(Map<String, String> map) {
    String message = "";
    boolean error = false;
    if (map.get("status").equalsIgnoreCase("Running")) {
      message = bundle.getString("jobRunning").replace("%1",map.get("processed")).replace("%2", map.get("total"));
    } else if (map.get("status").equalsIgnoreCase("Finished")) {
      downloadFile(map.get("path"));
      return;
    } else if (map.get("status").equalsIgnoreCase("NotFound")) {
      message = bundle.getString("jobNotFound").replace("%1",map.get("id"));
      error = true;
    }

    // Check if wait for finish
    if (parameters.containsKey("-w") && !error) {
      context.sendAfter(BasicConfig.MODULE_CLIENT, new RequestMessage(RequestMessage.Type.ASK, map.get("id")), 1);
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
      DPFManagerProperties.setFinished(true);
    }
  }

  public void parseCheckResponse(Map<String, String> map) {
    if (map.containsKey("error")) {
      // Error ocurred in server
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, map.get("myerror")));
    } else {
      // Everything OK
      if (parameters.containsKey("-w")) {
        context.sendAfter(BasicConfig.MODULE_CLIENT, new RequestMessage(RequestMessage.Type.ASK, map.get("id")), 1);
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("jobStarted").replace("%1",map.get("id"))));
        DPFManagerProperties.setFinished(true);
      }
    }
  }

  /**
   * Send
   */
  private void send(HttpClient client) {
    try {
      client.send();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorSendingRequest")));
    }
  }

  /**
   * Download
   */
  private void downloadFile(String path) {
    try {
      URL url = new URL(parameters.get("-url") + path);
      File downloaded = Files.createTempFile("report", "zip").toFile();
      FileUtils.copyURLToFile(url, downloaded);
      unZipReport(downloaded);
      downloaded.delete();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("cannotDownload")));
    }
  }

  private void unZipReport(File file) {
    // Output folder
    String output = "";
    if (parameters.containsKey("-o")) {
      output = parameters.get("-o");
    } else {
      // Default folder
      output = ReportGenerator.createReportPath();
    }

    // Unzip
    if (!unzipFileIntoDirectory(file, new File(output))) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("cannotUnzip")));
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, bundle.getString("reportDownloaded").replace("%1",output)));
      DPFManagerProperties.setFinished(true);
    }
  }

  private boolean unzipFileIntoDirectory(File file, File dest) {
    try {
      ZipFile zipFile = new ZipFile(file);
      Enumeration files = zipFile.entries();
      while (files.hasMoreElements()) {
        ZipEntry entry = (ZipEntry) files.nextElement();
        InputStream eis = zipFile.getInputStream(entry);
        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        File f = new File(dest.getAbsolutePath() + File.separator + entry.getName());

        if (entry.isDirectory()) {
          f.mkdirs();
          continue;
        } else {
          f.getParentFile().mkdirs();
          f.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(f);

        while ((bytesRead = eis.read(buffer)) != -1) {
          fos.write(buffer, 0, bytesRead);
        }
        fos.close();
      }
    } catch (IOException e) {
      return false;
    }
    return true;
  }

//  private void initFileDownload() {
//    try {
//      file = new File("D:/download.zip");
//      if (file.exists()) {
//        file.delete();
//      }
//      file.createNewFile();
//
//      FileWriter fw = new FileWriter(file, true);
//      BufferedWriter bw = new BufferedWriter(fw);
//      writer = new PrintWriter(bw);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  @PreDestroy
  public void finish() {
  }
}

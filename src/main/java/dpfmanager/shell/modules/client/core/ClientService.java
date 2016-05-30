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

  @PostConstruct
  public void init() {
    // No context yet
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
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error in url: " + parameters.get("-url")));
      return;
    }
    client.setId(id);
    send(client);
  }

  private void newCheckRequest(List<String> files, List<String> tmpFiles, Configuration config) {
    HttpClient client = new HttpClient(context, parameters.get("-url"));
    if (client.isError()) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error in url: " + parameters.get("-url")));
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
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot create the temporary configuration file"));
        return;
      }
      config.SaveFile(dest.getAbsolutePath());
      client.addConfig(dest);
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot create the temporary configuration file"));
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
      message = "This job is still running. Processeed " + map.get("processed") + " of " + map.get("total") + ".";
    } else if (map.get("status").equalsIgnoreCase("Finished")) {
      downloadFile(map.get("path"));
      return;
    } else if (map.get("status").equalsIgnoreCase("NotFound")) {
      message = "Cannot find a job with id: " + map.get("id");
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
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, map.get("error")));
    } else {
      // Everything OK
      if (parameters.containsKey("-w")) {
        context.sendAfter(BasicConfig.MODULE_CLIENT, new RequestMessage(RequestMessage.Type.ASK, map.get("id")), 1);
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Started job with id: " + map.get("id")));
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
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error sending the request to the server"));
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
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot download the report."));
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
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot unzip the report."));
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Report downloaded at: " + output));
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

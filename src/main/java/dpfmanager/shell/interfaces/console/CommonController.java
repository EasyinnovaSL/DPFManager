package dpfmanager.shell.interfaces.console;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.tools.zip.ZipEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipOutputStream;

/**
 * Created by Adrià Llorens on 14/10/2016.
 */
public class CommonController {

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The Dpf resourceBundle
   */
  private ResourceBundle bundle;

  /**
   * The parsed args
   */
  private Map<String, String> parameters;

  /**
   * The input files
   */
  private List<String> files;

  /**
   * Config related params
   */
  private Configuration config;
  private boolean xml;
  private boolean json;
  private boolean html;
  private boolean pdf;
  private String outputFolder;
  private boolean explicitFormats;
  private boolean explicitOutput;

  public CommonController(ConsoleContext c, ResourceBundle b) {
    config = null;
    explicitFormats = false;
    explicitOutput = false;
    context = c;
    bundle = b;
    files = new ArrayList<>();
    xml = true;
    html = true;
    parameters = (Map<String, String>) AppContext.getApplicationContext().getBean("parameters");
  }

  public void parseFiles(String arg){
    String arg_mod = arg;
    if (!new File(arg_mod).isAbsolute() && !new File(arg_mod).exists() && new File("../" + arg_mod).exists()) {
      arg_mod = "../" + arg;
    }
    files.add(arg_mod);
  }

  public boolean parseOutput(String output) {
    File tmp = new File(output);
    if (!tmp.exists()) {
      if (!tmp.mkdirs()) {
        printOut(bundle.getString("cannotOutput"));
        output = null;
      }
    } else if (!tmp.isDirectory()) {
      printOut(bundle.getString("outputMustDirectory"));
      output = null;
    } else if (tmp.listFiles().length > 0) {
      printOut(bundle.getString("outputMustEmpty"));
      output = null;
    }
    if (output != null) {
      setOutputFolder(output);
      setExplicitOutput(true);
      return true;
    }
    return false;
  }

  public boolean parseConfiguration(String xmlConfig) {
    Configuration configAux = new Configuration();
    try {
      configAux.ReadFile(xmlConfig);
      if (configAux.getFormats().size() == 0) {
        printOut(bundle.getString("missingReportFormat"));
        return false;
      } else {
        setConfig(configAux);
        return true;
      }
    } catch (Exception ex) {
      printOut(bundle.getString("incorrectConfigFile").replace("%1", xmlConfig));
      return false;
    }
  }

  public boolean parseFormats(String formats) {
    setXml(formats.contains("xml"));
    setJson(formats.contains("json"));
    setHtml(formats.contains("html"));
    setPdf(formats.contains("pdf"));
    String result = formats.replace("xml", "").replace("json", "").replace("html", "").replace("pdf", "").replace(",", "").replace("'", "");
    if (result.length() > 0) {
      printOut(bundle.getString("incorrectReportFormat"));
      return false;
    } else {
      setExplicitFormats(true);
      return true;
    }
  }

  public void parseConfig() {
    if (config != null) {
      if (explicitFormats) {
        config.getFormats().clear();
        if (xml) config.getFormats().add("XML");
        if (pdf) config.getFormats().add("PDF");
        if (html) config.getFormats().add("HTML");
        if (json) config.getFormats().add("JSON");
      }
      if (explicitOutput) {
        config.setOutput(outputFolder);
      }
    } else { //Create it
      config = new Configuration(null, null, new ArrayList<>());
      if (xml) config.getFormats().add("XML");
      if (pdf) config.getFormats().add("PDF");
      if (html) config.getFormats().add("HTML");
      if (json) config.getFormats().add("JSON");
      config.getIsos().add("Baseline");
      config.getIsos().add("Tiff/EP");
      config.getIsos().add("Tiff/IT");
      config.setOutput(outputFolder);
    }
  }

  public List<String> zipFolders() {
    List<String> tmpFiles = new ArrayList<>();
    for (String file : files) {
      if (new File(file).isDirectory()) {
        String zipPath = zipFolder(file);
        if (zipPath != null) {
          tmpFiles.add(zipPath);
        }
      }
    }
    return tmpFiles;
  }

  public String zipFolder(String folder) {
    try {
      if (!folder.endsWith("/") && !folder.endsWith("\\")){
        folder = folder + "/";
      }
      File outputFile = Files.createTempFile("input", ".zip").toFile();
      ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
      compressDirectoryToZipfile(folder, folder, zipFile);
      IOUtils.closeQuietly(zipFile);
      return outputFile.getAbsolutePath();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
    for (File file : new File(sourceDir).listFiles()) {
      if (file.isDirectory()) {
        compressDirectoryToZipfile(rootDir, sourceDir + file.getName() + File.separator, out);
      } else {
        ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName());
        out.putNextEntry(entry);

        FileInputStream in = new FileInputStream(sourceDir + file.getName());
        IOUtils.copy(in, out);
        IOUtils.closeQuietly(in);
      }
    }
  }

  /**
   * Parameters
   */
  public Map<String, String> getParameters() {
    return parameters;
  }

  public void putParameter(String key, String value){
    parameters.put(key, value);
  }

  public String getParameter(String key){
    return parameters.get(key);
  }

  public boolean containsParameter(String key){
    return parameters.containsKey(key);
  }

  public boolean containsParameters(List<String> keys){
    for (String key : keys){
      if (containsParameter(key)){
        return true;
      }
    }
    return false;
  }

  /**
   * Getters / Setters
   */
  public Configuration getConfig() {
    return config;
  }

  public void setConfig(Configuration config) {
    this.config = config;
  }

  public boolean isXml() {
    return xml;
  }

  public void setXml(boolean xml) {
    this.xml = xml;
  }

  public boolean isJson() {
    return json;
  }

  public void setJson(boolean json) {
    this.json = json;
  }

  public boolean isHtml() {
    return html;
  }

  public void setHtml(boolean html) {
    this.html = html;
  }

  public boolean isPdf() {
    return pdf;
  }

  public void setPdf(boolean pdf) {
    this.pdf = pdf;
  }

  public String getOutputFolder() {
    return outputFolder;
  }

  public void setOutputFolder(String outputFolder) {
    this.outputFolder = outputFolder;
  }

  public boolean isExplicitFormats() {
    return explicitFormats;
  }

  public void setExplicitFormats(boolean explicitFormats) {
    this.explicitFormats = explicitFormats;
  }

  public boolean isExplicitOutput() {
    return explicitOutput;
  }

  public void setExplicitOutput(boolean explicitOutput) {
    this.explicitOutput = explicitOutput;
  }

  public List<String> getFiles() {
    return files;
  }

  public void addFile(String file) {
    files.add(file);
  }

  /**
   * Custom print lines
   */
  private void printOut(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }
}

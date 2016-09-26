/**
 * <h1>ConsoleController.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.console;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.modules.client.messages.RequestMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicCheck;
import dpfmanager.shell.modules.periodic.core.Periodicity;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.tools.zip.ZipEntry;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrià Llorens on 11/04/2016.
 */
public class ConsoleController {

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  /**
   * The parsed args
   */
  private Map<String, String> parameters;

  // Config related params
  private String outputFolder;
  private List<String> files;
  private List<String> tmpFiles;
  private boolean xml;
  private boolean json;
  private boolean html;
  private boolean pdf;
  private Configuration config;
  private boolean explicitFormats;
  private boolean explicitOutput;
  private ResourceBundle bundle;

  public ConsoleController(ConsoleContext c, ResourceBundle r) {
    setDefault();
    context = c;
    bundle = r;
    tmpFiles = new ArrayList<>();
  }

  private void setDefault() {
    // Output formats
    xml = true;
    json = false;
    html = true;
    pdf = false;
    explicitFormats = false;
    // Output folder
    outputFolder = null;
    explicitOutput = false;
    // Configuration
    config = null;
  }

  /**
   * Main run function
   */
  public void run() {
    if (parameters.containsKey("-listperiodic")){
      // List periodicals checks
      context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.READ));
    } else if (parameters.containsKey("-addperiodic")){
      // Add periodical check
      PeriodicCheck info = parsePeriodicParameters(null);
      if (info != null){
        context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.SAVE, info));
      } else {
        printOut(bundle.getString("noSuchInfo"));
        displayHelp();
      }
    } else if (parameters.containsKey("-editperiodic")){
      // Edit periodical check
      String uuid = parameters.get("-editperiodic");
      PeriodicCheck info = parsePeriodicParameters(uuid);
      if (info != null){
        context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.EDIT, info));
      } else {
        printOut(bundle.getString("noSuchInfo"));
        displayHelp();
      }
    } else if (parameters.containsKey("-removeperiodic")){
      // Remove periodical check
      String uuid = parameters.get("-removeperiodic");
      context.send(BasicConfig.MODULE_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, uuid));
    } else if (!parameters.containsKey("-url")) {
      // Normal mode
      readConformanceChecker();
      parseConfig();
      context.send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(files, config));
    } else if (parameters.containsKey("-job")) {
      // Ask for job
      context.send(BasicConfig.MODULE_CLIENT, new RequestMessage(RequestMessage.Type.ASK, parameters.get("-job")));
    } else {
      // Remote check
      parseConfig();
      parseFolders();
      context.send(BasicConfig.MODULE_CLIENT, new RequestMessage(RequestMessage.Type.CHECK, files, tmpFiles, config));
    }
  }

  private PeriodicCheck parsePeriodicParameters(String uuid){
    PeriodicCheck check = new PeriodicCheck();
    if (uuid != null){
      check.setUuid(uuid);
    }

    // Input
    String input = "";
    for (String file : files){
      if (!input.isEmpty()){
        input += ";";
      }
      input += file;
    }
    if (input.isEmpty()){
      return null;
    }
    check.setInput(input);

    // Configuration
    String configuration = "";
    if (parameters.containsKey("-configuration")){
      configuration = getOnlyNameIfPossible(parameters.get("-configuration"));
    } else {
      printOut(bundle.getString("specifyConfiguration"));
      return null;
    }
    check.setConfiguration(configuration);

    // Time
    Periodicity periodicity = new Periodicity();
    String time = "00:00"; // Default value
    if (parameters.containsKey("-time") && isValidTime(parameters.get("-time"))){
      time = parameters.get("-time");
    }
    periodicity.setTime(LocalTime.parse(time));

    // Periodicity
    Periodicity.Mode mode;
    if (parameters.containsKey("-periodicity") && parseMode(parameters.get("-periodicity")) != null){
      mode = parseMode(parameters.get("-periodicity"));
    } else {
      printOut(bundle.getString("specifyPeriodicity"));
      return null;
    }
    periodicity.setMode(mode);
    if (mode.equals(Periodicity.Mode.WEEKLY)){
      String days = parameters.get("-extra");
      List<Integer> list = parseWeekDays(days);
      if (list == null || list.isEmpty()){
        printOut(bundle.getString("daysOfWeekError"));
        return null;
      }
      periodicity.setDaysOfWeek(list);
    } else if (mode.equals(Periodicity.Mode.MONTHLY)){
      Integer day = Integer.parseInt(parameters.get("-extra"));
      if (day < 1 || day > 28){
        printOut(bundle.getString("dayOfMonthError"));
        return null;
      }
      periodicity.setDayOfMonth(day);
    }
    check.setPeriodicity(periodicity);

    return check;
  }

  // Return the name if the configuration is into configs folder
  private String getOnlyNameIfPossible(String configuration){
    File configFile = new File(configuration);
    File configDir = new File(DPFManagerProperties.getConfigDir());
    if (configFile.getParentFile().equals(configDir)){
      return configFile.getName().replace(".dpf","");
    } else {
      return configuration;
    }
  }

  private List<Integer> parseWeekDays(String days){
    List<Integer> list = new ArrayList<>();
    for (String day : days.split(",")){
      Integer dayInt = Integer.parseInt(day);
      if (dayInt < 1 || dayInt > 7){
        return null;
      }
      list.add(dayInt);
    }
    return list;
  }

  private Periodicity.Mode parseMode(String mode){
    switch (mode){
      case "D":
        return Periodicity.Mode.DAILY;
      case "W":
        return Periodicity.Mode.WEEKLY;
      case "M":
        return Periodicity.Mode.MONTHLY;
      case "d":
        return Periodicity.Mode.DAILY;
      case "w":
        return Periodicity.Mode.WEEKLY;
      case "m":
        return Periodicity.Mode.MONTHLY;
    }
    return null;
  }

  private boolean isValidTime(String time){
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      sdf.parse(time);
      return true;
    } catch (ParseException ex) {
      return false;
    }
  }

  private void parseConfig() {
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

  private void parseFolders() {
    for (String file : files) {
      if (new File(file).isDirectory()) {
        String zipPath = zipFolder(file);
        if (zipPath != null) {
          tmpFiles.add(zipPath);
        }
      }
    }
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
   * Read conformance checker.
   */
  private void readConformanceChecker() {
    String xml = TiffConformanceChecker.getConformanceCheckerOptions();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setNamespaceAware(true);
    try {
      Document doc = loadXmlFromString(xml);

      NodeList name = doc.getElementsByTagName("name");
      if (name != null && name.getLength() > 0) {
        NodeList subList = name.item(0).getChildNodes();
        if (subList != null && subList.getLength() > 0) {
          printOut(bundle.getString("confCheck").replace("%1", subList.item(0).getNodeValue()));
        }
      }

      printOut(bundle.getString("extensions"));
      NodeList extensions = doc.getElementsByTagName("extension");
      String extensionsStr = "";
      if (extensions != null && extensions.getLength() > 0) {
        for (int i = 0; i < extensions.getLength(); i++) {
          NodeList subList = extensions.item(i).getChildNodes();
          if (subList != null && subList.getLength() > 0) {
            if (i > 0) {
              extensionsStr += ", ";
            }
            extensionsStr += subList.item(0).getNodeValue();
          }
        }
      }
      printOut(extensionsStr);

      NodeList standards = doc.getElementsByTagName("standard");
      if (standards != null && standards.getLength() > 0) {
        for (int i = 0; i < standards.getLength(); i++) {
          NodeList nodes = standards.item(i).getChildNodes();
          String stdName = "";
          String desc = "";
          for (int j = 0; j < nodes.getLength(); j++) {
            if (nodes.item(j).getNodeName().equals("name")) {
              stdName = nodes.item(j).getTextContent();
            } else if (nodes.item(j).getNodeName().equals("description")) {
              desc = nodes.item(j).getTextContent();
            }
          }
          printOut(bundle.getString("standard").replace("%1",stdName).replace("%2",desc));
        }
      }
      printOut("");
    } catch (Exception e) {
      printOut(bundle.getString("failedCC").replace("%1", e.getMessage()));
    }
  }

  /**
   * Load XML from string.
   *
   * @param xml the XML
   * @return the document
   * @throws Exception the exception
   */
  private Document loadXmlFromString(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    return builder.parse(is);
  }

  /**
   * Shows program usage.
   */
  public void displayHelp() {
    printOut("");
    printOut(bundle.getString("help1"));
    printOut(bundle.getString("help2"));
    printOut("");
    printOptions("helpO", 6);
    printOut("");
    printOptions("helpC", 7);
    printOut("");
    printOptions("helpR", 4);
    printOut("");
    printOptions("helpP", 6);
    printOut("        " + bundle.getString("helpP61"));
    printOut("        " + bundle.getString("helpP62"));
    printOut("    "+bundle.getString("helpP7"));
  }

  public void printOptions(String prefix, int max) {
    printOut(bundle.getString(prefix+"1"));
    for (int i = 2; i<=max; i++){
      printOut("    "+bundle.getString(prefix+i));
    }
  }

  /**
   * Shows program version.
   */
  public void displayVersion() {
    printOut(bundle.getString("dpfVersion").replace("%1",DPFManagerProperties.getVersion()));
  }

  /**
   * Setters
   */
  public void setOutputFolder(String outputFolder) {
    this.outputFolder = outputFolder;
  }

  public void setXml(boolean xml) {
    this.xml = xml;
  }

  public void setJson(boolean json) {
    this.json = json;
  }

  public void setHtml(boolean html) {
    this.html = html;
  }

  public void setPdf(boolean pdf) {
    this.pdf = pdf;
  }

  public void setConfig(Configuration config) {
    this.config = config;
  }

  public void setFiles(ArrayList<String> files) {
    this.files = files;
  }

  public void setExplicitFormats(boolean explicitFormats) {
    this.explicitFormats = explicitFormats;
  }

  public void setExplicitOutput(boolean explicitOutput) {
    this.explicitOutput = explicitOutput;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  /**
   * Custom print lines
   */
  private void printOut(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private void printErr(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  private void printException(Exception ex) {
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("exception"), ex));
  }
}

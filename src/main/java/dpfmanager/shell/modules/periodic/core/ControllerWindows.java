package dpfmanager.shell.modules.periodic.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrià Llorens on 07/07/2016.
 */
public class ControllerWindows extends Controller {

  private DpfContext context;
  private ResourceBundle bundle;

  public ControllerWindows(DpfContext context, ResourceBundle bundle) {

  }

  @Override
  public boolean savePeriodicalCheck(PeriodicCheck check) {
    try {
      createIfNotExistsVBS();
      String parsedInput = parseInput(check.getInput());
      String configPath = asString(getConfigurationPath(check.getConfiguration()));
      String params = " -s -configuration " + configPath + " " + parsedInput;
      String exe = asString(getVBSPath());
      String dpfCommand = exe + params;
      String command = "";
      Periodicity periodicity = check.getPeriodicity();
      switch (periodicity.getMode()) {
        case DAILY:
          command = "schtasks /create /tn \"" + check.getUuid() + "\" /tr \"" + dpfCommand + "\" /sc daily /st " + periodicity.getTimeString();
          break;
        case WEEKLY:
          command = "schtasks /create /tn \"" + check.getUuid() + "\" /tr \"" + dpfCommand + "\" /sc weekly /d " + parseDaysOfWeek(periodicity.getDaysOfWeek()) + " /st " + periodicity.getTimeString();
          break;
        case MONTHLY:
          command = "schtasks /create /tn \"" + check.getUuid() + "\" /tr \"" + dpfCommand + "\" /sc monthly /d " + periodicity.getDayOfMonth() + " /st " + periodicity.getTimeString();
          break;
      }

      // Run command
      Process proc = Runtime.getRuntime().exec(command);
      proc.waitFor();
      return (proc.exitValue() == 0);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean deletePeriodicalCheck(String uuid) {
    try {
      createIfNotExistsVBS();
      String command = "schtasks /delete /f /tn \"" + uuid + "\"";
      Process proc = Runtime.getRuntime().exec(command);
      proc.waitFor();
      return (proc.exitValue() == 0);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<PeriodicCheck> readPeriodicalChecks() {
    try {
      // WINDOWS
      String command = "schtasks /query /xml";
      Process proc = Runtime.getRuntime().exec(command);
      return readProcessOutput(proc);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ArrayList<PeriodicCheck>();
  }

  private List<PeriodicCheck> readProcessOutput(Process process) throws IOException {
    List<PeriodicCheck> list = new ArrayList<>();
    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    String xmlTask = "";
    String uuid = "";
    boolean readingDpfTask = false;
    while ((line = in.readLine()) != null) {
      if (readingDpfTask) {
        xmlTask += line;
        if (line.startsWith("</Task>")) {
          PeriodicCheck pc = parseXmlString(xmlTask, uuid);
          if (pc != null) {
            list.add(pc);
          }
          readingDpfTask = false;
          xmlTask = "";
        }
      } else if (line.startsWith("<!-- \\dpf")) {
        readingDpfTask = true;
        uuid = line.substring(6, line.length() - 4);
      }
    }
    in.close();
    return list;
  }

  public PeriodicCheck parseXmlString(String xmlTask, String uuid) {
    try {
      InputStream is = IOUtils.toInputStream(xmlTask, "UTF-16");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);

      // Parse input & configuration
      String configuration = "";
      String input = "";
      String arguments = doc.getDocumentElement().getElementsByTagName("Arguments").item(0).getTextContent();
      String aux = arguments.substring(18); // Skip -s -configuration
      String[] filesList = aux.split("\"");
      boolean first = true;
      for (String file : filesList) {
        if (!file.replaceAll(" ", "").isEmpty()) {
          if (first) {
            // Configuration
            configuration = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".dpf"));
            first = false;
          } else {
            // Input
            if (input.isEmpty()) {
              input = file;
            } else {
              input += ";" + file;
            }
          }
        }
      }

      // Parse periodicity
      Periodicity periodicity = new Periodicity();
      Periodicity.Mode mode;
      NodeList schDay = doc.getDocumentElement().getElementsByTagName("ScheduleByDay");
      NodeList schWeek = doc.getDocumentElement().getElementsByTagName("ScheduleByWeek");
      NodeList schMonth = doc.getDocumentElement().getElementsByTagName("ScheduleByMonth");
      if (schDay.getLength() > 0) {
        periodicity.setMode(Periodicity.Mode.DAILY);
      } else if (schWeek.getLength() > 0) {
        periodicity.setMode(Periodicity.Mode.WEEKLY);
        Element elemWeek = (Element) schWeek.item(0);
        Node node = elemWeek.getElementsByTagName("DaysOfWeek").item(0);
        NodeList childs = node.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
          Node child = childs.item(i);
          if (child.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) child;
            periodicity.addDaysOfWeek(parseDayName(elem.getTagName()));
          }
        }
      } else if (schMonth.getLength() > 0) {
        periodicity.setMode(Periodicity.Mode.MONTHLY);
        Element elem = (Element) schMonth.item(0);
        String dayStr = elem.getElementsByTagName("Day").item(0).getTextContent();
        periodicity.setDayOfMonth(Integer.parseInt(dayStr));
      }

      // Parse time
      String timeStr = doc.getDocumentElement().getElementsByTagName("StartBoundary").item(0).getTextContent();
      String time = timeStr.substring(11, 16);
      periodicity.setTime(LocalTime.parse(time));

      is.close();
      return new PeriodicCheck(uuid, input, configuration, periodicity);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Windows VBS file
   */

  private void createIfNotExistsVBS() {
    File vbsFile = new File(getVBSPath());
    if (!vbsFile.exists()) {
      try {
        InputStream in = getClass().getResourceAsStream("/rundpf.vbs");
        OutputStream out = new FileOutputStream(new File(getVBSPath()));

        int read;
        byte[] bytes = new byte[1024];
        while ((read = in.read(bytes)) != -1) {
          out.write(bytes, 0, read);
        }
        out.close();
        in.close();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorVBS")));
      }
    }
  }

  private String getVBSPath() {
    return new File(DPFManagerProperties.getDataDir() + "/rundpf.vbs").getAbsolutePath();
  }

}

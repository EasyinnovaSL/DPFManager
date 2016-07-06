package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class PeriodicalController extends DpfController<PeriodicalModel, PeriodicalView> {

  public PeriodicalController() {
  }

  /**
   * Read periodical checks from System tasks
   */
  public void readPeriodicalChecksWindows() {
    try {
      // WINDOWS
      String command = "schtasks /query /xml";
      Process proc = Runtime.getRuntime().exec(command);
      readProcessOutput(proc);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void readProcessOutput(Process process) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    String xmlTask = "";
    String uuid = "";
    boolean readingDpfTask = false;
    while ((line = in.readLine()) != null) {
      if (readingDpfTask) {
        xmlTask += line;
        if (line.startsWith("</Task>")) {
          getModel().parseXmlString(xmlTask, uuid);
          readingDpfTask = false;
          xmlTask = "";
        }
      } else if (line.startsWith("<!-- \\dpf")) {
        readingDpfTask = true;
        uuid = line.substring(6, line.length() - 4);
      }
    }
    in.close();
  }

  /**
   * Delete function
   */
  public boolean deletePeriodicalCheck(String id) {
    if (System.getProperty("os.name").startsWith("Windows")) {
      return deleteWindowsTask(id);
    } else {
      return deleteUnixCron(id);
    }
  }

  private boolean deleteWindowsTask(String id) {
    try {
      // WINDOWS
      createIfNotExistsVBS();
      String command = "schtasks /delete /f /tn \"" + id + "\"";
      // Run command
      Process proc = Runtime.getRuntime().exec(command);
      proc.waitFor();
      return (proc.exitValue() == 0);
    } catch (Exception e) {
      return false;
    }
  }

  private boolean deleteUnixCron(String id) {
    return false;
  }

  /**
   * Save function
   */
  public boolean savePeriodicalCheck(String id, String input, String configuration, Periodicity periodicity, boolean needDelete) {
    // First delete if needed
    boolean ok = true;
    if (needDelete) {
      ok = deletePeriodicalCheck(id);
    }

    if (ok) {
      String parsedInput = parseInput(input);
      String configPath = asString(getConfigurationPath(configuration));
      String params = " -s -configuration " + configPath + " " + parsedInput;
      // System dependent
      if (System.getProperty("os.name").startsWith("Windows")) {
        return saveWindowsTask(id, params, periodicity, needDelete);
      } else {
        return saveUnixCron(params, periodicity);
      }
    } else {
      return false;
    }
  }

  private boolean saveWindowsTask(String id, String params, Periodicity periodicity, boolean needDelete) {
    try {
      // WINDOWS
      createIfNotExistsVBS();
      String exe = asString(getVBSPath());
      String dpfCommand = exe + params;
      String command = "";
      switch (periodicity.getMode()) {
        case DAILY:
          command = "schtasks /create /tn \"" + id + "\" /tr \"" + dpfCommand + "\" /sc daily /st " + periodicity.getTimeString();
          break;
        case WEEKLY:
          command = "schtasks /create /tn \"" + id + "\" /tr \"" + dpfCommand + "\" /sc weekly /d " + parseDaysOfWeek(periodicity.getDaysOfWeek()) + " /st " + periodicity.getTimeString();
          System.out.println(command);
          break;
        case MONTHLY:
          command = "schtasks /create /tn \"" + id + "\" /tr \"" + dpfCommand + "\" /sc monthly /d " + periodicity.getDayOfMonth() + " /st " + periodicity.getTimeString();
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

  private boolean saveUnixCron(String params, Periodicity periodicity) {
    return false;
  }

  private String parseDaysOfWeek(List<Integer> days) {
    String ret = "";
    for (Integer day : days) {
      if (!ret.isEmpty()) {
        ret += ",";
      }
      switch (day) {
        case 1:
          ret += "MON";
          break;
        case 2:
          ret += "TUE";
          break;
        case 3:
          ret += "WED";
          break;
        case 4:
          ret += "THU";
          break;
        case 5:
          ret += "FRI";
          break;
        case 6:
          ret += "SAT";
          break;
        case 7:
          ret += "SUN";
          break;
      }
    }
    return ret;
  }

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
        getContext().send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, getBundle().getString("errorVBS")));
      }
    }
  }

  private String getVBSPath() {
    return new File(DPFManagerProperties.getDataDir() + "/rundpf.vbs").getAbsolutePath();
  }

  private String parseInput(String input) {
    String result = "";
    String[] files = input.split(";");
    for (String file : files) {
      result += asString(file) + " ";
    }
    return result;
  }

  private String getConfigurationPath(String config) {
    return DPFManagerProperties.getConfigDir() + "/" + config + ".dpf";
  }

  private String asString(String str) {
    return "\\\"" + str + "\\\"";
  }

}

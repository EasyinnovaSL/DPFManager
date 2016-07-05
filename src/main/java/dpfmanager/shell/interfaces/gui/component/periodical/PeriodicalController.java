package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class PeriodicalController extends DpfController<PeriodicalModel, PeriodicalView> {

  public PeriodicalController() {
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
      getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, getBundle().getString("errorDeleteCron")));
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
          command = "schtasks /create /tn \"" + id + "\" /tr \"" + dpfCommand + "\" /sc weekly /d " + parseDayOfWeek(periodicity.getDayOfWeek()) + " /st " + periodicity.getTimeString();
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

  private String parseDayOfWeek(Integer day) {
    switch (day) {
      case 1:
        return "MON";
      case 2:
        return "TUE";
      case 3:
        return "WED";
      case 4:
        return "THU";
      case 5:
        return "FRI";
      case 6:
        return "SAT";
      case 7:
        return "SUN";
    }
    return "";
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

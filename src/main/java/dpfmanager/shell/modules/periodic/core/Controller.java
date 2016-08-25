package dpfmanager.shell.modules.periodic.core;

import dpfmanager.shell.core.DPFManagerProperties;

import java.io.File;
import java.util.List;

/**
 * Created by Adrià Llorens on 07/07/2016.
 */
public abstract class Controller {

  public abstract boolean savePeriodicalCheck(PeriodicCheck check);

  public boolean editPeriodicalCheck(PeriodicCheck check){
    if (deletePeriodicalCheck(check.getUuid())) {
      return savePeriodicalCheck(check);
    } else {
      return false;
    }
  }

  public abstract boolean deletePeriodicalCheck(String uuid);

  public abstract List<PeriodicCheck> readPeriodicalChecks();

  protected String parseDaysOfWeek(List<Integer> days) {
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

  protected String parseInput(String input) {
    String result = "";
    String[] files = input.split(";");
    for (String file : files) {
      result += asString(file) + " ";
    }
    return result;
  }

  protected String getConfigurationPath(String config) {
    File file = new File(config);
    if (file.exists()){
      return file.getAbsolutePath();
    } else {
      return DPFManagerProperties.getConfigDir() + "/" + config + ".dpf";
    }
  }

  protected String asString(String str) {
    return "\\\"" + str + "\\\"";
  }

  protected Integer parseDayName(String name) {
    switch (name) {
      case "Monday":
        return 1;
      case "Tuesday":
        return 2;
      case "Wednesday":
        return 3;
      case "Thursday":
        return 4;
      case "Friday":
        return 5;
      case "Saturday":
        return 6;
      case "Sunday":
        return 7;
      default:
        return -1;
    }
  }

  protected String buildCommandArguments(PeriodicCheck check){
    String parsedInput = parseInput(check.getInput());
    String configPath = asString(getConfigurationPath(check.getConfiguration()));
    return "-s -configuration " + configPath + " " + parsedInput;
  }

  protected String getInputFromArguments(String arguments){
    String input = "";
    String aux = arguments.substring(18); // Skip -s -configuration
    String[] files = aux.split("\"");
    boolean first = true;
    for (String file : files) {
      if (!file.replaceAll(" ", "").isEmpty()) {
        if (first) {
          // Configuration
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
    return input;
  }

  protected String getConfigurationFromArguments(String arguments){
    String aux = arguments.substring(18); // Skip -s -configuration
    String[] files = aux.split("\"");
    for (String file : files) {
      if (!file.replaceAll(" ", "").isEmpty()) {
        // Configuration
        return file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".dpf"));
      }
    }
    return "";
  }

}

package dpfmanager.shell.modules.periodic.core;

import dpfmanager.shell.core.context.DpfContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 07/07/2016.
 */
public class ControllerLinux extends Controller {

  private DpfContext context;
  private ResourceBundle bundle;

  public ControllerLinux(DpfContext context, ResourceBundle bundle) {
    this.context = context;
    this.bundle = bundle;
  }

  @Override
  public boolean savePeriodicalCheck(PeriodicCheck check) {
    try {
      String lineUuid = "# "+check.getUuid();
      String line = toCronLine(check);
      if (addlineToCrontab(lineUuid)){
        return addlineToCrontab(line);
      }
    } catch (Exception e){
    }
    return false;
}

  @Override
  public boolean deletePeriodicalCheck(String uuid) {
    List<String> lines = readFullCrontab();
    int index = 0;
    int found = -1;
    // Search
    while (index < lines.size() && found == -1){
      if (lines.get(index).contains(uuid)){
        found = index;
      }
      index++;
    }
    // Delete
    if (found != -1){
      lines.remove(found+1);
      lines.remove(found);
    }
    // Write
    return writeFullCrontab(lines);
  }

  @Override
  public List<PeriodicCheck> readPeriodicalChecks() {
    List<PeriodicCheck> checks = new ArrayList<>();
    List<String> lines = readFullCrontab();
    int index = 0;
    while (index < lines.size()){
      String line = lines.get(index);
      if (line.startsWith("# dpf-")){
        index++;
        String uuid = line.substring(2);
        checks.add(fromCronLine(uuid, lines.get(index)));
      }
      index++;
    }
    return checks;
  }

  private String toCronLine(PeriodicCheck check) {
    String dpfCommand = "dpf-manager " + buildCommandArguments(check);
    String line = "";
    Periodicity periodicity = check.getPeriodicity();
    switch (periodicity.getMode()) {
      case DAILY:
        line = periodicity.getTime().getMinute() + " " + periodicity.getTime().getHour() + " * * * " + dpfCommand;
        break;
      case WEEKLY:
        line = periodicity.getTime().getMinute() + " " + periodicity.getTime().getHour() + " * * " + parseDaysOfWeekLinux(periodicity.getDaysOfWeek()) + " " + dpfCommand;
        break;
      case MONTHLY:
        line = periodicity.getTime().getMinute() + " " + periodicity.getTime().getHour() + " " +periodicity.getDayOfMonth() + " * * " + dpfCommand;
        break;
    }
    return line;
  }

  private PeriodicCheck fromCronLine(String uuid, String line) {
    String[] parts = line.split(" ");
    String minutes = parts[0];
    if (minutes.length() == 1){
      minutes = "0"+minutes;
    }
    String hours = parts[1];
    if (hours.length() == 1){
      hours = "0"+hours;
    }
    String dayOfMonth = parts[2];
    String dayOfWeek = parts[4];

    String command = parts[5];
    String arguments = line.substring(line.indexOf(command) + command.length()+1);

    // Parse input & configuration
    String input = getInputFromArguments(arguments);
    String configuration = getConfigurationFromArguments(arguments);

    // Parse periodicity
    Periodicity periodicity = new Periodicity();
    if (!dayOfMonth.equals("*")){
      periodicity.setMode(Periodicity.Mode.MONTHLY);
      periodicity.setDayOfMonth(Integer.parseInt(dayOfMonth));
    } else if (!dayOfWeek.equals("*")){
      periodicity.setMode(Periodicity.Mode.WEEKLY);
      periodicity.setDaysOfWeek(parseDaysList(dayOfWeek));
    } else {
      periodicity.setMode(Periodicity.Mode.DAILY);
    }
    periodicity.setTime(LocalTime.parse(hours+":"+minutes));

    return new PeriodicCheck(uuid, input, configuration, periodicity);
  }

  private List<Integer> parseDaysList(String days){
    List<Integer> ints = new ArrayList<>();
    for (String day : days.split(",")){
      ints.add(Integer.parseInt(day));
    }
    return ints;
  }

  private List<String> readFullCrontab(){
    List<String> lines = new ArrayList<>();
    try {
      Process process = Runtime.getRuntime().exec("crontab -l");
      BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        lines.add(line);
      }
      in.close();
      process.waitFor();
    } catch (Exception e){
    }
    return lines;
  }

  private boolean writeFullCrontab(List<String> lines){
    boolean ret = false;
    if (clearCronTab()) {
      if (lines.isEmpty()){
        return true;
      }
      for (String line : lines) {
        ret = addlineToCrontab(line);
      }
    }
    return ret;
  }

  private boolean addlineToCrontab(String line){
    try {
      String command = "(crontab -l 2>/dev/null; echo \""+line+"\") | crontab -";
      String[] cmd = {
          "/bin/sh",
          "-c",
          command
      };
      Process process = Runtime.getRuntime().exec(cmd);
      process.waitFor();
      return (process.exitValue() == 0);
    } catch (Exception e){
      return false;
    }
  }

  private boolean clearCronTab(){
    try {
      Process process = Runtime.getRuntime().exec("crontab -r");
      process.waitFor();
      return (process.exitValue() == 0);
    } catch (Exception e){
      return false;
    }
  }

  protected String parseDaysOfWeekLinux(List<Integer> days) {
    String ret = "";
    for (Integer day : days) {
      if (!ret.isEmpty()) {
        ret += ",";
      }
      ret += day;
    }
    return ret;
  }

}

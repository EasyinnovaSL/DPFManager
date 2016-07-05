package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.modules.database.tables.Crons;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 01/07/2016.
 */
public class Periodicity {

  public enum Mode {
    DAILY, WEEKLY, MONTHLY
  }

  private Mode mode;
  private LocalTime time;
  private String dayOfWeekString;
  private Integer dayOfWeek;
  private Integer dayOfMonth;

  public Periodicity(Mode mode, LocalTime time) {
    this.time = time;
    this.mode = mode;
  }

  public Periodicity(Crons cron) {
    time = LocalTime.parse(cron.getPeriodTime());
    switch (cron.getPeriodMode()){
      case 0:
        mode = Mode.DAILY;
        break;
      case 1:
        mode = Mode.WEEKLY;
        break;
      case 2:
        mode = Mode.MONTHLY;
        break;
    }
    if (cron.getSpecWeekly() != -1){
      dayOfWeek = cron.getSpecWeekly();
    }
    if (cron.getSpecMonthly() != -1){
      dayOfMonth = cron.getSpecMonthly();
    }
  }

  public void setDayOfMonth(Integer dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }

  public void setDayOfWeek(Integer dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public void setDayOfWeekString(String dayOfWeekString) {
    this.dayOfWeekString = dayOfWeekString;
  }

  public Mode getMode() {
    return mode;
  }

  public LocalTime getTime() {
    return time;
  }

  public String getTimeString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    return formatter.format(time);
  }

  public Integer getDayOfWeek() {
    return dayOfWeek;
  }

  public Integer getDayOfMonth() {
    return dayOfMonth;
  }

  public String getDayOfWeekString(ResourceBundle bundle) {
    if (dayOfWeekString == null){
      dayOfWeekString = parseFromNumber(dayOfWeek, bundle);
    }
    return dayOfWeekString;
  }

  private String parseFromNumber(int x, ResourceBundle bundle){
    switch (x){
      case 1:
        return bundle.getString("monday");
      case 2:
        return bundle.getString("tuesday");
      case 3:
        return bundle.getString("wednesday");
      case 4:
        return bundle.getString("thursday");
      case 5:
        return bundle.getString("friday");
      case 6:
        return bundle.getString("saturday");
      case 7:
        return bundle.getString("sunday");
    }
    return "";
  }
}

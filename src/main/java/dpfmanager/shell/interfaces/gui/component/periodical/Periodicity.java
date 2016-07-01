package dpfmanager.shell.interfaces.gui.component.periodical;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

  public String getDayOfWeekString() {
    return dayOfWeekString;
  }
}

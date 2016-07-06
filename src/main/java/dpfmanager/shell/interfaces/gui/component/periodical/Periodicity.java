package dpfmanager.shell.interfaces.gui.component.periodical;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
  private List<Integer> daysOfWeek;
  private Integer dayOfMonth;

  public Periodicity() {
    daysOfWeek = new ArrayList<>();
  }

  public Periodicity(Mode mode, LocalTime time) {
    daysOfWeek = new ArrayList<>();
    this.time = time;
    this.mode = mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public void setDayOfMonth(Integer dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
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

  public void addDaysOfWeek(Integer day){
    daysOfWeek.add(day);
  }

  public void setDaysOfWeek(List<Integer> daysOfWeek) {
    this.daysOfWeek = daysOfWeek;
  }

  public List<Integer> getDaysOfWeek() {
    return daysOfWeek;
  }

  public Integer getDayOfMonth() {
    return dayOfMonth;
  }

  public String getDayOfWeekString(ResourceBundle bundle) {
    String ret = "";
    for (Integer day : daysOfWeek){
      if (!ret.isEmpty()){
        ret += ", ";
      }
      ret += parseFromNumber(day, bundle);
    }
    return ret;
  }

  private String parseFromNumber(int x, ResourceBundle bundle) {
    switch (x) {
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

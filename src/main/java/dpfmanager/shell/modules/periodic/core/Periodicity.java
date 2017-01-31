/**
 * <h1>Periodicity.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */


package dpfmanager.shell.modules.periodic.core;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 01/07/2016.
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

  public void addDaysOfWeek(Integer day) {
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
    for (Integer day : daysOfWeek) {
      if (!ret.isEmpty()) {
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

  /**
   * Custom to string
   */
  public String toString(ResourceBundle bundle) {
    switch (getMode()) {
      case DAILY:
        return bundle.getString("dailyInfo").replace("%1", getTimeString());
      case WEEKLY:
        return bundle.getString("weeklyInfo").replace("%1", getDayOfWeekString(bundle)).replace("%2", getTimeString());
      case MONTHLY:
        return bundle.getString("monthlyInfo").replace("%1", getDayOfMonth().toString()).replace("%2", getTimeString());
    }
    return "";
  }
}

/**
 * <h1>PeriodicCheck.java</h1> <p> This program is free software: you can redistribute it
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


import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 01/07/2016.
 */
public class PeriodicCheck {

  private static String EL = "\n";

  private String uuid;
  private String input;
  private String configuration;
  private Periodicity periodicity;

  public PeriodicCheck() {
    uuid = "dpf-" + System.currentTimeMillis();
    input = null;
    configuration = null;
    periodicity = null;
  }

  public PeriodicCheck(String uuid, String input, String configuration, Periodicity periodicity) {
    this.uuid = uuid;
    this.input = input;
    this.configuration = configuration;
    this.periodicity = periodicity;
  }

  /**
   * Parse from params
   */
  public boolean parse(Map<String, String> parameters, List<String> files, DpfContext context, ResourceBundle bundle) {
    uuid = "dpf-" + System.currentTimeMillis();
    input = null;
    configuration = null;
    periodicity = null;

    // Input
    String input = "";
    for (String file : files){
      if (!input.isEmpty()){
        input += ";";
      }
      input += file;
    }
    if (input.isEmpty()){
      return false;
    }
    setInput(input);

    // Configuration
    String configuration = "";
    if (parameters.containsKey("-configuration")){
      configuration = getOnlyNameIfPossible(parameters.get("-configuration"));
    } else {
      printOut(context, bundle.getString("specifyConfiguration"));
      return false;
    }
    setConfiguration(configuration);

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
      printOut(context, bundle.getString("specifyPeriodicity"));
      return false;
    }
    periodicity.setMode(mode);
    if (mode.equals(Periodicity.Mode.WEEKLY)){
      String days = parameters.get("-extra");
      List<Integer> list = parseWeekDays(days);
      if (list == null || list.isEmpty()){
        printOut(context, bundle.getString("daysOfWeekError"));
        return false;
      }
      periodicity.setDaysOfWeek(list);
    } else if (mode.equals(Periodicity.Mode.MONTHLY)){
      Integer day = Integer.parseInt(parameters.get("-extra"));
      if (day < 1 || day > 28){
        printOut(context, bundle.getString("dayOfMonthError"));
        return false;
      }
      periodicity.setDayOfMonth(day);
    }
    setPeriodicity(periodicity);
    return true;
  }

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

  /**
   * Getters and setters
   */

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public Periodicity getPeriodicity() {
    return periodicity;
  }

  public void setPeriodicity(Periodicity periodicity) {
    this.periodicity = periodicity;
  }

  /**
   * To String
   */
  public String toString(ResourceBundle bundle) {
    String text = "ID: " + uuid + EL;
    text += "   " + bundle.getString("input") + " " + input + EL;
    text += "   " + bundle.getString("configuration") + " " + configuration + EL;
    text += "   " + bundle.getString("periodicity") + " " + periodicity.toString(bundle) + EL;
    return text;
  }

  private void printOut(DpfContext context, String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }
}

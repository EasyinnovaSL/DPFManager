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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.periodic.core;


import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 01/07/2016.
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
}

/**
 * <h1>DpFManagerConstants.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.core;

/**
 * Created by Adria Llorens on 05/05/2016.
 */
public class DpFManagerConstants {

  /**
   * The minimum width
   */
  public static final int MIN_WIDTH = 400;

  /**
   * The window width
   */
  public static final int WINDOW_WIDTH = 1040;

  /**
   * The initial width of the main panel
   */
  public static final int WIDTH = 970;

  /**
   * The initial height
   */
  public static final int HEIGHT = 950;

  /**
   * The maximum number of simultaneous checks
   */
  public static final int MAX_CHECKS = 3;

  /**
   * Database update intervalr in milliseconds
   */
  public static final Integer UPDATE_INTERVAL = 1000;

  /**
   * Hours that a job is in database
   */
  public static final Integer JOB_LIFE_HOURS = 48;

  /**
   * Minutes that a job is running without any change in DB
   */
  public static final Integer JOB_LAST_UPDATE_MINUTES = 30;

  /**
   * The database version, incress it for force delete tables and recreate
   */
  public static final int DATABASE_VERSION = 4;

  /**
   * The configuration xml version
   */
  public static final int CONFIGURATION_VERSION = 3;

}

/**
 * <h1>Jobs.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class Jobs {

  /**
   * String constants
   */

  // Table name
  public static String TABLE = "jobs";

  // Fields
  public static String ID = "id";
  public static String HASH = "hash";
  public static String STATE = "state";
  public static String TOTAL_FILES = "totalFiles";
  public static String PROCESSED_FILES = "processedFiles";
  public static String INIT = "init";
  public static String FINISH = "finish";
  public static String INPUT = "input";
  public static String ORIGIN = "origin";
  public static String PID = "pid";
  public static String OUTPUT = "output";
  public static String TIME = "time";
  public static String LAST_UPDATE = "lastUpdate";

  // Indexs
  public static String INDEX_ID = "index_id";
  public static String INDEX_PID = "index_pid";

  // Create SQL query
  public static String createSql = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
      "    " + ID + " BIGINT NOT NULL PRIMARY KEY," +
      "    " + HASH + " VARCHAR(255)," +
      "    " + STATE + " INTEGER," +
      "    " + TOTAL_FILES + " INTEGER," +
      "    " + PROCESSED_FILES + " INTEGER," +
      "    " + INIT + " BIGINT," +
      "    " + FINISH + " BIGINT," +
      "    " + INPUT + " VARCHAR(10000)," +
      "    " + ORIGIN + " VARCHAR(10)," +
      "    " + PID + " INTEGER NOT NULL," +
      "    " + OUTPUT + " VARCHAR(255)," +
      "    " + TIME + " BIGINT," +
      "    " + LAST_UPDATE + " BIGINT" +
      ");";

  // Index SQl querys
  public static String indexIdSql = "CREATE INDEX IF NOT EXISTS " + INDEX_ID + " ON " + TABLE + "(" + ID + ");";
  public static String indexPidSql = "CREATE INDEX IF NOT EXISTS " + INDEX_PID + " ON " + TABLE + "(" + PID + ");";

  // Delete SQL query
  public static String deleteSql = "DROP TABLE IF EXISTS " + TABLE + ";";
  public static String delIndexIdSql = "DROP INDEX IF EXISTS " + INDEX_ID + ";";
  public static String delIndexPidSql = "DROP INDEX IF EXISTS " + INDEX_PID + ";";

  // Insert SQL query
  public static String insertJobSql = "INSERT INTO " + TABLE + "(" + ID + "," + HASH + "," + STATE + "," + TOTAL_FILES + "," + PROCESSED_FILES + "," + INIT + "," + FINISH + "," + INPUT + "," + ORIGIN + "," + PID + "," + OUTPUT + "," + TIME + "," + LAST_UPDATE + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";


  /**
   * Object properties
   */

  private Long id;
  private String hash;
  private Integer state;
  private Integer totalFiles;
  private Integer processedFiles;
  private Long init;
  private Long finish;
  private String input;
  private String origin;
  private Integer pid;
  private String output;
  private Long time;
  private Long lastUpdate;

  public Jobs() {
    state = -1;
  }

  public void parseResultSet(ResultSet rs) throws SQLException {
    id = rs.getLong(ID);
    hash = rs.getString(HASH);
    state = rs.getInt(STATE);
    totalFiles = rs.getInt(TOTAL_FILES);
    processedFiles = rs.getInt(PROCESSED_FILES);
    init = rs.getLong(INIT);
    finish = rs.getLong(FINISH);
    input = rs.getString(INPUT);
    origin = rs.getString(ORIGIN);
    pid = rs.getInt(PID);
    output = rs.getString(OUTPUT);
    time = rs.getLong(TIME);
    lastUpdate = rs.getLong(LAST_UPDATE);
  }

  /**
   * Getters
   */

  public Long getId() {
    return id;
  }

  public String getHash() {
    return hash;
  }

  public Integer getState() {
    return state;
  }

  public Integer getTotalFiles() {
    return totalFiles;
  }

  public Integer getProcessedFiles() {
    return processedFiles;
  }

  public double getProgress() {
    if (state == 0) {
      return 0.0;
    } else if (state == 2) {
      return 1.0;
    } else {
      return (processedFiles * 1.0) / (totalFiles + 1.0);
    }
  }

  public Long getInit() {
    return init;
  }

  public Long getFinish() {
    return finish;
  }

  public String getInput() {
    return input;
  }

  public String getOrigin() {
    return origin;
  }

  public Integer getPid() {
    return pid;
  }

  public String getOutput() {
    return output;
  }

  public Long getTime() {
    return time;
  }

  public Long getLastUpdate() {
    return lastUpdate;
  }

  /**
   * Setters
   */

  public void setId(Long id) {
    this.id = id;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public void setTotalFiles(Integer totalFiles) {
    this.totalFiles = totalFiles;
  }

  public void setProcessedFiles(Integer processedFiles) {
    this.processedFiles = processedFiles;
  }

  public void setInit(Long init) {
    this.init = init;
  }

  public void setFinish(Long finish) {
    this.finish = finish;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public void setPid(Integer pid) {
    this.pid = pid;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public void setLastUpdate(Long lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}

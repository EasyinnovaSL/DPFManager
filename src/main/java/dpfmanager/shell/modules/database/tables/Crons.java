package dpfmanager.shell.modules.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class Crons {

  /**
   * String constants
   */

  // Table name
  public static String TABLE = "crons";

  // Fields
  public static String ID = "id";
  public static String INPUT = "input";
  public static String CONFIGURATION = "configuration";
  public static String PERIOD_TIME = "periodTime";
  /* 0 - Daily   1 - Weekly   2 - Monthly*/
  public static String PERIOD_MODE = "periodMode";
  public static String SPEC_WEEKLY = "specWeekly";
  public static String SPEC_MONTHLY = "specMonthly";

  // Indexs
  public static String INDEX_ID = "index_id";

  // Create SQL query
  public static String createSql = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
      "    " + ID + " VARCHAR(255) NOT NULL PRIMARY KEY," +
      "    " + INPUT + " VARCHAR(255)," +
      "    " + CONFIGURATION + " VARCHAR(255)," +
      "    " + PERIOD_TIME + " VARCHAR(255)," +
      "    " + PERIOD_MODE + " INTEGER," +
      "    " + SPEC_WEEKLY + " INTEGER," +
      "    " + SPEC_MONTHLY + " INTEGER" +
      ");";

  // Index SQl querys
  public static String indexIdSql = "CREATE INDEX IF NOT EXISTS " + INDEX_ID + " ON " + TABLE + "(" + ID + ");";

  // Delete SQL query
  public static String deleteSql = "DROP TABLE IF EXISTS " + TABLE + ";";
  public static String delIndexIdSql = "DROP INDEX IF EXISTS " + INDEX_ID + ";";

  // Insert SQL query
  public static String insertCronSql = "INSERT INTO " + TABLE + "(" + ID + "," + INPUT + "," + CONFIGURATION + "," + PERIOD_TIME + "," + PERIOD_MODE + "," + SPEC_WEEKLY + "," + SPEC_MONTHLY + ") VALUES(?,?,?,?,?,?,?);";


  /**
   * Object properties
   */

  private String id;
  private String input;
  private String configuration;
  private String periodTime;
  private Integer periodMode;
  private Integer specWeekly;
  private Integer specMonthly;

  public Crons() {
    specWeekly = -1;
    specMonthly = -1;
  }

  public void parseResultSet(ResultSet rs) throws SQLException {
    id = rs.getString(ID);
    input = rs.getString(INPUT);
    configuration = rs.getString(CONFIGURATION);
    periodTime = rs.getString(PERIOD_TIME);
    periodMode = rs.getInt(PERIOD_MODE);
    specWeekly = rs.getInt(SPEC_WEEKLY);
    specMonthly = rs.getInt(SPEC_MONTHLY);
  }

  /**
   * Getters
   */

  public String getId() {
    return id;
  }

  public String getInput() {
    return input;
  }

  public String getConfiguration() {
    return configuration;
  }

  public String getPeriodTime() {
    return periodTime;
  }

  public Integer getPeriodMode() {
    return periodMode;
  }

  public Integer getSpecWeekly() {
    return specWeekly;
  }

  public Integer getSpecMonthly() {
    return specMonthly;
  }

  /**
   * Setters
   */

  public void setId(String id) {
    this.id = id;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public void setPeriodTime(String periodTime) {
    this.periodTime = periodTime;
  }

  public void setPeriodMode(Integer periodMode) {
    this.periodMode = periodMode;
  }

  public void setSpecWeekly(Integer specWeekly) {
    this.specWeekly = specWeekly;
  }

  public void setSpecMonthly(Integer specMonthly) {
    this.specMonthly = specMonthly;
  }
}

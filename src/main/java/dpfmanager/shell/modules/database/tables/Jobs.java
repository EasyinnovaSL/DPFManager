package dpfmanager.shell.modules.database.tables;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;

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
  public static String STATE = "state";
  public static String TOTAL_FILES = "totalFiles";
  public static String PROCESSED_FILES = "processedFiles";
  public static String INIT = "init";
  public static String FINISH = "finish";
  public static String INPUT = "input";
  public static String ORIGIN = "origin";
  public static String PID = "pid";
  public static String OUTPUT = "output";

  // Indexs
  public static String INDEX_ID = "index_id";
  public static String INDEX_PID = "index_pid";

  // Create SQL query
  public static String createSql = "CREATE TABLE IF NOT EXISTS "+TABLE+" (" +
      "    "+ID+" INTEGER NOT NULL PRIMARY KEY," +
      "    "+STATE+" INTEGER," +
      "    "+TOTAL_FILES+" INTEGER," +
      "    "+PROCESSED_FILES+" INTEGER," +
      "    "+INIT+" DATE," +
      "    "+FINISH+" DATE," +
      "    "+INPUT+" VARCHAR(255)," +
      "    "+ORIGIN+" VARCHAR(3)," +
      "    "+PID+" INTEGER NOT NULL," +
      "    "+OUTPUT+" VARCHAR(255)" +
      ");";

  // Index SQl querys
  public static String indexIdSql = "CREATE INDEX IF NOT EXISTS "+INDEX_ID+" ON "+TABLE+"("+ID+");";
  public static String indexPidSql = "CREATE INDEX IF NOT EXISTS "+INDEX_PID+" ON "+TABLE+"("+PID+");";


  /**
   * Object properties
   */

  private Long id;
  private Integer state;
  private Integer totalFiles;
  private Integer processedFiles;
  private Long init;
  private Long finish;
  private String input;
  private String origin;
  private Integer pid;
  private String output;

  public Jobs(){
  }

  public void parseCursor(ISqlJetCursor cursor) throws SqlJetException {
    id = cursor.getInteger(ID);
    state = (int) cursor.getInteger(STATE);
    totalFiles = (int) cursor.getInteger(TOTAL_FILES);
    processedFiles = (int) cursor.getInteger(PROCESSED_FILES);
    init = cursor.getInteger(INIT);
    finish = cursor.getInteger(FINISH);
    input = cursor.getString(INPUT);
    origin = cursor.getString(ORIGIN);
    pid = (int) cursor.getInteger(PID);
    output = cursor.getString(OUTPUT);
  }

  /**
   * Getters
   */

  public Long getId() {
    return id;
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

  public double getProgress(){
    if (state == 0){
      return -1.0;
    } else if (state == 2){
      return 1.0;
    } else {
      return (processedFiles * 1.0) / (totalFiles+1.0);
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

  /**
   * Setters
   */

  public void setId(Long id) {
    this.id = id;
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
}

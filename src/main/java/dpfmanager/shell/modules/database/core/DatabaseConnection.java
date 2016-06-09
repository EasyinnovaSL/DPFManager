package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class DatabaseConnection {

  private Connection globalConnection;
  private SQLiteConfig dbConfig;
  private String dbUrl;

  private DpfContext context;
  private Long lastUpdate;
  private Long initTime;

  public DatabaseConnection(DpfContext c) {
    context = c;
    initTime = System.currentTimeMillis();
    lastUpdate = 0L;
  }

  /**
   * Init function
   */
  public void init() {
    String filename = getDatabaseFile();
    try {
      Class.forName("org.sqlite.JDBC");
      dbUrl = "jdbc:sqlite:" + filename;
      dbConfig = new SQLiteConfig();
      dbConfig.setBusyTimeout("30000");
      dbConfig.setOpenMode(SQLiteOpenMode.READWRITE);
      dbConfig.setLockingMode(SQLiteConfig.LockingMode.NORMAL);
      globalConnection = connect();
      createFirstTable();
    } catch (Exception e) {
      e.printStackTrace();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot connect to database (" + filename + ")."));
    }
  }

  /**
   * Connect function
   */
  public Connection connect() {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(dbUrl, dbConfig.toProperties());
    } catch (SQLException e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot connect to database."));
    }
    return connection;
  }

  /**
   * Close function
   */
  public void close() {
    try {
      globalConnection.close();
    } catch (SQLException e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot close the database."));
    }
  }

  /**
   * Decisions functions
   */
  public boolean deleteJob(Jobs job) {
    // Jobs older than 2 days (DpFManagerConstants.JOB_LIFE_HOURS)
    Long current = System.currentTimeMillis();
    if (checkDate(job.getInit())) {
      return true;
    }
    if (checkDate(job.getFinish())) {
      return true;
    }

    // No updates last 30min
    Long minutes = TimeUnit.MINUTES.convert(current - job.getLastUpdate(), TimeUnit.MILLISECONDS);
    if (job.getState() == 1 && minutes > DpFManagerConstants.JOB_LAST_UPDATE_MINUTES) {
      return true;
    }
    return false;
  }

  private boolean checkDate(Long time) {
    if (time != null && time != 0 && time < initTime) {
      Long diff = initTime - time;
      Long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
      return hours > DpFManagerConstants.JOB_LIFE_HOURS;
    }
    return false;
  }

  public boolean showJob(Jobs job) {
    // Jobs initiated after this app
    if (job.getInit() != null && job.getInit() > initTime) {
      return true;
    }
    // Jobs running when the app open
    if (job.getInit() != null && job.getFinish() == null) {
      return true;
    }
    // Pending jobs
    if (job.getState() == 0) {
      return true;
    }
    return false;
  }

  /**
   * Public functions
   */
  public void cleanJobs() {
    for (Jobs job : getAllJobs()) {
      if (deleteJob(job)) {
        deleteJob(job.getId());
      }
    }
  }

  public void updateJob(Jobs job) {
    Collection<Jobs> col = new ArrayList<>();
    col.add(job);
    updateJobs(col);
  }

  public Jobs getJob(Long id) {
    Jobs job = new Jobs();
    try {
      Statement stmt = globalConnection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM " + Jobs.TABLE+" WHERE "+Jobs.ID+" = "+ id);
      while (rs.next()) {
        job.parseResultSet(rs);
      }
      stmt.close();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error getting jobs."));
    }
    return job;
  }

  public Jobs getJob(String hash) {
    Jobs job = new Jobs();
    try {
      Statement stmt = globalConnection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM " + Jobs.TABLE+" WHERE "+Jobs.HASH+" LIKE \""+ hash+"\"");
      while (rs.next()) {
        job.parseResultSet(rs);
      }
      stmt.close();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error getting jobs."));
    }
    return job;
  }

  public List<Jobs> getJobs() {
    List<Jobs> jobs = new ArrayList<>();
    for (Jobs job : getAllJobs()) {
      if (showJob(job)) {
        jobs.add(job);
      }
    }
    return jobs;
  }

  /**
   * Access DB functions
   */
  synchronized public int getProgramPid() {
    Integer maxPid = -1;
    if (blockConnection()) {
      try {
        Statement stmt = globalConnection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT " + Jobs.PID + " FROM " + Jobs.TABLE + " ORDER BY " + Jobs.PID + " DESC");
        maxPid = 1;
        if (rs.next()) {
          Long pid = rs.getLong(1);
          maxPid = pid.intValue() + 1;
        }
        stmt.close();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error obtaining program pid."));
      } finally {
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Database timeout!"));
    }
    return maxPid;
  }

  synchronized public void insertNewJob(Jobs job) {
    if (blockConnection()) {
      try {
        PreparedStatement pstmt = globalConnection.prepareStatement(Jobs.insertJobSql);
        pstmt.setLong(1, job.getId());
        pstmt.setString(2, job.getHash());
        pstmt.setInt(3, job.getState());
        pstmt.setInt(4, job.getTotalFiles());
        pstmt.setInt(5, job.getProcessedFiles());
        if (job.getInit() != null) {
          pstmt.setLong(6, job.getInit());
        } else {
          pstmt.setNull(6, 0);
        }
        if (job.getFinish() != null) {
          pstmt.setLong(7, job.getFinish());
        } else {
          pstmt.setNull(7, 0);
        }
        pstmt.setString(8, job.getInput());
        pstmt.setString(9, job.getOrigin());
        pstmt.setLong(10, job.getPid());
        pstmt.setString(11, job.getOutput());
        if (job.getTime() != null) {
          pstmt.setLong(12, job.getTime());
        } else {
          pstmt.setNull(12, 0);
        }
        pstmt.setLong(13, job.getLastUpdate());
        pstmt.executeUpdate();
        pstmt.close();
        lastUpdate = System.currentTimeMillis();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error creating new job."));
      } finally {
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Database timeout!"));
    }
  }

  synchronized public void updateJobs(Collection<Jobs> jobs) {
    if (blockConnection()) {
      try {
        globalConnection.setAutoCommit(false);
        for (Jobs job : jobs) {
          String updateSql = "UPDATE " + Jobs.TABLE + " SET " +
              Jobs.STATE + " = " + job.getState() + ", " +
              Jobs.TOTAL_FILES + " = " + job.getTotalFiles() + ", " +
              Jobs.PROCESSED_FILES + " = " + job.getProcessedFiles() + ", " +
              Jobs.INIT + " = " + job.getInit() + ", " +
              Jobs.FINISH + " = " + job.getFinish() + ", " +
              Jobs.OUTPUT + " = '" + job.getOutput() + "', " +
              Jobs.TIME + " = " + job.getTime() + "" +
              " WHERE " + Jobs.ID + " = " + job.getId();
          Statement stmt = globalConnection.createStatement();
          stmt.execute(updateSql);
          stmt.close();
        }
        globalConnection.commit();
        globalConnection.setAutoCommit(true);
        lastUpdate = System.currentTimeMillis();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error updating job."));
      } finally {
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Database timeout!"));
    }
  }

  synchronized public List<Jobs> getAllJobs() {
    List<Jobs> jobs = new ArrayList<>();
    try {
      Statement stmt = globalConnection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM " + Jobs.TABLE);
      while (rs.next()) {
        Jobs job = new Jobs();
        job.parseResultSet(rs);
        jobs.add(job);
      }
      stmt.close();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error getting jobs."));
    }
    return jobs;
  }

  synchronized public void deleteJob(Long uuid) {
    if (blockConnection()) {
      try {
        String deleteSql = "DELETE FROM " + Jobs.TABLE + " WHERE " + Jobs.ID + " = " + uuid;
        Statement stmt = globalConnection.createStatement();
        stmt.execute(deleteSql);
        stmt.close();
        lastUpdate = System.currentTimeMillis();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error deleting job."));
      } finally {
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Database timeout!"));
    }
  }

  public Long getLastUpdate() {
    return lastUpdate;
  }

  /**
   * Locks functions
   */
  private boolean blockConnection() {
    try {
      File lockFile = new File(getDatabaseLockFile());
      int timeout = 0, maxTimeout = 30 * 4;
      if (!lockFile.exists()) {
        lockFile.createNewFile();
        return true;
      } else {
        while (lockFile.exists() && timeout < maxTimeout) {
          Thread.sleep(250);
          timeout++;
        }
        if (timeout != maxTimeout) {
          lockFile.createNewFile();
          return true;
        } else {
          return false;
        }
      }
    } catch (Exception e) {
      return false;
    }
  }

  private void releaseConnection() {
    File lockFile = new File(getDatabaseLockFile());
    if (lockFile.exists()) {
      lockFile.delete();
    }
  }


  /**
   * Private DB functions
   */
  synchronized private void createFirstTable() throws Exception {
    Statement stmt = globalConnection.createStatement();

    // Check need delete table
    if (DPFManagerProperties.getDatabaseVersion() != DpFManagerConstants.DATABASE_VERSION) {
      stmt.execute(Jobs.delIndexIdSql);
      stmt.execute(Jobs.delIndexPidSql);
      stmt.execute(Jobs.deleteSql);
      DPFManagerProperties.setDatabaseVersion(DpFManagerConstants.DATABASE_VERSION);
    }

    // Create table
    stmt.execute(Jobs.createSql);
    stmt.execute(Jobs.indexIdSql);
    stmt.execute(Jobs.indexPidSql);

    // Close statement
    stmt.close();
  }

  private void insertTestJob() {
    Jobs job = new Jobs();
    job.setId(System.currentTimeMillis());
    job.setState(1);
    job.setTotalFiles(50);
    job.setProcessedFiles(25);
    job.setInit(System.currentTimeMillis());
    job.setFinish(null);
    job.setInput("input");
    job.setOrigin("origin");
    job.setPid(5);
    job.setOutput("output");
    job.setTime(null);
    job.setLastUpdate(System.currentTimeMillis());

    insertNewJob(job);
  }

  private String getDatabaseFile() {
    return DPFManagerProperties.getDataDir() + "/dpfmanager.db";
  }

  private String getDatabaseLockFile() {
    return DPFManagerProperties.getDataDir() + "/database.lock";
  }

}

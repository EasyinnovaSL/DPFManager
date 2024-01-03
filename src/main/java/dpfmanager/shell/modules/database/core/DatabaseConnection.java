/**
 * <h1>DatabaseConnection.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adria Llorens on 21/04/2016.
 */
public class DatabaseConnection {

  private String dbUrl;

  private DpfContext context;
  private Long lastUpdate;
  private Long initTime;

  private ResourceBundle bundle;

  public DatabaseConnection(DpfContext c) {
    context = c;
    initTime = System.currentTimeMillis();
    lastUpdate = 0L;
  }

  /**
   * Init function
   */
  public void init() {
    bundle = DPFManagerProperties.getBundle();
    String filename = getDatabaseFile();
    try {
      checkLockFile();
      Class.forName("org.h2.Driver");
      dbUrl = "jdbc:h2:" + filename + ";AUTO_SERVER=TRUE";
      createFirstTable();
    } catch (Exception e) {
      e.printStackTrace();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("cannotConnectDB2").replace("%1", filename)));
    }
  }

  private void checkLockFile() throws Exception {
    File lockFile = new File(getDatabaseLockFile());
    if (lockFile.exists()) {
      BasicFileAttributes attr = Files.readAttributes(Paths.get(getDatabaseLockFile()), BasicFileAttributes.class);
      Long fileMilis = attr.creationTime().toMillis();
      Long currentMilis = System.currentTimeMillis();
      if (currentMilis - fileMilis > (60 * 1000)) {
        lockFile.delete();
      }
    }
  }

  /**
   * Connect function
   */
  public Connection connect() {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(dbUrl);
    } catch (SQLException e) {
      e.printStackTrace();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("cannotConnectDB")));
    }
    return connection;
  }

  /**
   * Decisions functions
   */
  public boolean deleteJob(Jobs job) {
    // Jobs older than 2 days (DpFManagerConstants.JOB_LIFE_HOURS)
    Long current = System.currentTimeMillis();
    if (checkDate(job.getLastUpdate())) {
      return true;
    }
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
    // Jobs updated after this app
    return (job.getLastUpdate() != null && job.getLastUpdate() > initTime);
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
    return getJobByQuery("SELECT * FROM " + Jobs.TABLE + " WHERE " + Jobs.ID + " = " + id);
  }

  public Jobs getJob(String hash) {
    return getJobByQuery("SELECT * FROM " + Jobs.TABLE + " WHERE " + Jobs.HASH + " LIKE '" + hash + "'");
  }

  public Jobs getJobByQuery(String query) {
    Jobs job = new Jobs();
    Statement stmt = null;
    Connection connection = connect();
    try {
      stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        job.parseResultSet(rs);
      }
      stmt.close();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorGetJobs")));
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (connection != null) connection.close();
      } catch (SQLException e){
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorGetJobs")));
      }
      releaseConnection();
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
    Statement stmt = null;
    Connection connection = connect();
    if (blockConnection()) {
      try {
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT " + Jobs.PID + " FROM " + Jobs.TABLE + " ORDER BY " + Jobs.PID + " DESC");
        maxPid = 1;
        if (rs.next()) {
          Long pid = rs.getLong(1);
          maxPid = pid.intValue() + 1;
        }
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorGetPid")));
      } finally {
        try {
          if (stmt != null) stmt.close();
          if (connection != null) connection.close();
        } catch (SQLException e){
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorGetPid")));
        }
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("dbTimeout")));
    }
    return maxPid;
  }

  synchronized public void insertNewJob(Jobs job) {
    PreparedStatement pstmt = null;
    Connection connection = connect();
    if (blockConnection()) {
      try {
        pstmt = connection.prepareStatement(Jobs.insertJobSql);
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
        lastUpdate = System.currentTimeMillis();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorCreateJob")));
      } finally {
        try {
          if (pstmt != null) pstmt.close();
          if (connection != null) connection.close();
        } catch (SQLException e){
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorCreateJob")));
        }
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("dbTimeout")));
    }
  }

  synchronized public void updateJobs(Collection<Jobs> jobs) {
    Statement stmt = null;
    Connection connection = connect();
    if (blockConnection()) {
      try {
        for (Jobs job : jobs) {
          String updateSql = "UPDATE " + Jobs.TABLE + " SET " +
              Jobs.STATE + " = " + job.getState() + ", " +
              Jobs.TOTAL_FILES + " = " + job.getTotalFiles() + ", " +
              Jobs.PROCESSED_FILES + " = " + job.getProcessedFiles() + ", " +
              Jobs.INIT + " = " + job.getInit() + ", " +
              Jobs.FINISH + " = " + job.getFinish() + ", " +
              Jobs.OUTPUT + " = '" + job.getOutput() + "', " +
              Jobs.TIME + " = " + job.getTime() + ", " +
              Jobs.LAST_UPDATE + " = " + job.getLastUpdate() +
              " WHERE " + Jobs.ID + " = " + job.getId();
          stmt = connection.createStatement();
          stmt.execute(updateSql);
        }
        lastUpdate = System.currentTimeMillis();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorUpdateJob")));
      } finally {
        try {
          if (stmt != null) stmt.close();
          if (connection != null) connection.close();
        } catch (SQLException e){
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorCreateJob")));
        }
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("dbTimeout")));
    }
  }

  synchronized public List<Jobs> getAllJobs() {
    List<Jobs> jobs = new ArrayList<>();
    Statement stmt = null;
    Connection connection = connect();
    try {
      stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM " + Jobs.TABLE);
      while (rs.next()) {
        Jobs job = new Jobs();
        job.parseResultSet(rs);
        jobs.add(job);
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorGetJobs")));
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (connection != null) connection.close();
      } catch (SQLException e){
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorGetJobs")));
      }
      releaseConnection();
    }
    return jobs;
  }

  synchronized public void deleteJob(Long uuid) {
    Statement stmt = null;
    Connection connection = connect();
    if (blockConnection()) {
      try {
        String deleteSql = "DELETE FROM " + Jobs.TABLE + " WHERE " + Jobs.ID + " = " + uuid;
        stmt = connection.createStatement();
        stmt.execute(deleteSql);
        lastUpdate = System.currentTimeMillis();
      } catch (Exception e) {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorDeleteJob")));
      } finally {
        try {
          if (stmt != null) stmt.close();
          if (connection != null) connection.close();
        } catch (SQLException e){
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("errorDeleteJob")));
        }
        releaseConnection();
      }
    } else {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("dbTimeout")));
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
    Statement stmt = null;
    Connection connection = connect();
    try{
      stmt = connection.createStatement();

      // Check need delete table
      if (DPFManagerProperties.getDatabaseVersion() != DpFManagerConstants.DATABASE_VERSION) {
        stmt.execute(Jobs.delIndexIdSql);
        stmt.execute(Jobs.delIndexPidSql);
        stmt.execute(Jobs.deleteSql);
        DPFManagerProperties.setDatabaseVersion(DpFManagerConstants.DATABASE_VERSION);
      }

      // Create tables
      stmt.execute(Jobs.createSql);
      stmt.execute(Jobs.indexIdSql);
      stmt.execute(Jobs.indexPidSql);
    } catch (Exception e){
      e.printStackTrace();
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("cannotConnectDB")));
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (connection != null) connection.close();
      } catch (SQLException e){
        e.printStackTrace();
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("cannotConnectDB")));
      }
      releaseConnection();
    }
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
    return DPFManagerProperties.getDataDir() + "/dpfmanager.h2";
  }

  private String getDatabaseLockFile() {
    return DPFManagerProperties.getDataDir() + "/database.lock";
  }

}

package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class DatabaseConnection {

  private SqlJetDb DB;
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
      DB = SqlJetDb.open(new File(filename), true);
      createFirstTable();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot connect to database (" + filename + ")."));
    }
  }

  /**
   * Close function
   */
  public void close() {
    try {
      DB.close();
    } catch (SqlJetException e) {
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
    if (time != null && time < initTime) {
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
    return false;
  }

  /**
   * Public functions
   */
  public void cleanJobs() {
    try {
      // Try to begin transaction
      if (beginTransaction(SqlJetTransactionMode.READ_ONLY)) {
        // From this point, DB file is locked
        ISqlJetCursor cursor = DB.getTable(Jobs.TABLE).order(Jobs.INDEX_ID);
        boolean next = !cursor.eof();
        while (next) {
          Jobs job = new Jobs();
          job.parseCursor(cursor);
          if (deleteJob(job)) {
            cursor.delete();
          }
          next = cursor.next();
        }
        cursor.close();

        // Finish transaction and unlock
        DB.commit();
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Timeout database."));
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error cleaning jobs."));
    }
  }

  public int getProgramPid() {
    try {
      // Try to begin transaction
      if (beginTransaction(SqlJetTransactionMode.WRITE)) {
        // From this point, DB file is locked
        ISqlJetCursor cursor = DB.getTable(Jobs.TABLE).order(Jobs.INDEX_PID).reverse();
        int maxPid = 1;
        if (!cursor.eof()) {
          Long pid = cursor.getInteger(Jobs.PID);
          maxPid = pid.intValue() + 1;
        }
        cursor.close();
        // Finish transaction and unlock
        DB.commit();
        return maxPid;
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Timeout database."));
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error obtaining program pid."));
    }
    return -1;
  }

  synchronized public void insertNewJob(Jobs job) {
    try {
      // Try to begin transaction
      if (beginTransaction(SqlJetTransactionMode.WRITE)) {
        // From this point, DB file is locked
        DB.getTable(Jobs.TABLE).insert(job.getId(), job.getState(), job.getTotalFiles(), 0, job.getInit(), null, job.getInput(), job.getOrigin(), job.getPid(), job.getOutput(), job.getTime(), job.getLastUpdate());

        // Finish transaction and unlock
        DB.commit();
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Timeout database."));
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error creating new job."));
    }
  }

  synchronized public void updateJob(Jobs job) {
    Collection<Jobs> col = new ArrayList<>();
    col.add(job);
    updateJobs(col);
  }

  synchronized public void updateJobs(Collection<Jobs> jobs) {
    try {
      // Try to begin transaction
      if (beginTransaction(SqlJetTransactionMode.WRITE)) {
        // From this point, DB file is locked
        for (Jobs job : jobs) {
          ISqlJetCursor cursor = DB.getTable(Jobs.TABLE).lookup(Jobs.INDEX_ID, job.getId());
          if (!cursor.eof()) {
            Map<String, Object> updates = new HashMap<>();
            updates.put(Jobs.STATE, job.getState());
            updates.put(Jobs.TOTAL_FILES, job.getTotalFiles());
            updates.put(Jobs.PROCESSED_FILES, job.getProcessedFiles());
            updates.put(Jobs.INIT, job.getInit());
            updates.put(Jobs.FINISH, job.getFinish());
            updates.put(Jobs.OUTPUT, job.getOutput());
            updates.put(Jobs.TIME, job.getTime());
            cursor.updateByFieldNames(updates);
            cursor.close();
            lastUpdate = System.currentTimeMillis();
          } else {
            context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot find job (" + job.getId() + ")"));
          }
        }
        // Finish transaction and unlock
        DB.commit();
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Timeout database."));
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error updating job."));
    }
  }

  public List<Jobs> getJobs() {
    List<Jobs> jobs = new ArrayList<>();
    try {
      // Try to begin transaction
      if (beginTransaction(SqlJetTransactionMode.READ_ONLY)) {
        // From this point, DB file is locked
        ISqlJetCursor cursor = DB.getTable(Jobs.TABLE).order(Jobs.INDEX_ID);
        boolean next = !cursor.eof();
        while (next) {
          Jobs job = new Jobs();
          job.parseCursor(cursor);
          if (showJob(job)) {
            jobs.add(job);
          }
          next = cursor.next();
        }
        cursor.close();

        // Finish transaction and unlock
        DB.commit();
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Timeout database."));
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error getting jobs."));
    }
    return jobs;
  }

  synchronized public void deleteJob(Long uuid) {
    try {
      // Try to begin transaction
      if (beginTransaction(SqlJetTransactionMode.WRITE)) {
        // From this point, DB file is locked
        ISqlJetCursor cursor = DB.getTable(Jobs.TABLE).lookup(Jobs.INDEX_ID, uuid);
        if (!cursor.eof()) {
          cursor.delete();
          cursor.close();
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot find job (" + uuid + ")"));
        }
        // Finish transaction and unlock
        DB.commit();
      } else {
        context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Timeout database."));
      }
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Error updating job."));
    }
  }

  public Long getLastUpdate() {
    return lastUpdate;
  }

  /**
   * Private DB functions
   */
  private void createFirstTable() throws Exception {
    if (beginTransaction(SqlJetTransactionMode.WRITE)) {
      // Check need delete table
      if (DPFManagerProperties.getDatabaseVersion() != DpFManagerConstants.DATABASE_VERSION) {
        if (existsTable(Jobs.TABLE)) {
          ISqlJetTable table = DB.getTable(Jobs.TABLE);
          if (!table.getIndexesNames().contains(Jobs.INDEX_ID)) {
            DB.dropIndex(Jobs.INDEX_ID);
          }
          if (!table.getIndexesNames().contains(Jobs.INDEX_PID)) {
            DB.dropIndex(Jobs.INDEX_PID);
          }
          DB.dropTable(Jobs.TABLE);
        }
        DPFManagerProperties.setDatabaseVersion(DpFManagerConstants.DATABASE_VERSION);
      }

      // Create table
      DB.createTable(Jobs.createSql);
      DB.createIndex(Jobs.indexIdSql);
      DB.createIndex(Jobs.indexPidSql);
      DB.commit();
    }
  }

  private boolean existsTable(String name) throws SqlJetException {
    try {
      DB.getTable(name);
      return true;
    } catch (SqlJetException e) {
      if (e.getMessage().contains("Table not found")) {
        return false;
      } else {
        throw e;
      }
    }
  }

  private boolean beginTransaction(SqlJetTransactionMode mode, int maxTimeout) {
    try {
      int timeout = 0;
      while (!tryBeginTransaction(mode) && timeout < maxTimeout) {
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Waiting... " + timeout);
        timeout++;
      }
      return timeout != maxTimeout * 2;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean beginTransaction(SqlJetTransactionMode mode) {
    return beginTransaction(mode, 30);
  }

  private boolean tryBeginTransaction(SqlJetTransactionMode mode) {
    try {
      DB.beginTransaction(mode);
      return true;
    } catch (SqlJetException e) {
      return false;
    }
  }

  private String getDatabaseFile() {
    return DPFManagerProperties.getDataDir() + "/dpfmanager.db";
  }

}

package dpfmanager.shell.modules.database.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.util.ArrayList;
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
   * Public functions
   */
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
        DB.getTable(Jobs.TABLE).insert(job.getId(), job.getState(), job.getTotalFiles(), 0, job.getInit(), null, job.getInput(), job.getOrigin(), job.getPid(), job.getOutput());
        lastUpdate = System.currentTimeMillis();

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
    try {
      // Try to begin transaction
      if (beginTransaction(SqlJetTransactionMode.WRITE)) {
        // From this point, DB file is locked
        ISqlJetCursor cursor = DB.getTable(Jobs.TABLE).lookup(Jobs.INDEX_ID, job.getId());
        if (!cursor.eof()) {
          Map<String, Object> updates = new HashMap<>();
          updates.put(Jobs.OUTPUT, job.getOutput());
          updates.put(Jobs.TOTAL_FILES, job.getTotalFiles());
          updates.put(Jobs.PROCESSED_FILES, job.getProcessedFiles());
          updates.put(Jobs.STATE, job.getState());
          updates.put(Jobs.INIT, job.getInit());
          updates.put(Jobs.FINISH, job.getFinish());
          cursor.updateByFieldNames(updates);
          cursor.close();
          lastUpdate = System.currentTimeMillis();
        } else {
          context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Cannot find job (" + job.getId() + ")"));
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
          if (job.getFinish() == 0 || job.getInit() == 0 || job.getInit() > initTime || job.getFinish() > initTime ) {
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

  synchronized public void deleteJob(Long uuid){
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
      DB.createTable(Jobs.createSql);
      DB.createIndex(Jobs.indexIdSql);
      DB.createIndex(Jobs.indexPidSql);
      DB.commit();
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

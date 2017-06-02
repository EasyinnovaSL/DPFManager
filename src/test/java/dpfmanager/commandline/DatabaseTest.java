package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.DpFManagerConstants;
import dpfmanager.shell.core.app.MainApp;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.interfaces.gui.component.report.ReportsModel;
import dpfmanager.shell.modules.database.tables.Jobs;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Easy on 20/07/2015.
 */
public class DatabaseTest extends CommandLineTest {

  @Test
  public void testDatabaseConnection() throws Exception {
    // Execute dpf manager (check for clean jobs)
    String[] args = new String[3];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "src/test/resources/Small/Bilevel.tif";
    MainApp.main(args);
    waitForFinishMultiThred(30);

    // Previous jobs count
    Class.forName("org.h2.Driver");
    Integer initial = countJobs();

    // Execute one job
    String[] args2 = new String[3];
    args2[0] = "check";
    args2[1] = "-s";
    args2[2] = "src/test/resources/Small/Bilevel.tif";
    MainApp.main(args2);
    waitForFinishMultiThred(30);

    // Compare current jobs count
    Integer current = countJobs();
    Integer expected = initial + 1;
    Assert.assertEquals("Job not created", expected, current);
  }

  private int countJobs() throws SQLException {
    int count = 0;
    Connection connection = null;
    Statement stmt = null;
    try {
      // Connect
      connection = DriverManager.getConnection("jdbc:h2:" + DPFManagerProperties.getDataDir() + "/dpfmanager.h2;AUTO_SERVER=TRUE");

      // Get jobs count
      stmt = connection.createStatement();
      stmt.execute(Jobs.indexIdSql);
      stmt.execute(Jobs.indexPidSql);
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + Jobs.TABLE);
      if (rs.next()){
        count = rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assert.assertEquals("Error in database query execution", true, false);
    } finally {
      if (connection != null) connection.close();
      if (stmt != null) stmt.close();
    }
    return count;
  }

}

package dpfmanager;

import com.easyinnova.tiff.reader.TiffReader;

import dpfmanager.shell.modules.interfaces.CommandLine;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.Before;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.application.Platform;
import junit.framework.TestCase;

/**
 * Created by Easy on 20/07/2015.
 */
public class MultipleReportGeneratorTest extends TestCase {
  TiffReader tr;

  /**
   * Pre test.
   */
  @Before
  public void PreTest() {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  public void testReportsXML() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[3];
    args[0] = "src/test/resources/Small/";
    args[1] = "-reportformat";
    args[2] = "xml";

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();
    Platform.exit();

    String path = getPath();
    File directori = new File(path);
    assertEquals(7, directori.list().length);
  }

  public void testReportsPDF() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[3];
    args[0] = "src/test/resources/Small/";
    args[1] = "-reportformat";
    args[2] = "pdf";

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();
    Platform.exit();

    String path = getPath();
    File directori = new File(path);
    assertEquals(7, directori.list().length);
  }

  private String getPath() {
    String path = "reports";
    File theDir = new File(path);
    // date folder
    path += "/" + FastDateFormat.getInstance("yyyyMMdd").format(new Date());
    theDir = new File(path);

    // index folder
    int index = 1;
    File file = new File(path + "/" + index);
    while (file.exists()) {
      index++;
      file = new File(path + "/" + index);
      if (!file.exists()) {
        file = new File(path + "/" + (index - 1));
        break;
      }
    }
    path += "/" + (index - 1);
    return path;
  }
}

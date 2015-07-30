package dpfmanager;

import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;

import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Easy on 20/07/2015.
 */
public class ReportGeneratorTest extends TestCase {
  TiffReader tr;

  @After
  public static void afterClass() {
    Platform.exit();
  }

  /**
   * Pre test.
   */
  @Before
  public void PreTest() {
    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  public void testReports1() throws Exception {
    String[] args = new String[3];
    args[0] = "src/test/resources/Small/Bilevel.tif";
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
    assertEquals(2, directori.list().length);
    Platform.exit();
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

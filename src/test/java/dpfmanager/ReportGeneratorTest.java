package dpfmanager;

import javafx.application.Platform;

import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.Before;

import java.io.File;
import java.util.Date;

/**
 * Created by Easy on 20/07/2015.
 */
public class ReportGeneratorTest extends TestCase {
  TiffReader tr;

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
    String[] args = new String[1];
    args[0] = "D:/workspacePREFORMA/Tiff-Library-4J/src/test/resources/Small/Bilevel.tif";
    MainApp.main(args);
    String path = getPath();

    File directori = new File(path);
    assertEquals(directori.list().length, 2);
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

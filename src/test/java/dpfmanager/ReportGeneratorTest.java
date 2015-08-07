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
import java.net.Socket;
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
    String[] args = new String[2];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
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
    assertEquals(6, directori.list().length);
    Platform.exit();
  }

  public void testReports2() throws Exception {
    String[] args = new String[2];
    args[0] = "src/test/resources/Small";
    args[1] = "-s";

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
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
    assertEquals(16, directori.list().length);
    Platform.exit();
  }

  public void testReports3() throws Exception {
    String[] args = new String[2];
    args[0] = "src/test/resources/Small.zip";
    args[1] = "-s";

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
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
    assertEquals(12, directori.list().length);
    Platform.exit();
  }

  public void testReports4() throws Exception {
    String dirWeb="http://dpfmanager.org/img/Bilevel.tif";
    try{

      Socket s = new Socket("www.google.com", 80);
      String[] args = new String[2];
      args[0] =dirWeb;
      args[1] = "-s";

      Application.Parameters params=new Application.Parameters() {
        @Override
        public List<String> getRaw() {
          ArrayList<String> listRaw=new ArrayList<String>();
          listRaw.add(args[0]);
          listRaw.add(args[1]);
          return listRaw;
        }

        @Override
        public List<String> getUnnamed() {
          ArrayList<String> listRaw=new ArrayList<String>();
          listRaw.add(args[0]);
          listRaw.add(args[1]);
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
      assertEquals(6, directori.list().length);
      Platform.exit();
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public void testReportsFormat() throws Exception {

    assertReportsFormat("html");
    assertReportsFormat("xml");
    assertReportsFormat("json");
    assertReportsFormat("xml,html");
    assertReportsFormat("xml,json");
    assertReportsFormat("json,html");
    assertReportsFormat("xml,json,html");
  }

  private void assertReportsFormat(String formats) throws Exception {
    String[] args = new String[4];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-reportformat";
    args[3] = formats;

    Application.Parameters params=new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        listRaw.add(args[3]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw=new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        listRaw.add(args[3]);
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
    int filesExpect=(formats.split(",").length)*2;

    assertEquals(directori.list().length,filesExpect);

    String extension="";
    boolean isXML=false;
    boolean isHTML=false;
    boolean isJSON=false;
    for(File file:directori.listFiles()){
      extension=file.getAbsolutePath();
      if(!file.isDirectory()) {
        if (formats.contains("xml")&&!isXML) {
          isXML=extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".xml");
        }
        if (formats.contains("json")&&!isJSON) {
          isJSON=extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".json");
        }
        if (formats.contains("html")&&!isHTML) {
         isHTML=extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".html");
        }
      }
    }
    if (formats.contains("xml")) {
      assertEquals(true,isXML);
    }
    if (formats.contains("json")) {
      assertEquals(true,isJSON);
    }
    if (formats.contains("html")) {
      assertEquals(true,isHTML);
    }
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

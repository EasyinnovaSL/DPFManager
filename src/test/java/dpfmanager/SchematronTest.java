package dpfmanager;

import dpfmanager.shell.modules.classes.Schematron;
import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;

import junit.framework.TestCase;

import com.easyinnova.tiff.reader.TiffReader;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.After;
import org.junit.Before;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by easy on 01/10/2015.
 */
public class SchematronTest extends TestCase {
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

  public void testSchematron1() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[3];
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
    boolean found = false;
    String xml = null;
    for (String file : directori.list()){
      if (file.equals("summary.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml = new String(encoded);
      }
    }
    assertEquals(xml != null, true);
    Schematron sch = new Schematron();
    String result = sch.testXML(xml);

    assertEquals(true, result.indexOf("fired-rule context=\"globalreport\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"individualreports\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tiff_structure\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"ifdTree\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tags\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("failed") == -1);

    Platform.exit();
  }

  public void testSchematron2() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[3];
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
    String xml = null;
    for (String file : directori.list()){
      if (file.equals("summary.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml = new String(encoded);
      }
    }
    assertEquals(xml != null, true);
    Schematron sch = new Schematron();
    String result = sch.testXML(xml, "sch/rules2.sch");

    assertEquals(true, result.indexOf("fired-rule context=\"globalreport\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"individualreports\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tiff_structure\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"ifdTree\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tags\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("failed") != -1);

    Platform.exit();
  }

  public void testReport() throws Exception {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = "0";
    prefs.put(PREF_NAME, newValue);

    String[] args = new String[2];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
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
    String xmlFile = path + "/Bilevel.tif.xml";

    assertEquals(true, new File(xmlFile).exists());

    byte[] encoded = Files.readAllBytes(Paths.get(xmlFile));
    String res = new String(encoded, Charset.defaultCharset());

    assertEquals(true, res.indexOf("fired-rule") > -1);
  }

  /**
   * Calls getTextValue and returns a int value
   */
  private int getIntValue(Element ele, String tagName) {
    //in production application you would catch the exception
    return Integer.parseInt(getTextValue(ele, tagName));
  }

  /**
   * I take a xml element and the tag name, look for the tag and get the text content i.e for
   * <employee><name>John</name></employee> xml snippet if the Element points to employee node and
   * tagName is 'name' I will return John
   */
  private String getTextValue(Element ele, String tagName) {
    String textVal = null;
    NodeList nl = ele.getElementsByTagName(tagName);
    if (nl != null && nl.getLength() > 0) {
      Element el = (Element) nl.item(0);
      textVal = el.getFirstChild().getNodeValue();
    }

    return textVal;
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

package dpfmanager.commandline;

import dpfmanager.shell.interfaces.CLI.CommandLine;
import dpfmanager.shell.interfaces.UserInterface;
import javafx.application.Application;

import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.junit.Before;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by easy on 13/10/2015.
 */
public class PolicyTest extends TestCase {
  TiffReader tr;

  /**
   * Pre test.
   */
  @Before
  public void PreTest() {
    UserInterface.setFeedback(false);

    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  public void testAddRemoveTag() throws Exception {
    UserInterface.setFeedback(false);

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String path = "temp/output";
    int idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String configfile = "temp/xx.cfg";
    idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "ISO\tTiff/EP\n" +
        "ISO\tTiff/IT\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "RULE\tImageWidth,>,1000\n" +
        "RULE\tImageHeight,>,500\n" +
        "RULE\tPixelDensity,>,10\n");
    bw.close();

    String[] args = new String[6];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_orig.contains("Invalid ImageWidth"), true);
    assertEquals(xml_orig.contains("failed-assert"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  public void testEndianessOk() throws Exception {
    UserInterface.setFeedback(false);

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String path = "temp/output";
    int idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String configfile = "temp/xx.cfg";
    idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tXML\n" +
        "RULE\tByteOrder,=,BIG_ENDIAN\n");
    bw.close();

    String[] args = new String[6];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_orig.contains("Invalid ByteOrder"), false);
    assertEquals(xml_orig.contains("failed-assert"), false);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  public void testEndianessKo() throws Exception {
    UserInterface.setFeedback(false);

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String path = "temp/output";
    int idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String configfile = "temp/xx.cfg";
    idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tXML\n" +
        "RULE\tByteOrder,=,LITTLE_ENDIAN\n");
    bw.close();

    String[] args = new String[6];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLine cl = new CommandLine(params);
    cl.launch();

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_orig.contains("Invalid ByteOrder"), true);
    assertEquals(xml_orig.contains("failed-assert"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }
}

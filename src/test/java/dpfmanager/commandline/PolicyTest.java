package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by easy on 13/10/2015.
 */
public class PolicyTest extends CommandLineTest {
  @Test
  public void testAddRemoveTag() throws Exception {
    DPFManagerProperties.setFeedback(false);

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

    MainConsoleApp.main(args);

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

  @Test
  public void testEndianessOk() throws Exception {
    DPFManagerProperties.setFeedback(false);

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

    MainConsoleApp.main(args);

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

  @Test
  public void testEndianessKo() throws Exception {
    DPFManagerProperties.setFeedback(false);

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

    MainConsoleApp.main(args);

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

package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

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
public class FixerTest extends CommandLineTest {
  @Test
  public void testAddRemoveTag() throws Exception {
    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String configfile = "temp/xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "ISO\tTiff/EP\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "RULE\tImageWidth,>,1000\n" +
        "FIX\tCopyright,Remove Tag,\n" +
        "FIX\tImageDescription,Add Tag,description\n");
    bw.close();

    String path = "temp/output";
    idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String[] args = new String[7];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "--configuration";
    args[5] = configfile;
    args[6] = "src/test/resources/Small/Bilevel.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(60);

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("1-Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_modif != null, true);
    assertEquals(xml_orig.contains("<name>Copyright</name>"), true);
    assertEquals(xml_modif.contains("<name>Copyright</name>"), false);
    assertEquals(xml_orig.contains("<name>ImageDescription</name>"), false);
    assertEquals(xml_modif.contains("<name>ImageDescription</name>"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testAddExistingTag() throws Exception {
    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String configfile = "temp/xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "FIX\tArtist,Add Tag,NewArtist\n");
    bw.close();

    String path = "temp/output";
    idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String[] args = new String[7];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "--configuration";
    args[5] = configfile;
    args[6] = "src/test/resources/Small/Bilevel.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(60);

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("1-Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_modif != null, true);
    assertEquals(xml_orig.contains("NewArtist"), false);
    assertEquals(xml_modif.contains("NewArtist"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testAutofixPrivateData() throws Exception {
    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String configfile = "temp/xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "FIX\tclearPrivateData,Yes\n");
    bw.close();

    String path = "temp/output";
    idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String[] args = new String[7];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-c";
    args[5] = configfile;
    args[6] = "src/test/resources/Small/Bilevel.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(60);

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("1-Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
    }
    assertEquals(xml_orig != null, true);
    assertEquals(xml_modif != null, true);
    assertEquals(xml_modif.contains("GPS"), false);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testReports() throws Exception {
    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String configfile = "temp/xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "ISO\tTiff/EP\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "RULE\tImageWidth,>,1000\n" +
        "FIX\tCopyright,Remove Tag,\n" +
        "FIX\tImageDescription,Add Tag,description\n");
    bw.close();

    String path = "temp/output";
    idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String[] args = new String[7];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-c";
    args[5] = configfile;
    args[6] = "src/test/resources/Small/Bilevel.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(60);

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml_orig = null;
    String xml_modif = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_orig = new String(encoded);
      }
      if (file.equals("1-Bilevel.tif_fixed.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml_modif = new String(encoded);
      }
      System.out.println("File: " + file);
    }
    assertEquals(true, xml_orig != null);
    assertEquals(true, xml_modif != null);

    directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html_orig = null;
    String html_modif = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html_orig = new String(encoded);
      }
      if (file.equals("1-Bilevel.tif_fixed.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html_modif = new String(encoded);
      }
    }
    assertEquals(html_orig != null, true);
    assertEquals(html_modif != null, true);
    assertEquals(html_orig.contains("Bilevel.tif_fixed.html"), true);
    assertEquals(html_modif.contains("Bilevel.tif.html"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

}

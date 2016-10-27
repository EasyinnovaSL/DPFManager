package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), true);

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), false);

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testWidthKo() throws Exception {
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
        "RULE\tImageWidth,>,10000,0\n");
    bw.close();

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), true);
    assertEquals(xml_orig.contains("Invalid ImageWidth"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testWidthOk() throws Exception {
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
        "RULE\tImageWidth,>,100\n");
    bw.close();

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), false);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testWidthWarning() throws Exception {
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
        "RULE\tImageWidth,<,10000,1\n");
    bw.close();

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), false);
    assertEquals(xml_orig.contains("Warning on ImageWidth"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testNumberImagesKo() throws Exception {
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
        "RULE\tNumberImages,<,1,0\n");
    bw.close();

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), true);
    assertEquals(xml_orig.contains("Invalid NumberImages"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testNumberImagesOk() throws Exception {
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
        "RULE\tNumberImages,<,10,0\n");
    bw.close();

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
    waitForFinishMultiThred(30);

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
    assertEquals(xml_orig.contains("<policyCheckerOutput><error>"), false);
    assertEquals(xml_orig.contains("Invalid NumberImages"), false);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testPoliciesOk() throws Exception {
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
        "FORMAT\tHTML\n" +
        "RULE\tDPI,=,Even\n" +
        "RULE\tEqualXYResolution,=,True\n");
    bw.close();

    String[] args = new String[7];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-c";
    args[5] = configfile;
    args[6] = "src/test/resources/classes/IMG_OK.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    for (String file : directori.list()) {
      if (file.equals("1-IMG_OK.tif.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    int index = html.indexOf("<table class=\"center-table CustomTable\">");
    assertEquals(true, index > -1);
    String table = html.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    String[] trs = table.split("</tr>");

    // Policy errors
    for (String tr : trs){
      if (tr.contains("<td>" + TiffConformanceChecker.POLICY_ISO + "</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
        assertEquals(false, tr.contains("<td class=\"error\">"));
      } else if (tr.contains("<td>"+ ImplementationCheckerLoader.getIsoName("TIFF_Baseline_Core_6_0")+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      }
    }

    FileUtils.deleteDirectory(new File(path));
    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testPoliciesKo() throws Exception {
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
        "FORMAT\tHTML\n" +
        "RULE\tDPI,=,Uneven\n" +
        "RULE\tEqualXYResolution,=,False\n");
    bw.close();

    String[] args = new String[7];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-c";
    args[5] = configfile;
    args[6] = "src/test/resources/classes/IMG_OK.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    for (String file : directori.list()) {
      if (file.equals("1-IMG_OK.tif.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    int index = html.indexOf("<table class=\"center-table CustomTable\">");
    assertEquals(true, index > -1);
    String table = html.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    String[] trs = table.split("</tr>");

    // Policy errors
    for (String tr : trs){
      if (tr.contains("<td>" + TiffConformanceChecker.POLICY_ISO + "</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
        assertEquals(true, tr.contains("<td class=\"error\">2</td>"));
      } else if (tr.contains("<td>"+ ImplementationCheckerLoader.getIsoName("TIFF_Baseline_Core_6_0")+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      }
    }

    FileUtils.deleteDirectory(new File(path));
    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

}

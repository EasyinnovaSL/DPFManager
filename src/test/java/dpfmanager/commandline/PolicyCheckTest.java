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

/**
 * Created by easy on 13/04/2016.
 */
public class PolicyCheckTest extends CommandLineTest {
  @Test
  public void testPCErrors() throws Exception {
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
        "FORMAT\tPDF\n" +
        "RULE\tImageWidth,>,10000\n");
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

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    String htmlglobal = null;
    byte[] encoded = Files.readAllBytes(Paths.get(path + "/report.html"));
    htmlglobal = new String(encoded);
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.html")) {
        encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    assertEquals(html.contains("This file conforms to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), true);
    assertEquals(html.contains("This file does NOT conform to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), false);
    assertEquals(html.contains("This file conform to "+TiffConformanceChecker.POLICY_ISO), false);
    assertEquals(html.contains("This file does NOT conform to "+TiffConformanceChecker.POLICY_ISO), true);

    int index = html.indexOf("<table class=\"center-table CustomTable\">");
    assertEquals(true, index > -1);
    String table = html.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    String[] trs = table.split("</tr>");

    // 1 Policy error
    for (String tr : trs){
      if (tr.contains("<td>"+ TiffConformanceChecker.POLICY_ISO+"</td>")){
        assertEquals(true, tr.contains("<td class=\"error\">1</td>"));
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      } else if (tr.contains("<td>"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"</td>")){
        assertEquals(false, tr.contains("<td class=\"error\">1</td>"));
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      }
    }

    // Global errors / warnings resume
    assertEquals(htmlglobal != null, true);
    index = htmlglobal.indexOf("<table class=\"center-table CustomTable2\">");
    assertEquals(true, index > -1);
    table = htmlglobal.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    trs = table.split("</tr>");

    for (String tr : trs){
      if (tr.contains(">"+ TiffConformanceChecker.POLICY_ISO+"<")){
        assertEquals(true, tr.contains(">1 errors<"));
        assertEquals(true, tr.contains(">0 warnings<"));
      } else if (tr.contains(">"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"<")){
        assertEquals(true, tr.contains(">0 errors<"));
        assertEquals(true, tr.contains(">0 warnings<"));
      }
    }

    FileUtils.deleteDirectory(new File(path));
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testPCWarnings() throws Exception {
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
        "FORMAT\tPDF\n" +
        "RULE\tImageWidth,<,1000,1\n" +
        "RULE\tImageLength,>,10,1\n");
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

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    String htmlglobal = null;
    byte[] encoded = Files.readAllBytes(Paths.get(path + "/report.html"));
    htmlglobal = new String(encoded);
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.html")) {
        encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    assertEquals(html.contains("This file conforms to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), true);
    assertEquals(html.contains("This file does NOT conform to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), false);
    assertEquals(html.contains("This file conforms to "+TiffConformanceChecker.POLICY_ISO+", BUT it has some warnings"), true);
    assertEquals(html.contains("This file does NOT conform to "+TiffConformanceChecker.POLICY_ISO), false);

    int index = html.indexOf("<table class=\"center-table CustomTable\">");
    assertEquals(true, index > -1);
    String table = html.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    String[] trs = table.split("</tr>");

    // 1 Policy error
    for (String tr : trs){
      if (tr.contains("<td>"+ TiffConformanceChecker.POLICY_ISO+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
        assertEquals(true, tr.contains("<td class=\"warning\">2</td>"));
      } else if (tr.contains("<td>"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      }
    }

    // Global errors / warnings resume
    assertEquals(htmlglobal != null, true);
    index = htmlglobal.indexOf("<table class=\"center-table CustomTable2\">");
    assertEquals(true, index > -1);
    table = htmlglobal.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    trs = table.split("</tr>");

    for (String tr : trs){
      if (tr.contains(">"+ TiffConformanceChecker.POLICY_ISO+"<")){
        assertEquals(true, tr.contains(">0 errors<"));
        assertEquals(true, tr.contains(">2 warnings<"));
      } else if (tr.contains(">"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"<")){
        assertEquals(true, tr.contains(">0 errors<"));
        assertEquals(true, tr.contains(">0 warnings<"));
      }
    }

    FileUtils.deleteDirectory(new File(path));
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testPCKo() throws Exception {
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
        "FORMAT\tPDF\n" +
        "RULE\tCompression,=,JPEG;LZW\n");
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

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    String htmlglobal = null;
    byte[] encoded = Files.readAllBytes(Paths.get(path + "/report.html"));
    htmlglobal = new String(encoded);
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.html")) {
        encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    assertEquals(html.contains("This file conforms to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), true);
    assertEquals(html.contains("This file does NOT conform to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), false);
    assertEquals(html.contains("This file conforms to "+TiffConformanceChecker.POLICY_ISO), false);
    assertEquals(html.contains("This file does NOT conform to "+TiffConformanceChecker.POLICY_ISO), true);

    int index = html.indexOf("<table class=\"center-table CustomTable\">");
    assertEquals(true, index > -1);
    String table = html.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    String[] trs = table.split("</tr>");

    // 1 Policy error
    for (String tr : trs){
      if (tr.contains("<td>"+ TiffConformanceChecker.POLICY_ISO+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
        assertEquals(true, tr.contains("<td class=\"error\">1</td>"));
      } else if (tr.contains("<td>"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      }
    }

    // Global errors / warnings resume
    assertEquals(htmlglobal != null, true);
    index = htmlglobal.indexOf("<table class=\"center-table CustomTable2\">");
    assertEquals(true, index > -1);
    table = htmlglobal.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    trs = table.split("</tr>");

    for (String tr : trs){
      if (tr.contains(">"+ TiffConformanceChecker.POLICY_ISO+"<")){
        assertEquals(true, tr.contains(">1 errors<"));
        assertEquals(true, tr.contains(">0 warnings<"));
      } else if (tr.contains(">"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"<")){
        assertEquals(true, tr.contains(">0 errors<"));
        assertEquals(true, tr.contains(">0 warnings<"));
      }
    }

    FileUtils.deleteDirectory(new File(path));
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testPCOk() throws Exception {
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
        "FORMAT\tPDF\n" +
        "RULE\tCompression,=,OJPEG;None\n");
    bw.close();

    String[] args = new String[7];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "src/test/resources/Small/Bilevel.tif";
    args[3] = "-o";
    args[4] = path;
    args[5] = "--configuration";
    args[6] = configfile;

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    String htmlglobal = null;
    byte[] encoded = Files.readAllBytes(Paths.get(path + "/report.html"));
    htmlglobal = new String(encoded);
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.html")) {
        encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    assertEquals(html.contains("This file conforms to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), true);
    assertEquals(html.contains("This file does NOT conform to " + ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")), false);
    assertEquals(html.contains("This file conforms to "+TiffConformanceChecker.POLICY_ISO), true);
    assertEquals(html.contains("This file does NOT conform to "+TiffConformanceChecker.POLICY_ISO), false);

    int index = html.indexOf("<table class=\"center-table CustomTable\">");
    assertEquals(true, index > -1);
    String table = html.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    String[] trs = table.split("</tr>");

    // 1 Policy error
    for (String tr : trs){
      if (tr.contains("<td>"+ TiffConformanceChecker.POLICY_ISO+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      } else if (tr.contains("<td>"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"</td>")){
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
        assertEquals(true, tr.contains("<td class=\"info\">0</td>"));
      }
    }

    // Global errors / warnings resume
    assertEquals(htmlglobal != null, true);
    index = htmlglobal.indexOf("<table class=\"center-table CustomTable2\">");
    assertEquals(true, index > -1);
    table = htmlglobal.substring(index);
    table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
    trs = table.split("</tr>");

    for (String tr : trs){
      if (tr.contains(">"+ TiffConformanceChecker.POLICY_ISO+"<")){
        assertEquals(true, tr.contains(">0 errors<"));
        assertEquals(true, tr.contains(">0 warnings<"));
      } else if (tr.contains(">"+ ImplementationCheckerLoader.getIsoName("BaselineProfileChecker")+"<")){
        assertEquals(true, tr.contains(">0 errors<"));
        assertEquals(true, tr.contains(">0 warnings<"));
      }
    }

    FileUtils.deleteDirectory(new File(path));
    FileUtils.deleteDirectory(new File("temp"));
  }
}

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
    assertEquals(html.contains("<div style=\"display: none;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: block;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<div style=\"display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), false);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), true);
    int index = html.indexOf("<tr class=\"##ROW_PC##\">");
    String substring = html.substring(index, html.indexOf("</tr>", index));
    assertEquals(substring.contains("<td>Policy Checker</td>"), true);
    assertEquals(substring.contains("<td class=\"error\">1</td>"), true);

    assertEquals(htmlglobal != null, true);
    assertEquals(htmlglobal.contains("<tr class=\"##ROW_PC##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_PC##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##ROW_BL##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_BL##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##BL_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##BL_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##PC_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##PC_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

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
        "RULE\tImageWidth,<,1000,1\n");
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
    assertEquals(html.contains("<div style=\"display: block;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"warning\"><i class=\"fa fa-exclamation-triangle\"></i> This file conforms to TIFF Baseline, BUT it has some warnings</div>"), true);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), true);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), false);
    int index = html.indexOf("<tr class=\"##ROW_PC##\">");
    String substring = html.substring(index, html.indexOf("</tr>", index));
    assertEquals(substring.contains("<td>Policy Checker</td>"), true);
    assertEquals(substring.contains("<td class=\"error\">1</td>"), false);

    assertEquals(htmlglobal != null, true);
    assertEquals(htmlglobal.contains("<tr class=\"##ROW_PC##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_PC##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0<"), false);

    assertEquals(htmlglobal.contains("<tr class=\"##ROW_BL##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_BL##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##BL_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##BL_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##PC_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##PC_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">1 warnings<"), true);

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
    assertEquals(html.contains("<div style=\"display: none;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: block;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<div style=\"display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), false);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), true);
    int index = html.indexOf("<tr class=\"##ROW_PC##\">");
    String substring = html.substring(index, html.indexOf("</tr>", index));
    assertEquals(substring.contains("<td>Policy Checker</td>"), true);
    assertEquals(substring.contains("<td class=\"error\">1</td>"), true);

    assertEquals(htmlglobal != null, true);
    assertEquals(htmlglobal.contains("<tr class=\"##ROW_PC##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_PC##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##ROW_BL##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_BL##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##BL_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##BL_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##PC_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##PC_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

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
    assertEquals(html.contains("<div style=\"display: block;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<div style=\"display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), true);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), false);
    int index = html.indexOf("<tr class=\"##ROW_PC##\">");
    String substring = html.substring(index, html.indexOf("</tr>", index));
    assertEquals(substring.contains("<td>Policy Checker</td>"), true);
    assertEquals(substring.contains("<td class=\"info\">0</td>"), true);

    assertEquals(htmlglobal != null, true);
    assertEquals(htmlglobal.contains("<tr class=\"##ROW_PC##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_PC##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##ROW_BL##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_BL##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##BL_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##BL_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##PC_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##PC_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

    FileUtils.deleteDirectory(new File(path));

    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testPCBlankPagesWarnings() throws Exception {
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
        "RULE\tBlankPage,=,False,1\n");
    bw.close();

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
    assertEquals(html.contains("<div style=\"display: block;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), false);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to TIFF Baseline</div>"), true);
    assertEquals(html.contains("<div style=\"display: none;\" class=\"warning\"><i class=\"fa fa-exclamation-triangle\"></i> This file conforms to TIFF Baseline, BUT it has some warnings</div>"), true);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: block;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), true);
    assertEquals(html.contains("<span style=\"margin-left: 20px; display: none;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to Policy Checker</span>"), false);
    int index = html.indexOf("<tr class=\"##ROW_PC##\">");
    String substring = html.substring(index, html.indexOf("</tr>", index));
    assertEquals(substring.contains("<td>Policy Checker</td>"), true);
    assertEquals(substring.contains("<td class=\"error\">1</td>"), false);

    assertEquals(htmlglobal != null, true);
    assertEquals(htmlglobal.contains("<tr class=\"##ROW_PC##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_PC##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0<"), false);

    assertEquals(htmlglobal.contains("<tr class=\"##ROW_BL##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##ROW_BL##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">1<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##BL_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##BL_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">0 warnings<"), true);

    assertEquals(htmlglobal.contains("<tr class=\"##PC_CLASS##\">"), true);
    index = htmlglobal.indexOf("<tr class=\"##PC_CLASS##\">");
    substring = htmlglobal.substring(index, htmlglobal.indexOf("</tr>", index));
    assertEquals(substring.contains(">0 errors<"), true);
    assertEquals(substring.contains(">1 warnings<"), true);

    FileUtils.deleteDirectory(new File(path));

    FileUtils.deleteDirectory(new File("temp"));
  }
}

package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.conformancechecker.tiff.policy_checker.Schematron;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.junit.Test;
import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by easy on 01/10/2015.
 */
public class SchematronTest extends CommandLineTest {
  @Test
  public void testSchematron1() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "check";
    args[1] = "src/test/resources/Small/Bilevel.tif";
    args[2] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();

    File directori = new File(path);
    boolean found = false;
    String xml = null;
    for (String file : directori.list()) {
      if (file.equals("summary.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml = new String(encoded);
      }
    }
    assertEquals(xml != null, true);
    Schematron sch = new Schematron();
    String content = new Scanner(new File("sch/rules.sch")).useDelimiter("\\Z").next();
    String result = sch.testXML(xml, content);

    assertEquals(true, result.indexOf("fired-rule context=\"globalreport\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"individualreports\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tiff_structure\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"ifdTree\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tags\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("failed") == -1);
  }

  @Test
  public void testSchematron2() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "check";
    args[1] = "src/test/resources/Small/Bilevel.tif";
    args[2] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();

    File directori = new File(path);
    String xml = null;
    for (String file : directori.list()) {
      if (file.equals("summary.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml = new String(encoded);
      }
    }
    assertEquals(xml != null, true);
    Schematron sch = new Schematron();

    String sch2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<schema xmlns=\"http://purl.oclc.org/dsdl/schematron\">\n" +
        "<title>Check Report</title>\n" +
        "  <pattern>\n" +
        "   <rule context=\"globalreport\">\n" +
        "\t<assert test=\"individualreports\">Individual reports is mandatory</assert>\n" +
        "   </rule>\n" +
        "   <rule context=\"individualreports\">\n" +
        "\t<assert test=\"report\">At least one report is required</assert>\n" +
        "   </rule>\n" +
        "   <rule context=\"report\">\n" +
        "\t<assert test=\"tiff_structure\">Tiff structure is mandatory</assert>\n" +
        "   </rule>\n" +
        "   <rule context=\"tiff_structure\">\n" +
        "\t<assert test=\"ifdTree\">Ifd tree is mandatory</assert>\n" +
        "   </rule>\n" +
        "   <rule context=\"ifdTree\">\n" +
        "    <assert test=\"count(ifdNode)!= 0\">The Tiff file must have at least one IFD</assert>\n" +
        "   </rule>\n" +
        "   <rule context=\"tags\">\n" +
        "    <assert test=\"count(tag)!= 0\">IFDs must have at least one tag</assert>\n" +
        "   </rule>\n" +
        "  </pattern>\n" +
        "  <pattern>\n" +
        "   <rule context=\"report\">\n" +
        "\t<assert test=\"Foo\">Image foo is mandatory</assert>\n" +
        "   </rule>\n" +
        "  </pattern>\n" +
        "</schema>";

    String result = sch.testXML(xml, sch2);

    assertEquals(true, result.indexOf("fired-rule context=\"globalreport\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"individualreports\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tiff_structure\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"ifdTree\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"tags\"") != -1);
    assertEquals(true, result.indexOf("fired-rule context=\"report\"") != -1);
    assertEquals(true, result.indexOf("failed") != -1);
  }

  @Test
  public void testReport() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "check";
    args[1] = "src/test/resources/Small/Bilevel.tif";
    args[2] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();
    String xmlFile = path + "/1-Bilevel.tif.xml";

    assertEquals(true, new File(xmlFile).exists());

    byte[] encoded = Files.readAllBytes(Paths.get(xmlFile));
    String res = new String(encoded, Charset.defaultCharset());

    assertEquals(true, res.indexOf("fired-rule") == -1);
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
    String path = ReportGenerator.createReportPath(true);
    return path;
  }
}

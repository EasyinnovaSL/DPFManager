package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.conformancechecker.tiff.policy_checker.Schematron;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by easy on 01/10/2015.
 */
public class SchematronTest extends CommandLineTest {
  @Test
  public void testSchematron1() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[2];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";

    MainConsoleApp.main(args);

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
    String result = sch.testXML(xml);

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

    String[] args = new String[2];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";

    MainConsoleApp.main(args);

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
    String result = sch.testXML(xml, "sch/rules2.sch");

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

    String[] args = new String[2];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";

    MainConsoleApp.main(args);

    String path = getPath();
    String xmlFile = path + "/1-Bilevel.tif.xml";

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
    String path = ReportGenerator.createReportPath(true);
    return path;
  }
}

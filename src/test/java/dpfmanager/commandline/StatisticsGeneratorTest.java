package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import com.google.gson.stream.JsonReader;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Easy on 20/07/2015.
 */
public class StatisticsGeneratorTest extends CommandLineTest {
  @Test
  public void testStatistics() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[5];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-f";
    args[3] = "html,json,xml";
    args[4] = "src/test/resources/Small/Bilevel.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();
    String xmlFile = path + "/summary.xml";
    String jsonFile = path + "/summary.json";
    String htmlFile = path + "/report.html";

    assertXML(xmlFile, 1);
    assertJSON(jsonFile, 1);
    assertHTML(htmlFile, 1);
  }

  @Test
  public void testStatistics2() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "check";
    args[1] = "src/test/resources/Small/";
    args[2] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();
    String xmlFile = path + "/summary.xml";
    String htmlFile = path + "/report.html";

    assertXML(xmlFile, 6);
    assertHTML(htmlFile, 6);
  }

  private String getPath() {
    String path = ReportGenerator.createReportPath(true);
    return path;
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


  /**
   * Calls getTextValue and returns a int value
   */
  private int getIntValue(Element ele, String tagName) {
    //in production application you would catch the exception
    return Integer.parseInt(getTextValue(ele, tagName));
  }

  private void assertXML(String xmlFile, int files) throws Exception {
    File fXmlFile = new File(xmlFile);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(fXmlFile);
    doc.getDocumentElement().normalize();


    NodeList nList = doc.getElementsByTagName("stats");

    if (nList != null && nList.getLength() > 0) {
      Element el = (Element) nList.item(0);
      assertEquals(files, getIntValue(el, "reports_count"));
      assertEquals(3, getIntValue(el, "valid_files"));
      assertEquals(3, getIntValue(el, "invalid_files"));
    }
  }

  private void assertJSON(String json, int files) throws Exception {
    JsonReader jsonReader = new JsonReader(new FileReader(json));

    jsonReader.beginObject();
    String name = "";
    String valor = "";
    while (jsonReader.hasNext()) {
      name = jsonReader.nextName();

      if (name.equalsIgnoreCase("stats")) {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
          valor = jsonReader.nextName();
          if (valor.equalsIgnoreCase("reports_count")) {
            assertEquals(jsonReader.nextInt(), files);
          } else if (valor.equalsIgnoreCase("valid_files")) {
            assertEquals(jsonReader.nextInt(), 3);
          } else if (valor.equalsIgnoreCase("invalid_files")) {
            assertEquals(jsonReader.nextInt(), 3);
          } else {
            jsonReader.skipValue();
          }
        }
        jsonReader.endObject();
      } else {
        jsonReader.skipValue();
      }

    }
    jsonReader.endObject();
    jsonReader.close();
  }

  private void assertHTML(String html, int files) throws Exception {
    byte[] encoded = Files.readAllBytes(Paths.get(html));
    String content = new String(encoded);

    String subs = content.substring(content.indexOf("<h3>"));
    subs = subs.substring(subs.indexOf(">")+1);
    subs = subs.substring(0, subs.indexOf("<"));
    if (files == 1) {
      assertEquals(subs, files + " file processed");
    } else {
      assertEquals(subs, files + " files processed");
    }

    subs = content.substring(content.indexOf("id=\"pie-global\""));
    subs = subs.substring(subs.indexOf("pie-info"));
    subs = subs.substring(subs.indexOf(">")+1);
    subs = subs.substring(subs.indexOf(">")+1);
    String field1 = subs.substring(0, subs.indexOf("<"));
    subs = subs.substring(subs.indexOf("<div"));
    subs = subs.substring(subs.indexOf(">")+1);
    String field2 = subs.substring(0, subs.indexOf("<"));
    subs = subs.substring(subs.indexOf("<div"));
    subs = subs.substring(subs.indexOf(">")+1);
    String field3 = subs.substring(0, subs.indexOf("<"));

    assertEquals( "3 passed", field1);
    assertEquals("3 failed", field2);
    assertEquals("Global score 50%", field3);
  }
}

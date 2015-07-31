package dpfmanager;

import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;

import com.google.gson.stream.JsonReader;

import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.commons.lang.time.FastDateFormat;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Easy on 20/07/2015.
 */
public class StatisticsGeneratorTest extends TestCase {
  TiffReader tr;

  /**
   * Pre test.
   */
  @Before
  public void PreTest() {
    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  public void testStatistics() throws Exception {
    String[] args = new String[1];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
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
    String xmlFile = path + "/summary.xml";
    String jsonFile = path + "/summary.json";
    String htmlFile = path + "/index.html";

    assertXML(xmlFile, 1);
    assertJSON(jsonFile, 1);
    assertHTML(htmlFile, 1);

  }

  public void testStatistics2() throws Exception {
    String[] args = new String[1];
    args[0] = "src/test/resources/Small/";
    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    //MainApp.main(args);
    CommandLine cl = new CommandLine(params);
    cl.launch();
    Platform.exit();

    String path = getPath();
    String xmlFile = path + "/summary.xml";
    String jsonFile = path + "/summary.json";
    String htmlFile = path + "/index.html";

    assertXML(xmlFile, 6);
    assertJSON(jsonFile, 6);
    assertHTML(htmlFile, 6);
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
      assertEquals(getIntValue(el, "reports_count"), files);
      assertEquals(getIntValue(el, "valid_files"), files);
      assertEquals(getIntValue(el, "invalid_files"), 0);
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
            assertEquals(jsonReader.nextInt(), files);
          } else if (valor.equalsIgnoreCase("invalid_files")) {
            assertEquals(jsonReader.nextInt(), 0);
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
    File htmlFile = new File(html);
    org.jsoup.nodes.Document doc = Jsoup.parse(htmlFile, "UTF-8");

    String info = "info_chart";
    String filesTotal = "h3";

    Elements fileNum = doc.getElementsByAttributeValue("id", info);
    Elements fileTotal = doc.getElementsByTag(filesTotal);

    assertEquals(fileTotal.text(), files + " files processed");
    assertEquals(fileNum.text(), files + " passed 0 failed Global score 100%");
  }
}

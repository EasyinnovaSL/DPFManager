package dpfmanager.commandline;

import dpfmanager.shell.interfaces.console.commandline.CommandLineApp;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.application.Application;

import com.google.gson.stream.JsonReader;

import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
    DPFManagerProperties.setFeedback(false);

    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  public void testStatistics() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[4];
    args[0] = "src/test/resources/Small/Bilevel.tif";
    args[1] = "-s";
    args[2] = "-reportformat";
    args[3] = "'html,json,xml'";
    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i=0;i<args.length;i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i=0;i<args.length;i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLineApp cl = new CommandLineApp(params);
    cl.launch();

    String path = getPath();
    String xmlFile = path + "/summary.xml";
    String jsonFile = path + "/summary.json";
    String htmlFile = path + "/report.html";

    assertXML(xmlFile, 1);
    assertJSON(jsonFile, 1);
    assertHTML(htmlFile, 1);
  }

  public void testStatistics2() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[2];
    args[0] = "src/test/resources/Small/";
    args[1] = "-s";
    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i=0;i<args.length;i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        for (int i=0;i<args.length;i++) listRaw.add(args[i]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    //MainApp.main(args);
    CommandLineApp cl = new CommandLineApp(params);
    cl.launch();

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
      assertEquals(0, getIntValue(el, "valid_files"));
      assertEquals(files, getIntValue(el, "invalid_files"));
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
            assertEquals(jsonReader.nextInt(), 0);
          } else if (valor.equalsIgnoreCase("invalid_files")) {
            assertEquals(jsonReader.nextInt(), files);
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

    String info = "pie-global";
    String filesTotal = "h3";

    Elements fileNum = doc.getElementsByAttributeValue("id", info);
    Elements fileTotal = doc.getElementsByTag(filesTotal);

    if (files == 1) {
      assertEquals(fileTotal.text(), files + " file processed");
    } else {
      assertEquals(fileTotal.text(), files + " files processed");
    }
    assertEquals(fileNum.get(0).parent().text(), "0 passed " + files + " failed Global score 0%");
  }
}

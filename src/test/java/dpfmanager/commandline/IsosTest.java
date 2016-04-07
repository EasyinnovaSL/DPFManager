package dpfmanager.commandline;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.console.CommandLineApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.application.Application;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by easy on 30/03/2016.
 */
public class IsosTest extends TestCase {
  public void testAllIsos() {
    try {
      DPFManagerProperties.setFeedback(false);

      if (!new File("temp").exists()) {
        new File("temp").mkdir();
      }

      String configfile = "temp/xx.cfg";
      int idx = 0;
      while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

      PrintWriter bw = new PrintWriter(configfile);
      bw.write("ISO\tBaseline\n" +
          "ISO\tBaseline\n" +
          "ISO\tTiff/EP\n" +
          "ISO\tTiff/IT\n" +
          "ISO\tTiff/IT-1\n" +
          "ISO\tTiff/IT-2\n" +
          "FORMAT\tHTML\n");
      bw.close();

      ArrayList<String> listRaw = new ArrayList<String>();
      listRaw.add("src/test/resources/Small/Bilevel.tif");
      listRaw.add("-reportformat");
      listRaw.add("html");
      listRaw.add("-configuration");
      listRaw.add(configfile);

      Application.Parameters params = new Application.Parameters() {
        @Override
        public List<String> getRaw() {
          return listRaw;
        }

        @Override
        public List<String> getUnnamed() {
          return listRaw;
        }

        @Override
        public Map<String, String> getNamed() {
          return null;
        }
      };

      CommandLineApp cl = new CommandLineApp(params);
      cl.launch();

      String path = ReportGenerator.createReportPath(true);
      File directori = new File(path);
      assertEquals(2, directori.list().length);

      File htmlFile = new File(path + "/report.html");
      org.jsoup.nodes.Document doc = Jsoup.parse(htmlFile, "UTF-8");

      Elements elem = doc.getElementsByAttributeValue("class", "##ROW_BL##");
      assertEquals(1, elem.size());
      assertEquals(true, elem.html().contains(">1<"));

      elem = doc.getElementsByAttributeValue("class", "##ROW_EP##");
      assertEquals(1, elem.size());
      assertEquals(true, elem.html().contains(">0<"));

      elem = doc.getElementsByAttributeValue("class", "##ROW_IT##");
      assertEquals(1, elem.size());
      assertEquals(true, elem.html().contains(">0<"));

      new File(configfile).delete();
      FileUtils.deleteDirectory(new File("temp"));
    } catch (Exception ex ){
      assertEquals(7, 6);
    }
  }
}

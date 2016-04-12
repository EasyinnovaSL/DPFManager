package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by easy on 30/03/2016.
 */
public class IsosTest extends CommandLineTest {
  @Test
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


      String[] args = new String[5];
      args[0] = "src/test/resources/Small/Bilevel.tif";
      args[1] = "-reportformat";
      args[2] = "html";
      args[3] = "-configuration";
      args[4] = configfile;

      MainConsoleApp.main(args);

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
      assertEquals(true, false);
    }
  }
}

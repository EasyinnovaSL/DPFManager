package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

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

      String[] args = new String[6];
      args[0] = "src/test/resources/Small/Bilevel.tif";
      args[1] = "--reportformat";
      args[2] = "html";
      args[3] = "-configuration";
      args[4] = configfile;
      args[5] = "-s";

      MainConsoleApp.main(args);

      // Wait for finish
      waitForFinishMultiThred(30);

      String path = ReportGenerator.createReportPath(true);
      File directori = new File(path);
      assertEquals(2, directori.list().length);

      byte[] encoded = Files.readAllBytes(Paths.get(path + "/report.html"));
      String content = new String(encoded);
      int i1 = content.indexOf("##ROW_BL##");
      assertEquals(true, i1 > -1);
      String subs = content.substring(i1);
      subs = subs.substring(0, subs.indexOf("</tr"));
      int i2 = subs.indexOf("<td");
      assertEquals(true, i2 > -1);
      int i3 = subs.indexOf("<td", i2+1);
      assertEquals(true, i3 > -1);
      subs = subs.substring(i2);
      subs = subs.substring(subs.indexOf(">")+1);
      subs = subs.substring(0, subs.indexOf("<"));
      assertEquals(true, subs.equals("1"));

      i1 = content.indexOf("##ROW_EP##");
      assertEquals(true, i1 > -1);
      subs = content.substring(i1);
      subs = subs.substring(0, subs.indexOf("</tr"));
      i2 = subs.indexOf("<td");
      assertEquals(true, i2 > -1);
      i3 = subs.indexOf("<td", i2+1);
      assertEquals(true, i3 > -1);
      subs = subs.substring(i2);
      subs = subs.substring(subs.indexOf(">")+1);
      subs = subs.substring(0, subs.indexOf("<"));
      assertEquals(true, subs.equals("0"));

      i1 = content.indexOf("##ROW_IT##");
      assertEquals(true, i1 > -1);
      subs = content.substring(i1);
      subs = subs.substring(0, subs.indexOf("</tr"));
      i2 = subs.indexOf("<td");
      assertEquals(true, i2 > -1);
      i3 = subs.indexOf("<td", i2+1);
      assertEquals(true, i3 > -1);
      subs = subs.substring(i2);
      subs = subs.substring(subs.indexOf(">")+1);
      subs = subs.substring(0, subs.indexOf("<"));
      assertEquals(true, subs.equals("0"));

      FileUtils.deleteDirectory(new File("temp"));
    } catch (Exception ex) {
      assertEquals(true, false);
    }
  }
}

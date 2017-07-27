package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
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
          "ISO\tTiff/EP\n" +
          "ISO\tTiff/IT\n" +
          "ISO\tTiff/IT-1\n" +
          "ISO\tTiff/IT-2\n" +
          "FORMAT\tHTML\n");
      bw.close();

      String[] args = new String[7];
      args[0] = "check";
      args[1] = "-f";
      args[2] = "html";
      args[3] = "-c";
      args[4] = configfile;
      args[5] = "-s";
      args[6] = "src/test/resources/Small/Bilevel.tif";

      MainConsoleApp.main(args);

      // Wait for finish
      waitForFinishMultiThred(30);

      String path = ReportGenerator.createReportPath(true);
      File directori = new File(path);
      assertEquals(5, directori.list().length);

      // Conforms table
      byte[] encoded = Files.readAllBytes(Paths.get(path + "/report.html"));
      String content = new String(encoded);
      int i1 = content.indexOf("col-md-5 bot30-sm");
      assertEquals(true, i1 > -1);
      assertEquals(true, i1 > -1);
      String table = content.substring(i1);
      table = table.substring(table.indexOf("<tr>"), table.indexOf("</table>"));
      String[] trs = table.split("</tr>");

      // Olny conforms to baseline
      for (String tr : trs){
        if (tr.contains("conforms to " + ImplementationCheckerLoader.getIsoName("TIFF_Baseline_Core_6_0"))){
          assertEquals(true, tr.contains(">1<"));
        } else if (tr.contains("conforms to " + ImplementationCheckerLoader.getIsoName("TiffITProfileChecker")) || tr.contains("conforms to " + ImplementationCheckerLoader.getIsoName("TIFF_EP"))){
          assertEquals(true, tr.contains(">0<"));
        }
      }

      FileUtils.deleteDirectory(new File("temp"));
    } catch (Exception ex) {
      assertEquals(true, false);
    }
  }
}

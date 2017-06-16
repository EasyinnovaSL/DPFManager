package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Easy on 20/07/2015.
 */
public class ReportGeneratorTest extends CommandLineTest {
  @Test
  public void testHTMLTags() throws Exception {
    DPFManagerProperties.setFeedback(false);

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String path = "temp/output";
    int idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String[] args = new String[5];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "src/test/resources/Small/Bilevel.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    File directori = new File(path + "/html");
    assertEquals(directori.exists(), true);

    String html = null;
    for (String file : directori.list()) {
      if (file.equals("1-Bilevel.tif.html")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/html/" + file));
        html = new String(encoded);
      }
    }
    assertEquals(html != null, true);
    assertEquals(html.contains("256</td><td>ImageWidth</td><td>999</td>"), true);
    assertEquals(html.contains("257</td><td>ImageLength</td><td>662</td>"), true);
    assertEquals(html.contains("305</td><td>Software</td><td>Adobe Photoshop CS6 (Macintosh)</td>"), true);

    FileUtils.deleteDirectory(new File(path));

    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testReportsFile() throws Exception {
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
    int n=0;
    for (String file : directori.list()) {
      if (!file.contains(".mets") && !file.contains(".ser") && !file.contains("serialized")) n++;
    }
    assertEquals(4, n);
  }

  @Test
  public void testReportsSubfoldersWithEqualFilenames() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[4];
    args[0] = "check";
    args[1] = "-r";
    args[2] = "src/test/resources/S2";
    args[3] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();

    File directori = new File(path);
    int n=0;
    for (String file : directori.list()) {
      if (!file.contains(".mets") && !file.contains(".ser") && !file.contains("serialized")) n++;
    }
    assertEquals(7, n);
  }

  @Test
  public void testReportsFolder() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "check";
    args[1] = "src/test/resources/Small";
    args[2] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();

    File directori = new File(path);
    int n=0;
    for (String file : directori.list()) {
      if (!file.contains(".mets") && !file.contains(".ser") && !file.contains("serialized")) n++;
    }

    assertEquals(9, n);
  }

  @Test
  public void testReportsZip() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "check";
    args[1] = "src/test/resources/Small.zip";
    args[2] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();

    File directori = new File(path);
    int n=0;
    for (String file : directori.list()) {
      if (!file.contains(".mets") && !file.contains(".ser") && !file.contains("serialized")) n++;
    }

    assertEquals(7, n);
  }

  @Test
  public void testReportsURL() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String dirWeb = "http://dpfmanager.org/img/Bilevel.tif";
    try {
      String[] args = new String[3];
      args[0] = "check";
      args[1] = dirWeb;
      args[2] = "-s";

      MainConsoleApp.main(args);

      // Wait for finish
      waitForFinishMultiThred(30);

      String path = getPath();

      File directori = new File(path);
      int n=0;
      for (String file : directori.list()) {
        if (!file.contains(".mets") && !file.contains(".ser") && !file.contains("serialized")) n++;
      }

      assertEquals(4, n);

      // Check tiff to jpg
      String jpgPath = path+"html/img/1-Bilevel.tif.gif";
      File jpgFile = new File(jpgPath);
      assertEquals(true, jpgFile.exists());
    } catch (Exception ex) {
      ex.printStackTrace();
      assertEquals(1, 0);
    }
  }

  @Test
  public void testReportsURLZip() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String dirWeb = "http://dpfmanager.org/test/Bilevel.zip";
    try {
      String[] args = new String[3];
      args[0] = "check";
      args[1] = "-s";
      args[2] = dirWeb;

      MainConsoleApp.main(args);

      // Wait for finish
      waitForFinishMultiThred(30);

      String path = getPath();

      File directori = new File(path);
      int n=0;
      for (String file : directori.list()) {
        if (!file.contains(".mets") && !file.contains(".ser") && !file.contains("serialized")) n++;
      }

      assertEquals(4, n);

      // Check tiff to jpg
      String jpgPath = path+"html/img/1-Bilevel.tif.gif";
      File jpgFile = new File(jpgPath);
      assertEquals(true, jpgFile.exists());
    } catch (Exception ex) {
      ex.printStackTrace();
      assertEquals(1, 0);
    }
  }

  @Test
  public void testReportsFormat() throws Exception {
    assertReportsFormat("html");
    assertReportsFormat("xml");
    assertReportsFormat("json");
    assertReportsFormat("xml,html");
    assertReportsFormat("xml,json");
    assertReportsFormat("json,html");
    assertReportsFormat("xml,json,html");
  }

  private void assertReportsFormat(String formats) throws Exception {
    String[] args = new String[5];
    args[0] = "check";
    args[1] = "-s";
    args[2] = "-f";
    args[3] = formats;
    args[4] = "src/test/resources/Small/Bilevel.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();

    File directori = new File(path);
    int filesExpect = (formats.split(",").length) * 2;

    int n=0;
    for (String file : directori.list()) {
      if (!file.contains(".mets") && !file.contains(".ser") && !file.contains("serialized")) n++;
    }

    assertEquals(filesExpect, n);

    String extension = "";
    boolean isXML = false;
    boolean isHTML = false;
    boolean isJSON = false;
    for (File file : directori.listFiles()) {
      extension = file.getAbsolutePath();
      if (!file.isDirectory()) {
        if (formats.contains("xml") && !isXML) {
          isXML = extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".xml");
        }
        if (formats.contains("json") && !isJSON) {
          isJSON = extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".json");
        }
        if (formats.contains("html") && !isHTML) {
          isHTML = extension.substring(extension.lastIndexOf(".")).equalsIgnoreCase(".html");
        }
      }
    }
    if (formats.contains("xml")) {
      assertEquals(true, isXML);
    }
    if (formats.contains("json")) {
      assertEquals(true, isJSON);
    }
    if (formats.contains("html")) {
      assertEquals(true, isHTML);
    }
  }

  private String getPath() {
    String path = ReportGenerator.createReportPath(true);
    return path;
  }
}

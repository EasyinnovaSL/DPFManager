package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * Created by easy on 31/05/2016.
 */
public class DeleteFileTest extends CommandLineTest {
  @Test
  public void testDeleteXml() throws Exception {
    DPFManagerProperties.setFeedback(false);

    int n = 0;
    while (new File("tt"+n+".tif").exists()) n++;
    String name = "tt"+n+".tif";
    Files.copy(new File("src/test/resources/Small/Bilevel.tif").toPath(), new File(name).toPath());

    String[] args = new String[4];
    args[0] = name;
    args[1] = "-reportformat";
    args[2] = "xml";
    args[3] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    Files.delete(new File(name).toPath());
    assertEquals(new File(name).exists(), false);
  }

  @Test
  public void testDeleteJson() throws Exception {
    DPFManagerProperties.setFeedback(false);

    int n = 0;
    while (new File("tt"+n+".tif").exists()) n++;
    String name = "tt"+n+".tif";
    Files.copy(new File("src/test/resources/Small/Bilevel.tif").toPath(), new File(name).toPath());

    String[] args = new String[4];
    args[0] = name;
    args[1] = "-reportformat";
    args[2] = "json";
    args[3] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    Files.delete(new File(name).toPath());
    assertEquals(new File(name).exists(), false);
  }

  @Test
  public void testDeleteHtml() throws Exception {
    DPFManagerProperties.setFeedback(false);

    int n = 0;
    while (new File("tt"+n+".tif").exists()) n++;
    String name = "tt"+n+".tif";
    Files.copy(new File("src/test/resources/Small/Bilevel.tif").toPath(), new File(name).toPath());

    String[] args = new String[4];
    args[0] = name;
    args[1] = "-reportformat";
    args[2] = "html";
    args[3] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    Files.delete(new File(name).toPath());
    assertEquals(new File(name).exists(), false);
  }

  @Test
  public void testDeletePdf() throws Exception {
    DPFManagerProperties.setFeedback(false);

    int n = 0;
    while (new File("tt"+n+".tif").exists()) n++;
    String name = "tt"+n+".tif";
    Files.copy(new File("src/test/resources/Small/Bilevel.tif").toPath(), new File(name).toPath());

    String[] args = new String[4];
    args[0] = name;
    args[1] = "-reportformat";
    args[2] = "xml";
    args[3] = "-s";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    Files.delete(new File(name).toPath());
    assertEquals(new File(name).exists(), false);
  }

  @Test
  public void testDeletePolicyXml() throws Exception {
    DPFManagerProperties.setFeedback(false);

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String path = "temp/output";
    int idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String configfile = "temp/xx.cfg";
    idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tXML\n" +
        "RULE\tBlankPage,=,0,1\n");
    bw.close();

    int n = 0;
    while (new File("tt"+n+".tif").exists()) n++;
    String name = "tt"+n+".tif";
    Files.copy(new File("src/test/resources/Small/Bilevel.tif").toPath(), new File(name).toPath());

    String[] args = new String[4];
    args[0] = name;
    args[1] = "-s";
    args[2] = "-configuration";
    args[3] = configfile;

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    Files.delete(new File(name).toPath());
    assertEquals(new File(name).exists(), false);

    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testDeletePolicyHtml() throws Exception {
    DPFManagerProperties.setFeedback(false);

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String path = "temp/output";
    int idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String configfile = "temp/xx.cfg";
    idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tHTML\n" +
        "RULE\tBlankPage,=,0,1\n");
    bw.close();

    int n = 0;
    while (new File("tt"+n+".tif").exists()) n++;
    String name = "tt"+n+".tif";
    Files.copy(new File("src/test/resources/Small/Bilevel.tif").toPath(), new File(name).toPath());

    String[] args = new String[4];
    args[0] = name;
    args[1] = "-s";
    args[2] = "-configuration";
    args[3] = configfile;

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    Files.delete(new File(name).toPath());
    assertEquals(new File(name).exists(), false);

    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testDeletePolicyAll() throws Exception {
    DPFManagerProperties.setFeedback(false);

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String path = "temp/output";
    int idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String configfile = "temp/xx.cfg";
    idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tHTML\n" +
        "FORMAT\tXML\n" +
        "FORMAT\tPDF\n" +
        "FORMAT\tJSON\n" +
        "RULE\tBlankPage,=,1\n");
    bw.close();

    int n = 0;
    while (new File("tt"+n+".tif").exists()) n++;
    String name = "tt"+n+".tif";
    Files.copy(new File("src/test/resources/Small/Bilevel.tif").toPath(), new File(name).toPath());

    String[] args = new String[4];
    args[0] = name;
    args[1] = "-s";
    args[2] = "-configuration";
    args[3] = configfile;

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    Files.delete(new File(name).toPath());
    assertEquals(new File(name).exists(), false);

    FileUtils.deleteDirectory(new File("temp"));
  }
}
